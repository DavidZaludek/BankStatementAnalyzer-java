package main.View.FxmlFiles.SubViews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import main.AppRoot.App;
import main.InternalUtils.Exceptions.BankNotFoundException;
import main.Model.Bank.AbstractBank;
import main.Model.Bank.BankFactory;
import main.Model.TableFile;
import main.View.Enums.SubViewEnum;
import main.View.FxmlFiles.AbstractController;
import main.View.Utils.LocalDateCellFactory;
import main.View.Utils.LocalDateComparator;
import main.View.ViewDataHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FilesSubViewController extends AbstractController {
	public Button FileUploadButton;
	public Button ChooseFileButton;
	public ChoiceBox<String> BankSelector;
	public Label FileNameLabel;

	@FXML
	public TableView<TableFile> FileTable;
	@FXML
	public TableColumn<TableFile, String> idCol;
	@FXML
	public TableColumn<TableFile, String> fileNameCol;
	@FXML
	public TableColumn<TableFile, LocalDate> dateFromCol;
	@FXML
	public TableColumn<TableFile, LocalDate> dateToCol;
	@FXML
	public TableColumn<TableFile, String> bankNameCol;
	@FXML
	public TableColumn<TableFile, String> numOfRecordsCol;

	private final ObservableList<TableFile> data =
			FXCollections.observableArrayList();

	private File currentFile;

	@FXML
	protected void initialize() {

		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		fileNameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));

		dateFromCol.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));
		dateFromCol.setCellFactory(new LocalDateCellFactory.LocalDateFileCellFactory());
		dateFromCol.setComparator(new LocalDateComparator());

		dateToCol.setCellValueFactory(new PropertyValueFactory<>("dateTo"));
		dateToCol.setCellFactory(new LocalDateCellFactory.LocalDateFileCellFactory());
		dateToCol.setComparator(new LocalDateComparator());

		bankNameCol.setCellValueFactory(new PropertyValueFactory<>("bankName"));
		numOfRecordsCol.setCellValueFactory(new PropertyValueFactory<>("numOfRecords"));

		FileTable.setItems(data);

		ArrayList<String> tmpBanks = BankFactory.getBankNames();
		BankSelector.getItems().setAll(FXCollections.observableArrayList(tmpBanks));

	}

	public void ChooseFileButtonOnAction(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");

		currentFile = fileChooser.showOpenDialog(App.getRootStage());
		FileNameLabel.setText(currentFile.getName());
	}

	public void FileUploadButtonOnAction(){

		AbstractBank tmpBank;

		try {
			tmpBank = BankFactory.getBankInstanceByName(BankSelector.getValue());
		} catch (BankNotFoundException e) {
			return;
		}

		try {
			App.actionHandler.AddFile(tmpBank,currentFile);
		} catch (IOException e) {
			return;
		} catch (SQLException e) {
			return;
		}
		App.Update();
	}

	@Override
	public void setViewData(ViewDataHandler viewData) {

		if (viewData.getSubViewEnum() != SubViewEnum.FilesView){
			return;
		}

		ArrayList<TableFile> tmpTableFiles = new ArrayList<>();

		for (int i = 0; i < viewData.getUser().getFiles().size(); i++) {
			tmpTableFiles.add(viewData.getUser().getFiles().get(i).getTableFile());
		}

		FileTable.setItems(FXCollections.observableArrayList(tmpTableFiles));
		FileTable.refresh();
	}
}
