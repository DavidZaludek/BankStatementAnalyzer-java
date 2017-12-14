package main.Model;

import javafx.beans.property.SimpleStringProperty;
import main.InternalUtils.Enums.CategoryEnum;
import main.InternalUtils.Enums.TransactionTypeEnum;

import java.time.LocalDate;

public class TableRecord {

	private Record record;
	private final SimpleStringProperty id;
	private final LocalDate dateTime;
	private final Double amount;
	private final SimpleStringProperty currency;
	private final SimpleStringProperty bankName;
	private final TransactionTypeEnum type;
	private final SimpleStringProperty company;
	private final CategoryEnum category;

	public TableRecord(Record record,String id, LocalDate dateTime, double amount, String currency, String bankName, TransactionTypeEnum type, String company, CategoryEnum category) {

		this.record = record;
		this.id = new SimpleStringProperty(id);
		this.dateTime = dateTime;
		this.amount = amount;
		this.currency = new SimpleStringProperty(currency);
		this.bankName = new SimpleStringProperty(bankName);
		this.type = type;
		this.company = new SimpleStringProperty(company);
		this.category = category;
	}

	public String getId() {

		return id.get();
	}

	public SimpleStringProperty idProperty() {

		return id;
	}

	public void setId(String id) {

		this.id.set(id);
	}

	public LocalDate getDateTime() {

		return dateTime;
	}

	public Double getAmount() {

		return amount;
	}

	public String getCurrency() {

		return currency.get();
	}

	public SimpleStringProperty currencyProperty() {

		return currency;
	}

	public void setCurrency(String currency) {

		this.currency.set(currency);
	}

	public String getBankName() {

		return bankName.get();
	}

	public SimpleStringProperty bankNameProperty() {

		return bankName;
	}

	public void setBankName(String bankName) {

		this.bankName.set(bankName);
	}

	public String getCompany() {

		return company.get();
	}

	public SimpleStringProperty companyProperty() {

		return company;
	}

	public void setCompany(String company) {

		this.company.set(company);
	}

	public TransactionTypeEnum getType() {

		return type;
	}

	public CategoryEnum getCategory() {

		return record.getCategory();
	}

	public Record getRecord(){
		return record;
	}

	@Override
	public String toString() {
		return
				"id=" + getId() + ",\n" +
				"dateTime=" + dateTime + ",\n" +
				"amount=" + amount + ",\n" +
				"currency=" + getCurrency() + ",\n" +
				"bankName=" + getBankName() + ",\n" +
				"type=" + getType() + ",\n" +
				"company=" + getCompany();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (!(o instanceof TableRecord)) return false;

		TableRecord that = (TableRecord) o;

		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
