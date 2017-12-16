package main.Model.Filter.AreaGraph;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import main.InternalUtils.Enums.GraphTypeEnum;
import main.Model.Filter.AbstractFilter;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

import java.io.IOException;

public class AreaFilter extends AbstractFilter {
	private final Pane pane;
	private final AbstractController controller;

	public AreaFilter() throws IOException {

		super(GraphTypeEnum.AreaChart.toString());
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Model/Filter/AreaGraph/AreaGraphView.fxml"));
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
