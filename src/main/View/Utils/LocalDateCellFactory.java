package main.View.Utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import main.Model.TableFile;
import main.Model.TableRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateCellFactory {
	public static class LocalDateRecordCellFactory implements Callback<TableColumn<TableRecord, LocalDate>, TableCell<TableRecord, LocalDate>> {

		@Override
		public TableCell<TableRecord, LocalDate> call(TableColumn<TableRecord, LocalDate> param) {
			TableCell<TableRecord, LocalDate> cell = new TableCell<TableRecord, LocalDate>() {

				@Override
				public void updateItem(final LocalDate item, boolean empty) {
					if (item != null) {
						setText(item.format(DateTimeFormatter.ISO_LOCAL_DATE));
					}
				}
			};
			return cell;
		}
	}
	public static class LocalDateFileCellFactory implements Callback<TableColumn<TableFile, LocalDate>, TableCell<TableFile, LocalDate>> {

		@Override
		public TableCell<TableFile, LocalDate> call(TableColumn<TableFile, LocalDate> param) {
			TableCell<TableFile, LocalDate> cell = new TableCell<TableFile, LocalDate>() {

				@Override
				public void updateItem(final LocalDate item, boolean empty) {
					if (item != null) {
						setText(item.format(DateTimeFormatter.ISO_LOCAL_DATE));
					}
				}
			};
			return cell;
		}
	}
}
