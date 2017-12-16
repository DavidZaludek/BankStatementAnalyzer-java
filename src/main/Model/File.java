package main.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class File {
	private String GUID;
	private String UserGUID;
	private String FileName;
	private LocalDate FromDateTime;
	private LocalDate ToDateTime;
	private String BankName;
	private ArrayList<Record> records;

	public File(String GUID,String userGUID, String fileName, LocalDate fromDateTime, LocalDate toDateTime, String bankName,ArrayList<Record> records) {
		init(GUID,userGUID,fileName,fromDateTime,toDateTime,bankName,records);
	}

	public File(String userGUID, String fileName, LocalDate fromDateTime, LocalDate toDateTime, String bankName, ArrayList<Record> records) {
		init(UUID.randomUUID().toString(),userGUID,fileName,fromDateTime,toDateTime,bankName,records);
	}

	private void init(String GUID,String userGUID, String fileName, LocalDate fromDateTime, LocalDate toDateTime, String bankName,ArrayList<Record> records){
		this.GUID = GUID;
		UserGUID = userGUID;
		FileName = fileName;
		FromDateTime = fromDateTime;
		ToDateTime = toDateTime;
		BankName = bankName;
		this.records = records;
	}

	public String getUserGUID() {

		return UserGUID;
	}

	public String getFileName() {

		return FileName;
	}

	public LocalDate getFromDateTime() {

		return FromDateTime;
	}

	public LocalDate getToDateTime() {

		return ToDateTime;
	}

	public String getBankName() {

		return BankName;
	}

	public ArrayList<Record> getRecords() {

		return records;
	}

	public void setRecords(ArrayList<Record> records) {

		this.records = records;
	}

	public String getGUID() {

		return GUID;
	}

	public void setFromDateTime(LocalDate fromDateTime) {

		FromDateTime = fromDateTime;
	}

	public void setToDateTime(LocalDate toDateTime) {

		ToDateTime = toDateTime;
	}

	public TableFile getTableFile(){
		return new TableFile(this,getGUID(),getFileName(), getFromDateTime(), getToDateTime(), getBankName(), String.valueOf(getRecords().size()));
	}

	public void removeRecord(Record record){
		records.remove(record);
	}
}

