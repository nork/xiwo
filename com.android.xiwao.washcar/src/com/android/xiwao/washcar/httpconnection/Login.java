package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends BaseCommand {
	private static final String LOGIN_CMD = "customerLogin.do";
	
	private static final String PARAM_USER = "Mobile_number";
	private static final String PARAM_PASSWD = "Password";
	
	public static final String JSON_CUSTOM_ID = "Customer_id";
	public static final String JSON_ERROR_MESSAGE = "ErrorMessage";
	public static final String JSON_RESPONE_TYPE = "ResponseType";
	public static final String JSON_CUSTOM_NAME = "Customer_name";
	public static final String JSON_MOBILE_NUM = "Mobile_number";
	public static final String JSON_EMAIL = "Email";
	
	
	private String mUser;
	private String mPasswd;
	
	public static class Response extends BaseResponse {

		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;
		
		public long id;
		public String errorMessage;
		public String responseType;
		public String customerName;
		public String mobileNum;
		public String email;
	}
		
	public Login() {
		super();
	}
	
	public void setUser(String user){
		mUser = user;
	}
	
	public void setPasswd(String passwd){
		mPasswd = passwd;
	}


	@Override
	protected List<NameValuePair> getParameters() {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add( new BasicNameValuePair(PARAM_USER, mUser));
		nvps.add( new BasicNameValuePair(PARAM_PASSWD, mPasswd));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		Response login = new Response();
		
		try {
			JSONObject jsonObj = new JSONObject(content);
			login.responseType = jsonObj.getString(JSON_RESPONE_TYPE);
			login.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			login.okey = true;
			
			login.id = jsonObj.getLong(JSON_CUSTOM_ID);
			login.customerName = jsonObj.getString(JSON_CUSTOM_NAME);
			login.mobileNum = jsonObj.getString(JSON_MOBILE_NUM);
			login.email = jsonObj.getString(JSON_EMAIL);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return login;
	}

	@Override
	protected String getComand() {
		return LOGIN_CMD;
	}
}
