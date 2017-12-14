package main.Model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class TableFile {
	private final SimpleStringProperty id;
	private final SimpleStringProperty fileName;
	private final LocalDate dateFrom;
	private final LocalDate dateTo;
	private final SimpleStringProperty bankName;
	private final SimpleStringProperty numOfRecords;

	public TableFile(String id, String fileName, LocalDate dateFrom, LocalDate dateTo, String bankName, String numOfRecords) {
		this.id = new SimpleStringProperty(id);
		this.fileName = new SimpleStringProperty(fileName);
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.bankName = new SimpleStringProperty(bankName);
		this.numOfRecords = new SimpleStringProperty(numOfRecords);
	}

	public String getId() {

		return id.get();
	}

	public String getBankName() {

		return bankName.get();
	}


	public String getNumOfRecords() {

		return numOfRecords.get();
	}


	public String getFileName() {

		return fileName.get();
	}


	public void setId(String id) {

		this.id.set(id);
	}

	public void setFileName(String fileName) {

		this.fileName.set(fileName);
	}

	public void setBankName(String bankName) {

		this.bankName.set(bankName);
	}

	public void setNumOfRecords(String numOfRecords) {

		this.numOfRecords.set(numOfRecords);
	}

	public LocalDate getDateFrom() {

		return dateFrom;
	}

	public LocalDate getDateTo() {

		return dateTo;
	}

}