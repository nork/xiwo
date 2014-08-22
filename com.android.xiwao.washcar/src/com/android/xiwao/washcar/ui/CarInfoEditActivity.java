package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.PlaceOrder;
import com.android.xiwao.washcar.utils.DialogUtils;

public class CarInfoEditActivity extends Activity {
	private final static String TAG = "CarInfoEditActivity";
	private Context mContext;
	private RelativeLayout serverType;
	private RelativeLayout carNum;
	private RelativeLayout website;
	private RelativeLayout contactNum;
	private Button submitBtn;
	private Button backBtn;
	private Spinner spinnerServerType;
	private TextView carNumEdt;
	private TextView websitEdt;
	private EditText contactEdt;

	private ArrayAdapter typeAdapter;
	
	private CarInfo choiceCar;
	private AddressData choiceAddress;
	
	// ����
	private DialogUtils dialogUtils;

	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	// ���������ض���
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
		setContentView(R.layout.car_info_edit);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		initAdapter();
		
		((XiwaoApplication)getApplication()).setIfNeedRefreshOrder(true);
	}

	public void initContentView() {
		// TODO Auto-generated method stub
		serverType = (RelativeLayout) findViewById(R.id.server_type);
		carNum = (RelativeLayout) findViewById(R.id.car_num);
		website = (RelativeLayout) findViewById(R.id.website);
		contactNum = (RelativeLayout) findViewById(R.id.contact);
		submitBtn = (Button) findViewById(R.id.submit);
		spinnerServerType = (Spinner) findViewById(R.id.spinner_server_type);
		carNumEdt = (TextView) findViewById(R.id.car_num_edt);
		websitEdt = (TextView) findViewById(R.id.websit_edt);
		contactEdt = (EditText) findViewById(R.id.contact_edt);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.car_info);
		
		submitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				radioButton.setChecked(true);
//				Intent intent = new Intent(CarInfoEditActivity.this, PayDetailActivity.class);
//				intent.putExtra("choice_car", choiceCar);
//				intent.putExtra("choice_address", choiceAddress);
//				intent.putExtra("mobile_num", contactEdt.getText().toString());
//				startActivity(intent);
				if(carNumEdt.getText().toString().length() <= 0){
					dialogUtils.showToast("�����복����Ϣ��");
					return;
				}else if(websitEdt.getText().toString().length() <= 0){
					dialogUtils.showToast("�������ַ��Ϣ��");
					return;
				}else if(contactEdt.getText().toString().length() <= 0){
					dialogUtils.showToast("������绰���룡");
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
	}

	private void placeOrder(){
		String serviceType = "";
		switch(spinnerServerType.getSelectedItemPosition()){
		case 0:
			serviceType = "01";
			break;
		case 1:
			serviceType = "02";
			break;
		}
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getPlaceOrder(mLocalSharePref.getUserId(), serviceType, contactEdt.getText().toString(), choiceCar.getCarId()
						, choiceAddress.getDistractId(), choiceAddress.getAddressId(), "00", null, choiceAddress.getAddressDetail());

		mExecuter.execute(carRegister, mPlaceOrderRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onPlaceOrderSuccess(int fee, long orderId){
		((XiwaoApplication)getApplication()).setIfNeedRefreshOrder(true);
		Intent intent = new Intent(CarInfoEditActivity.this, PayDetailActivity.class);
		intent.putExtra("fee", fee);
		intent.putExtra("order_id", orderId);
		startActivity(intent);
		finish();
	}
	
	/**
	 * ������������صĳ���ע����
	 * @param rsp ���񷵻صĳ���ע������Ϣ
	 */
	private void onReceivePlaceOrderResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			PlaceOrder.Response placeOrder = (PlaceOrder.Response) rsp;
			if (placeOrder.responseType.equals("N")) {
				onPlaceOrderSuccess(placeOrder.fee, placeOrder.orderId);
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
	 * ��ʼ����Ҫ�Ĺ���
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
		//title�߶�
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		// ��������
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0,
				0);
		serverType.setLayoutParams(params);
		// ���ƺ���
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f),
				0, 0);
		carNum.setLayoutParams(params);
		// ��������
		website.setLayoutParams(params);
		// ��ϵ�绰
		contactNum.setLayoutParams(params);

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
		// ���������б�ķ��
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter2 ��ӵ�spinner��
		spinnerServerType.setAdapter(typeAdapter);

		// ����¼�Spinner�¼�����
		spinnerServerType
				.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AppLog.v(TAG, "�յ�����22");
		
		if(resultCode != RESULT_OK){
			return;
		}
		switch(requestCode){
		case Constants.CHIOCE_CAR_RESULT_CODE:
			choiceCar = data.getParcelableExtra("choice_car");
			AppLog.v(TAG, "car id:" + choiceCar.getCarCode());
			carNumEdt.setText(choiceCar.getCarCode());
			break;
		case Constants.CHIOCE_ADDRESS_RESULT_CODE:
			choiceAddress = data.getParcelableExtra("choice_address");
			String branchName = data.getStringExtra("branch_name");
			websitEdt.setText(branchName + choiceAddress.getAddressDetail());
			break;
		}
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
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
