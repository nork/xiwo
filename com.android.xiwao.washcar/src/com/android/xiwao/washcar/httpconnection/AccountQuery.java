package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountQuery extends BaseCommand{
private final static String CMD = "accountQuery.do";
	
	private final static String PARAMS_CUSTOMER_ID = "Customer_id";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_ACCOUNT_INFO = "Account_info";
	
	private long customerId;
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public long accountInfo;
	}
	
	public long getCustmerId() {
		return customerId;
	}

	public void setCustmerId(long custmerId) {
		this.customerId = custmerId;
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
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response accountQuery = new Response();

		try {
			JSONObject jsonObj = new JSONObject(content);
			accountQuery.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			accountQuery.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			accountQuery.okey = true;

			accountQuery.accountInfo = jsonObj.getLong(JSON_ACCOUNT_INFO);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return accountQuery;
	}
}
