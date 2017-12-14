package main.Model;

import main.Model.Bank.AbstractBank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class User {
	private String name;
	private String password;

	private String GUID;
	private ArrayList<Record> records;
	private ArrayList<File> files;
	private ArrayList<AbstractBank> banks;

	private HashSet<String> currencySet;
	private HashSet<String> transactionTypeSet;
	private HashSet<String> companyNameSet;
	private HashSet<String> categorySet;

	public User(String name, String password) {
		this.password = password;
		this.GUID = UUID.randomUUID().toString();
		this.name = name;
		this.records = new ArrayList<Record>();
		this.banks = new ArrayList<AbstractBank>();
		this.files = new ArrayList<File>();

		currencySet = new HashSet<>();
		transactionTypeSet = new HashSet<>();
		companyNameSet = new HashSet<>();
		categorySet = new HashSet<>();
	}

	public User(String name, String GUID, ArrayList<File> files) {
		this.name = name;
		this.GUID = GUID;
		this.files = new ArrayList<>();
		this.banks = banks;
		this.records = new ArrayList<>();

		currencySet = new HashSet<>();
		transactionTypeSet = new HashSet<>();
		companyNameSet = new HashSet<>();
		categorySet = new HashSet<>();

		for (int i = 0; i < files.size(); i++) {
			File tmpFile = files.get(i);
			AddFile(tmpFile);
		}
	}

	public void AddFile(File file){
		this.files.add(file);
		this.records.addAll(file.getRecords());
		for (int i = 0; i < file.getRecords().size(); i++) {
			currencySet.add(file.getRecords().get(i).getCurrency());
			transactionTypeSet.add(file.getRecords().get(i).getTransactionType().toString());
			companyNameSet.add(file.getRecords().get(i).getCompanyName());
			categorySet.add(file.getRecords().get(i).getCategory().toString());
		}
	}

	public String getName() {

		return name;
	}

	public String getGUID() {

		return GUID;
	}

	public ArrayList<Record> getRecords() {

		return records;
	}

	public ArrayList<AbstractBank> getBanks() {
		return banks;
	}

	@Override
	public String toString() {
		return String.format("User{name='%s', userID='%s', records=%s}", name, GUID, records);
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<File> getFiles() {

		return files;
	}

	public HashSet<String> getCurrencySet() {

		return currencySet;
	}

	public HashSet<String> getTransactionTypeSet() {

		return transactionTypeSet;
	}

	public HashSet<String> getCompanyNameSet() {

		return companyNameSet;
	}

	public HashSet<String> getCategorySet() {

		return categorySet;
	}

	public void removeRecord(Record record) {
		records.remove(record);
	}

	public void removeFile(File file) {
		files.remove(file);
	}
}
