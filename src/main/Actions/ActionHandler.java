package main.Actions;

import main.AppRoot.App;
import main.DBUtils.Exceptions.UserExistsException;
import main.DBUtils.Exceptions.UserNotFoundException;
import main.InternalUtils.Enums.CategoryEnum;
import main.InternalUtils.Exceptions.InvalidLoginDetailsException;
import main.Model.Bank.AbstractBank;
import main.Model.Record;
import main.View.Enums.SubViewEnum;
import main.View.Enums.ViewEnum;
import main.View.ViewDataHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ActionHandler {

	private ViewDataHandler viewDataHandler;

	public ActionHandler(ViewDataHandler viewData) {
		this.viewDataHandler = viewData;
	}

	public void LoginUser(String username,String password) throws UserNotFoundException, SQLException, InvalidLoginDetailsException {
		App.userDataHandler.LoginUser(username,password);
	}

	public void CreateUser(String username,String password) throws UserExistsException, SQLException {
		App.userDataHandler.SignUpUser(username,password);
	}

	public void SwitchView(ViewEnum viewEnum){
		viewDataHandler.setViewEnum(viewEnum);
	}

	public void SwitchSubView(SubViewEnum subViewEnum){
		viewDataHandler.setSubViewEnum(subViewEnum);
	}

	public void LogOut() {
		App.userDataHandler.LogOut();
	}

	public void AddFile(AbstractBank tmpBank, File currentFile) throws IOException, SQLException {
		App.userDataHandler.AddFile(tmpBank,currentFile);
		App.Update();
	}

	public void SetGraphSelection(String s) {
		viewDataHandler.setGraphSelection(s);
	}

	public void UpdateRecords() {
		App.userDataHandler.ReCategorizeRecords();
	}

	public void SetRecordCategory(Record record, CategoryEnum categoryEnum) {
		App.userDataHandler.SetRecordCategory(record,categoryEnum);
	}

	public void RemoveRecord(Record record) {
		App.userDataHandler.RemoveRecord(record);
	}

	public void RemoveFile(main.Model.File file){
		App.userDataHandler.RemoveFile(file);
		App.Update();
	}
}
