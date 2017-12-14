package main.View.FxmlFiles.Views;

import main.AppRoot.App;
import main.View.Enums.SubViewEnum;
import main.View.Enums.ViewEnum;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class HomePageViewController extends AbstractController {

	public Button HomePageButton;
	public Button LogOutButton;
	public Button FilesViewButton;
	public Label UserLabel;
	public Button GraphsViewButton;

	public AnchorPane subScene;

	public void LogOutButtonOnAction(){
		App.actionHandler.LogOut();
		App.actionHandler.SwitchView(ViewEnum.LoginPageView);
	}

	public void GraphsViewButtonOnAction(){
		App.actionHandler.SwitchSubView(SubViewEnum.GraphSelectionView);
	}

	public void HomePageButtonOnAction(){
		App.actionHandler.SwitchSubView(SubViewEnum.MenuView);
	}

	public void FilesViewButtonOnAction(){
		App.actionHandler.SwitchSubView(SubViewEnum.FilesView);
	}

	public void RecordsViewButtonOnAction(){
		App.actionHandler.SwitchSubView(SubViewEnum.RecordsView);
	}

	@Override
	public void setViewData(ViewDataHandler viewData) {
		UserLabel.setText("Logged in as " + viewData.getUser().getName() +".");

		Pane tmpPane  = App.scenesHandler.getSubScenes().get(viewData.getSubViewEnum());

		subScene.getChildren().setAll(tmpPane);

		AnchorPane.setTopAnchor(tmpPane,0.0);
		AnchorPane.setBottomAnchor(tmpPane,0.0);
		AnchorPane.setLeftAnchor(tmpPane,0.0);
		AnchorPane.setRightAnchor(tmpPane,0.0);
	}
}
