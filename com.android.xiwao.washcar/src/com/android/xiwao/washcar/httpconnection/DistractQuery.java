package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.xiwao.washcar.data.WebSiteData;

public class DistractQuery extends BaseCommand{
	private final static String CMD = "distractQuery.do";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_DISTRACT_LIST = "Distract_List";
	public final static String JSON_BRANCH_NAME = "branch_name";
	public final static String JSON_DISTRACT_ID = "distract_first_id";
	public final static String JSON_PROVINCE = "province";
	public final static String JSON_CITY = "city";
	public final static String JSON_DISTRACT = "distract";
	public final static String JSON_STREET = "street";
	public final static String JSON_MAP = "map";
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public List<WebSiteData> webDataList;
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
		Response distractQuery = new Response();

		try {
			distractQuery.webDataList = new ArrayList<WebSiteData>();
			distractQuery.okey = true;
			JSONObject jsonObj = new JSONObject(content);
			JSONArray jsonArray;
			JSONObject jsonSingleInfo;
			WebSiteData brief;

			distractQuery.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			distractQuery.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
		
			jsonArray = jsonObj.getJSONArray(JSON_DISTRACT_LIST);
			for(int i = 0; i<jsonArray.length(); i++){
				jsonSingleInfo = jsonArray.getJSONObject(i);
				brief = new WebSiteData();
				brief.setBranchName(jsonSingleInfo.getString(JSON_BRANCH_NAME));
				brief.setCity(jsonSingleInfo.getString(JSON_CITY));
				brief.setDistract(jsonSingleInfo.getString(JSON_DISTRACT));
				brief.setDistractId(jsonSingleInfo.getLong(JSON_DISTRACT_ID));
				brief.setMap(jsonSingleInfo.getString(JSON_MAP));
				brief.setProvince(jsonSingleInfo.getString(JSON_PROVINCE));
				brief.setStreet(jsonSingleInfo.getString(JSON_STREET));
				
				distractQuery.webDataList.add(brief);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return distractQuery;
	}
}
