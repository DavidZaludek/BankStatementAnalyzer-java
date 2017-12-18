package main.Model.Filter.StackedBarGraph;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import main.InternalUtils.Enums.GranularityEnum;
import main.Model.Record;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.time.LocalDate;
import java.util.*;

import static main.InternalUtils.MathUtils.calcMeanCI;

public class StackedBarGraphViewController extends AbstractController {
	public DatePicker FromDatePicker;
	public DatePicker ToDatePicker;
	public ChoiceBox<String> CurrencySelection;
	public CheckBox CIButton;
	public Slider GranularitySlider;
	public StackedBarChart<String,Number> StackedBarChart;

	private ArrayList<Record> records;
	private HashSet<String> currencySet;

	private GranularityEnum granularity;

	@FXML
	void FromDatePickerSet(ActionEvent event) {
		filterData();
	}

	@FXML
	void ToDatePickerSet(ActionEvent event) {
		filterData();
	}

	private void filterData() {

		granularity = GranularityEnum.fromDouble(GranularitySlider.getValue());
		LocalDate from = FromDatePicker.getValue();
		LocalDate to = ToDatePicker.getValue();
		String currency = CurrencySelection.getValue();

		SummaryStatistics stats = new SummaryStatistics();

		ArrayList<Record> tmpRecordsOutcome = new ArrayList<>();

		XYChart.Series outcomeSeries = new XYChart.Series();
		outcomeSeries.setName("Spending during time period");

		for (int i = 0; i < records.size(); i++) {
			Record tmpRecord = records.get(i);
			if (tmpRecord.getDateTime().isBefore(to) && tmpRecord.getDateTime().isAfter(from) && tmpRecord.getCurrency().equals(currency)) {
				if (tmpRecord.getAmount() < 0) {
					tmpRecordsOutcome.add(tmpRecord);
				}
			}
		}

		int cntOut = 0;
		double outcomeAccumulated = 0.0;

		LinkedHashMap<String, Double> outcomeMap = new LinkedHashMap<>();

		LocalDate tmpDate = LocalDate.parse(from.toString());

		String key = GranularityEnum.getFormated(tmpDate,granularity);

		while (true) {

			while (tmpRecordsOutcome.size() > cntOut && GranularityEnum.getFormated(tmpRecordsOutcome.get(cntOut).getDateTime(), granularity).equals(GranularityEnum.getFormated(tmpDate, granularity))) {
				outcomeAccumulated += tmpRecordsOutcome.get(cntOut).getAmount();
				cntOut++;
			}

			tmpDate = tmpDate.plusDays(1);

			if (GranularityEnum.getFormated(tmpDate, granularity).equals(key)) {
				continue;
			}

			outcomeMap.put(key, outcomeAccumulated * -1);
			outcomeAccumulated = 0;
			key = GranularityEnum.getFormated(tmpDate, granularity);

			if (tmpDate.isAfter(to)){
				break;
			}
		}

		Iterator<String> itt = outcomeMap.keySet().iterator();

		String tmpKey = null;

		Stack<Data<String,Number>> values = new Stack<>();

		while (itt.hasNext()){
			tmpKey = itt.next();

			if (outcomeMap.get(tmpKey) < 0)
				continue;

			if (outcomeMap.get(tmpKey) != 0)
				stats.addValue(outcomeMap.get(tmpKey));

			if (CIButton.isSelected()){
				values.push(new Data<>(tmpKey, outcomeMap.get(tmpKey)));
			}

			outcomeSeries.getData().add(new Data<>(tmpKey, outcomeMap.get(tmpKey)));
		}

		double ci = calcMeanCI(stats, 0.95);

		StackedBarChart.getData().setAll(outcomeSeries);

		XYChart.Series confidenceIntervalSeriesFill = new XYChart.Series();
		XYChart.Series confidenceIntervalSeries = new XYChart.Series();
		confidenceIntervalSeries.setName("95% confidence interval");
		confidenceIntervalSeriesFill.setName("");

		if (outcomeMap.size() > 10 && CIButton.isSelected()){

			while (!values.empty()){
				Data<String,Number> value = values.pop();

				if (value.getYValue().doubleValue() == 0){
					confidenceIntervalSeriesFill.getData().addAll(new Data<>(value.getXValue(),stats.getMean() - ci));
					confidenceIntervalSeries.getData().addAll(new Data<>(value.getXValue(),ci * 2));
				}

				if (value.getYValue().doubleValue() != 0)
					break;
			}

			StackedBarChart.getData().addAll(confidenceIntervalSeriesFill,confidenceIntervalSeries);
		}

		for (XYChart.Series<String, Number> series : StackedBarChart.getData()) {

			if (series.getName().equals("Spending during time period"))
				for (Data<String, Number> d : series.getData()) {
					Tooltip.install(d.getNode(), new Tooltip(d.getXValue().toString() + "\n" + "Value : " + String.format("%.2f", d.getYValue()) + " " + currency));
				}

			if (series.getName().equals("95% confidence interval"))
				for (Data<String, Number> d : series.getData()) {
					Tooltip.install(d.getNode(), new Tooltip(d.getXValue().toString() + "\n" + "95% confidence interval : "
							+ String.format("%.2f", stats.getMean() - d.getYValue().doubleValue() /2 ) + " " + currency + " - "
							+ String.format("%.2f", stats.getMean() + d.getYValue().doubleValue() /2 ) + " " + currency ));

				}
		}

	}

	public void initialize() {
		records = new ArrayList<>();
		currencySet = new HashSet<>();

		StackedBarChart.setAnimated(false);

		granularity = GranularityEnum.fromDouble(GranularitySlider.getValue());

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

	public void CIButtonOnAction(ActionEvent actionEvent) {
		filterData();
	}
}
