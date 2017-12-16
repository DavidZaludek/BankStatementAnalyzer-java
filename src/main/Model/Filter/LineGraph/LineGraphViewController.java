package main.Model.Filter.LineGraph;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import main.InternalUtils.Enums.GranularityEnum;
import main.Model.Record;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class LineGraphViewController extends AbstractController {

	ArrayList<Record> records;
	HashSet<String> currencySet;

	@FXML
	private DatePicker FromDatePicker;

	@FXML
	private ChoiceBox<String> CurrencySelection;

	@FXML
	private CheckBox LinearRegresion;

	@FXML
	private DatePicker ToDatePicker;

	@FXML
	private Slider GranularitySlider;

	@FXML
	private LineChart<String,Number> LineChart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private CheckBox IncomeDisplay;

	@FXML
	private CheckBox SpendingDisplay;

	@FXML
	private CheckBox AccountBalanceDisplay;

	private GranularityEnum granularity;

	@FXML
	void FromDatePickerSet(ActionEvent event) {
		filterData();
	}

	@FXML
	void ToDatePickerSet(ActionEvent event) {
		filterData();
	}

	@FXML
	void GranularitySliderDragged(){
		filterData();
	}

	@FXML
	void LinearRegresionOnAction(){
		filterData();
	}

	@FXML
	void IncomeDisplayOnAction(ActionEvent event) {
		filterData();
	}

	@FXML
	void SpendingDisplayOnAction(ActionEvent event) {
		filterData();
	}

	@FXML
	void AccountBalanceDisplayOnAction(){
		filterData();
	}

	private void filterData() {

		if (records.size() == 0) {
			return;
		}

		granularity = GranularityEnum.fromDouble(GranularitySlider.getValue());
		LocalDate from = FromDatePicker.getValue();
		LocalDate to = ToDatePicker.getValue();
		String currency = CurrencySelection.getValue();

		ArrayList<Record> tmpRecordsIncome = new ArrayList<>();
		ArrayList<Record> tmpRecordsOutcome = new ArrayList<>();

		XYChart.Series incomeSeries = new XYChart.Series();
		incomeSeries.setName("Accumulated income");
		XYChart.Series outcomeSeries = new XYChart.Series();
		outcomeSeries.setName("Accumulated spending");
		XYChart.Series balanceSeries = new XYChart.Series();
		balanceSeries.setName("Account balance");


		for (int i = 0; i < records.size(); i++) {
			Record tmpRecord = records.get(i);
			if (tmpRecord.getDateTime().isBefore(to) && tmpRecord.getDateTime().isAfter(from) && tmpRecord.getCurrency().equals(currency)) {
				if (tmpRecord.getAmount() > 0) {
					tmpRecordsIncome.add(tmpRecord);
				} else {
					tmpRecordsOutcome.add(tmpRecord);
				}
			}
		}

		int cntInc = 0;
		int cntOut = 0;

		double incomeAccumulated = 0.0;
		double outcomeAccumulated = 0.0;

		LinkedHashMap<String, Double> incomeMap = new LinkedHashMap<>();
		LinkedHashMap<String, Double> outcomeMap = new LinkedHashMap<>();

		LocalDate tmpDate = LocalDate.parse(from.toString());

		for (int i = 0; i < DAYS.between(from, to); i++) {

			while (tmpRecordsIncome.size() > cntInc && tmpRecordsIncome.get(cntInc).getDateTime().isEqual(tmpDate)) {
				incomeAccumulated += tmpRecordsIncome.get(cntInc).getAmount();
				cntInc++;
			}

			while (tmpRecordsOutcome.size() > cntOut && tmpRecordsOutcome.get(cntOut).getDateTime().isEqual(tmpDate)) {
				outcomeAccumulated += tmpRecordsOutcome.get(cntOut).getAmount();
				cntOut++;
			}

			String key = GranularityEnum.getFormated(tmpDate, granularity);

			incomeMap.put(key, incomeAccumulated);

			outcomeMap.put(key, outcomeAccumulated * -1);

			if (tmpRecordsIncome.size() == cntInc && tmpRecordsOutcome.size() == cntOut){
				incomeMap.put(key, (double) -1);
				outcomeMap.put(key, (double) -1);
			}

			tmpDate = tmpDate.plusDays(1);

		}

		SimpleRegression regressionIncome = new SimpleRegression();
		SimpleRegression regressionOutcome = new SimpleRegression();

		XYChart.Series regressionIncomeSeries = new XYChart.Series();
		regressionIncomeSeries.setName("Linear regression of accumulated income");
		XYChart.Series regressionOutcomeSeries = new XYChart.Series();
		regressionOutcomeSeries.setName("Linear regression of accumulated spending");

		cntInc = 0;
		Iterator<String> itt = incomeMap.keySet().iterator();

		String tmpKey = null;

		while (itt.hasNext()){
			tmpKey = itt.next();

			if (incomeMap.get(tmpKey) < 0)
				continue;

			regressionIncome.addData(cntInc++,incomeMap.get(tmpKey));

			incomeSeries.getData().add(new XYChart.Data<>(tmpKey, incomeMap.get(tmpKey)));
		}

		cntOut = 0;
		itt = outcomeMap.keySet().iterator();

		while (itt.hasNext()){
			tmpKey = itt.next();

			if (outcomeMap.get(tmpKey) < 0)
				continue;

			regressionOutcome.addData(cntOut++,outcomeMap.get(tmpKey));
			outcomeSeries.getData().add(new XYChart.Data<>(tmpKey, outcomeMap.get(tmpKey)));
			balanceSeries.getData().add(new XYChart.Data<>(tmpKey, incomeMap.get(tmpKey) - outcomeMap.get(tmpKey)));
		}

		itt = outcomeMap.keySet().iterator();

		cntOut = 0;
		while (itt.hasNext()) {
			tmpKey = itt.next();
			regressionOutcomeSeries.getData().add(new XYChart.Data<>(tmpKey, regressionOutcome.predict(cntOut)));
			regressionIncomeSeries.getData().add(new XYChart.Data<>(tmpKey, regressionIncome.predict(cntOut++)));
		}

		LineChart.setAnimated(false);

		if (granularity.equals(GranularityEnum.DAY) || granularity.equals(GranularityEnum.WEEK)){
			LineChart.setAnimated(false);
		}

		LineChart.getData().setAll();

		if (AccountBalanceDisplay.isSelected())
		{
			LineChart.getData().addAll(balanceSeries);
		}

		if (IncomeDisplay.isSelected()){
			LineChart.getData().addAll(incomeSeries);
		}

		if (SpendingDisplay.isSelected()){
			LineChart.getData().addAll(outcomeSeries);
		}


		if (SpendingDisplay.isSelected() && LinearRegresion.isSelected()){
			LineChart.getData().addAll(regressionOutcomeSeries);
		}
		if (IncomeDisplay.isSelected() && LinearRegresion.isSelected()){
			LineChart.getData().addAll(regressionIncomeSeries);
		}

		xAxis.setTickLabelsVisible(true);
		LineChart.setVerticalGridLinesVisible(true);

		switch (granularity){
			case DAY:
				LineChart.setVerticalGridLinesVisible(false);
				xAxis.setLabel("Days");
				break;
			case WEEK:
				xAxis.setLabel("Weeks");
				break;
			case MONTH:
				xAxis.setLabel("Months");
				break;
			case YEAR:
				xAxis.setLabel("Years");
				break;
		}

		for (XYChart.Series<String, Number> series : LineChart.getData()) {

			if (series.getName().equals("Accumulated income") || series.getName().equals("Accumulated spending") || series.getName().equals("Account balance")) //if Name is "blue" then continue
			{
				for (XYChart.Data<String, Number> d : series.getData()) {
					Tooltip.install(d.getNode(), new Tooltip(d.getXValue().toString() + "\n" + "Value : " + String.format("%.2f",d.getYValue()) + " " + currency));
				}
				continue;
			}

			for (XYChart.Data<String, Number> data : series.getData()) {
				StackPane stackPane = (StackPane) data.getNode();
				stackPane.setVisible(false);
			}
		}

	}

	public void initialize(){

		records = new ArrayList<>();
		currencySet = new HashSet<>();

		GranularitySlider.setLabelFormatter(new StringConverter<Double>() {
			@Override
			public String toString(Double n) {
				if (n < 0.5) return "Day";
				if (n < 1.5) return "Week";
				if (n < 2.5) return "Month";

				return "Year";
			}


			@Override
			public Double fromString(String s) {
				switch (s) {
					case "Day":
						return 0d;
					case "Week":
						return 1d;
					case "Month":
						return 2d;
					case "Year":
						return 3d;
					default:
						return 3d;
				}
			}
		});

		GranularitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (Math.abs(newValue.doubleValue()%1) < 0.01 ){
				if (!granularity.equals(GranularityEnum.fromDouble(newValue.doubleValue())))
					filterData();
			}
		});

	}

	@Override
	public void setViewData(ViewDataHandler viewData) {
		records= new ArrayList<>();
		currencySet = new HashSet<>();

		records.addAll(viewData.getUser().getRecords());

		if (records.size() == 0){
			return;
		}

		Collections.sort(records, Record.getDateTimeComparator());

		for (int i = 0; i < records.size(); i++) {
			currencySet.add(records.get(i).getCurrency());
		}

		FromDatePicker.setValue(records.get(0).getDateTime());
		ToDatePicker.setValue(records.get(records.size()-1).getDateTime());

		CurrencySelection.setItems(FXCollections.observableArrayList(currencySet));
		CurrencySelection.setValue(currencySet.iterator().next());

		filterData();
	}
}
