package main.Model.Bank;

import com.opencsv.CSVReader;
import main.InternalUtils.Enums.TransactionTypeEnum;
import main.InternalUtils.CategoryParser;
import main.Model.File;
import main.Model.Record;
import main.Model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FioBank extends AbstractBank {
	public FioBank() {
		super("FIOCZ", "Fio.cz");
	}

	@Override
	public File ParseFile(User user, java.io.File rawFile) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(rawFile.getPath()), StandardCharsets.UTF_8), ';');

		File tmpFile = new File(user.getGUID(),rawFile.getName(),LocalDate.now(),LocalDate.now(),getBankName(),new ArrayList<>());

		ArrayList<Record> tmpRecords = new ArrayList<>();

		String [] nextLine;

		reader.readNext();

		LocalDate maxDate = LocalDate.MIN;
		LocalDate minDate = LocalDate.MAX;

		while ((nextLine = reader.readNext()) != null) {
			TransactionTypeEnum tt = parseTransactionType(nextLine[7]);

			String companyName = "";

			if (nextLine[5].startsWith("Nákup: ")){
				companyName = nextLine[5].split(",")[0].replace("Nákup: ","");
			}

			LocalDate tmpLocalDate = LocalDate.parse(nextLine[0], DateTimeFormatter.ofPattern("dd.MM.yyyy"));

			if (tmpLocalDate.isAfter(maxDate)){
				maxDate = tmpLocalDate;
			}
			if (tmpLocalDate.isBefore(minDate)){
				minDate = tmpLocalDate;
			}

			Record tmpRecord = new Record(tmpFile.getGUID(),
					user.getGUID(),
					Double.valueOf(nextLine[1].replace(',','.')),
					nextLine[2],
					tmpLocalDate,
					CategoryParser.getCategory(companyName),
					tt,
					nextLine[5],
					companyName,
					this.getBankName());

			tmpRecords.add(tmpRecord);
		}

		tmpFile.setRecords(tmpRecords);

		if (tmpRecords.size() > 0){
			tmpFile.setFromDateTime(minDate);
			tmpFile.setToDateTime(maxDate);
		}

		return tmpFile;
	}

	private TransactionTypeEnum parseTransactionType(String s) {
		if (s.equals("Vklad v hotovosti")){
			return TransactionTypeEnum.CASH_DEPOSIT;
		}
		if (s.equals("Karetní transakce")){
			return TransactionTypeEnum.CARD;
		}
		if (s.equals("Bezhotovostní příjem")){
			return TransactionTypeEnum.TRANSFER_IN;
		}
		if (s.equals("Platba převodem uvnitř banky") || s.equals("Bezhotovostní platba")){
			return TransactionTypeEnum.TRANSFER_OUT;
		}
		if (s.equals("Poplatek - platební karta") || s.equals("Poplatek")){
			return TransactionTypeEnum.FEE;
		}

		System.out.println(s);

		return TransactionTypeEnum.DEFAULT;
	}

}
