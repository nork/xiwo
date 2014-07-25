package com.android.xiwao.washcar.httpconnection;

public class DefaultCommandFactory extends CommandFactory {
	
	public BaseCommand getLogin(String user, String password){
		Login login = new Login();
		login.setUser(user);
		login.setPasswd(password);
		
		return login;
	}

	@Override
	public BaseCommand getRegister(String codestr, String phonenumber,
			String password, String nickNameStr) {
		// TODO Auto-generated method stub
		Register register = new Register();
		register.setCode(codestr);
		register.setPassword(password);
		register.setPhone(phonenumber);
		register.setNickName(nickNameStr);
		return register;
	}

	@Override
	public BaseCommand getCode(String phone, String operate) {
		// TODO Auto-generated method stub
		GetCode getCode = new GetCode();
		getCode.setMobileNum(phone);
		getCode.setMessageType("01");
		getCode.setMessageContent("01");
		return getCode;
	}
	
}
