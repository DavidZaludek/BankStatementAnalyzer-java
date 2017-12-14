package main.InternalUtils.Exceptions;

public class BankNotFoundException extends Exception {
	public BankNotFoundException(String bankEnum) {
		super("Bank name was not found in banks loaded " + bankEnum + ". Add implementation to Banks folder and retry.");
	}


}
