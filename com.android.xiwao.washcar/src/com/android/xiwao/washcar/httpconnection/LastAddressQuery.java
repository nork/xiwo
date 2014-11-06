package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class LastAddressQuery extends BaseCommand{
private final static String CMD = "lastAddressQuery.do";
	
	private final static String PARAMS_CUSTOMER_ID = "Customer_id";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_CAR_ID = "Car_id";
	public final static String JSON_ADDRESS_ID = "Address_id";
	public final static String JSON_CAR_CODE = "Car_code";
	public final static String JSON_ADDRESS = "Address";
	public final static String JSON_CAR_TYPE = "Car_type";
	public final static String JSON_DISTRACT_FIRST_ID = "Distract_first_id";
	
	private long customerId;
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public int carId;
		public int addressId;
		public String carCode;
		public String address;
		public String carType;
		public long carDistractFirstId;
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
		Response lastAddressQuery = new Response();

		try {
			lastAddressQuery.okey = true;
			JSONObject jsonObj = new JSONObject(content);

			lastAddressQuery.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			lastAddressQuery.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			lastAddressQuery.address = jsonObj.getString(JSON_ADDRESS);
			lastAddressQuery.addressId = jsonObj.getInt(JSON_ADDRESS_ID);
			lastAddressQuery.carCode = jsonObj.getString(JSON_CAR_CODE);
			lastAddressQuery.carId = jsonObj.getInt(JSON_CAR_ID);
			lastAddressQuery.carType = jsonObj.getString(JSON_CAR_TYPE);
			lastAddressQuery.carDistractFirstId = jsonObj.getLong(JSON_DISTRACT_FIRST_ID);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lastAddressQuery;
	}
}
