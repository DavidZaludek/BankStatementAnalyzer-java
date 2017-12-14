package main.Model.Filter.PieGraph;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import main.InternalUtils.Enums.GraphTypeEnum;
import main.Model.Filter.AbstractFilter;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

import java.io.IOException;

public class PieFilter extends AbstractFilter {
	private final Pane pane;
	private final AbstractController controller;

	public PieFilter() throws IOException {

		super(GraphTypeEnum.PieChart.toString());
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Model/Filter/PieGraph/PieGraphView.fxml"));
		pane = loader.load();
		controller = loader.getController();
	}

	@Override
	public Pane getPane() {
		return pane;
	}

	@Override
	public void setViewData(ViewDataHandler view) {
		controller.setViewData(view);
	}
}
