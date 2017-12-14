package main.DBUtils;

import main.DBUtils.Exceptions.UserExistsException;
import main.DBUtils.Exceptions.UserNotFoundException;
import main.InternalUtils.Crypto.BCrypt;
import main.InternalUtils.Enums.CategoryEnum;
import main.InternalUtils.Enums.TransactionTypeEnum;
import main.InternalUtils.Exceptions.InvalidLoginDetailsException;
import main.Model.File;
import main.Model.Record;
import main.Model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {

	private boolean Initialized;
	private boolean Connected;

	private Connection Connection = null;
	private String Path;

	public Database(String path) {
		Path = path;
	}

	private void Connect() throws SQLException {
		String url = "jdbc:sqlite:" + Path;
		try {

			Connection = DriverManager.getConnection(url);

			if (Connection != null) {
				Connected = true;

				DatabaseMetaData meta = Connection.getMetaData();

				String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
						+ "    UID INTEGER PRIMARY KEY,\n"
						+ "    GUID text,\n"
						+ "    Username text,\n"
						+ "    PassHash text\n" +")";

				Statement stmt = Connection.createStatement();
				stmt.execute(sql);

				sql = "CREATE TABLE IF NOT EXISTS Files (\n"
						+ "    UID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
						+ "    GUID TEXT,\n"
						+ "    UserGUID TEXT,\n"
						+ "    FileName TEXT,\n"
						+ "    FromDateTime TEXT,\n"
						+ "    ToDateTime TEXT,\n"
						+ "    BankName TEXT\n"
						+ ");";

				stmt = Connection.createStatement();
				stmt.execute(sql);

				sql = "CREATE TABLE IF NOT EXISTS Records (\n"
						+ "    UID INTEGER PRIMARY KEY AUTOINCREMENT, \n"
						+ "    GUID TEXT, \n"
						+ "    FileGUID TEXT, \n"
						+ "    UserGUID TEXT, \n"
						+ "    Amount REAL, \n"
						+ "    Currency TEXT, \n"
						+ "    DateTime TEXT, \n"
						+ "    Category TEXT, \n"
						+ "    TransactionType TEXT, \n"
						+ "    SpecialData TEXT, \n"
						+ "    CompanyName TEXT, \n"
						+ "    BankName TEXT \n"
						+ "); \n";

				stmt = Connection.createStatement();
				stmt.execute(sql);

				System.out.println("Database initialized.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public User LoadUser(String name, String password) throws UserNotFoundException,InvalidLoginDetailsException, SQLException {

		Init();

		String sql = "SELECT * FROM Users";

		Statement stmt = Connection.createStatement();
		ResultSet resultSetUsers = stmt.executeQuery(sql);

		try {
			while (resultSetUsers.next()) {

				String tmpName = resultSetUsers.getString("Username");

				if (tmpName.equals(name)) {
					String tmpPassHash = resultSetUsers.getString("PassHash");

					if (!BCrypt.checkpw(password, tmpPassHash)) {
						throw new InvalidLoginDetailsException();
					}

					System.out.println(resultSetUsers.getInt("UID")
							+ "\n" + resultSetUsers.getString("Username")
							+ "\n" + resultSetUsers.getString("GUID")
							+ "\n" + resultSetUsers.getString("PassHash"));

					String tmpUsername = resultSetUsers.getString("Username");
					String tmpGUID = resultSetUsers.getString("GUID");

					resultSetUsers.close();

					ArrayList<File> tmpFiles = loadFiles(tmpGUID);

					return new User(tmpUsername,tmpGUID,tmpFiles);
				}
			}

			throw new UserNotFoundException(name);

		} catch (Exception e) {
			resultSetUsers.close();
			throw e;
		}
	}

	public void AddUser(User user) throws UserExistsException, SQLException {
		Init();

		try {
			LoadUser(user.getName(),"");
		} catch (UserNotFoundException userNotFoundExeption) {
		} catch (InvalidLoginDetailsException e) {
			throw new UserExistsException(user);
		}

		String sql = "INSERT INTO Users(Username,Guid,PassHash) VALUES(?,?,?)";

		String passSalt = BCrypt.gensalt();
		String passHash = BCrypt.hashpw(user.getPassword(),passSalt);

		PreparedStatement pstmt = Connection.prepareStatement(sql) ;
		pstmt.setString(1, user.getName());
		pstmt.setString(2, user.getGUID());
		pstmt.setString(3, passHash);
		pstmt.executeUpdate();
	}

	public void RemoveUser(String name) throws SQLException, UserNotFoundException {

		String sql = "SELECT GUID FROM Users WHERE Username=?";

		PreparedStatement pstmt = Connection.prepareStatement(sql) ;
		pstmt.setString(1, name);
		ResultSet resultSetUserIDs = pstmt.executeQuery();

		String tmpUserGUID = null;

		try {

			while (resultSetUserIDs.next()) {

				tmpUserGUID = resultSetUserIDs.getString("GUID");

				resultSetUserIDs.close();

			}

		} catch (Exception e) {
			resultSetUserIDs.close();
			throw e;
		}

		if (tmpUserGUID == null)
			throw new UserNotFoundException(name);

		sql = "SELECT UID FROM Files WHERE UserGUID=?";

		pstmt = Connection.prepareStatement(sql) ;
		pstmt.setString(1, tmpUserGUID);
		ResultSet resultSetUserFiles = pstmt.executeQuery();

		ArrayList<Integer> rawUserFileUIDs = new ArrayList<>();

		try {
			while (resultSetUserFiles.next()) {
				rawUserFileUIDs.add(resultSetUserFiles.getInt("UID"));
			}
			resultSetUserFiles.close();
		} catch (Exception e) {
			resultSetUserFiles.close();
			throw e;
		}

		sql = "SELECT UID FROM Records WHERE UserGUID=?";
		pstmt = Connection.prepareStatement(sql) ;
		pstmt.setString(1, tmpUserGUID);
		ResultSet resultSetUserRecords = pstmt.executeQuery();

		ArrayList<Integer> rawUserRecordsUIDs = new ArrayList<>();

		try {
			while (resultSetUserRecords.next()) {
				rawUserRecordsUIDs.add(resultSetUserRecords.getInt("UID"));
			}
			resultSetUserRecords.close();
		} catch (Exception e) {
			resultSetUserRecords.close();
			throw e;
		}

		for (int i = 0; i < rawUserRecordsUIDs.size(); i++) {

			sql = "DELETE FROM Records WHERE UID = ?";
			pstmt = Connection.prepareStatement(sql);
			pstmt.setInt(1, rawUserRecordsUIDs.get(i));
			pstmt.executeUpdate();
		}

		for (int i = 0; i < rawUserFileUIDs.size(); i++) {

			sql = "DELETE FROM Files WHERE UID = ?";
			pstmt = Connection.prepareStatement(sql);
			pstmt.setInt(1, rawUserFileUIDs.get(i));
			pstmt.executeUpdate();

		}

		sql = "DELETE FROM Users WHERE GUID = ?";
		pstmt = Connection.prepareStatement(sql);
		pstmt.setString(1, tmpUserGUID);
		pstmt.executeUpdate();

	}

	public void AddFiles(List<File> files) throws SQLException {
		for (int i = 0; i < files.size(); i++) {
			File tmpFile = files.get(i);

			String sql = "INSERT INTO Files(GUID,UserGUID,FileName,FromDateTime,ToDateTime,BankName) VALUES(?,?,?,?,?,?)";

			PreparedStatement pstmt = Connection.prepareStatement(sql) ;
			pstmt.setString(1, tmpFile.getGUID());
			pstmt.setString(2, tmpFile.getUserGUID());
			pstmt.setString(3, tmpFile.getFileName());
			pstmt.setString(4, tmpFile.getFromDateTime().toString());
			pstmt.setString(5, tmpFile.getToDateTime().toString());
			pstmt.setString(6, tmpFile.getBankName());
			pstmt.executeUpdate();

			AddRecords(tmpFile.getRecords());
		}
	}

	private ArrayList<File> loadFiles(String userGUID) throws SQLException {

		ArrayList<File> tmpFiles = new ArrayList<>();

		String sql = "SELECT * FROM Files WHERE UserGUID=?";

		PreparedStatement pstmt = Connection.prepareStatement(sql) ;
		pstmt.setString(1, userGUID);
		ResultSet resultSetFiles = pstmt.executeQuery();

		try {
			while (resultSetFiles.next()){

				tmpFiles.add( new File(
						resultSetFiles.getString("GUID"),
						resultSetFiles.getString("UserGUID"),
						resultSetFiles.getString("FileName"),
						LocalDate.parse(resultSetFiles.getString("FromDateTime")),
						LocalDate.parse(resultSetFiles.getString("ToDateTime")),
						resultSetFiles.getString("BankName"),
						new ArrayList<Record>()));
			}
		} catch (Exception e) {
			resultSetFiles.close();
			throw e;
		}

		resultSetFiles.close();

		for (int i = 0; i < tmpFiles.size(); i++) {
			File tmpFile = tmpFiles.get(i);
			tmpFile.setRecords(loadRecords(tmpFile.getGUID()));
		}

		return tmpFiles;
	}

	public void RemoveFiles(List<File> files) throws SQLException {
		for (int i = 0; i < files.size(); i++) {
			File tmpFile = files.get(i);

			RemoveRecords(tmpFile.getRecords());

			String sql = "DELETE FROM Files WHERE GUID=?";

			PreparedStatement pstmt = Connection.prepareStatement(sql);
			pstmt.setString(1, tmpFile.getGUID());
			pstmt.executeUpdate();
		}
	}

	public void UpdateRecord(Record record) throws SQLException {
		String sql = "UPDATE Records SET Category = ? WHERE GUID = ? ";

		PreparedStatement pstmt = Connection.prepareStatement(sql);
		pstmt.setString(1, record.getGUID());
		pstmt.setString(2, record.getCategory().toString());
		pstmt.executeUpdate();
	}

	public void RemoveRecords(List<Record> records) throws SQLException {
		for (int i = 0; i < records.size(); i++) {
			Record tmpRecord = records.get(i);

			String sql = "DELETE FROM Records WHERE GUID=?";

			PreparedStatement pstmt = Connection.prepareStatement(sql);
			pstmt.setString(1, tmpRecord.getGUID());
			pstmt.executeUpdate();
		}
	}

	public void AddRecords(List<Record> records) throws SQLException {

		for (int i = 0; i < records.size(); i++) {
			Record tmpRecord = records.get(i);

			String sql = "INSERT INTO Records(GUID,FileGUID,UserGUID,Amount,Currency,DateTime,Category,TransactionType,SpecialData,CompanyName,BankName) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

			PreparedStatement pstmt = Connection.prepareStatement(sql);
			pstmt.setString(1, tmpRecord.getGUID());
			pstmt.setString(2, tmpRecord.getFileGUID());
			pstmt.setString(3, tmpRecord.getUserGUID());
			pstmt.setDouble(4, tmpRecord.getAmount());
			pstmt.setString(5, tmpRecord.getCurrency());
			pstmt.setString(6, tmpRecord.getDateTime().toString());
			pstmt.setString(7, tmpRecord.getCategory().toString());
			pstmt.setString(8, tmpRecord.getTransactionType().toString());
			pstmt.setString(9, tmpRecord.getSpecialData());
			pstmt.setString(10, tmpRecord.getCompanyName());
			pstmt.setString(11, tmpRecord.getBankName());

			pstmt.executeUpdate();
		}

	}

	private ArrayList<Record> loadRecords(String fileGUID) throws SQLException {

		ArrayList<Record> resultRecords = new ArrayList<>();

		String sql = "SELECT * FROM Records WHERE FileGUID=?";

		PreparedStatement stmt = Connection.prepareStatement(sql);
		stmt.setString(1,fileGUID);
		ResultSet resultSetFiles = stmt.executeQuery();

		try {
			while (resultSetFiles.next()){
				resultRecords.add(new Record(
						resultSetFiles.getString("GUID"),
						resultSetFiles.getString("FileGUID"),
						resultSetFiles.getString("UserGUID"),
						resultSetFiles.getDouble("Amount"),
						resultSetFiles.getString("Currency"),
						LocalDate.parse(resultSetFiles.getString("DateTime")),
						CategoryEnum.valueOf(resultSetFiles.getString("Category")),
						TransactionTypeEnum.valueOf(resultSetFiles.getString("TransactionType")),
						resultSetFiles.getString("SpecialData"),
						resultSetFiles.getString("CompanyName"),
						resultSetFiles.getString("BankName")));
			}
		} catch (Exception e) {

		}

		resultSetFiles.close();

		return resultRecords;
	}

	public void Init() throws SQLException {
		if (Initialized && Connected) return;
		Initialized = true;

		Connect();
	}

	public void Clear() throws SQLException {
		Init();

		String sqlCommand = "DROP TABLE IF EXISTS Users ";

		Statement stm = Connection.createStatement();
		stm.execute(sqlCommand);

		sqlCommand = "DROP TABLE IF EXISTS Files ";

		stm = Connection.createStatement();
		stm.execute(sqlCommand);

		sqlCommand = "DROP TABLE IF EXISTS Records ";

		stm = Connection.createStatement();
		stm.execute(sqlCommand);

		System.out.println("Database cleared.");
	}

}
