package main.DBUtils.Exceptions;

public class UserNotFoundException extends Exception {
	public UserNotFoundException(String name) {
		super ("User " + name + " wasn't fount in database.");
	}
	public static String getSimpleName() {
		return "UserNotFoundException";
	}
}
