package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCode extends BaseCommand{
	private static final String CMD = "messageSend.do";
	
	private static final String PARAM_MOBILE_NUM = "Mobile_number";
	private static final String PARAM_MESSAGE_TYPE = "Message_type";
	private static final String PARAM_MESSAGE_CONTENT = "Message_content";
	
	public static final String JSON_RESPONSE_TYPE = "ResponseType";
	public static final String JSON_ERROR_MESSAGE = "ErrorMessage";
	public static final String JSON_IDENTIFY_CODE = "identifying_code";
	
	private String mobileNum;
	private String messageType;
	private String messageContent;
	
	public static class Response extends BaseResponse {

		public static String ISSUC_FAILED = "E";
		public static String ISSUC_SUCC = "N";

		public String errorMessage;
		public String responseType;
		public String identifyCode;
	}
	
	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
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
		
		nvps.add( new BasicNameValuePair(PARAM_MOBILE_NUM, mobileNum));
		nvps.add( new BasicNameValuePair(PARAM_MESSAGE_TYPE, messageType));
		nvps.add( new BasicNameValuePair(PARAM_MESSAGE_CONTENT, messageContent));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response getCode = new Response();
		
		try {
			getCode.okey = true;
			JSONObject jsonObj = new JSONObject(content);
			getCode.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			getCode.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			getCode.identifyCode = jsonObj.getString(JSON_IDENTIFY_CODE);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return getCode;
	}

}
