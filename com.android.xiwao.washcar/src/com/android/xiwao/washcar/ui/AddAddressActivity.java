package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.WebSiteData;
import com.android.xiwao.washcar.httpconnection.AddressCreate;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.DistractQuery;
import com.android.xiwao.washcar.utils.DialogUtils;

public class AddAddressActivity extends Activity {
	private Context mContext;
	private Spinner websitInfo;
	private EditText detailAddressEdt;
	private Button cancelBtn;
	private Button sureBtn;
	private Button backBtn;

	private ArrayAdapter<String> typeAdapter;
	private List<WebSiteData> websitListData;

	// ����
	private DialogUtils dialogUtils;

	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	// ���������ض���
	private Handler mHandler;
	private CommandExecuter mExecuter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		mContext = this;
		mLocalSharePref = new LocalSharePreference(this);
		
		websitListData = (List<WebSiteData>) getIntent().getSerializableExtra("websit_list");

		setContentView(R.layout.add_address);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		initSpinner();
	}

	private void initContentView() {
		websitInfo = (Spinner) findViewById(R.id.websit_info);
		detailAddressEdt = (EditText) findViewById(R.id.address_detail);
		sureBtn = (Button) findViewById(R.id.sure_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		backBtn = (Button) findViewById(R.id.backbtn);
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		sureBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String detailAddress = detailAddressEdt.getText().toString();
				long position = websitInfo.getSelectedItemId();
				addressCreate(detailAddress, position);
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@SuppressWarnings("unused")
	private void getWebsitInfo(){
		BaseCommand distractQuery = ClientSession.getInstance().getCmdFactory()
				.getDistractQuery();	//����ID���˴���ʱ����Ϊ1��Ϊ����·����

		mExecuter.execute(distractQuery, mDistractQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAddressQuerySuccess(){
		initSpinner();
	}
	
	/**
	 * ������������صĵ�ַ��ѯ
	 * @param rsp ���񷵻صĵ�ַ��ѯ�����Ϣ
	 */
	private void onReceiveDistractQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			DistractQuery.Response distractQuery = (DistractQuery.Response) rsp;
			if (distractQuery.responseType.equals("N")) {
//				websitListData = distractQuery.webDataList;
				onAddressQuerySuccess();
//				dialogUtils.showToast(addressQueryRsp.errorMessage);
			} else {
				dialogUtils.showToast(distractQuery.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mDistractQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveDistractQueryResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * �������õ�ַ
	 * @param detailAddress
	 * @param id
	 */
	private void addressCreate(String detailAddress, long pos){
		long distractId = websitListData.get((int) pos).getDistractId();
		long customerId = mLocalSharePref.getUserId();
		BaseCommand addressCreate = ClientSession.getInstance().getCmdFactory()
				.getAddressCreate(distractId, detailAddress, customerId);	//����ID���˴���ʱ����Ϊ1��Ϊ����·����

		mExecuter.execute(addressCreate, mAddressCreateRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAddressCreateSuccess(){
		setResult(RESULT_OK);
		finish();
	}
	
	/**
	 * ������������صĵ�ַ��ѯ
	 * @param rsp ���񷵻صĵ�ַ��ѯ�����Ϣ
	 */
	private void onReceiveAddressCreateResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			AddressCreate.Response addressCreate = (AddressCreate.Response) rsp;
			if (addressCreate.responseType.equals("N")) {
				
				onAddressCreateSuccess();
				dialogUtils.showToast(addressCreate.errorMessage);
			} else {
				dialogUtils.showToast(addressCreate.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mAddressCreateRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveAddressCreateResponse(rsp);
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
		RelativeLayout websitInfo = (RelativeLayout) findViewById(R.id.websit);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0, 0);
		websitInfo.setLayoutParams(params);

		// ��ϸ��ַ
		RelativeLayout detailAddress = (RelativeLayout) findViewById(R.id.address);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f), 0, 0);
		detailAddress.setLayoutParams(params);

		// ��ť
		LinearLayout buttonGroup = (LinearLayout) findViewById(R.id.button_group);
		LinearLayout.LayoutParams sureBtnParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.12f + 0.5f));
		sureBtnParams.setMargins(0, (int) (displayHeight * 0.002f + 0.5f), 0, 0);
		buttonGroup.setLayoutParams(sureBtnParams);

		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.4f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int) (displayWidth * 0.05f + 0.5f), 0, 0, 0);
		cancelBtn.setLayoutParams(btnParams);
		btnParams = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.4f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int) (displayWidth * 0.1f + 0.5f), 0, 0, 0);
		sureBtn.setLayoutParams(btnParams);
	}
	
	private void initSpinner() {
		int listLength = websitListData.size();
		String [] websitArray = new String[listLength];
		for(int i = 0; i<listLength; i++){
			websitArray[i] = websitListData.get(i).getBranchName();
		}
		typeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, websitArray);
		// ���������б�ķ��
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter2 ��ӵ�spinner��
		websitInfo.setAdapter(typeAdapter);

		// ����¼�Spinner�¼�����
		websitInfo.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
	}

	class SpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}
}
