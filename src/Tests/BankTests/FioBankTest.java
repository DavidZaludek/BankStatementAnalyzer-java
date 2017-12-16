package BankTests;

import main.Model.Bank.FioBank;
import main.InternalUtils.CategoryParser;
import main.Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

class FioBankTest {

	FioBank fioBank;
	User user;

	@BeforeEach
	void setUp() {
		CategoryParser.Init("/Users/davidzaludek/IdeaProjects/BankStatementAnalyzer/src/Tests/TestFiles/testCategoryMap.json");
		user = new User("Guest","aaaa");
		fioBank = new FioBank();
	}

	@AfterEach
	void tearDown() {

	}

	@Test
	void parseFile() {
		try {
			fioBank.ParseFile(user,new File("/Users/davidzaludek/IdeaProjects/BankStatementAnalyzer/src/Tests/TestFiles/FIOCZ_BANK_STATEMENT.csv"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("");
		}
	}
}