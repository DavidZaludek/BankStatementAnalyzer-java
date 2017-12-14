package main.Model.Bank;

//Creates instances of Abstract banks

import main.InternalUtils.Exceptions.BankNotFoundException;

import java.util.ArrayList;

public class BankFactory {

	private static ArrayList<AbstractBank> banks;
	private static boolean initialized;

	public static void Init(){
		if (initialized)
			return;

		initialized = true;

		// class loader here

		banks = new ArrayList<AbstractBank>();

		banks.add(new FioBank());
		banks.add(new VubBank());
	}

	public static AbstractBank getBankInstanceByEnum(String bankEnum) throws BankNotFoundException {
		Init();
		for (AbstractBank bank: banks) {
			if (bank.getBankEnum().equals(bankEnum)){
				return bank;
			}
		}

		throw new BankNotFoundException(bankEnum);
	}

	public static AbstractBank getBankInstanceByName(String bankName) throws BankNotFoundException {
		Init();

		for (AbstractBank bank: banks) {
			if (bank.getBankName().equals(bankName)){
				return bank;
			}
		}
		throw new BankNotFoundException(bankName);
	}

	public static ArrayList<AbstractBank> getBanks(){
		Init();

		return banks;
	}

	public static ArrayList<String> getBankNames(){
		Init();

		ArrayList<String> names = new ArrayList<>();

		for (int i = 0; i < banks.size(); i++) {
			AbstractBank tmpBank = banks.get(i);

			names.add(tmpBank.getBankName());
		}

		return names;
	}

}
