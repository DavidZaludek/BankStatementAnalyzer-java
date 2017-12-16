package main.AppRoot;

import main.Actions.ActionHandler;
import main.DBUtils.Database;
import main.InternalUtils.CategoryParser;
import main.Model.Filter.FilterFactory;
import main.Model.UserDataHandler;
import main.View.Enums.ViewEnum;
import main.View.ScenesHandler;
import main.View.ViewDataHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public final class App extends Application {

	private static String CATEGORY_MAP_FILE_PATH = "Resources/CategoryMap.json";
	private static String USERS_DATABASE_PATH = "Database/UsersData.db";

    public static Database usersDatabase;
    public static ViewDataHandler viewData;
    public static CategoryParser categoryParser;

    public static ActionHandler actionHandler;
	public static UserDataHandler userDataHandler;
	public static ScenesHandler scenesHandler;

    private static Stage rootStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

	    try {
		    FilterFactory.Init();

		    usersDatabase = new Database(USERS_DATABASE_PATH);
		    usersDatabase.Init();

		    userDataHandler = new UserDataHandler(usersDatabase);
		    viewData = new ViewDataHandler(userDataHandler);
		    actionHandler = new ActionHandler(viewData);

		    CategoryParser.Init(CATEGORY_MAP_FILE_PATH);

		    scenesHandler  = new ScenesHandler();
	    } catch (SQLException e) {
		    e.printStackTrace();
		    System.err.println("Failed initializing database.");
	    } catch (IOException e) {
		    e.printStackTrace();
		    System.err.println("Failed on fxml load.");
	    } finally {
		    // error popup close application
	    }

	    System.out.println("Loaded Application.");

        rootStage = primaryStage;

        primaryStage.setScene(App.scenesHandler.Scenes.get(ViewEnum.LoginPageView));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(900);
        primaryStage.setTitle("Bank statement analyzer");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void Update() {
	    scenesHandler.SetViewData(viewData);
        rootStage.setScene(scenesHandler.Scenes.get(viewData.getViewEnum()));
    }

	public static Stage getRootStage() {

		return rootStage;
	}
}
