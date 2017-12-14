package main.DBUtils.Exceptions;

import main.Model.User;

public class UserExistsException extends Exception {
	public UserExistsException(User user) {
		super ("User with name already exist in database. Forgot password ? ");
	}
}
