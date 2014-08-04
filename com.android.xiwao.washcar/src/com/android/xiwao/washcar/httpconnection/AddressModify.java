package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressModify extends BaseCommand{

	private final static String CMD = "accountCharge.do";
	
	private final static String PARAMS_ADDRESS_ID = "Address_id";
	private final static String PARAMS_ADDRESS_DETAIL = "Address_detail";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_ADDRESS_ID = "Address_id";
	
	private long addressId;
	private String addressDetail;
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public long addressId;
	}
	
	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
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
		nvps.add(new BasicNameValuePair(PARAMS_ADDRESS_DETAIL, addressDetail));
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response addressModify = new Response();

		try {
			JSONObject jsonObj = new JSONObject(content);
			addressModify.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			addressModify.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			addressModify.okey = true;

			addressModify.addressId = jsonObj.getLong(JSON_ADDRESS_ID);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return addressModify;
	}

}
