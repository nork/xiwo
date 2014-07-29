package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class PasswordReset extends BaseCommand{
	private final static String CMD = "passwordReset.do";
	
	private final static String PARAM_MOBILE_NUM = "Mobile_number";
	private final static String PARAM_IDENTIFY_CODE = "Identifying_code";
	private final static String PARAM_PASSWORD = "Password";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	
	private String phone;
	private String code;
	private String password;
	
	public static class Response extends BaseResponse {

		public static String ISSUC_FAILED = "E";
		public static String ISSUC_SUCC = "N";

		public String errorMessage;
		public String responseType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	protected String getComand() {
		// TODO Auto-generated method stub
		return CMD;
	}

	@Override
	protected List<NameValuePair> getParameters() {
		// TODO Auto-generated method stub
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add( new BasicNameValuePair(PARAM_MOBILE_NUM, phone));
		nvps.add( new BasicNameValuePair(PARAM_IDENTIFY_CODE, code));
		nvps.add( new BasicNameValuePair(PARAM_PASSWORD, password));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response passwordReset = new Response();
		
		try {
			passwordReset.okey = true;
			JSONObject jsonObj = new JSONObject(content);
			passwordReset.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			passwordReset.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return passwordReset;
	}

}
