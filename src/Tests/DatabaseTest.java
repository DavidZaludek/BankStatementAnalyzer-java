package Tests;

import main.DBUtils.Database;
import main.DBUtils.Exceptions.UserExistsException;
import main.DBUtils.Exceptions.UserNotFoundException;
import main.InternalUtils.Exceptions.InvalidLoginDetailsException;
import main.Model.File;
import main.Model.Record;
import main.Model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

class DatabaseTest {

	Database Database;

	@org.junit.jupiter.api.BeforeEach
	void setUp() {

		try {
			Database = new Database("TestDatabase.db");
			Database.Init();

			Database.AddUser(new User("Francis0","Mrkvicka11"));

			Database.AddUser(new User("Francis1","Mrkvicka111"));

			Database.AddUser(new User("Francis2","Mrkvicka1111"));

			Database.AddUser(new User("Francis3","Mrkvicka11111"));

			Database.AddUser(new User("Francis4","Mrkvicka111111"));

			Database.AddUser(new User("Francis5","Mrkvicka1111111"));

			Database.AddUser(new User("Francis6","Mrkvicka11111111"));

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UserExistsException e) {
			e.printStackTrace();
		}
	}

	@org.junit.jupiter.api.AfterEach
	void tearDown() {

		try {
			Database.Clear();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@org.junit.jupiter.api.Test
	void loadUserNormal() {

		try {
			User user = Database.LoadUser("Francis0","Mrkvicka11");

			System.out.println(user.toString());
		} catch (InvalidLoginDetailsException e) {
			e.printStackTrace();
			fail("");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("");
		} catch (UserNotFoundException userNotFoundExeption) {
			userNotFoundExeption.printStackTrace();
			fail("");
		}
	}

	@org.junit.jupiter.api.Test
	void loadUserWrongCredentials() {
		try {
			User user = Database.LoadUser("Francis0","Mrkvicka111");
			System.out.println(user.toString());

			fail("expected action to throw " + InvalidLoginDetailsException.getSimpleName() + " but it did not.");

		} catch (InvalidLoginDetailsException e) {
			System.out.println("Test passed InvalidLoginDetailsException thrown.");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("");
		} catch (UserNotFoundException userNotFoundExeption) {
			userNotFoundExeption.printStackTrace();
			fail("");
		}
	}

	@org.junit.jupiter.api.Test
	void loadUserNotExisting() {
		try {
			User user = Database.LoadUser("Francis0aaa","Mrkvicka111");
			System.out.println(user.toString());

			fail("expected action to throw " + InvalidLoginDetailsException.getSimpleName() + " but it did not.");

		} catch (UserNotFoundException userNotFoundExeption) {
			System.out.println("UserNotFoundException thrown test passed");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("");
		}  catch (InvalidLoginDetailsException e) {
			e.printStackTrace();
			fail("");
		}
	}

	@org.junit.jupiter.api.Test
	void removeUserNormal(){
		try {
			Database.RemoveUser("Francis0");
		} catch (UserNotFoundException userNotFoundExeption) {
			userNotFoundExeption.printStackTrace();
			fail("");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("");
		}

		try {
			Database.LoadUser("Francis0","Mrkvicka111");

			fail("expected action to throw " + UserNotFoundException.getSimpleName() + " but it did not.");
		} catch (UserNotFoundException userNotFoundExeption) {
			System.out.println("UserNotFoundException thrown test passed");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("");
		} catch (InvalidLoginDetailsException e) {
			e.printStackTrace();
			fail("");
		}
	}

	@org.junit.jupiter.api.Test
	void removeUserNotExisting() {
		try {
			Database.RemoveUser("Francis0aaa");

			fail("expected action to throw " + UserNotFoundException.getSimpleName() + " but it did not.");

		} catch (UserNotFoundException userNotFoundExeption) {
			System.out.println("UserNotFoundException thrown test passed");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("");
		}
	}

	@org.junit.jupiter.api.Test
	void addFileNormal(){
		File tmpFile = new File(UUID.randomUUID().toString(),
				UUID.randomUUID().toString(),
				"file.csv",
				LocalDate.now(),
				LocalDate.now(),
				"",
				new ArrayList<Record>());

		ArrayList<File> files = new ArrayList<>();
		files.add(tmpFile);
		try {
			Database.AddFiles(files);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("");
		}
	}

	@org.junit.jupiter.api.Test
	void removeFileNormal(){
		File tmpFile = new File("AAAAAAA",
				UUID.randomUUID().toString(),
				"file.csv",
				LocalDate.now(),
				LocalDate.now(),
				"",
				new ArrayList<Record>()
		);

		ArrayList<File> files = new ArrayList<>();
		files.add(tmpFile);
		try {
			Database.AddFiles(files);
		} catch (SQLException e) {
			fail("");
			e.printStackTrace();
		}

		try {
			Database.RemoveFiles(files);
		} catch (SQLException e) {
			fail("");
			e.printStackTrace();
		}
	}

	@org.junit.jupiter.api.Test
	void removeFileNotExist(){
		File tmpFile = new File(UUID.randomUUID().toString(),
				UUID.randomUUID().toString(),
				"file.csv",
				LocalDate.now(),
				LocalDate.now(),
				"",
				new ArrayList<Record>()
		);

		ArrayList<File> files = new ArrayList<>();
		files.add(tmpFile);

		try {
			Database.RemoveFiles(files);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}