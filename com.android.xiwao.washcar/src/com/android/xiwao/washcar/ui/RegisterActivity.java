package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import com.android.xiwao.washcar.httpconnection.GetCode;
import com.android.xiwao.washcar.httpconnection.Register;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.EncryDecryUtils;

public class RegisterActivity extends Activity {
	public static Context mcontext;
	private String TAG = "Register ";

	//控件
	private View getcodeview;
	private EditText phoneedt;
	private Button getcodebtn;
	private ImageView agreebtn;
	private TextView registtitle;
	private View agreementview;
	private View registerview;
	private EditText codeedt;
	private EditText psw01;
	private EditText psw02;
	private EditText nickName;

	private TimeCount time;
	private Button getaginbtn;
	
	// 工具
	private DialogUtils dialogUtils;
	
	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	public static final int FINISHREGIST = 0;
	
	//参数
	private String code;	//验证码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		mLocalSharePref = new LocalSharePreference(this);
		mcontext = this;
		setContentView(R.layout.registerview);
		initContentView();
		initExecuter();
		initUtils();
		setHwView();
//		Typeface type = Typeface.createFromAsset(getContext().getAssets(), "kaiu.ttf");
	}

	@SuppressLint("CutPasteId")
	private void initContentView() {
		registtitle = (TextView) findViewById(R.id.title);
		getcodeview = findViewById(R.id.getcodeview);
		agreementview = findViewById(R.id.agreementview);
		registerview = findViewById(R.id.registerview);
		nickName = (EditText) findViewById(R.id.nick_name);

		Button backbtn = (Button) findViewById(R.id.backbtn);

		phoneedt = (EditText) findViewById(R.id.teledt);
		getcodebtn = (Button) findViewById(R.id.getcodebtn);

		codeedt = (EditText) findViewById(R.id.codeedt);
		getaginbtn = (Button) findViewById(R.id.getagin);

		psw01 = (EditText) findViewById(R.id.pswedt01);
		psw02 = (EditText) findViewById(R.id.pswedt02);
		Button registerbtn = (Button) findViewById(R.id.registerbtn);

		time = new TimeCount(60000, 1000);
		
		TextView title = (TextView)findViewById(R.id.title);		
		title.setText(R.string.register);

		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doBack();
			}
		});

		// 同意相关协议
		agreebtn = (ImageView) findViewById(R.id.agreebtn);
		agreebtn.setSelected(false); // 默认不选中
		getcodebtn.setEnabled(false);
		getcodebtn.setBackgroundResource(R.drawable.graybtn_bg);

		TextView txt = (TextView) findViewById(R.id.agreetxt);
		agreebtn.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (agreebtn.isSelected()) {
					agreebtn.setSelected(false);
					getcodebtn.setEnabled(false);
					getcodebtn.setBackgroundResource(R.drawable.graybtn_bg);
				} else {
					agreebtn.setSelected(true);
					getcodebtn.setEnabled(true);
					getcodebtn.setBackgroundResource(R.drawable.orange_btn_bg);
				}
			}
		});

		txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoAgreementView();
			}
		});

		// 获取短信验证码
		getcodebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(mcontext, "获取验证码", 1).show();
				String phonenumber = phoneedt.getText().toString();
				if (phonenumber.length() != 11) {
					dialogUtils.showToast("请正确输入手机号码！");
					return;
				} else {
					time.start();
				}
				getCode(phonenumber);
			}
		});
		// 获取验证码按钮倒计时
		getaginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				time.start();
				getCode(phoneedt.getText().toString());
			}
		});

		// 注册
		registerbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String codestr = codeedt.getText().toString();
				String psw1 = psw01.getText().toString();
				String psw2 = psw02.getText().toString();
				String nickNameStr = nickName.getText().toString();
				String phonenumber = phoneedt.getText().toString();
				Pattern p = Pattern
						.compile("[\\`\\+\\~\\!\\#\\$\\%\\^\\&\\*\\(\\)\\|\\}\\{\\=\\'\\！\\￥\\……\\（\\）\\-\\+\\~\\!\\#\\$\\%\\&\\(\\)\\|\\}\\{\\=\\'\\！\\?\\:\\<\\>\\•\\“\\”\\；\\‘\\‘\\〈\\〉\\...\\（\\）\\——\\｛\\｝\\【\\】\\/\\;\\：\\？\\《\\》\\。\\，\\、\\[\\]\\,\\@\\#]+");
				Matcher m = p.matcher(nickNameStr);

				if (codestr.length() != 6) {

					dialogUtils.showToast(getString(R.string.code_wrong));
					return;
				} 
				else if (psw1.length() < 6 || psw1.length() > 20) {
					dialogUtils.showToast(getString(R.string.pwd_wrong));
					return;
				}
				else if(psw2.length() < 6 || psw2.length() > 20){
					dialogUtils.showToast(getString(R.string.pwd_wrong));
					return;
				}
				else if (psw2 == null || psw2.length() == 0) {
					dialogUtils.showToast(getString(R.string.pwd_dif_erro));
					return;
				} else if (!psw1.equals(psw2)) {
					dialogUtils.showToast(getString(R.string.pwd_dif_erro));
					return;
				} 

				//此处验证码暂时使用服务器返回的值
				doRegister(codestr, phonenumber, EncryDecryUtils.str2Md5(psw2), nickNameStr); // 对密码进行加密
			}
		});

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
	
	private void setHwView() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		LinearLayout.LayoutParams params;
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int) (displayHeight * 0.05f + 0.5f), 0,
				0);
		getcodeview.setLayoutParams(params); // 获取验证码部分设置
		registerview.setLayoutParams(params); // 输入注册信息部分设置

		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.06f + 0.5f),
				(int) (displayHeight * 0.06f + 0.5f),
				(int) (displayWidth * 0.06f + 0.5f), 0);
		getcodebtn.setLayoutParams(params);

		LinearLayout nickNamePart = (LinearLayout) findViewById(R.id.nick_name_part); // 昵称输入框
		LinearLayout codeInputPart = (LinearLayout) findViewById(R.id.code_input_part);

		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.05f + 0.5f), 0,
				0);
		nickNamePart.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		psw02.setLayoutParams(params); // 确认密码部分
		psw01.setLayoutParams(params); // 设置密码部分
		codeInputPart.setLayoutParams(params); // 输入验证码部分

		Button registerbtn = (Button) findViewById(R.id.registerbtn); // 提交按钮
		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.06f + 0.5f),
				(int) (displayWidth * 0.05f + 0.3f), 0);
		registerbtn.setLayoutParams(params);

		LinearLayout inputPart = (LinearLayout) findViewById(R.id.input_part); // 手机号码输入框部分
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		inputPart.setLayoutParams(params);
	}

	/**
	 * 处理返回
	 */
	private void doBack() {

		if (getcodeview.isShown()) {
			finish();
		} else if (registerview.isShown()) {
			gotoGetcodeview();
		} else if (agreementview.isShown()) {
			gotoGetcodeview();
		}
	}

	/**
	 * 跳转到注册界面
	 */
	private void gotoRegisterview() {

		getcodeview.setVisibility(View.GONE);
		registerview.setVisibility(View.VISIBLE);
		agreementview.setVisibility(View.GONE);
		registtitle.setText(R.string.register);
	}

	/**
	 * 跳转到获取验证码界面
	 */
	private void gotoGetcodeview() {
		registerview.setVisibility(View.GONE);
		getcodeview.setVisibility(View.VISIBLE);
		agreementview.setVisibility(View.GONE);
		registtitle.setText(R.string.register);
	}

	/**
	 * 跳转到用户协议界面
	 */
	private void gotoAgreementView() {
		registerview.setVisibility(View.GONE);
		getcodeview.setVisibility(View.GONE);
		agreementview.setVisibility(View.VISIBLE);
		registtitle.setText(R.string.agreement);
		// registtitle.setTextColor(getResources().getColor(R.color.black));
	}
	
	/**
	 * 获取验证码
	 * @param phone
	 */
	private void getCode(String phone){
		BaseCommand getCode = ClientSession.getInstance().getCmdFactory()
				.getCode(phone, "01");

		mExecuter.execute(getCode, mGetCodeRespHandler);

		dialogUtils.showProgress();
	}
	
	/**
	 * 获取验证码成功
	 * @param identifyCode 获取到的验证码
	 */
	private void onGetCodeSuccess(String identifyCode){
		code = identifyCode;
		gotoRegisterview();
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
//				gotoRegisterview();
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
	 * 注册操作
	 * @param codestr
	 * @param phonenumber
	 * @param password
	 * @param nickNameStr
	 */
	private void doRegister(String codestr, String phonenumber, String password, String nickNameStr){
		BaseCommand register = ClientSession.getInstance().getCmdFactory()
				.getRegister(codestr, phonenumber, password, nickNameStr);

		mExecuter.execute(register, mRespHandler);

		dialogUtils.showProgress();
	}
	
	/**
	 * 处理服务器返回的注册结果
	 * @param rsp 服务返回的注册信息
	 */
	private void onReceiveRegisterResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			Register.Response registerRsp = (Register.Response) rsp;
			if (registerRsp.responseType.equals("N")) {
				ClientSession session = ClientSession.getInstance();
				session.setSessionCookies(rsp.cookies);
				session.setUserId(registerRsp.id);
				onRegisterSuccess(registerRsp.id);
				dialogUtils.showToast(registerRsp.errorMessage);
			} else {
				dialogUtils.showToast(registerRsp.errorMessage);
			}
		}
	}
	
	/**
	 * 注册成功之后的处理
	 */
	private void onRegisterSuccess(long userId){		
//    	Intent intent = new Intent(this, MainActivity.class);
//    	mLocalSharePref.setUserName(phoneedt.getText().toString());
//    	mLocalSharePref.setUserPassword(psw01.getText().toString());
//    	mLocalSharePref.setUserId(userId);
//    	mLocalSharePref.setLoginState(true);
//    	startActivity(intent);
//    	ActivityManage.getInstance().exitInError();
		Intent intent = new Intent();
		intent.putExtra("user_name", phoneedt.getText().toString());
		intent.putExtra("password", psw02.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
    }
	
	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveRegisterResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	
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
