package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.Login;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.EncryDecryUtils;

public class LoginActivity extends Activity {
	// 控件
	private Button loginBtn;
	private Button registerBtn;
	private Button forgetBtn;
	private TextView title;
	private EditText phoneEdt;
	private EditText pwdEdt;
	private LinearLayout phoneView;
	private LinearLayout passwordView;

	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	//用户名密码
	private String userName;
	private String password;
	private String nickName;
	private String email;
	private String headStr;
	private String userType;
	private long userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		mLocalSharePref = new LocalSharePreference(this);
		
		if(mLocalSharePref.getLoginState()){
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
	    	startActivity(intent);
	    	finish();
		}
		setContentView(R.layout.login);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
	}

	public void initContentView() {
		// TODO Auto-generated method stub
		loginBtn = (Button) findViewById(R.id.login_btn);
		registerBtn = (Button) findViewById(R.id.register_btn);
		forgetBtn = (Button) findViewById(R.id.forget_pwd);
		title = (TextView) findViewById(R.id.title);
		phoneEdt = (EditText) findViewById(R.id.phone_num);
		pwdEdt = (EditText) findViewById(R.id.password);
		phoneView = (LinearLayout) findViewById(R.id.phone_view);
		passwordView = (LinearLayout) findViewById(R.id.password_view);

		title.setText(this.getResources().getString(R.string.login));
		if(mLocalSharePref.getUserName() != null || !mLocalSharePref.getUserName().equals("")){
			phoneEdt.setText(mLocalSharePref.getUserName());
		}
		loginBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userName = phoneEdt.getText().toString();
				password = pwdEdt.getText().toString();
				AppLog.v("TAG", EncryDecryUtils.str2Md5(password));
//				doLogin(userName, EncryDecryUtils.str2Md5(password));
				doLogin(userName, password);
			}
		});

		registerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivityForResult(intent, Constants.REGISTER_RESULT);
			}
		});

		forgetBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						FindPasswordActivity.class);
				intent.putExtra("mobile", phoneEdt.getText().toString());
				startActivity(intent);
			}
		});
	}

	/**
	 * 设置控件的宽高度
	 */
	public void setHwView() {
		int displayHeight = ((XiwaoApplication) getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getApplication())
				.getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		// 登录按钮
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.06f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		loginBtn.setLayoutParams(params);
		// 注册按钮
		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.03f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		registerBtn.setLayoutParams(params);
		// 手机号码输入框
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		phoneView.setLayoutParams(params);
		phoneView.setPadding((int) (displayWidth * 0.03f + 0.5f), 0, 0, 0);
		// 密码输入框
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayWidth * 0.002f + 0.5f), 0, 0);
		passwordView.setLayoutParams(params);
		passwordView.setPadding((int) (displayWidth * 0.03f + 0.5f), 0, 0, 0);
		// 忘记密码按钮
		params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.02f + 0.5f), 0, 0);
		forgetBtn.setLayoutParams(params);
	}

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

	/**
	 * 账号登录
	 * 
	 * @param user
	 * @param password
	 */
	public void doLogin(String user, String password) {
		if (user.trim().length() != 11) {
			dialogUtils.showToast(getString(R.string.phone_wrong));
			return;
		} else if(password.trim().length() < 6 || password.trim().length() > 20){
			dialogUtils.showToast(getString(R.string.pwd_wrong));
			return;
		}

		BaseCommand login = ClientSession.getInstance().getCmdFactory()
				.getLogin(user, EncryDecryUtils.str2Md5(password));

		mExecuter.execute(login, mRespHandler);

		dialogUtils.showProgress();
	}
	
	/**
	 * 登录成功之后的处理
	 */
	private void onLoginSuccess(){
		AppLog.v("TAG", "userName:" + userName);
		AppLog.v("TAG", "getUserName:" + mLocalSharePref.getUserName());
		if(!userName.equals(mLocalSharePref.getUserName())){
			mLocalSharePref.putStringPref(LocalSharePreference.USER_LAST_ADDRESS_DETAIL, "");
			mLocalSharePref.putStringPref(LocalSharePreference.USER_LAST_BRANCH_NAME, "");
			mLocalSharePref.putStringPref(LocalSharePreference.USER_LAST_CAR_NUM, "");
			mLocalSharePref.putLongPref(LocalSharePreference.USER_LAST_ADDRESS_ID, 0);
			mLocalSharePref.putLongPref(LocalSharePreference.USER_LAST_CAR_ID, 0);
			mLocalSharePref.putLongPref(LocalSharePreference.USER_LAST_DISTRACT_ID, 0);
			mLocalSharePref.putStringPref(LocalSharePreference.USER_LAST_CAR_TYPE, "");
			mLocalSharePref.setUserHead("");
		}
		mLocalSharePref.setUserName(userName);
		mLocalSharePref.setUserPassword(password);
		mLocalSharePref.setLoginState(true);	//保存登录状态
		mLocalSharePref.setNickName(nickName);
		mLocalSharePref.setUserEmail(email);
		mLocalSharePref.setUserHead(headStr);
		mLocalSharePref.setUserType(userType);
		mLocalSharePref.setUserId(userId);

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
//		finish();
		ActivityManage.getInstance().exitInError();
    }

	/**
	 * 处理服务器返回的登录结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveLoginResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			Login.Response loginRsp = (Login.Response) rsp;
			if (loginRsp.responseType.equals("N")) {
				nickName = loginRsp.customerName;
				email = loginRsp.email;
				headStr = loginRsp.headStr;
				userType = loginRsp.customerType;
				userId = loginRsp.id;
				onLoginSuccess();
			} else {
				dialogUtils.showToast(loginRsp.errorMessage);
			}
		}
	}

	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveLoginResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Constants.REGISTER_RESULT && resultCode == RESULT_OK){
			userName = data.getStringExtra("user_name");
			password = data.getStringExtra("password");
			doLogin(userName, password);
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

		dialogUtils.dismissProgress();
		mExecuter.setHandler(null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
		mExecuter.setHandler(mHandler);
	}
}
