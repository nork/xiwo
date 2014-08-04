package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressCreate extends BaseCommand{

	private final static String CMD = "addressCreate.do";
	
	private final static String PARAMS_CUSTOMER_ID = "Customer_id";
	private final static String PARAMS_DISTRACT_ID = "Distract_id";
	private final static String PARAMS_ADDRESS_DETAIL = "Address_detail";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_ADDRESS_ID = "Address_id";
	
	private long customerId;
	private long distractId;
	private String addressDetail;
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public long addressId;
	}
	
	public long getCustmerId() {
		return customerId;
	}

	public void setCustmerId(long custmerId) {
		this.customerId = custmerId;
	}

	

	public long getDistractId() {
		return distractId;
	}

	public void setDistractId(long distractId) {
		this.distractId = distractId;
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

		nvps.add(new BasicNameValuePair(PARAMS_CUSTOMER_ID, Long
				.toString(customerId)));
		nvps.add(new BasicNameValuePair(PARAMS_DISTRACT_ID, Long
				.toString(distractId)));
		nvps.add(new BasicNameValuePair(PARAMS_ADDRESS_DETAIL, addressDetail));
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response addressCreate = new Response();

		try {
			JSONObject jsonObj = new JSONObject(content);
			addressCreate.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			addressCreate.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			addressCreate.okey = true;

			addressCreate.addressId = jsonObj.getLong(JSON_ADDRESS_ID);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return addressCreate;
	}

}
