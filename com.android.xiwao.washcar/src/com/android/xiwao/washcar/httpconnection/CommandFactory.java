package com.android.xiwao.washcar.httpconnection;

public abstract class CommandFactory {
	public abstract BaseCommand getLogin(String user, String password);
	public abstract BaseCommand getRegister(String codestr, String phonenumber, String password, String nickNameStr);
	public abstract BaseCommand getCode(String phone, String operate);
}
