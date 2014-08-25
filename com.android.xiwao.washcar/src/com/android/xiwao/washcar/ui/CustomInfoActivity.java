package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.CustomerModify;
import com.android.xiwao.washcar.utils.DialogUtils;

public class CustomInfoActivity extends Activity{
	private EditText userTxt;
	private EditText emailTxt;
	private TextView phoneTxt;
	
	private Button backBtn;
	private Button modifyBtn;
	
	// ����
	private DialogUtils dialogUtils;

	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	// ���������ض���
	private Handler mHandler;
	private CommandExecuter mExecuter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		mLocalSharePref = new LocalSharePreference(this);

		setContentView(R.layout.customer_info);
		initExecuter();
		initUtils();
		initContentView();
		setViewHw();
	}
	
	public void initContentView(){
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(R.string.custom_info);
		
		userTxt = (EditText)findViewById(R.id.user_name_txt);
		emailTxt = (EditText)findViewById(R.id.email_txt);
		phoneTxt = (TextView)findViewById(R.id.phone_txt);
		modifyBtn = (Button) findViewById(R.id.modify_btn);
		
		userTxt.setText(mLocalSharePref.getNickName());
		emailTxt.setText(mLocalSharePref.getUserEmail());
		phoneTxt.setText(mLocalSharePref.getUserName());
		
		backBtn = (Button)findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		modifyBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String btnTxt = modifyBtn.getText().toString();
				if(btnTxt.equals("�޸�")){
					emailTxt.setFocusable(true);
					emailTxt.setFocusableInTouchMode(true);
					userTxt.setFocusable(true);
					userTxt.setFocusableInTouchMode(true);
					userTxt.requestFocus();
					userTxt.requestFocusFromTouch();
					modifyBtn.setText("�ύ");
				}else if(btnTxt.equals("�ύ")){
					modifyUserInfo(userTxt.getText().toString(), emailTxt.getText().toString());
				}
			}
		});
	}
	
	private void modifyUserInfo(String userName, String email){
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getCustomerModify(mLocalSharePref.getUserId(), userName, email);

		mExecuter.execute(carRegister, mCustomerModifyRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onCustomerModifySuccess(){
		modifyBtn.setText("�޸�");
		mLocalSharePref.setNickName(userTxt.getText().toString());
		mLocalSharePref.setUserEmail(emailTxt.getText().toString());
		emailTxt.setFocusable(false);
		userTxt.setFocusable(false);
	}
	
	/**
	 * ������������صĳ���ע����
	 * @param rsp ���񷵻صĳ���ע������Ϣ
	 */
	private void onReceiveCustomerModifyResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			CustomerModify.Response customerModify = (CustomerModify.Response) rsp;
			if (customerModify.responseType.equals("N")) {
				onCustomerModifySuccess();
				dialogUtils.showToast(customerModify.errorMessage);
			} else {
				dialogUtils.showToast(customerModify.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mCustomerModifyRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCustomerModifyResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	public void setViewHw(){
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
//		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		//title�߶�
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		
		//ͷ�񲿷�
		RelativeLayout head = (RelativeLayout) findViewById(R.id.head_img);
		RelativeLayout name = (RelativeLayout) findViewById(R.id.name);
		RelativeLayout user = (RelativeLayout) findViewById(R.id.user);
		RelativeLayout email = (RelativeLayout) findViewById(R.id.email);
		RelativeLayout phone = (RelativeLayout) findViewById(R.id.phone);
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.24f + 0.5f));
		listParams.setMargins(0, (int)(displayHeight * 0.04f + 0.5f), 0, 0);
		head.setLayoutParams(listParams);
		listParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		listParams.setMargins(0, (int)(displayHeight * 0.001f + 0.5f), 0, 0);
		name.setLayoutParams(listParams);
		user.setLayoutParams(listParams);
		email.setLayoutParams(listParams);
		phone.setLayoutParams(listParams);
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}
	
}
