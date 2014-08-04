package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.xiwao.washcar.data.AddressData;

public class AddressQuery extends BaseCommand{

	private final static String CMD = "accountCharge.do";
	
	private final static String PARAMS_CUSTOMER_ID = "Customer_id";
	private final static String PARAMS_DISTRACT_ID = "Distract_id";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_LAST_ADDRESS_ID = "Last_Address_id";
	public final static String JSON_LAST_ADDRESS_DETAIL = "Last_Address_detail";
	public final static String JSON_ADDRESS_LIST = "Address_List";
	public final static String JSON_ADDRESS_ID = "Address_id";
	public final static String JSON_ADDRESS_DETAIL = "Address_detail";
	public final static String JSON_DISTRACT_ID = "Distract_id";
	public final static String JSON_CUSTOMER_ID = "Customer_id";
	public final static String JSON_ADDRESS_TYPE = "Address_type";
	public final static String JSON_POST_CODE = "Post_code";
	
	private long customerId;
	private long distractId;
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public long lastAddressId;
		public String lastAddressDetail;
		public List<AddressData> addressDataList;
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
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response addressQuery = new Response();

		try {
			addressQuery.addressDataList = new ArrayList<AddressData>();
			addressQuery.okey = true;
			AddressData addressData;
			JSONArray jsonArray;
			JSONObject jsonSingleInfo;
			JSONObject jsonObj = new JSONObject(content);
			addressQuery.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			addressQuery.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			
			jsonArray = jsonObj.getJSONArray(JSON_ADDRESS_LIST);
			for(int i = 0; i<jsonArray.length(); i++){
				jsonSingleInfo = jsonArray.getJSONObject(i);
				addressData = new AddressData();
				addressData.setAddressDetail(jsonSingleInfo.getString(JSON_ADDRESS_DETAIL));
				addressData.setAddressId(jsonSingleInfo.getLong(JSON_ADDRESS_ID));
				addressData.setAddressType(jsonSingleInfo.getString(JSON_ADDRESS_TYPE));
				addressData.setCustomerId(jsonSingleInfo.getLong(JSON_CUSTOMER_ID));
				addressData.setDistractId(jsonSingleInfo.getLong(JSON_DISTRACT_ID));
				addressData.setPostCode(jsonSingleInfo.getString(JSON_POST_CODE));
				
				addressQuery.addressDataList.add(addressData);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return addressQuery;
	}

}
