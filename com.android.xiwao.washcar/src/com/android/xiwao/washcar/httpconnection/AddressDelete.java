package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressDelete  extends BaseCommand{
private final static String CMD = "addressDelete.do";
	
	private final static String PARAMS_ADDRESS_ID = "Address_id";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	
	private long addressId;
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
	}
	
	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
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

		nvps.add(new BasicNameValuePair(PARAMS_ADDRESS_ID, Long
				.toString(addressId)));
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

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return accountQuery;
	}
}
