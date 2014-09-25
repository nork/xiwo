package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.xiwao.washcar.data.FeeData;

public class RateQuery extends BaseCommand{
	
	private static final String CMD = "rateQuery.do";
	
	public static final String JSON_RESPONSE_TYPE = "ResponseType";
	public static final String JSON_ERROR_MESSAGE = "ErrorMessage";
	public static final String JSON_FEE_LIST = "FeeList";
	public static final String JSON_RATE_ID = "rate_id";
	public static final String JSON_FEE_TYPE = "fee_type";
	public static final String JSON_FEE = "fee";
	
	public static class Response extends BaseResponse {

		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public List<FeeData> briefs;
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
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response rateQuery = new Response();
		
		try {
			rateQuery.briefs = new ArrayList<FeeData>();
			JSONObject jsonObj = new JSONObject(content);
			JSONArray jsonArray;
			JSONObject jsonSingleInfo;
			FeeData brief;
			
			rateQuery.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			rateQuery.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			rateQuery.okey = true;
			
			jsonArray = jsonObj.getJSONArray(JSON_FEE_LIST);
			for(int i = 0; i<jsonArray.length(); i++){
				jsonSingleInfo = jsonArray.getJSONObject(i);
				brief = new FeeData();
				brief.setFeeId(jsonSingleInfo.getLong(JSON_RATE_ID));	
				brief.setFee(jsonSingleInfo.getInt(JSON_FEE));
				brief.setFeeType(jsonSingleInfo.getString(JSON_FEE_TYPE));
				rateQuery.briefs.add(brief);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return rateQuery;
	}

}
