package main.Model.Filter.PieGraph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import main.InternalUtils.MapUtil;
import main.Model.Record;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PieGraphViewController extends AbstractController {

	public DatePicker FromDatePicker;
	public DatePicker ToDatePicker;
	public ChoiceBox<String> CurrencySelection;


	public RadioButton TransactionRadio;
	public RadioButton CategoryRadio;
	public RadioButton CompanyRadio;
	public RadioButton BankRadio;
	public RadioButton MonthRadio;
	public RadioButton WeekDaysRadio;

	@FXML
	private PieChart IncomeChart;
	@FXML
	private PieChart SpendingChart;

	private ArrayList<Record> records;
	private HashSet<String> currencySet;

	public void ToDatePickerSet(ActionEvent actionEvent) {
		filterData();

	}

	public void FromDatePickerSet(ActionEvent actionEvent) {
		filterData();
	}

	private void filterData() {

		LocalDate from = FromDatePicker.getValue();
		LocalDate to = ToDatePicker.getValue();
		String currency = CurrencySelection.getValue();

		ArrayList<Record> tmpRecordsOutcome = new ArrayList<>();
		ArrayList<Record> tmpRecordsIncome = new ArrayList<>();

		LinkedHashMap<String, Double> spendingData = new LinkedHashMap<>();
		LinkedHashMap<String, Double> incomeData = new LinkedHashMap<>();

		for (int i = 0; i < records.size(); i++) {
			Record tmpRecord = records.get(i);
			if (tmpRecord.getDateTime().isBefore(to) && tmpRecord.getDateTime().isAfter(from) && tmpRecord.getCurrency().equals(currency) ) {
				if (TransactionRadio.isSelected()){
					if (tmpRecord.getAmount() > 0){
						AddToHashMap(tmpRecord.getTransactionType().toString(),tmpRecord.getAmount(),incomeData);
					}else{
						AddToHashMap(tmpRecord.getTransactionType().toString(),tmpRecord.getAmount(),spendingData);
					}
				}
				if (CategoryRadio.isSelected()){
					if (tmpRecord.getCategory().equals("UNKNOWN")){
						continue;
					}
					if (tmpRecord.getAmount() > 0){
						AddToHashMap(tmpRecord.getCategory().toString(),tmpRecord.getAmount(),incomeData);
					}else{
						AddToHashMap(tmpRecord.getCategory().toString(),tmpRecord.getAmount(),spendingData);
					}
				}
				if (CompanyRadio.isSelected()){
					if (tmpRecord.getCompanyName().equals("")){
						continue;
					}
					if (tmpRecord.getAmount() > 0){
						AddToHashMap(tmpRecord.getCompanyName(),tmpRecord.getAmount(),incomeData);
					}else{
						AddToHashMap(tmpRecord.getCompanyName(),tmpRecord.getAmount(),spendingData);
					}
				}
				if (BankRadio.isSelected()){
					if (tmpRecord.getAmount() > 0){
						AddToHashMap(tmpRecord.getBankName(),tmpRecord.getAmount(),incomeData);
					}else{
						AddToHashMap(tmpRecord.getBankName(),tmpRecord.getAmount(),spendingData);
					}
				}
				if (MonthRadio.isSelected()){
					if (tmpRecord.getAmount() > 0){
						AddToHashMap(tmpRecord.getDateTime().format(DateTimeFormatter.ofPattern("MMMM")),tmpRecord.getAmount(),incomeData);
					}else{
						AddToHashMap(tmpRecord.getDateTime().format(DateTimeFormatter.ofPattern("MMMM")),tmpRecord.getAmount(),spendingData);
					}
				}
				if (WeekDaysRadio.isSelected()){
					if (tmpRecord.getAmount() > 0){
						AddToHashMap(tmpRecord.getDateTime().format(DateTimeFormatter.ofPattern("cccc")),tmpRecord.getAmount(),incomeData);
					}else{
						AddToHashMap(tmpRecord.getDateTime().format(DateTimeFormatter.ofPattern("cccc")),tmpRecord.getAmount(),spendingData);
					}
				}

			}
		}

		incomeData = (LinkedHashMap<String, Double>) MapUtil.sortByValue(incomeData);
		spendingData = (LinkedHashMap<String, Double>) MapUtil.sortByValue(spendingData);

		ObservableList<PieChart.Data> spendingObservableData =
				FXCollections.observableArrayList();
		ObservableList<PieChart.Data> incomeObservableData =
				FXCollections.observableArrayList();

		Iterator<String> itt = incomeData.keySet().iterator();

		String tmpKey = null;

		while (itt.hasNext()){
			tmpKey = itt.next();
			incomeObservableData.add(new PieChart.Data(tmpKey, incomeData.get(tmpKey)));
		}

		itt = spendingData.keySet().iterator();

		while (itt.hasNext()){
			tmpKey = itt.next();
			spendingObservableData.add(new PieChart.Data(tmpKey, spendingData.get(tmpKey)));
		}

		SpendingChart.setData(spendingObservableData);
		IncomeChart.setData(incomeObservableData);
	}

	public void AddToHashMap(String key,Double value,HashMap<String,Double> spendingData) {

		Double tmpAmount = 0.0;

		if (spendingData.containsKey(key)) {
			tmpAmount += spendingData.get(key);
		}
		tmpAmount += -1 * value;
		spendingData.put(key, tmpAmount);
	}



	public void initialize(){

		ToggleGroup toggleGroup = new ToggleGroup();

		TransactionRadio.setToggleGroup(toggleGroup);
		CategoryRadio.setToggleGroup(toggleGroup);
		CompanyRadio.setToggleGroup(toggleGroup);
		BankRadio.setToggleGroup(toggleGroup);
		MonthRadio.setToggleGroup(toggleGroup);
		WeekDaysRadio.setToggleGroup(toggleGroup);
		CategoryRadio.setSelected(true);

		toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> filterData());

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
