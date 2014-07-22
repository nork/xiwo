package com.android.xiwao.washcar.httpconnection;

public class DefaultCommandFactory extends CommandFactory {
	
	public BaseCommand getLogin(String user, String password){
		Login login = new Login();
		login.setUser(user);
		login.setPasswd(password);
		
		return login;
	}
}
