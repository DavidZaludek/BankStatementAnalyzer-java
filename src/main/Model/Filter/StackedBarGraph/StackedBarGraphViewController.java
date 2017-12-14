package main.Model.Filter.StackedBarGraph;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import main.View.FxmlFiles.AbstractController;
import main.View.ViewDataHandler;

public class StackedBarGraphViewController extends AbstractController {
	public DatePicker FromDatePicker;
	public DatePicker ToDatePicker;
	public ChoiceBox CurrencySelection;
	public Slider GranularitySlider;

	public void FromDatePickerSet(ActionEvent actionEvent) {

	}

	public void ToDatePickerSet(ActionEvent actionEvent) {

	}

	@Override
	public void setViewData(ViewDataHandler viewData) {

	}
}
