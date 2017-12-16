package main.Model.Filter.BarGraph;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;
import main.InternalUtils.Enums.GranularityEnum;
import main.Model.Record;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

import java.time.LocalDate;
import java.util.*;

public class BarGraphViewController extends AbstractController {

	ArrayList<Record> records;
	HashSet<String> currencySet;

	@FXML
	private DatePicker FromDatePicker;

	@FXML
	private DatePicker ToDatePicker;

	@FXML
	private ChoiceBox<String> CurrencySelection;

	@FXML
	private BarChart<String,Number> BarChart;

	@FXML
	private Slider GranularitySlider;

	private GranularityEnum granularity;

	@FXML
	void FromDatePickerSet(ActionEvent event) {
		filterData();
	}

	@FXML
	void ToDatePickerSet(ActionEvent event) {
		filterData();
	}

	public void initialize(){
		records = new ArrayList<>();
		currencySet = new HashSet<>();

		BarChart.setAnimated(false);

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

	private void filterData() {
		granularity = GranularityEnum.fromDouble(GranularitySlider.getValue());
		LocalDate from = FromDatePicker.getValue();
		LocalDate to = ToDatePicker.getValue();
		String currency = CurrencySelection.getValue();

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

		while (itt.hasNext()){
			tmpKey = itt.next();

			if (outcomeMap.get(tmpKey) < 0)
				continue;

			outcomeSeries.getData().add(new XYChart.Data<>(tmpKey, outcomeMap.get(tmpKey)));
		}

		BarChart.getData().setAll(outcomeSeries);

		for (XYChart.Series<String, Number> series : BarChart.getData()) {
			for (XYChart.Data<String, Number> d : series.getData()) {
				Tooltip.install(d.getNode(), new Tooltip(d.getXValue().toString() + "\n" + "Value : " + String.format("%.2f", d.getYValue()) + " " + currency));
			}
		}
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
