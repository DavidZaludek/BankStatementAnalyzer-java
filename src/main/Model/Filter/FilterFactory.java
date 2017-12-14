package main.Model.Filter;

import main.Model.Filter.AreaGraph.AreaFilter;
import main.Model.Filter.BarGraph.BarFilter;
import main.Model.Filter.LineGraph.LineFilter;
import main.Model.Filter.PieGraph.PieFilter;
import main.Model.Filter.StackedBarGraph.StackedBarFilter;

import java.io.IOException;
import java.util.ArrayList;

public class FilterFactory {
	private static ArrayList<AbstractFilter> filters;
	private static boolean initialized;

	public static void Init(){
		if (initialized)
			return;

		initialized = true;

		filters = new ArrayList<>();

		try {
			filters.add( new LineFilter());
			filters.add( new AreaFilter());
			filters.add( new BarFilter());
			filters.add( new PieFilter());
			filters.add( new StackedBarFilter());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<AbstractFilter> getFilters() {
		Init();
		return filters;
	}

	public static AbstractFilter getFilterByName(String filterName){
		Init();

		for (int i = 0; i < filters.size(); i++) {
			if (filters.get(i).getGraphName().equals(filterName)){
				return filters.get(i);
			}
		}

		return filters.get(0);
	}
}
