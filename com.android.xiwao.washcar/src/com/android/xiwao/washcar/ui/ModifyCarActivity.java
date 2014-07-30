package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
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
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CarModify;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.utils.DialogUtils;

public class ModifyCarActivity extends Activity {
	private RelativeLayout carType;
	private RelativeLayout carNum;
	private RelativeLayout carBrand;
	private RelativeLayout carColor;
	private RelativeLayout carPic;

	private Button submitBtn;
	private Button backBtn;
	private Spinner spinnerCarType;
	private EditText carNumEdt;
	private EditText carBrandEdt;
	private EditText carColorEdt;

	@SuppressWarnings("rawtypes")
	private ArrayAdapter typeAdapter;

	// ����
	private DialogUtils dialogUtils;

	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	// ���������ض���
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	//����
	private CarInfo modifyCarInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);

		mLocalSharePref = new LocalSharePreference(this);
		modifyCarInfo = getIntent().getExtras().getParcelable(Constants.CAR_INFO_SEND);

		setContentView(R.layout.add_car);
		
		initExecuter();
		initUtils();
		initContentView();
		initAdapter();
		initCarData();
		setHwView();
	}

	/**
	 * 
	 */
	public void initContentView() {
		// TODO Auto-generated method stub
		carType = (RelativeLayout) findViewById(R.id.car_type);
		carNum = (RelativeLayout) findViewById(R.id.car_num);
		carBrand = (RelativeLayout) findViewById(R.id.car_brand);
		carColor = (RelativeLayout) findViewById(R.id.car_color);
		carPic = (RelativeLayout) findViewById(R.id.car_pic);
		submitBtn = (Button) findViewById(R.id.submit);
		spinnerCarType = (Spinner) findViewById(R.id.spinner_car_type);
		carNumEdt = (EditText) findViewById(R.id.car_num_edt);
		carBrandEdt = (EditText) findViewById(R.id.car_brand_edt);
		carColorEdt = (EditText) findViewById(R.id.car_color_edt);
		backBtn = (Button) findViewById(R.id.backbtn);
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.car_info);

		submitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addCar(carNumEdt.getText().toString().trim(), carBrandEdt.getText().toString().trim()
						, carColorEdt.getText().toString().trim(), spinnerCarType.getSelectedItemPosition()
						, null);
			}
		});
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	/**
	 * ���Ĭ�ϵ�������Ϣ
	 */
	private void initCarData(){
		carNumEdt.setText(modifyCarInfo.getCarCode());
		carColorEdt.setText(modifyCarInfo.getCarColor());
		carBrandEdt.setText(modifyCarInfo.getCarBrand());
		if(modifyCarInfo.getCarType().equals("00")){
			spinnerCarType.setSelection(0);
		}else if(modifyCarInfo.getCarType().equals("01")){
			spinnerCarType.setSelection(1);
		}
	}

	private void addCar(String carCode, String carBrand,
			String carColor, int type, String carPic){
		if(carCode.length() <= 0){
			dialogUtils.showToast(getString(R.string.car_code_null_error));
			return;
		}else if(carBrand.length() <= 0){
			dialogUtils.showToast(getString(R.string.car_brand_null_error));
			return;
		}else if(carColor.length() <= 0){
			dialogUtils.showToast(getString(R.string.car_color_null_error));
			return;
		}
		long customerId = mLocalSharePref.getUserId();
		String carType = null;
		switch(type){
		case 0:
			carType = "00";
			break;
		case 1:
			carType = "01";
			break;
		}
		BaseCommand carModify = ClientSession.getInstance().getCmdFactory()
				.getCarModify(modifyCarInfo.getCarId(), carCode, carBrand, carColor, carType, carPic, customerId);

		mExecuter.execute(carModify, mRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onCarModifySuccess(){
		setResult(RESULT_OK);
		finish();
	}
	
	/**
	 * ������������صĳ���ע����
	 * @param rsp ���񷵻صĳ���ע������Ϣ
	 */
	private void onReceiveCarModifyResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			CarModify.Response carModifyRsp = (CarModify.Response) rsp;
			if (carModifyRsp.responseType.equals("N")) {
				onCarModifySuccess();
				dialogUtils.showToast(carModifyRsp.errorMessage);
			} else {
				dialogUtils.showToast(carModifyRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCarModifyResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	private void setHwView() {
		int displayHeight = ((XiwaoApplication) getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getApplication())
				.getDisplayWidth();
		// title�߶�
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		// ��������
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0, 0);
		carNum.setLayoutParams(params);
		// ���ƺ���
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f), 0, 0);

		carType.setLayoutParams(params);
		// ��������
		carColor.setLayoutParams(params);
		// ��ϵ�绰
		carBrand.setLayoutParams(params);
		// ��Ƭ
		carPic.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.1f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		submitBtn.setLayoutParams(params);
	}

	private void initAdapter() {
		typeAdapter = ArrayAdapter.createFromResource(this, R.array.car_types,
				android.R.layout.simple_spinner_item);
		// ���������б�ķ��
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter2 ��ӵ�spinner��
		spinnerCarType.setAdapter(typeAdapter);

		// ����¼�Spinner�¼�����
		spinnerCarType
				.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
	}

	class SpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// view2.setText("��ʹ��ʲô�����ֻ���" + adapter2.getItem(arg2));
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
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
}
