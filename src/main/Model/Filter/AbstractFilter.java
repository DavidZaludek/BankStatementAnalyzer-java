package main.Model.Filter;

import javafx.scene.layout.Pane;
import main.View.ViewDataHandler;

public abstract class AbstractFilter {

	private String GraphName;

	public AbstractFilter(String graphName) {

		GraphName = graphName;
	}

	public abstract Pane getPane();

	public abstract void setViewData(ViewDataHandler view);

	public String getGraphName() {

		return GraphName;
	}
}
