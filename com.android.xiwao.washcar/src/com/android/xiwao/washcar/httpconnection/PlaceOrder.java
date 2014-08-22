package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceOrder extends BaseCommand{
	private final static String CMD = "placeOrder.do";
	
	private final static String PARAMS_CUSTOM_ID = "Customer_id";
	private final static String PARAMS_SERVICE_TYPE = "Service_type";
	private final static String PARAMS_MOBILE_NUM = "Mobile_number";
	private final static String PARAMS_CAR_ID = "Car_id";
	private final static String PARAMS_DISTRACT_ID = "Distract_first_id";
	private final static String PARAMS_ADDRESS_ID = "Address_id";
	private final static String PARAMS_PAY_TYPE = "Pay_type";
	private final static String PARAMS_NOTE = "Note";
	private final static String PARAMS_ADDRESS = "Address";
	
	public final static String JSON_RESPONSE_TYPE = "ResponseType";
	public final static String JSON_ERROR_MESSAGE = "ErrorMessage";
	public final static String JSON_ORDER_ID = "Order_id";
	public final static String JSON_FEE = "Fee";
	
	private long customerId;
	private String serviceType;
	private String mobileNum;
	private long carId;
	private long distractId;
	private long addressId;
	private String payType;
	private String note;
	private String address;
	
	public static class Response extends BaseResponse {
		
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
		public long orderId;
		public int fee;
	}

	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public long getCarId() {
		return carId;
	}

	public void setCarId(long carId) {
		this.carId = carId;
	}

	public long getDistractId() {
		return distractId;
	}

	public void setDistractId(long distractId) {
		this.distractId = distractId;
	}

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		nvps.add(new BasicNameValuePair(PARAMS_SERVICE_TYPE, serviceType));
		nvps.add(new BasicNameValuePair(PARAMS_MOBILE_NUM, mobileNum));
		nvps.add(new BasicNameValuePair(PARAMS_CAR_ID, Long
				.toString(carId)));
		nvps.add(new BasicNameValuePair(PARAMS_DISTRACT_ID, Long
				.toString(distractId)));
		nvps.add(new BasicNameValuePair(PARAMS_ADDRESS_ID, Long
				.toString(addressId)));
		nvps.add(new BasicNameValuePair(PARAMS_PAY_TYPE, payType));
		nvps.add(new BasicNameValuePair(PARAMS_NOTE,note));
		nvps.add(new BasicNameValuePair(PARAMS_ADDRESS, address));
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response placeOrder = new Response();

		try {
			placeOrder.okey = true;
			JSONObject jsonObj = new JSONObject(content);

			placeOrder.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			placeOrder.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			placeOrder.fee = jsonObj.getInt(JSON_FEE);
			placeOrder.orderId = jsonObj.getLong(JSON_ORDER_ID);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return placeOrder;
	}
}
