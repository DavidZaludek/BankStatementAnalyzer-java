package main.View.FxmlFiles.SubViews;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import main.Model.Record;
import main.View.Enums.SubViewEnum;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;

import static main.InternalUtils.MathUtils.calcHistogramData;

public class MenuSubViewController  extends AbstractController{
	@FXML
	private AreaChart<String,Number> AreaChart;

	@FXML
	private ListView<String> ListView;

	public void initialize(){

	}

	@Override
	public void setViewData(ViewDataHandler viewData) {
		if (viewData.getSubViewEnum() != SubViewEnum.MenuView)
			return;

		ArrayList<Record> records =  viewData.getUser().getRecords();

		ArrayList<Double> tmpValues = new ArrayList<>();

		SummaryStatistics spendingStats = new SummaryStatistics();
		SummaryStatistics incomeStats = new SummaryStatistics();

		for (Record tmpRecord: records) {
			if (tmpRecord.getAmount() < 0){
				tmpValues.add(tmpRecord.getAmount() * -1);
				spendingStats.addValue(tmpRecord.getAmount() * -1);
			}else{
				incomeStats.addValue(tmpRecord.getAmount());
			}
		}

		long[] histogramData = calcHistogramData(tmpValues);

		XYChart.Series<String,Number> series = new XYChart.Series<>();

		for (int i = 0, histogramDataLength = histogramData.length; i < histogramDataLength; i++) {
			long tmpHistData = histogramData[i];
			series.getData().add(new XYChart.Data<>(String.valueOf(i), tmpHistData));
		}

		ListView.setItems(FXCollections.observableArrayList());

		AreaChart.getData().setAll(series);
	}
}
