package com.android.xiwao.washcar.httpconnection;

public abstract class CommandFactory {
	public abstract BaseCommand getLogin(String user, String password);
}
