import main.InternalUtils.Enums.CategoryEnum;
import main.Model.CategoryParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

class CategoryParserTest {
	@BeforeEach
	void setUp() {

		List<String> lines = Arrays.asList("{}");
		Path file = Paths.get("/Users/davidzaludek/IdeaProjects/BankStatementAnalyzer/src/Tests/TestFiles/testCategoryMap.json");

		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.err.println("Couldn't save file.");
		}

		CategoryParser.Init("/Users/davidzaludek/IdeaProjects/BankStatementAnalyzer/src/Tests/TestFiles/testCategoryMap.json");

		CategoryParser.addCategoryPair("Tesco", CategoryEnum.HOUSING);
		CategoryParser.addCategoryPair("HM",CategoryEnum.PERSONAL);
		CategoryParser.addCategoryPair("TomTailor",CategoryEnum.PERSONAL);
		CategoryParser.addCategoryPair("DameJidlo",CategoryEnum.FOOD);
		CategoryParser.addCategoryPair("DRMAX",CategoryEnum.PERSONAL);
		CategoryParser.addCategoryPair("ORDR",CategoryEnum.FOOD);
		CategoryParser.addCategoryPair("Brewery",CategoryEnum.FOOD);
	}

	@AfterEach
	void tearDown() {

	}

	@Test
	void getCategory() {
		if (!CategoryParser.getCategory("Tesco").equals(CategoryEnum.HOUSING)){
			fail("");
		}
	}

	@Test
	void ChangeCategoryForValue() {

		if (!CategoryParser.getCategory("Tesco").equals(CategoryEnum.HOUSING)){
			fail("");
		}

		CategoryParser.addCategoryPair("Tesco",CategoryEnum.FOOD);

		if (!CategoryParser.getCategory("Tesco").equals(CategoryEnum.FOOD)){
			fail("");
		}
	}


}