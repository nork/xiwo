package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.GetCode;
import com.android.xiwao.washcar.httpconnection.PasswordReset;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.EncryDecryUtils;

/**
 * 找回密码
 * 
 * @author admin
 * 
 */
public class FindPasswordActivity extends Activity {

	public Context mcontext;
	public View view;
//	private String TAG = "FindPassword ";

	private View getcodeview;
	private EditText phoneedt;

	private String phonenumber;

	private View resetrview;
	private EditText codeedt;
	private EditText psw01;
	private EditText psw02;

	private TimeCount time;
	private Button getaginbtn;
	
	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	// 工具
	private DialogUtils dialogUtils;
	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;
	
	//参数
	private String code;	//验证码
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		mLocalSharePref = new LocalSharePreference(this);
		mcontext = this;

		LayoutInflater inflater = LayoutInflater.from(mcontext);
		view = inflater.inflate(R.layout.findpswview, null);

		setContentView(view); // R.layout.managemoneyview
		initExecuter();
		initUtils();
		String mobile = getIntent().getStringExtra("mobile");
		initView(mobile);
		setViewHw();
	}

	/**
	 * 设置控件宽高度
	 */
	public void setViewHw() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		LinearLayout.LayoutParams params;
		LinearLayout teleInputText = (LinearLayout) findViewById(R.id.tele_input_text);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.05f + 0.5f), 0,
				0);
		teleInputText.setLayoutParams(params); // 获取验证码部分设置

		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int) (displayHeight * 0.05f + 0.5f), 0,
				0);
		resetrview.setLayoutParams(params); // 输入注册信息部分设置

		Button getCodeBtn = (Button) findViewById(R.id.getcodebtn);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.06f + 0.5f),
				(int) (displayHeight * 0.06f + 0.5f),
				(int) (displayWidth * 0.06f + 0.5f), 0);
		getCodeBtn.setLayoutParams(params);

		LinearLayout codeInputPart = (LinearLayout) findViewById(R.id.code_input_text);

		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		psw02.setLayoutParams(params); // 确认密码部分
		psw01.setLayoutParams(params); // 设置密码部分
		codeInputPart.setLayoutParams(params); // 输入验证码部分

		Button registerbtn = (Button) view.findViewById(R.id.registerbtn); // 提交按钮
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.06f + 0.5f),
				(int) (displayHeight * 0.06f + 0.5f),
				(int) (displayWidth * 0.06f + 0.5f), 0);
		registerbtn.setLayoutParams(params);
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
	 * 初始化各组件并添加相应事件
	 * 
	 * @param mobile
	 */
	private void initView(String mobile) {

		getcodeview = findViewById(R.id.getcodeview);
		resetrview = findViewById(R.id.registerview);

		Button backbtn = (Button) view.findViewById(R.id.backbtn);

		phoneedt = (EditText) findViewById(R.id.teledt);
		phoneedt.setText(mobile);
		Button getcodebtn = (Button) view.findViewById(R.id.getcodebtn);

		codeedt = (EditText) findViewById(R.id.codeedt);
		getaginbtn = (Button) findViewById(R.id.getagin);
		psw01 = (EditText) findViewById(R.id.pswedt01);
		psw02 = (EditText) findViewById(R.id.pswedt02);
		Button submitbtn = (Button) view.findViewById(R.id.registerbtn);
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(R.string.find_pwd);

		time = new TimeCount(60000, 1000);

		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doBack();
			}
		});

		// 获取验证码按钮倒计时
		getaginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				time.start();
				getCode(phonenumber);
			}
		});

		// 获取短信验证码
		getcodebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(mcontext, "获取验证码", 1).show();

				phonenumber = phoneedt.getText().toString();
				if (phonenumber.length() != 11) {
					dialogUtils.showToast(getString(R.string.phone_wrong));
					return;
				} else {
					time.start();
				}

				getCode(phonenumber);
			}
		});

		// 提交
		submitbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String codestr = codeedt.getText().toString();
				String psw1 = psw01.getText().toString();
				String psw2 = psw02.getText().toString();

				if (codestr.length() != 6) {
					dialogUtils.showToast(getString(R.string.code_wrong));
					return;
				} else if (psw1.length() < 6 || psw1.length() > 20) {
					dialogUtils.showToast(getString(R.string.pwd_wrong));
					return;
				}

				else if (psw2.length() < 6 || psw2.length() > 20) {
					dialogUtils.showToast(getString(R.string.pwd_wrong));
					return;
				} else if (!psw1.equals(psw2)) {

					dialogUtils.showToast(getString(R.string.pwd_dif_erro));
					return;
				}
				
				 findPsw(codestr, phonenumber, EncryDecryUtils.str2Md5(psw2));
			}
		});

	}
	
	/**
	 * 获取验证码成功
	 * @param identifyCode 获取到的验证码
	 */
	private void onGetCodeSuccess(String identifyCode){
//		code = identifyCode;
		gotoResetview();
		dialogUtils.dismissProgress();
	}
	
	/**
	 * 解析获取的验证码结果
	 * @param rsp 服务器返回的结果
	 */
	private void onReceiveGetCodeResponse(BaseResponse rsp){
		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
			dialogUtils.dismissProgress();
		} else {
			GetCode.Response getCodeRsp = (GetCode.Response) rsp;
			if (getCodeRsp.responseType.equals(GetCode.Response.ISSUC_SUCC)) {				
				onGetCodeSuccess(getCodeRsp.identifyCode);
			} else {
				dialogUtils.showToast(getCodeRsp.errorMessage);
//				gotoResetview();
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mGetCodeRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveGetCodeResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * 获取验证码
	 * @param phone
	 */
	private void getCode(String phone){
		BaseCommand getCode = ClientSession.getInstance().getCmdFactory()
				.getCode(phone, "00");

		mExecuter.execute(getCode, mGetCodeRespHandler);

		dialogUtils.showProgress();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			doBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 处理返回
	 */
	private void doBack() {

		if (getcodeview.isShown()) {
			finish();
		} else if (resetrview.isShown()) {
			gotoResetrview();
		}
	}

	/**
	 * /跳转到注册界面
	 */
	private void gotoResetview() {

		getcodeview.setVisibility(View.GONE);
		resetrview.setVisibility(View.VISIBLE);
	}

	/**
	 * 跳转到获取验证码界面
	 */
	private void gotoResetrview() {
		resetrview.setVisibility(View.GONE);
		getcodeview.setVisibility(View.VISIBLE);
	}

	private void onPasswordResetSuccess(){
		mLocalSharePref.setLoginState(false);	//保存登录状态		
//    	Intent intent = new Intent(this, LoginActivity.class);
//    	startActivity(intent);
    	finish();
	}
	/**
	 * 解析重置密码结果
	 * @param rsp 服务器返回的结果
	 */
	private void onReceivePasswordResetResponse(BaseResponse rsp){
		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
			dialogUtils.dismissProgress();
		} else {
			PasswordReset.Response passwordResetRsp = (PasswordReset.Response) rsp;
			if (passwordResetRsp.responseType.equals(PasswordReset.Response.ISSUC_SUCC)) {				
				onPasswordResetSuccess();
				dialogUtils.showToast(passwordResetRsp.errorMessage);
			} else {
				dialogUtils.showToast(passwordResetRsp.errorMessage);
			}
		}
	}
	private CommandExecuter.ResponseHandler mPasswordResetRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceivePasswordResetResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * 找回密码
	 * 
	 * @param validateCode
	 * @param mobile
	 * @param password
	 */
	public void findPsw(String validateCode, final String mobile,
			String password) {
		BaseCommand passwordReset = ClientSession.getInstance().getCmdFactory()
				.getPasswordReset(validateCode, mobile, password);

		mExecuter.execute(passwordReset, mPasswordResetRespHandler);

		dialogUtils.showProgress();
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}


	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			getaginbtn.setText("重新获取验证码");
			getaginbtn.setClickable(true);
			getaginbtn.setBackgroundResource(R.drawable.orange_btn_bg_little);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			getaginbtn.setClickable(false);
			getaginbtn.setText(millisUntilFinished / 1000 + "秒后重新发送");
			getaginbtn.setBackgroundResource(R.drawable.graybtn_bg_little);
		}
	}

}
