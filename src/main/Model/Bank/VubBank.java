package main.Model.Bank;

import main.Model.File;
import main.Model.User;

import java.io.IOException;

public class VubBank extends AbstractBank {
	VubBank() {
		super("VUBSK", "Vub.sk");
	}

	@Override
	public File ParseFile(User user, java.io.File rawFile) throws IOException {

		return null;
	}
}
