package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class CarRegister extends BaseCommand{
	private static final String CMD = "carRegister.do";
	
	private static final String PARAM_CAR_CODE = "Car_code";
	private static final String PARAM_CUSTOM_ID = "Customer_id";
	private static final String PARAM_CAR_COLOR = "Car_Color";
	private static final String PARAM_CAR_BRAND = "Car_Brand";
	private static final String PARAM_CAR_TYPE = "Car_type";
	private static final String PARAM_CAR_PIC = "Car_pic";
	
	public static final String JSON_RESPONSE_TYPE = "ResponseType";
	public static final String JSON_ERROR_MESSAGE = "ErrorMessage";
	public static final String JSON_CAR_ID = "Car_id";
	
	private String carCode;
	private String carColor;
	private long customerId;
	private String carBrand;
	private String carType;
	private String carPic;
	
	public static class Response extends BaseResponse {
		public String carId;
		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;

		public String errorMessage;
		public String responseType;
	}
	
	public String getCarCode() {
		return carCode;
	}

	public void setCarCode(String carCode) {
		this.carCode = carCode;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarPic() {
		return carPic;
	}

	public void setCarPic(String carPic) {
		this.carPic = carPic;
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
		nvps.add( new BasicNameValuePair(PARAM_CAR_CODE, carCode));
		nvps.add( new BasicNameValuePair(PARAM_CAR_COLOR, carColor));
		nvps.add( new BasicNameValuePair(PARAM_CAR_BRAND, carBrand));
		nvps.add( new BasicNameValuePair(PARAM_CAR_TYPE, carType));
		nvps.add( new BasicNameValuePair(PARAM_CAR_PIC, carPic));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		// TODO Auto-generated method stub
		Response carRegister = new Response();
		
		try {
			JSONObject jsonObj = new JSONObject(content);
			carRegister.responseType = jsonObj.getString(JSON_RESPONSE_TYPE);
			carRegister.errorMessage = jsonObj.getString(JSON_ERROR_MESSAGE);
			carRegister.okey = true;
			
			carRegister.carId = jsonObj.getString(JSON_CAR_ID);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return carRegister;
	}

}
