package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.xiwao.washcar.data.OrderData;

public class OrderQuery extends BaseCommand{
	private final static String CMD = "orderQuery.do";
	
	private final static String PARAMS_CUSTOM_ID = "Customer_id";
	private final static String PARAMS_ORDER_STATE = "Order_state";
	private final static String PARAMS_START_INDEX = "StartIndex";
	private final static String PARAMS_PAGE = "Page";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_ORDER_LIST = "Order_List";
	public final static String JSON_ORDER_ID = "order_id";
	public final static String JSON_CUSTOM_ID = "customer_id";
	public final static String JSON_SERVICE_TYPE = "service_type";
	public final static String JSON_MOBILE_NUM = "mobile_number";
	public final static String JSON_CAR_CODE = "car_code";
	public final static String JSON_ADDRESS = "address";
	public final static String JSON_CREATE_TIME = "creat_time";
	public final static String JSON_PAY_TIME = "pay_time";
	public final static String JSON_WASH_BEGINTIME = "wash_begin";
	public final static String JSON_WASH_ENDTIME = "wash_end";
	public final static String JSON_ORDER_STATE = "order_state";
	public final static String JSON_NOTE = "note";
	public final static String JSON_FEE = "fee";
	
	private long customerId;
	private String orderState;
	private int startIndex;
	private int page;
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public List<OrderData> orderDataList;
	}

	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
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
		nvps.add(new BasicNameValuePair(PARAMS_CUSTOM_ID, Long
				.toString(customerId)));
		nvps.add(new BasicNameValuePair(PARAMS_ORDER_STATE, orderState));
		nvps.add(new BasicNameValuePair(PARAMS_START_INDEX, Integer
				.toString(startIndex)));
		nvps.add(new BasicNameValuePair(PARAMS_PAGE, Integer
				.toString(page)));
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response orderQuery = new Response();

		try {
			orderQuery.orderDataList = new ArrayList<OrderData>();
			orderQuery.okey = true;
			JSONObject jsonObj = new JSONObject(content);
			JSONArray jsonArray;
			JSONObject jsonSingleInfo;
			OrderData brief;

			orderQuery.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			orderQuery.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
		
			jsonArray = jsonObj.getJSONArray(JSON_ORDER_LIST);
			for(int i = 0; i<jsonArray.length(); i++){
				jsonSingleInfo = jsonArray.getJSONObject(i);
				brief = new OrderData();
				brief.setAddressDetail(jsonSingleInfo.getString(JSON_ADDRESS));
				brief.setCarCode(jsonSingleInfo.getString(JSON_CAR_CODE));
				brief.setCreateTime(jsonSingleInfo.getString(JSON_CREATE_TIME));
				brief.setCustmerId(jsonSingleInfo.getLong(JSON_CUSTOM_ID));
				brief.setMobileNum(jsonSingleInfo.getString(JSON_MOBILE_NUM));
				brief.setNote(jsonSingleInfo.getString(JSON_NOTE));
				brief.setOrderId(jsonSingleInfo.getLong(JSON_ORDER_ID));
				brief.setOrderState(jsonSingleInfo.getString(JSON_ORDER_STATE));
				brief.setPayTime(jsonSingleInfo.getString(JSON_PAY_TIME));
				String serviceType = jsonSingleInfo.getString(JSON_SERVICE_TYPE);
				if(serviceType.equals("01")){
					serviceType = "Ï´³µ";
				}else if(serviceType.equals("02")){
					serviceType = "Ï´³µ\n´òÀ¯";
				}
				brief.setServiceType(serviceType);
				brief.setWashBegin(jsonSingleInfo.getString(JSON_WASH_BEGINTIME));
				brief.setWashEnd(jsonSingleInfo.getString(JSON_WASH_ENDTIME));
				int fee = jsonSingleInfo.getInt(JSON_FEE);
				brief.setFee((fee/100) + ".00");
//				brief.setFee(jsonSingleInfo.getString(JSON_FEE));
				
				orderQuery.orderDataList.add(brief);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderQuery;
	}
}
