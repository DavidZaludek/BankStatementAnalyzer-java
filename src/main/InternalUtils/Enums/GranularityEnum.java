package main.InternalUtils.Enums;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum GranularityEnum {
	DAY,WEEK,MONTH,YEAR;

	public static GranularityEnum fromDouble(double n) {
			if (n < 0.5) return DAY;
			if (n < 1.5) return WEEK;
			if (n < 2.5) return MONTH;
			return YEAR;
	}

	public static String getFormated(LocalDate localDate, GranularityEnum granularityEnum){
		switch (granularityEnum){

			case DAY:
				return localDate.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
			case WEEK:
				return localDate.format(DateTimeFormatter.ofPattern("ww yyyy"));
			case MONTH:
				return localDate.format(DateTimeFormatter.ofPattern("MMM yyyy"));
			case YEAR:
				return localDate.format(DateTimeFormatter.ofPattern("uuuu"));
		}

		return localDate.format(DateTimeFormatter.ofPattern("uuuu"));
	}
}

