package main.Model.Bank;

import main.Model.Filter.AbstractFilter;
import main.Model.User;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AbstractBank {

	private String bankEnum;
	private String bankName;

	private ArrayList<AbstractFilter> defaultFilters;
	private ArrayList<AbstractFilter> filters;

	AbstractBank(String bankEnum,String bankName)
	{
		this.bankEnum = bankEnum;
		this.bankName = bankName;

		filters = new ArrayList<AbstractFilter>();
		defaultFilters = new ArrayList<AbstractFilter>();
	}

	public abstract main.Model.File ParseFile(User user, File rawFile) throws IOException;

	public ArrayList<AbstractFilter> getDefaultFilters() {

		return defaultFilters;
	}

	public ArrayList<AbstractFilter> getFilters() {

		return filters;
	}

	public String getBankEnum() {

		return bankEnum;
	}

	public String getBankName() {

		return bankName;
	}
}
