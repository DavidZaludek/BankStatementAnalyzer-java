package main.View;

import main.View.Enums.SubViewEnum;
import main.View.Enums.ViewEnum;
import main.View.FxmlFiles.AbstractController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;

public class ScenesHandler {

	public HashMap<ViewEnum,Scene> Scenes;
	public HashMap<ViewEnum, AbstractController> Controllers;

	public HashMap<SubViewEnum,Pane> SubScenes;
	public HashMap<SubViewEnum,AbstractController> SubControllers;

	public ScenesHandler() throws IOException {

		Scenes = new HashMap<>();
		Controllers = new HashMap<>();

		SubScenes = new HashMap<>();
		SubControllers = new HashMap<>();

		// main views load

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/View/FxmlFiles/Views/LoginPageView.fxml"));
		Scenes.putIfAbsent(ViewEnum.LoginPageView,new Scene(loader.load()));

		AbstractController controller = loader.getController();
		Controllers.putIfAbsent(ViewEnum.LoginPageView,controller);

		loader = new FXMLLoader(getClass().getResource("/main/View/FxmlFiles/Views/HomePageView.fxml"));
		Scenes.putIfAbsent(ViewEnum.HomePageView,new Scene(loader.load()));

		controller = loader.getController();
		Controllers.putIfAbsent(ViewEnum.HomePageView,controller);

		loader = new FXMLLoader(getClass().getResource("/main/View/FxmlFiles/Views/UserRegistrationView.fxml"));
		Scenes.putIfAbsent(ViewEnum.UserRegistrationView,new Scene(loader.load()));

		controller = loader.getController();
		Controllers.putIfAbsent(ViewEnum.UserRegistrationView,controller);

		// sub views load

		loader = new FXMLLoader(getClass().getResource("/main/View/FxmlFiles/SubViews/RecordsSubView.fxml"));
		SubScenes.putIfAbsent(SubViewEnum.RecordsView,loader.load());

		controller = loader.getController();
		SubControllers.putIfAbsent(SubViewEnum.RecordsView,controller);

		loader = new FXMLLoader(getClass().getResource("/main/View/FxmlFiles/SubViews/FilesSubView.fxml"));
		SubScenes.putIfAbsent(SubViewEnum.FilesView,loader.load());

		controller = loader.getController();
		SubControllers.putIfAbsent(SubViewEnum.FilesView,controller);

		loader = new FXMLLoader(getClass().getResource("/main/View/FxmlFiles/SubViews/GraphSelectionSubView.fxml"));
		SubScenes.putIfAbsent(SubViewEnum.GraphSelectionView,loader.load());

		controller = loader.getController();
		SubControllers.putIfAbsent(SubViewEnum.GraphSelectionView,controller);

		loader = new FXMLLoader(getClass().getResource("/main/View/FxmlFiles/SubViews/GraphSubView.fxml"));
		SubScenes.putIfAbsent(SubViewEnum.GraphView,loader.load());

		controller = loader.getController();
		SubControllers.putIfAbsent(SubViewEnum.GraphView,controller);

		loader = new FXMLLoader(getClass().getResource("/main/View/FxmlFiles/SubViews/MenuSubView.fxml"));
		SubScenes.putIfAbsent(SubViewEnum.MenuView,loader.load());

		controller = loader.getController();
		SubControllers.putIfAbsent(SubViewEnum.MenuView,controller);

	}

	public HashMap<ViewEnum, Scene> getScenes(){
		return Scenes;
	}

	public HashMap<ViewEnum, AbstractController> getControllers()  {
		return Controllers;
	}

	public HashMap<SubViewEnum, Pane> getSubScenes() {

		return SubScenes;
	}

	public HashMap<SubViewEnum, AbstractController> getSubControllers() {

		return SubControllers;
	}

	public void SetViewData(ViewDataHandler view){

		for (AbstractController a: getControllers().values())  {
			a.setViewData(view);
		}

		for (AbstractController a: getSubControllers().values())  {
			a.setViewData(view);
		}
	}
}
