package main.Model.Bank;

import com.opencsv.CSVReader;
import main.InternalUtils.CategoryParser;
import main.InternalUtils.Enums.TransactionTypeEnum;
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

public class VubBank extends AbstractBank {
	VubBank() {
		super("VUBSK", "Vub.sk");
	}
	@Override
	public File ParseFile(User user, java.io.File rawFile) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(rawFile.getPath()), StandardCharsets.UTF_8), ',');

		File tmpFile = new File(user.getGUID(),rawFile.getName(), LocalDate.now(),LocalDate.now(),getBankName(),new ArrayList<>());

		ArrayList<Record> tmpRecords = new ArrayList<>();

		String [] nextLine;

		reader.readNext();

		LocalDate maxDate = LocalDate.MIN;
		LocalDate minDate = LocalDate.MAX;

		while ((nextLine = reader.readNext()) != null) {
			TransactionTypeEnum tt = parseTransactionType(nextLine[7]);

			String companyName = nextLine[11];

			LocalDate tmpLocalDate = LocalDate.parse(nextLine[0], DateTimeFormatter.ofPattern("yyyyMMdd"));

			if (tmpLocalDate.isAfter(maxDate)){
				maxDate = tmpLocalDate;
			}
			if (tmpLocalDate.isBefore(minDate)){
				minDate = tmpLocalDate;
			}

			Record tmpRecord = new Record(tmpFile.getGUID(),
					user.getGUID(),
					Double.valueOf(nextLine[5].replace(',','.').replace(" ","")),
					nextLine[6],
					tmpLocalDate,
					CategoryParser.getCategory(companyName),
					tt,
					nextLine[11],
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

		return TransactionTypeEnum.DEFAULT;
	}
}
