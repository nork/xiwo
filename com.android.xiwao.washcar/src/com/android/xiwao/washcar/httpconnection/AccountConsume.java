package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;	

public class AccountConsume extends BaseCommand{
private final static String CMD = "accountConsume.do";
	
	private final static String PARAMS_CUSTOMER_ID = "Customer_id";
	private final static String PARAMS_CHANGE_AMT = "Change_amt";
	private final static String PARAMS_ORDER_ID = "Order_id";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_ACCOUNT_INFO = "Account_info";
	
	private long customerId;
	private long changeAmt;
	private long orderId;
	
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

	public long getChangeAmt() {
		return changeAmt;
	}

	public void setChangeAmt(long changeAmt) {
		this.changeAmt = changeAmt;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
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
		nvps.add(new BasicNameValuePair(PARAMS_CHANGE_AMT, Long
				.toString(changeAmt)));
		nvps.add(new BasicNameValuePair(PARAMS_ORDER_ID, Long
				.toString(orderId)));
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response accountConsume = new Response();

		try {
			JSONObject jsonObj = new JSONObject(content);
			accountConsume.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			accountConsume.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			accountConsume.okey = true;

			accountConsume.accountInfo = jsonObj.getLong(JSON_ACCOUNT_INFO);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return accountConsume;
	}
}
