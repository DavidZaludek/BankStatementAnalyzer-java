package main.Model.Filter.AreaGraph;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import main.InternalUtils.Enums.GranularityEnum;
import main.Model.Record;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class AreaGraphViewController extends AbstractController {

	ArrayList<Record> records;
	HashSet<String> currencySet;

	@FXML
	private DatePicker FromDatePicker;

	@FXML
	private ChoiceBox<String> CurrencySelection;

	@FXML
	private DatePicker ToDatePicker;

	@FXML
	private Slider GranularitySlider;

	@FXML
	private javafx.scene.chart.AreaChart<String,Number> AreaChart;

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


		cntInc = 0;
		Iterator<String> itt = incomeMap.keySet().iterator();

		String tmpKey = null;

		while (itt.hasNext()){
			tmpKey = itt.next();

			if (incomeMap.get(tmpKey) < 0)
				continue;

			incomeSeries.getData().add(new XYChart.Data<>(tmpKey, incomeMap.get(tmpKey)));
		}

		cntOut = 0;
		itt = outcomeMap.keySet().iterator();

		while (itt.hasNext()){
			tmpKey = itt.next();

			if (outcomeMap.get(tmpKey) < 0)
				continue;

			outcomeSeries.getData().add(new XYChart.Data<>(tmpKey, outcomeMap.get(tmpKey)));
			balanceSeries.getData().add(new XYChart.Data<>(tmpKey, incomeMap.get(tmpKey) - outcomeMap.get(tmpKey)));
		}

		AreaChart.setAnimated(false);

		if (granularity.equals(GranularityEnum.DAY) || granularity.equals(GranularityEnum.WEEK)){
			AreaChart.setAnimated(false);
		}

		AreaChart.getData().setAll();

		if (AccountBalanceDisplay.isSelected())
		{
			AreaChart.getData().addAll(balanceSeries);
		}

		if (IncomeDisplay.isSelected()){
			AreaChart.getData().addAll(incomeSeries);
		}

		if (SpendingDisplay.isSelected()){
			AreaChart.getData().addAll(outcomeSeries);
		}

		xAxis.setTickLabelsVisible(true);
		AreaChart.setVerticalGridLinesVisible(true);

		switch (granularity){
			case DAY:
				AreaChart.setVerticalGridLinesVisible(false);
				xAxis.setLabel("Days");
				xAxis.setTickLabelsVisible(true);
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

		for (XYChart.Series<String, Number> series : AreaChart.getData()) {

			if (series.getName().equals("Accumulated income") || series.getName().equals("Accumulated spending") || series.getName().equals("Account balance")) //if Name is "blue" then continue
				continue;

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
