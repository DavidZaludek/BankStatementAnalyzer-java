package main.View;

import main.AppRoot.App;
import main.Model.User;
import main.Model.UserDataHandler;
import main.View.Enums.SubViewEnum;
import main.View.Enums.ViewEnum;

public class ViewDataHandler {
	private ViewEnum viewEnum;
	private SubViewEnum subViewEnum;
	private String graphSelection;
	private UserDataHandler userDataHandler;

	public ViewDataHandler(UserDataHandler userDataHandler){
		this.userDataHandler = userDataHandler;
		this.viewEnum = ViewEnum.LoginPageView;
		this.subViewEnum = SubViewEnum.FilesView;
	}

	public void setViewEnum(ViewEnum viewEnum) {
		if (this.viewEnum == ViewEnum.HomePageView)
			subViewEnum = SubViewEnum.MenuView;

		this.viewEnum = viewEnum;
		App.Update();
	}

	public void setSubViewEnum(SubViewEnum subViewEnum) {
		this.subViewEnum = subViewEnum;
		App.Update();
	}

	public SubViewEnum getSubViewEnum() {
		return subViewEnum;
	}

	public ViewEnum getViewEnum() {
		return viewEnum;
	}

	public User getUser(){
		return userDataHandler.getUserData();
	}

	public void setGraphSelection(String graphSelection) {
		this.graphSelection = graphSelection;
	}

	public String getGraphSelection() {

		return graphSelection;
	}
}
