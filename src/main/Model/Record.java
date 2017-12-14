package main.Model;

import main.InternalUtils.Enums.CategoryEnum;
import main.InternalUtils.Enums.TransactionTypeEnum;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public class Record{
	private String GUID;
	private String FileGUID;
	private String UserGUID;
	private double Amount;
	private String Currency;
	private LocalDate DateTime;
	private CategoryEnum Category;
	private String CompanyName;
	private TransactionTypeEnum TransactionType;
	private String SpecialData;
	private String BankName;

	public Record(String fileGUID, String userGUID, double amount, String currency, LocalDate date, CategoryEnum category, TransactionTypeEnum transactionType, String specialData, String companyName, String bankName) {
		BankName = bankName;
		GUID = UUID.randomUUID().toString();
		FileGUID = fileGUID;
		UserGUID = userGUID;
		Amount = amount;
		Currency = currency;
		DateTime = date;
		Category = category;
		TransactionType = transactionType;
		SpecialData = specialData;
		CompanyName = companyName;
	}
	public Record(String GUID,String fileGUID, String userGUID, double amount, String currency, LocalDate date, CategoryEnum category, TransactionTypeEnum transactionType, String specialData, String companyName, String bankName) {
		BankName = bankName;
		this.GUID = GUID;
		FileGUID = fileGUID;
		UserGUID = userGUID;
		Amount = amount;
		Currency = currency;
		DateTime = date;
		Category = category;
		TransactionType = transactionType;
		SpecialData = specialData;
		CompanyName = companyName;
	}

	public String getGUID() {

		return GUID;
	}

	public String getFileGUID() {

		return FileGUID;
	}

	public String getUserGUID() {

		return UserGUID;
	}

	public double getAmount() {

		return Amount;
	}

	public String getCurrency() {

		return Currency;
	}

	public LocalDate getDateTime() {

		return DateTime;
	}

	public CategoryEnum getCategory() {
		return Category;
	}

	public TransactionTypeEnum getTransactionType() {

		return TransactionType;
	}

	public String getSpecialData() {

		return SpecialData;
	}

	public String getCompanyName() {

		return CompanyName;
	}

	public String getBankName() {
		return BankName;
	}

	public TableRecord getTableRecord(){
		return new TableRecord(this,getGUID(),getDateTime(),getAmount(),getCurrency(),getBankName(),getTransactionType(),getCompanyName(),getCategory());
	}

	public void setCategory(CategoryEnum category) {
		Category = category;
	}

	public static Comparator<Record> getDateTimeComparator() {
		return (o1, o2) -> o1.getDateTime().isAfter(o2.getDateTime()) ? 1:-1;
	}

	public static Comparator<Record> getAmountComparator() {
		return (o1, o2) -> o1.getAmount() > o2.getAmount() ? 1:-1;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (!(o instanceof Record)) return false;

		Record record = (Record) o;

		if (Double.compare(record.getAmount(), getAmount()) != 0) return false;
		if (!getUserGUID().equals(record.getUserGUID())) return false;
		if (!getCurrency().equals(record.getCurrency())) return false;
		if (!getDateTime().equals(record.getDateTime())) return false;
		if (getCategory() != record.getCategory()) return false;
		if (!getCompanyName().equals(record.getCompanyName())) return false;
		if (getTransactionType() != record.getTransactionType()) return false;
		if (!getSpecialData().equals(record.getSpecialData())) return false;
		return getBankName().equals(record.getBankName());
	}

	@Override
	public int hashCode() {

		int result;
		long temp;
		result = getUserGUID().hashCode();
		temp = Double.doubleToLongBits(getAmount());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + getCurrency().hashCode();
		result = 31 * result + getDateTime().hashCode();
		result = 31 * result + getCategory().hashCode();
		result = 31 * result + getCompanyName().hashCode();
		result = 31 * result + getTransactionType().hashCode();
		result = 31 * result + getSpecialData().hashCode();
		result = 31 * result + getBankName().hashCode();
		return result;
	}
}
