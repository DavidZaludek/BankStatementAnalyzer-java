package main.View.FxmlFiles.SubViews;

import javafx.scene.layout.AnchorPane;
import main.Model.Filter.AbstractFilter;
import main.Model.Filter.FilterFactory;
import main.View.Enums.SubViewEnum;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

public class GraphSubViewController extends AbstractController {

	public AnchorPane subScene;

	AbstractFilter selectedFilter;

	@Override
	public void setViewData(ViewDataHandler viewData) {
		if (viewData.getSubViewEnum() == SubViewEnum.GraphView) {
			if (viewData.getGraphSelection()!=null) {
				selectedFilter = FilterFactory.getFilterByName(viewData.getGraphSelection());
				selectedFilter.setViewData(viewData);

				subScene.getChildren().setAll(selectedFilter.getPane());

				AnchorPane.setTopAnchor(selectedFilter.getPane(),0.0);
				AnchorPane.setBottomAnchor(selectedFilter.getPane(),0.0);
				AnchorPane.setLeftAnchor(selectedFilter.getPane(),0.0);
				AnchorPane.setRightAnchor(selectedFilter.getPane(),0.0);

			}
		}

	}
}
