package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class Register extends BaseCommand{
	
	private static final String CMD = "customerRegister.do";
	
	private static final String PARAM_MOBILE_NUM = "Mobile_number";
	private static final String PARAM_CUSTOR_NAME = "Customer_name";
	private static final String PARAM_IDETIFY_CODE = "Identifying_code";
	private static final String PARAM_PASSWORD = "Password";
	
	public static final String JSON_RESPONSE_TYPE = "ResponseType";
	public static final String JSON_ERROR_MESSAGE = "ErrorMessage";
	public static final String JSON_CUSTOM_ID = "Customer_id";
	
	private String phone;
	private String password;
	private String code;
	private String nickName;
	
	public static class Response extends BaseResponse {

		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;
		
		public long id;
		public String errorMessage;
		public String responseType;
	}
	
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
		return CMD;
	}

	@Override
	protected List<NameValuePair> getParameters() {
		// TODO Auto-generated method stub
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add( new BasicNameValuePair(PARAM_MOBILE_NUM, phone));
		nvps.add( new BasicNameValuePair(PARAM_CUSTOR_NAME, nickName));
		nvps.add( new BasicNameValuePair(PARAM_IDETIFY_CODE, code));
		nvps.add( new BasicNameValuePair(PARAM_PASSWORD, password));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response register = new Response();
		
		try {
			JSONObject jsonObj = new JSONObject(content);
			register.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			register.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			register.okey = true;
			
			register.id = jsonObj.getLong(JSON_CUSTOM_ID);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return register;
	}

}
