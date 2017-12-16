package main.View.FxmlFiles.SubViews;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.AppRoot.App;
import main.InternalUtils.Enums.CategoryEnum;
import main.InternalUtils.CategoryParser;
import main.Model.Record;
import main.Model.TableRecord;
import main.View.Enums.SubViewEnum;
import main.View.Enums.ViewEnum;
import main.View.FxmlFiles.AbstractController;
import main.View.Utils.LocalDateCellFactory;
import main.View.Utils.LocalDateComparator;
import main.View.ViewDataHandler;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class RecordsSubViewController extends AbstractController {
	public DatePicker FromDatePicker;
	public DatePicker ToDatePicker;

	public ChoiceBox<String> CurrencySelection;
	public ChoiceBox<String> CategorySelection;
	public ChoiceBox<String> TransactionTypeSelection;
	public ChoiceBox<String> CompanySelection;

	public ChoiceBox<String> SelectRecordCategory;
	public TextArea SelectedRecordText;
	public Button SetCategoryButton;
	public Button RemoveRecordButton;

	public Label AvgLabel;
	public Label SumLabel;
	public Label MinLabel;
	public Label MaxLabel;
	public Label CountLabel;

	@FXML
	TableView<TableRecord> recordTable;

	@FXML
	private TableColumn<TableRecord,String> typeCol;

	@FXML
	private TableColumn<TableRecord,String> idCol;

	@FXML
	private TableColumn<TableRecord,Double> amountCol;

	@FXML
	private TableColumn<TableRecord,String> currencyCol;

	@FXML
	private TableColumn<TableRecord,String> bankNameCol;

	@FXML
	private TableColumn<TableRecord,String> companyCol;

	@FXML
	private TableColumn<TableRecord,String> categoryCol;

	@FXML
	private TableColumn<TableRecord,LocalDate> dateCol;

	private ArrayList<Record> records;
	private HashSet<TableRecord> currentSet;

	private HashSet<String> currencySet;
	private HashSet<String> transactionTypeSet;
	private HashSet<String> companyNameSet;
	private HashSet<String> categorySet;

	private TableRecord selectedTableRecord;

	public void initialize(){
		currentSet = new HashSet<>();

		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		amountCol.setCellFactory(col ->
				new TableCell<TableRecord, Double>() {
					@Override
					public void updateItem(Double price, boolean empty) {
						super.updateItem(price, empty);
						if (empty) {
							setText(null);
						} else {
							setText(String.format("%.2f", price));
						}
					}
				});
		currencyCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
		bankNameCol.setCellValueFactory(new PropertyValueFactory<>("bankName"));
		companyCol.setCellValueFactory(new PropertyValueFactory<>("company"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

		dateCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
		dateCol.setCellFactory(new LocalDateCellFactory.LocalDateRecordCellFactory());
		dateCol.setComparator(new LocalDateComparator());

		recordTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedTableRecord = recordTable.getSelectionModel().getSelectedItem();
				updateRecordView();
			}
		});

		CurrencySelection.getSelectionModel()
				.selectedItemProperty()
				.addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					if (oldValue != null && newValue != null)					filterData();
				} );
		CompanySelection.getSelectionModel()
				.selectedItemProperty()
				.addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					if (oldValue != null && newValue != null)						filterData();
				} );
		TransactionTypeSelection.getSelectionModel()
				.selectedItemProperty()
				.addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					if (oldValue != null && newValue != null)
						filterData();
				} );
		CategorySelection.getSelectionModel()
				.selectedItemProperty()
				.addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					if (oldValue != null && newValue != null)						filterData();
				} );

	}

	private void updateRecordView() {
		SelectedRecordText.setText(selectedTableRecord.toString());
		SelectRecordCategory.setValue(selectedTableRecord.getCategory().toString());
	}

	public void SetCategoryButtonOnAction(ActionEvent actionEvent) {
		if (!selectedTableRecord.getCompany().equals("")){
			CategoryParser.addCategoryPair(selectedTableRecord.getCompany(),CategoryEnum.valueOf(SelectRecordCategory.getValue()));
			App.actionHandler.UpdateRecords();
		}else{
			App.actionHandler.SetRecordCategory(selectedTableRecord.getRecord(),CategoryEnum.valueOf(SelectRecordCategory.getValue()));
		}
		filterData();
	}

	public void RemoveRecordButtonOnAction(){
		App.actionHandler.RemoveRecord(selectedTableRecord.getRecord());
		records = App.userDataHandler.getUserData().getRecords();
		filterData();
	}

	public void FromDatePickerSet(ActionEvent actionEvent) {
		filterData();
	}

	public void ToDatePickerSet(ActionEvent actionEvent) {
		filterData();
	}

	private void filterData() {
		if (records.size() == 0){
			return;
		}

		SummaryStatistics tmpStatistics = new SummaryStatistics();

		SelectRecordCategory.getItems().setAll(FXCollections.observableArrayList(Arrays.asList(CategoryParser.getCategoriesAsString())));

		HashSet<TableRecord> tmpTableFiles = new HashSet<>();

		for (int i = 0; i < records.size(); i++) {
			Record tmpRecord = records.get(i);
			if (!(tmpRecord.getCurrency().equals( CurrencySelection.getValue()) || CurrencySelection.getValue().equals( "ALL"))){
				continue;
			}

			String company = CompanySelection.getValue();

			if (!(tmpRecord.getCompanyName().equals(CompanySelection.getValue()) || company.equals( "ALL"))){
				continue;
			}
			if (!(tmpRecord.getTransactionType().toString().equals(TransactionTypeSelection.getValue()) || TransactionTypeSelection.getValue().equals( "ALL"))){
				continue;
			}
			if (!(tmpRecord.getCategory().toString().equals(CategorySelection.getValue()) || CategorySelection.getValue().equals( "ALL"))){
				continue;
			}
			if (tmpRecord.getDateTime().isBefore(FromDatePicker.getValue()) || tmpRecord.getDateTime().isAfter(ToDatePicker.getValue())){
				continue;
			}

			tmpStatistics.addValue(records.get(i).getAmount());

			tmpTableFiles.add(records.get(i).getTableRecord());
		}

		TableColumn sortcolumn = null;
		TableColumn.SortType st = null;
		if (recordTable.getSortOrder().size()>0) {
			sortcolumn = (TableColumn) recordTable.getSortOrder().get(0);
			st = sortcolumn.getSortType();
		}

		if (currentSet.equals(tmpTableFiles)){
			recordTable.refresh();
			return;
		}

		currentSet = tmpTableFiles;

		recordTable.setItems(FXCollections.observableArrayList(tmpTableFiles));
		recordTable.refresh();

		if (sortcolumn!=null) {
			recordTable.getSortOrder().add(sortcolumn);
			sortcolumn.setSortType(st);
			sortcolumn.setSortable(true); // This performs a sort
		}

		CountLabel.setText("Count: " + tmpTableFiles.size());
		SumLabel.setText("Sum: " + String.format( "%.2f", tmpStatistics.getSum()));
		MinLabel.setText("Min: " + String.format( "%.2f", tmpStatistics.getMin()));
		MaxLabel.setText("Max: " + String.format( "%.2f", tmpStatistics.getMax()));
		AvgLabel.setText("Avg: " + String.format( "%.2f", tmpStatistics.getMean()));
	}

	@Override
	public void setViewData(ViewDataHandler viewData) {
		records = new ArrayList<>();

		currencySet = viewData.getUser().getCurrencySet();
		transactionTypeSet =viewData.getUser().getTransactionTypeSet();
		companyNameSet = viewData.getUser().getCompanyNameSet();
		categorySet = viewData.getUser().getCategorySet();

		if (viewData.getSubViewEnum() == SubViewEnum.RecordsView && viewData.getViewEnum() == ViewEnum.HomePageView){
			records.addAll(viewData.getUser().getRecords());

			if (records.size() == 0) {
				recordTable.setItems(FXCollections.observableArrayList());
				recordTable.refresh();
				CountLabel.setText("Count: " + 0);
				SumLabel.setText("Sum: " + 0);
				MinLabel.setText("Min: " + 0);
				MaxLabel.setText("Max: " + 0);
				AvgLabel.setText("Avg: " + 0);
				return;
			}

			Collections.sort(records,Record.getDateTimeComparator());

			FromDatePicker.setValue(records.get(0).getDateTime());
			ToDatePicker.setValue(records.get(records.size()-1).getDateTime());

			CurrencySelection.getItems().setAll("ALL");
			CurrencySelection.getItems().addAll(FXCollections.observableArrayList(currencySet));
			CurrencySelection.setValue("ALL");

			TransactionTypeSelection.getItems().setAll("ALL");
			TransactionTypeSelection.getItems().addAll(FXCollections.observableArrayList(transactionTypeSet));
			TransactionTypeSelection.setValue("ALL");

			CompanySelection.getItems().setAll("ALL");
			CompanySelection.getItems().addAll(FXCollections.observableArrayList(companyNameSet));
			CompanySelection.setValue("ALL");

			CategorySelection.getItems().setAll("ALL");
			CategorySelection.getItems().addAll(FXCollections.observableArrayList(categorySet));
			CategorySelection.setValue("ALL");

			filterData();
		}

	}

}
