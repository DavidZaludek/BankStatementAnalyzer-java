package main.View.FxmlFiles.Views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Paint;
import main.AppRoot.App;
import main.DBUtils.Exceptions.UserExistsException;
import main.DBUtils.Exceptions.UserNotFoundException;
import main.InternalUtils.Exceptions.InvalidLoginDetailsException;
import main.View.Enums.SubViewEnum;
import main.View.Enums.ViewEnum;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

import java.sql.SQLException;

public class LoginPageViewController extends AbstractController {

	public TextField UsernameField;
	public PasswordField PasswordField;
	public Button LoginButton;
	public Button CreateAccountButton;
	public Button ContinueAsGuestButton;
	public Label ErrorLabel;

	public void initialize(){
		BackgroundImage myBI= new BackgroundImage(new Image("Resources/Images/bar-chart.png",32,32,false,true),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);


	}

	public void LoginButtonOnAction(){
		String username = UsernameField.getText();
		String password = PasswordField.getText();

		try {
			App.userDataHandler.LoginUser(username,password);
		} catch (UserNotFoundException userNotFoundExeption) {
			ErrorLabel.setText("Invalid login credentials.");
			ErrorLabel.setTextFill(Paint.valueOf("red"));
		} catch (InvalidLoginDetailsException e) {
			ErrorLabel.setText("Invalid login credentials.");
			ErrorLabel.setTextFill(Paint.valueOf("red"));
		} catch (SQLException e) {
			ErrorLabel.setText("Database error try again later.");
			ErrorLabel.setTextFill(Paint.valueOf("red"));
		}

		App.actionHandler.SwitchSubView(SubViewEnum.MenuView);
		App.actionHandler.SwitchView(ViewEnum.HomePageView);
	}

	public void CreateAccountButtonOnAction(){
		App.actionHandler.SwitchView(ViewEnum.UserRegistrationView);
	}

	public void ContinueAsGuestButtonOnAction(){

		try {
			App.actionHandler.CreateUser("Guest","aaaaaaaa");
		} catch (UserExistsException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		App.actionHandler.SwitchView(ViewEnum.HomePageView);
	}

	@Override
	public void setViewData(ViewDataHandler viewData) {
		UsernameField.setText("");
		PasswordField.setText("");
	}
}
