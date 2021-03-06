package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.xiwao.washcar.data.CarInfo;

public class CarQuery extends BaseCommand{
	private static final String CMD = "carQuery.do";
	
	private static final String PARAM_CUSTOM_ID = "Customer_id";
	
	public static final String JSON_RESPONSE_TYPE = "ResponseType";
	public static final String JSON_ERROR_MESSAGE = "ErrorMessage";
	public static final String JSON_CAR_LIST = "Car_List";	
	public static final String JSON_CAR_ID = "car_id";
	public static final String JSON_CAR_CODE = "car_code";
	public static final String JSON_CAR_COLOR = "car_Color";
	public static final String JSON_CAR_BRAND = "car_Brand";
	public static final String JSON_CAR_TYPE = "car_type";
	public static final String JSON_CAR_PIC = "car_pic";
	public static final String JSON_CUSTOM_ID = "customer_id";
	public static final String JSON_RGE_DATE = "reg_date";
	
	private long customerId;
	
	public static class Response extends BaseResponse {
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public List<CarInfo> briefs;
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
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
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response query = new Response();
		
		try {
			query.briefs = new ArrayList<CarInfo>();
			query.okey = true;
			JSONObject jsonObj = new JSONObject(content);
			JSONArray jsonArray;
			JSONObject jsonSingleInfo;
			CarInfo brief;
			
			query.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			query.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			
			jsonArray = jsonObj.getJSONArray(JSON_CAR_LIST);
			for(int i = 0; i<jsonArray.length(); i++){
				jsonSingleInfo = jsonArray.getJSONObject(i);
				brief = new CarInfo();
				brief.setCarBrand(jsonSingleInfo.getString(JSON_CAR_BRAND));
				brief.setCarCode(jsonSingleInfo.getString(JSON_CAR_CODE).toUpperCase());
				brief.setCarColor(jsonSingleInfo.getString(JSON_CAR_COLOR));
				brief.setCarId(jsonSingleInfo.getLong(JSON_CAR_ID));
				brief.setCarPic(jsonSingleInfo.getString(JSON_CAR_PIC));
				brief.setCarType(jsonSingleInfo.getString(JSON_CAR_TYPE));
				brief.setCustomerId(jsonSingleInfo.getLong(JSON_CUSTOM_ID));
				brief.setRegDate(jsonSingleInfo.getString(JSON_RGE_DATE));
				
				query.briefs.add(brief);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return query;
	}}
