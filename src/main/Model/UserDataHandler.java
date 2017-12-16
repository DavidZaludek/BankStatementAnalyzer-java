package main.Model;

import main.DBUtils.Database;
import main.DBUtils.Exceptions.UserExistsException;
import main.DBUtils.Exceptions.UserNotFoundException;
import main.InternalUtils.CategoryParser;
import main.InternalUtils.Enums.CategoryEnum;
import main.InternalUtils.Exceptions.InvalidLoginDetailsException;
import main.Model.Bank.AbstractBank;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class UserDataHandler {

	private Database database;
	private User userData;

	private HashSet<Record> usersRecords;
	private HashSet<File> userFiles;

	public UserDataHandler(Database database) {
		this.database = database;
		this.userData = new User("Guest","aaaaa");
		usersRecords = new HashSet<>();
		userFiles = new HashSet<>();
	}

	public User getUserData() {
		return userData;
	}

	public void AddFile(AbstractBank bank, java.io.File file) throws SQLException, IOException {
		File tmpFile = bank.ParseFile(userData,file);

		ArrayList<Record> tmpRecords = new ArrayList<>();

		for (int i = 0; i < tmpFile.getRecords().size(); i++) {
			Record tmpRecord = tmpFile.getRecords().get(i);

			if (!usersRecords.contains(tmpRecord)){
				tmpRecords.add(tmpRecord);
				usersRecords.add(tmpRecord);
			}
		}

		tmpFile.setRecords(tmpRecords);
		userData.AddFile(tmpFile);

		userFiles.add(tmpFile);

		ArrayList<File> tmpFiles = new ArrayList<>();
		tmpFiles.add(tmpFile);
		database.AddFiles(tmpFiles);
	}

	public void LoginUser(String username, String password) throws UserNotFoundException, SQLException, InvalidLoginDetailsException {
		this.userData = database.LoadUser(username,password);

		userFiles = new HashSet<>();
		for (File tmpFile: this.userData.getFiles()) {
			userFiles.add(tmpFile);
		}

		usersRecords = new HashSet<>();
		for (Record tmpRecord: this.userData.getRecords()) {
			usersRecords.add(tmpRecord);
		}
	}

	public void SignUpUser(String username, String password) throws UserExistsException, SQLException {
		User tmpUser = new User(username,password);

		if (username.equals("Guest"))
		{
			try {
				database.RemoveUser(username);
			} catch (UserNotFoundException userNotFoundExeption) {

			}
		}

		database.AddUser(tmpUser);
		userData = tmpUser;
		usersRecords = new HashSet<>();
	}

	public void LogOut() {
		this.userData = new User("Guest","aaaaa");
	}

	public void ReCategorizeRecords() {
		for (int i = 0; i < userData.getRecords().size(); i++) {
			Record tmpRecord = userData.getRecords().get(i);
			if (!CategoryParser.getCategory(tmpRecord.getCompanyName()).equals(CategoryEnum.UNKNOWN))
				SetRecordCategory(tmpRecord,CategoryParser.getCategory(tmpRecord.getCompanyName()));
		}
	}

	public void SetRecordCategory(Record record, CategoryEnum categoryEnum) {
		if (!record.getCategory().equals(categoryEnum)){
			record.setCategory(categoryEnum);
			try {
				database.UpdateRecord(record);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void RemoveRecord(Record record) {

		//removing from user records
		usersRecords.remove(record);

		//removing from user data
		this.userData.removeRecord(record);

		//removing from file
		for (File tmpFile: userFiles) {
			if (tmpFile.getGUID().equals(record.getFileGUID())){
				tmpFile.removeRecord(record);
			}
		}

		//remove from database
		ArrayList<Record> tmpFiles = new ArrayList<>();
		tmpFiles.add(record);

		try {
			database.RemoveRecords(tmpFiles);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void RemoveFile(File file){

		for (Record rec : file.getRecords()){
			usersRecords.remove(rec);
			this.userData.removeRecord(rec);
		}

		userFiles.remove(file);

		try {
			database.RemoveRecords(file.getRecords());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.userData.removeFile(file);
	}
}
