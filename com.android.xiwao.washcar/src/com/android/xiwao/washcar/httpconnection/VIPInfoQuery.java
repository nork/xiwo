package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.xiwao.washcar.data.MonthlyCarData;

public class VIPInfoQuery extends BaseCommand{
	private final static String CMD = "VIPInfoQuery.do";
	
	private final static String PARAMS_CUSTOMER_ID = "Customer_id";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_VIPINFO_LIST = "VIPInfoList";
	public final static String JSON_CAR_ID = "car_id";
	public final static String JSON_CAR_CODE = "car_code";
	public final static String JSON_CAR_COLOR = "car_color";
	public final static String JSON_CAR_BRAND = "car_brand";
	public final static String JSON_CUSTOMER_ID = "customer_id";
	public final static String JSON_START_DATE = "start_date";
	public final static String JSON_END_DATE = "end_date";
	public final static String JSON_DISTRACT_FIRST_ID = "distract_first_id";
	public final static String JSON_BUFF_TIMES = "buff_times";
	public final static String JSON_WASH_TIMES = "wash_times";
	public final static String JSON_INTERIOR_TIMES = "interior_times";
	
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
		public List<MonthlyCarData> monthlyCarDataList;
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
		Response vipInfoQuery = new Response();

		try {
			vipInfoQuery.monthlyCarDataList = new ArrayList<MonthlyCarData>();
			vipInfoQuery.okey = true;
			JSONObject jsonObj = new JSONObject(content);
			JSONArray jsonArray;
			JSONObject jsonSingleInfo;
			MonthlyCarData brief;

			vipInfoQuery.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			vipInfoQuery.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
		
			jsonArray = jsonObj.getJSONArray(JSON_VIPINFO_LIST);
			for(int i = 0; i<jsonArray.length(); i++){
				jsonSingleInfo = jsonArray.getJSONObject(i);
				brief = new MonthlyCarData();
				brief.setBuffTimes(jsonSingleInfo.getInt(JSON_BUFF_TIMES));
				brief.setCarBrand(jsonSingleInfo.getString(JSON_CAR_BRAND));
				brief.setCarCode(jsonSingleInfo.getString(JSON_CAR_CODE));
				brief.setCarColor(jsonSingleInfo.getString(JSON_CAR_COLOR));
				brief.setCarId(jsonSingleInfo.getInt(JSON_CAR_ID));
				brief.setCustomerId(jsonSingleInfo.getInt(JSON_CUSTOMER_ID));
				brief.setDistractFirstId(jsonSingleInfo.getInt(JSON_DISTRACT_FIRST_ID));
				brief.setEndDate(jsonSingleInfo.getString(JSON_END_DATE));
				brief.setInteriorTimes(jsonSingleInfo.getInt(JSON_INTERIOR_TIMES));
				brief.setStartDate(jsonSingleInfo.getString(JSON_START_DATE));
				brief.setWashTimes(jsonSingleInfo.getInt(JSON_WASH_TIMES));
				
				vipInfoQuery.monthlyCarDataList.add(brief);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vipInfoQuery;
	}
}
