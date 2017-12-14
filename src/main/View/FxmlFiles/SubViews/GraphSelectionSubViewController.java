package main.View.FxmlFiles.SubViews;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.AppRoot.App;
import main.InternalUtils.Enums.GraphTypeEnum;
import main.View.Enums.SubViewEnum;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

public class GraphSelectionSubViewController extends AbstractController {

	@FXML
	private Button BarButton;

	@FXML
	private Button PieButton;

	@FXML
	private Button StackedBarButton;

	@FXML
	private Button AreaButton;

	@FXML
	private Button LineButton;

	@FXML
	private MenuButton BankSpecificButton;

	public void initialize(){
		Image BarImage = new Image("Resources/Images/bar-chart.png", 50, 50, false, false);
		Image LineImage = new Image("Resources/Images/profits.png", 50, 50, false, false);
		Image PieImage = new Image("Resources/Images/pie-chart-2.png", 50, 50, false, false);
		Image StackedBarImage = new Image("Resources/Images/bar-chart.png", 50, 50, false, false);
		Image AreaImage = new Image("Resources/Images/analytics.png", 50, 50, false, false);

		BarButton.setGraphic(new ImageView(BarImage));
		LineButton.setGraphic(new ImageView(LineImage));
		PieButton.setGraphic(new ImageView(PieImage));
		StackedBarButton.setGraphic(new ImageView(StackedBarImage));
		AreaButton.setGraphic(new ImageView(AreaImage));
	}

	public void BarButtonOnAction(){
		App.actionHandler.SetGraphSelection(GraphTypeEnum.BarChart.toString());
		App.actionHandler.SwitchSubView(SubViewEnum.GraphView);
	}

	public void PieButtonOnAction(){
		App.actionHandler.SetGraphSelection(GraphTypeEnum.PieChart.toString());
		App.actionHandler.SwitchSubView(SubViewEnum.GraphView);
	}

	public void StackedBarButtonOnAction(){
		App.actionHandler.SetGraphSelection(GraphTypeEnum.StackedBarChart.toString());
		App.actionHandler.SwitchSubView(SubViewEnum.GraphView);
	}

	public void AreaButtonOnAction(){
		App.actionHandler.SetGraphSelection(GraphTypeEnum.AreaChart.toString());
		App.actionHandler.SwitchSubView(SubViewEnum.GraphView);
	}

	public void LineButtonOnAction(){
		App.actionHandler.SetGraphSelection(GraphTypeEnum.LineChart.toString());
		App.actionHandler.SwitchSubView(SubViewEnum.GraphView);
	}


	@Override
	public void setViewData(ViewDataHandler viewData) {

	}
}
