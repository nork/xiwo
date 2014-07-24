package com.android.xiwao.washcar.httpconnection;

import java.util.List;

import org.apache.http.NameValuePair;

public class Register extends BaseCommand{

	private String phone;
	private String password;
	private String code;
	private String nickName;
	
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	protected String getComand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<NameValuePair> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		return null;
	}

}
