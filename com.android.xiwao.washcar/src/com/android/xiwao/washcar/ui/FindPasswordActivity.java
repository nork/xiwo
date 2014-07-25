package com.android.xiwao.washcar.ui;

import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Toast;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

/**
 * 找回密码
 * 
 * @author admin
 * 
 */
public class FindPasswordActivity extends Activity {

	public Context mcontext;
	public View view;
	private String TAG = "FindPassword ";

	private View getcodeview;
	private EditText phoneedt;

	private String phonenumber;

	private View resetrview;
	private EditText codeedt;
	private EditText psw01;
	private EditText psw02;

	// private Map<String, String> params;
	private JSONObject params;
	private String results;

	private TimeCount time;
	private Button getaginbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		mcontext = this;

		LayoutInflater inflater = LayoutInflater.from(mcontext);
		view = inflater.inflate(R.layout.findpswview, null);

		setContentView(view); // R.layout.managemoneyview

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

		// LinearLayout nickNamePart =
		// (LinearLayout)findViewById(R.id.nick_name_part); //昵称输入框
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

		time = new TimeCount(50000, 1000);

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
				if (phonenumber == null || phonenumber.length() == 0) {
					Toast.makeText(mcontext, "请输入手机号码", 0).show();
					return;
				} else if (!Pattern.matches("[0-9]+", phonenumber)) {
					Toast.makeText(mcontext, "手机号码只能输入数字,请重新输入", 0).show();
					return;
				} else if (phonenumber.length() != 11) {
					Toast.makeText(mcontext, "手机号码输入位数错误,请重新输入", 0).show();
					return;
				} else {
					time.start();
				}

//				getCode(phonenumber);
				gotoResetview();
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

				if (codestr == null || codestr.length() == 0) {

					Toast.makeText(mcontext, "验证码不能为空", 0).show();
					return;

				} else if (!Pattern.matches("[0-9]+", codestr)) {

					Toast.makeText(mcontext, "验证码为数字", 0).show();
					return;

				} else if (codestr.length() != 4) {

					Toast.makeText(mcontext, "请输入4位数字验证码", 0).show();
					return;
				} else if (psw1 == null || psw1.length() == 0) {

					Toast.makeText(mcontext, "密码不能为空", 0).show();
					return;

				} else if (psw1.length() < 6 || psw1.length() > 16
						|| Pattern.matches("[0-9]+", psw1)
						|| Pattern.matches("[a-zA-Z]+", psw1)
						|| Pattern.matches("[_]+", psw1)) {
					Toast.makeText(mcontext, "密码由6-16个字母加数字或下划线组成", 1).show();
					return;
				}

				else if (psw2 == null || psw2.length() == 0) {
					Toast.makeText(mcontext, "确认密码不能为空", 0).show();
					return;
				} else if (!psw1.equals(psw2)) {

					Toast.makeText(mcontext, "两次输入的密码不一致，请您重新输入", 0).show();
					return;
				}
				
				// findPsw(codestr, phonenumber, psw2);
				// findPsw(codestr, phonenumber, Utils.MD5(psw2));
			}
		});

	}
	
	/**
	 * 获取验证码
	 * 
	 * @param phonenumber
	 */
	public void getCode(String phonenumber) {

		// 请求参数 json={“mobile”: ””,”oprType”:””} mobile:手机号
		// oprType:操作类型(”0”:注册, ”1”:重置密码)

		// params = new HashMap<String, String>();
		// params = new JSONObject();
		// try {
		// params.put("mobile", phonenumber);
		// params.put("oprType", "1");
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// new Thread(new Runnable() {
		// public void run() {
		//
		// results = HttpUtil.getContent(mcontext, UrlStrings.getValidateCode,
		// params, null);
		// if (null == results) {
		// return;
		// }
		//
		// ((Activity) mcontext).runOnUiThread(new Runnable() {
		// public void run() {
		//
		// try {
		//
		// JSONObject jsonobj = new JSONObject(results);
		//
		// // {“status”: ”0”,”respDesc”:
		// // ”成功”,”validataCode”:”000000”}
		// String status = jsonobj.getString("status");
		// String respDesc = jsonobj.getString("respDesc");
		// // String validataCode =
		// // jsonobj.getString("validataCode");
		//
		// // Log.i(TAG,"----------validataCode----------"+
		// // validataCode); //
		// if ("0".equals(status)) {
		//
		// gotoResetview();
		//
		// } else {
		// Toast.makeText(mcontext, respDesc, 0).show();
		// }
		//
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// });
		// }
		// }).start();

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

	/**
	 * 找回密码
	 * 
	 * @param validateCode
	 * @param mobile
	 * @param password
	 */
	public void findPsw(String validateCode, final String mobile,
			String password) {

		// Log.i(TAG, "------validateCode=" + validateCode + "----mobile=" +
		// mobile + "----password=" + password);
		// // json={“validateCode”: ”” ,”password”:”” ,”mobile”:”” }
		// // params = new HashMap<String, String>();
		// params = new JSONObject();
		// try {
		// params.put("validateCode", validateCode);
		// params.put("password", password);
		// params.put("mobile", mobile);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// new Thread(new Runnable() {
		// public void run() {
		//
		// results = HttpUtil.getContent(mcontext, UrlStrings.findpsw, params,
		// null);
		// if (null == results) {
		// return;
		// }
		//
		// ((Activity) mcontext).runOnUiThread(new Runnable() {
		// @SuppressWarnings("static-access")
		// public void run() {
		//
		// try {
		//
		// JSONObject jsonobj = new JSONObject(results);
		//
		// // {“status”:0,”respDesc”:”成功”}
		// String status = jsonobj.getString("status");
		// String respDesc = jsonobj.getString("respDesc");
		//
		// if("0".equals(status)){
		// //找回密码成功，返回登陆
		// Toast.makeText(mcontext, "密码设置成功，请登录", 1).show();
		// Intent intent = new Intent();
		// intent.putExtra("type", login);
		// intent.putExtra("mobile", mobile);
		// // intent.putExtra("mobile", mobile);
		// Log.v(TAG, "-------------- <SendResult> results: " +
		// login);
		// setResult(mcontext.CONTEXT_RESTRICTED, intent);
		// finish();
		//
		// }else{
		//
		// Toast.makeText(mcontext, respDesc, 0).show();
		// }
		//
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// });
		// }
		// }).start();

	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			getaginbtn.setText("再次获取验证码");
			getaginbtn.setClickable(true);
			// getaginbtn.setBackgroundResource(R.drawable.button_green);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			getaginbtn.setClickable(false);
			getaginbtn.setText(millisUntilFinished / 1000 + "秒后可重新发送");
			// getaginbtn.setBackgroundResource(R.drawable.button_green_light);
		}
	}

}
