package main.InternalUtils.Exceptions;

public class InvalidLoginDetailsException extends Exception {
	public InvalidLoginDetailsException() {

		super("Invalid name or password. Try again.");
	}

	public static String getSimpleName() {

		return "InvalidLoginDetailsException";
	}
}
