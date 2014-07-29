package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class PasswordModify extends BaseCommand{
	
	private final static String CMD = "passwordModify.do";
	private final static String PARAM_CUSTOM_ID = "Customer_id";
	private final static String PARAM_PASSWORD = "Password";
	private final static String PARAM_NEW_PASSWORD = "New_password";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_CUSTOM_ID = "Customer_id";

	private long customerId;
	private String oldPwd;
	private String newPwd;
	
	public static class Response extends BaseResponse {

		public static String ISSUC_FAILED = "E";
		public static String ISSUC_SUCC = "N";

		public String errorMessage;
		public String responseType;
		public long customerId;
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
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
		
		nvps.add( new BasicNameValuePair(PARAM_CUSTOM_ID, Long.toString(customerId)));
		nvps.add( new BasicNameValuePair(PARAM_PASSWORD, oldPwd));
		nvps.add( new BasicNameValuePair(PARAM_NEW_PASSWORD, newPwd));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response passwordModify = new Response();
		
		try {
			passwordModify.okey = true;
			JSONObject jsonObj = new JSONObject(content);
			passwordModify.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			passwordModify.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			passwordModify.customerId = jsonObj.getLong(JSON_CUSTOM_ID);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return passwordModify;
	}

}
