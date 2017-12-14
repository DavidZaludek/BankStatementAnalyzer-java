package main.View.FxmlFiles.Views;

import main.AppRoot.App;
import main.DBUtils.Exceptions.UserExistsException;
import main.View.Enums.ViewEnum;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

public class UserRegistrationViewController extends AbstractController {

	public Button CreateAccountButton;
	public TextField UsernameField;
	public PasswordField PasswordField;
	public PasswordField ConfirmPasswordField;
	public Label ErrorLabel;
	public Hyperlink BackToLogin;

	public void initialize(){
		ErrorLabel.setTextFill(Paint.valueOf("red"));
	}

	public void BackToLoginOnAction(){
		App.actionHandler.SwitchView(ViewEnum.LoginPageView);
	}

	public void CreateAccountButtonButtonOnAction(){

		if (UsernameField.getText().length() < 4 || !StringUtils.isAlphanumeric(UsernameField.getCharacters())){
			ErrorLabel.setText("Name must be only alphanumeric and ");
			return;
		}

		if (!StringUtils.isAlphanumeric(PasswordField.getText()) || PasswordField.getText().length() < 8 || !PasswordField.getText().equals(ConfirmPasswordField.getText())){
			ErrorLabel.setText("Password incorrect format.");
			return;
		}

		if (!PasswordField.getText().equals(ConfirmPasswordField.getText())){
			ErrorLabel.setText("Passwords doesn't match.");
			return;
		}

		try {
			App.actionHandler.CreateUser(UsernameField.getText(),PasswordField.getText());
		} catch (UserExistsException e) {
			ErrorLabel.setText("Username already exists. Forgot password ? ");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		App.actionHandler.SwitchView(ViewEnum.HomePageView);
	}

	@Override
	public void setViewData(ViewDataHandler viewData) {
		UsernameField.setText("");
		PasswordField.setText("");
		ConfirmPasswordField.setText("");
		ErrorLabel.setText("");
	}
}
