package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.AddressData;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.data.FeeData;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.PlaceOrder;
import com.android.xiwao.washcar.httpconnection.RateQuery;
import com.android.xiwao.washcar.utils.DialogUtils;

public class CarInfoEditActivity extends Activity {
	private final static String TAG = "CarInfoEditActivity";
	private Context mContext;
	private RelativeLayout serverType;
	private RelativeLayout carNum;
	private RelativeLayout website;
	private RelativeLayout contactNum;
	private LinearLayout allMountTimePart;
	private LinearLayout cleanInPart;
	private Button submitBtn;
	private Button backBtn;
	private Spinner spinnerServerType;
	private TextView carNumEdt;
	private TextView websitEdt;
	private TextView price;
	private EditText contactEdt;
	private Button cleanInBtn;

	private ArrayAdapter typeAdapter;
	
	private CarInfo choiceCar;
	private AddressData choiceAddress;
	private int priceCount;	//总价
	private int monthTime = 1;	//包月次数
//	private String branchName;
	
	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		mLocalSharePref = new LocalSharePreference(this);
		mContext = this;
		try{
			choiceCar = getIntent().getParcelableExtra("choice_car");
		}catch(Exception e){
			e.printStackTrace();
			choiceCar = null;
		}
		getAddrCarInfo();
		setContentView(R.layout.car_info_edit);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		initAdapter();
		if(MainActivity.feeDataList.size() <= 0){
			rateQuery();
		}
		setPriceView();
//		((XiwaoApplication)getApplication()).setIfNeedRefreshOrder(true);
	}

	public void initContentView() {
		// TODO Auto-generated method stub
		serverType = (RelativeLayout) findViewById(R.id.server_type);
		carNum = (RelativeLayout) findViewById(R.id.car_num);
		website = (RelativeLayout) findViewById(R.id.website);
		contactNum = (RelativeLayout) findViewById(R.id.contact);
		allMountTimePart = (LinearLayout) findViewById(R.id.all_month_time_part);
		cleanInPart = (LinearLayout) findViewById(R.id.clean_in_part);
		submitBtn = (Button) findViewById(R.id.submit);
		spinnerServerType = (Spinner) findViewById(R.id.spinner_server_type);
		carNumEdt = (TextView) findViewById(R.id.car_num_edt);
		websitEdt = (TextView) findViewById(R.id.websit_edt);
		contactEdt = (EditText) findViewById(R.id.contact_edt);
		price = (TextView) findViewById(R.id.price);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("订单");
		
		submitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(carNumEdt.getText().toString().length() <= 0){
					dialogUtils.showToast("请填入正确的车辆信息！");
					return;
				}else if(websitEdt.getText().toString().length() <= 0){
					dialogUtils.showToast("请填入正确的停车地址！");
					return;
				}else if(contactEdt.getText().toString().length() <= 0){
					dialogUtils.showToast("请填入正确的电话号码！");
					return;
				}
				placeOrder();
			}
		});
		
		backBtn = (Button)findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		carNum.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CarInfoEditActivity.this, CarListActivity.class);
				startActivityForResult(intent, Constants.CHIOCE_CAR_RESULT_CODE);
			}
		});
		
		website.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CarInfoEditActivity.this, AddressActivity.class);
				startActivityForResult(intent, Constants.CHIOCE_ADDRESS_RESULT_CODE);
			}
		});
		
		if(choiceAddress != null){
			websitEdt.setText(choiceAddress.getBranchName() + " " + choiceAddress.getAddressDetail());
		}
		if(choiceCar != null){
			carNumEdt.setText(choiceCar.getCarCode());
		}
		contactEdt.setText(mLocalSharePref.getUserName());
		
		//清洗内饰
		cleanInBtn = (Button) findViewById(R.id.clean_in_btn);
		cleanInBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int cleanInPrice = 0;
				for(FeeData feeData : MainActivity.feeDataList){
					if(feeData.getFeeType().equals("01")){
						cleanInPrice = feeData.getFee();
						break;
					}
				}
				if(v.isSelected()){
					v.setSelected(false);
					priceCount -= cleanInPrice;
				}else{
					v.setSelected(true);
					priceCount += cleanInPrice;
				}
				String priceStr = Integer.toString(priceCount);
				try{
					price.setText(priceStr.subSequence(0, priceStr.length() - 2) + "." + priceStr.substring(priceStr.length() - 2));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		//包月部分
		Button addBtn = (Button) findViewById(R.id.add_btn);
		Button plusBtn = (Button) findViewById(R.id.plus_btn);
		final EditText monthTimeEdt = (EditText) findViewById(R.id.all_month_time);
		monthTimeEdt.setText(Integer.toString(monthTime));
		monthTimeEdt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				try{
					monthTime = Integer.parseInt(monthTimeEdt.getText().toString());
				}catch(Exception e){
					monthTime = 1;
					monthTimeEdt.setText(Integer.toString(monthTime));
					dialogUtils.showToast("包月次数只能在1-12之间");
				}
				if(monthTime > 12 || monthTime < 1){
					monthTime = 1;
					monthTimeEdt.setText(Integer.toString(monthTime));
					dialogUtils.showToast("包月次数只能在1-12之间");
				}
				String priceStr = Integer.toString(priceCount * monthTime);
				try{
					price.setText(priceStr.subSequence(0, priceStr.length() - 2) + "." + priceStr.substring(priceStr.length() - 2));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		addBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				monthTime ++;
				if(monthTime > 12){
					monthTime = 1;
				}
				monthTimeEdt.setText(Integer.toString(monthTime));
			}
		});
		plusBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				monthTime --;
				if(monthTime < 1){
					monthTime = 12;
				}
				monthTimeEdt.setText(Integer.toString(monthTime));
			}
		});
	}	

	private void placeOrder(){
		String serviceType = "";
		switch(spinnerServerType.getSelectedItemPosition()){
		case 0:
			serviceType = "A";
			cleanInPart.setVisibility(View.VISIBLE);
			allMountTimePart.setVisibility(View.GONE);
			break;
		case 1:
			serviceType = "B";
			cleanInPart.setVisibility(View.VISIBLE);
			allMountTimePart.setVisibility(View.GONE);
			break;
		case 2:
			serviceType = "C";
			cleanInPart.setVisibility(View.GONE);
			allMountTimePart.setVisibility(View.VISIBLE);
			break;
		}
		String serverTypeMi = "";
		if(cleanInBtn.isSelected()){
			serverTypeMi = "01";
		}
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getPlaceOrder(mLocalSharePref.getUserId(), serviceType, contactEdt.getText().toString(), choiceCar.getCarId()
						, choiceAddress.getDistractId(), choiceAddress.getAddressId(), "00", null, websitEdt.getText().toString()
						, serverTypeMi, priceCount * monthTime);

		mExecuter.execute(carRegister, mPlaceOrderRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onPlaceOrderSuccess(int saleFee, long orderId){
		saveAddrCarInfo();
		((XiwaoApplication)getApplication()).setIfNeedRefreshOrder(true);
		Intent intent = new Intent(CarInfoEditActivity.this, PayDetailActivity.class);
		intent.putExtra("fee", priceCount * monthTime);
		intent.putExtra("sale_fee", saleFee);//账户支付价格
		intent.putExtra("order_id", orderId);
		intent.putExtra("car_code", carNumEdt.getText().toString());
		intent.putExtra("server_type", spinnerServerType.getSelectedItem().toString());
		intent.putExtra("phone", contactEdt.getText().toString());
		intent.putExtra("address", websitEdt.getText().toString());
		intent.putExtra("monthly_time", monthTime);	//包月数量
		intent.putExtra("if_clean_in", cleanInBtn.isSelected());//是否清洗内饰
		startActivity(intent);
		finish();
	}
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceivePlaceOrderResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			PlaceOrder.Response placeOrder = (PlaceOrder.Response) rsp;
			if (placeOrder.responseType.equals("N")) {
				onPlaceOrderSuccess(placeOrder.saleFee, placeOrder.orderId);
				dialogUtils.showToast(placeOrder.errorMessage);
			} else {
				dialogUtils.showToast(placeOrder.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mPlaceOrderRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceivePlaceOrderResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * 查询费用
	 */
	public void rateQuery(){
		BaseCommand login = ClientSession.getInstance().getCmdFactory()
				.getRateQuery();

		mExecuter.execute(login, mRespHandler);
		dialogUtils.showProgress();
	}
	
	/**
	 * 处理服务器返回的查询结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveRateQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			RateQuery.Response rateQueryRsp = (RateQuery.Response) rsp;
			if (rateQueryRsp.responseType.equals("N")) {
				MainActivity.feeDataList = rateQueryRsp.briefs;
			} else {
				dialogUtils.showToast(rateQueryRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveRateQueryResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * 初始化需要的工具
	 */
	public void initUtils() {
		dialogUtils = new DialogUtils();
	}

	private void initExecuter() {

		mHandler = new Handler();

		mExecuter = new CommandExecuter();
		mExecuter.setHandler(mHandler);
	}
	
	private void setHwView() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		//title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		// 价格
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0,
				0);
		
		RelativeLayout pricePart = (RelativeLayout) findViewById(R.id.price_part);
		pricePart.setLayoutParams(params);
//		serverType.setLayoutParams(params);
		// 车牌号码
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f),
				0, 0);
		serverType.setLayoutParams(params);
		carNum.setLayoutParams(params);
		// 所在网点
		website.setLayoutParams(params);
		// 联系电话
		contactNum.setLayoutParams(params);
		allMountTimePart.setLayoutParams(params);
		//清洗内饰
		cleanInPart.setLayoutParams(params);
		
		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.1f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		submitBtn.setLayoutParams(params);
	}

	private void initAdapter() {
		typeAdapter = ArrayAdapter.createFromResource(mContext, R.array.server_types,
				android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter2 添加到spinner中
		spinnerServerType.setAdapter(typeAdapter);

		// 添加事件Spinner事件监听
		spinnerServerType
				.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		
		spinnerServerType.setSelection(getIntent().getIntExtra("service_type", 0));
	}
	
	/**
	 * 设置价格
	 */
	private void setPriceView(){
		priceCount = 0;
		switch(spinnerServerType.getSelectedItemPosition()){
		case 0://洗车
			for(FeeData feeData : MainActivity.feeDataList){
				if(feeData.getFeeType().equals("A")){
					priceCount = feeData.getFee();
					break;
				}
			}
			break;
		case 1://打蜡
			for(FeeData feeData : MainActivity.feeDataList){
				if(feeData.getFeeType().equals("B")){
					priceCount = feeData.getFee();
					break;
				}
			}
			break;
		case 2://包月
			for(FeeData feeData : MainActivity.feeDataList){
				if(feeData.getFeeType().equals("C")){
					priceCount = feeData.getFee();
					break;
				}
			}
			break;
		}
		AppLog.v(TAG, "car Type:" + choiceCar.getCarType());
		if(spinnerServerType.getSelectedItemPosition() != 2){	//包月不分车型和是否清洗内饰
			if(choiceCar.getCarType().equals("01")){
				for(FeeData feeData : MainActivity.feeDataList){
					if(feeData.getFeeType().equals("00")){
						priceCount += feeData.getFee();
						break;
					}
				}
			}
			if(cleanInBtn.isSelected()){
				for(FeeData feeData : MainActivity.feeDataList){
					if(feeData.getFeeType().equals("01")){
						priceCount += feeData.getFee();
						break;
					}
				}
			}
		}
		String priceStr = Integer.toString(priceCount);
		try{
			price.setText(priceStr.subSequence(0, priceStr.length() - 2) + "." + priceStr.substring(priceStr.length() - 2));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AppLog.v(TAG, "收到反馈22");
		
		if(resultCode != RESULT_OK){
			return;
		}
		switch(requestCode){
		case Constants.CHIOCE_CAR_RESULT_CODE:
			choiceCar = data.getParcelableExtra("choice_car");
			AppLog.v(TAG, "car id:" + choiceCar.getCarCode());
			carNumEdt.setText(choiceCar.getCarCode());
			setPriceView();
			break;
		case Constants.CHIOCE_ADDRESS_RESULT_CODE:
			choiceAddress = data.getParcelableExtra("choice_address");
			websitEdt.setText(choiceAddress.getBranchName() + " " + choiceAddress.getAddressDetail());
			break;
		}
	}
	/**
	 * 保存车辆和地址信息
	 */
	private void saveAddrCarInfo(){
		mLocalSharePref.putStringPref(LocalSharePreference.USER_LAST_ADDRESS_DETAIL, choiceAddress.getAddressDetail());
		mLocalSharePref.putStringPref(LocalSharePreference.USER_LAST_BRANCH_NAME, choiceAddress.getBranchName());
		mLocalSharePref.putStringPref(LocalSharePreference.USER_LAST_CAR_NUM, choiceCar.getCarCode());
		mLocalSharePref.putLongPref(LocalSharePreference.USER_LAST_ADDRESS_ID, choiceAddress.getAddressId());
		mLocalSharePref.putLongPref(LocalSharePreference.USER_LAST_CAR_ID, choiceCar.getCarId());
		mLocalSharePref.putLongPref(LocalSharePreference.USER_LAST_DISTRACT_ID, choiceAddress.getDistractId());
		mLocalSharePref.putStringPref(LocalSharePreference.USER_LAST_CAR_TYPE, choiceCar.getCarType());
	}

	private void getAddrCarInfo(){
		if(choiceCar != null){
			return;
		}
		choiceAddress = new AddressData();
		choiceCar = new CarInfo();
		choiceAddress.setAddressDetail(mLocalSharePref.getStringPref(LocalSharePreference.USER_LAST_ADDRESS_DETAIL, ""));
		choiceAddress.setAddressId(mLocalSharePref.getLongPref(LocalSharePreference.USER_LAST_ADDRESS_ID, 0));
		choiceAddress.setDistractId(mLocalSharePref.getLongPref(LocalSharePreference.USER_LAST_DISTRACT_ID, 0));
		choiceCar.setCarCode(mLocalSharePref.getStringPref(LocalSharePreference.USER_LAST_CAR_NUM, ""));
		choiceCar.setCarId(mLocalSharePref.getLongPref(LocalSharePreference.USER_LAST_CAR_ID, 0));
		choiceAddress.setBranchName(mLocalSharePref.getStringPref(LocalSharePreference.USER_LAST_BRANCH_NAME, ""));
		choiceCar.setCarType(mLocalSharePref.getStringPref(LocalSharePreference.USER_LAST_CAR_TYPE, ""));
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}



	class SpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			setPriceView();
			switch(arg2){
			case 0:
				cleanInPart.setVisibility(View.VISIBLE);
				allMountTimePart.setVisibility(View.GONE);
				break;
			case 1:
				cleanInPart.setVisibility(View.VISIBLE);
				allMountTimePart.setVisibility(View.GONE);
				break;
			case 2:
				cleanInPart.setVisibility(View.GONE);
				allMountTimePart.setVisibility(View.VISIBLE);
				break;
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
