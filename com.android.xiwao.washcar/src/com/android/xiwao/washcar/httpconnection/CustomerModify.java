package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomerModify extends BaseCommand{
private final static String CMD = "customerModify.do";
	
	private final static String PARAMS_CUSTOMER_ID = "Customer_id";
	private final static String PARAMS_CUSTOMER_NAME = "Customer_name";
	private final static String PARAMS_EMAIL = "Email";
	private final static String PARAMS_CUSTOMER_PIC = "Customer_pic";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_CUSTOMER_ID = "Customer_id";
	
	private long customerId;
	private String customerName;
	private String email;
	private String headImg;
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public long customerId;
	}
	
	public long getCustmerId() {
		return customerId;
	}

	public void setCustmerId(long custmerId) {
		this.customerId = custmerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
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

		nvps.add(new BasicNameValuePair(PARAMS_CUSTOMER_ID, Long
				.toString(customerId)));
		nvps.add(new BasicNameValuePair(PARAMS_CUSTOMER_NAME, customerName));
		nvps.add(new BasicNameValuePair(PARAMS_EMAIL, email));
		nvps.add(new BasicNameValuePair(PARAMS_CUSTOMER_PIC, headImg));
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response customerModify = new Response();

		try {
			JSONObject jsonObj = new JSONObject(content);
			customerModify.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			customerModify.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			customerModify.okey = true;

			customerModify.customerId = jsonObj.getInt(JSON_CUSTOMER_ID);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return customerModify;
	}
}
