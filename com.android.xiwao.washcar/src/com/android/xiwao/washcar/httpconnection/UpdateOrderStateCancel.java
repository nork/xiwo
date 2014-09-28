package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateOrderStateCancel extends BaseCommand{
	private final static String CMD = "updateOrderStateCancel.do";
	public final static String PARAMS_ORDER_ID = "Order_id";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	
	private int orderId;
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
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
		nvps.add(new BasicNameValuePair(PARAMS_ORDER_ID, Integer
				.toString(orderId)));
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response updateOrderStateCancel = new Response();

		try {
			JSONObject jsonObj = new JSONObject(content);
			updateOrderStateCancel.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			updateOrderStateCancel.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			updateOrderStateCancel.okey = true;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return updateOrderStateCancel;
	}
}
