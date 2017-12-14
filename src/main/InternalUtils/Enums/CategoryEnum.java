package main.InternalUtils.Enums;

import java.util.stream.Stream;

public enum CategoryEnum {
	INCOME,SAVINGS,UTILITIES,HOUSING,FOOD,TRANSPORTATION,INSURANCE,DEBT,LEISURE,EDUCATION,PERSONAL,MISC,UNKNOWN;

	public static String[] names() {
		return Stream.of(CategoryEnum.values()).map(CategoryEnum::name).toArray(String[]::new);
	}
}
