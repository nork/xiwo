package com.android.xiwao.washcar.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;

public class RegisterActivity extends Activity {
	public static Context mcontext;
	private String TAG = "Register ";

	private View getcodeview;
	private EditText phoneedt;
	private Button getcodebtn;
	private ImageView agreebtn;

	private String phonenumber;

	private TextView registtitle;
	private View agreementview;
	private View registerview;
	private EditText codeedt;
	private EditText psw01;
	private EditText psw02;
	private EditText nickName;

	// private Map<String, String> params;
	private JSONObject params;
	private String results;

	private TimeCount time;
	private Button getaginbtn;

	public static final int FINISHREGIST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mcontext = this;
		setContentView(R.layout.registerview);
		initContentView();
		setHwView();
	}

	@SuppressLint("NewApi")
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

		time = new TimeCount(50000, 1000);
		
		TextView title = (TextView) findViewById(R.id.title);		
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
		agreebtn.setSelected(true); // 默认选中
		getcodebtn.setEnabled(true);
		getcodebtn.setBackgroundResource(R.drawable.orange_btn);

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
				phonenumber = phoneedt.getText().toString();
				if (phonenumber == null || phonenumber.length() == 0) {
					Toast.makeText(mcontext,
							getResources().getString(R.string.please_phone),
							Toast.LENGTH_LONG).show();
					return;
				} else if (!Pattern.matches("[0-9]+", phonenumber)) {
					Toast.makeText(mcontext, "手机号码只能输入数字,请重新输入",
							Toast.LENGTH_LONG).show();
					return;
				} else if (phonenumber.length() != 11) {
					Toast.makeText(mcontext,
							getResources().getString(R.string.telephone_wrong),
							Toast.LENGTH_LONG).show();
					return;
				} else {
					time.start();
				}
				gotoRegisterview();
			}
		});
		// 获取验证码按钮倒计时
		getaginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				time.start();
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

				Pattern p = Pattern
						.compile("[\\`\\+\\~\\!\\#\\$\\%\\^\\&\\*\\(\\)\\|\\}\\{\\=\\'\\！\\￥\\……\\（\\）\\-\\+\\~\\!\\#\\$\\%\\&\\(\\)\\|\\}\\{\\=\\'\\！\\?\\:\\<\\>\\•\\“\\”\\；\\‘\\‘\\〈\\〉\\...\\（\\）\\——\\｛\\｝\\【\\】\\/\\;\\：\\？\\《\\》\\。\\，\\、\\[\\]\\,\\@\\#]+");
				Matcher m = p.matcher(nickNameStr);

				if (codestr == null || codestr.length() == 0) {

					Toast.makeText(mcontext, "验证码不能为空", Toast.LENGTH_LONG)
							.show();
					return;

				} else if (codestr.length() != 4) {

					Toast.makeText(mcontext, "验证码为4位数，请重新输入", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (psw1 == null || psw1.length() == 0) {

					Toast.makeText(mcontext, "密码不能为空", Toast.LENGTH_LONG)
							.show();
					return;

				}

				else if (psw1.length() < 6 || psw1.length() > 16
						|| Pattern.matches("[0-9]+", psw1)
						|| Pattern.matches("[a-zA-Z]+", psw1)
						|| Pattern.matches("[_]+", psw1)) {
					Toast.makeText(mcontext, "密码由6-16个字母加数字或下划线组成",
							Toast.LENGTH_LONG).show();
					return;
				}

				else if (psw2 == null || psw2.length() == 0) {
					Toast.makeText(mcontext, "确认密码不能为空", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (!psw1.equals(psw2)) {

					Toast.makeText(mcontext, "两次输入的密码不一致，请您重新输入",
							Toast.LENGTH_LONG).show();
					return;
				} else if (nickNameStr.length() < 2) {
					Toast.makeText(mcontext, "昵称长度不能小于2", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (m.find()) {
					Toast.makeText(mcontext,
							"昵称应为长度2-16个字符之间的中文、英文字母、数字、下划线组成",
							Toast.LENGTH_LONG).show();
					return;

				}

				// register(codestr, phonenumber, psw2, nickNameStr); // 对密码进行加密
			}
		});

	}

	@SuppressWarnings("deprecation")
	private void setHwView() {

		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		LinearLayout.LayoutParams params;
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int) (Constants.displayHeight * 0.05f + 0.5f), 0,
				0);
		getcodeview.setLayoutParams(params); // 获取验证码部分设置
		registerview.setLayoutParams(params); // 输入注册信息部分设置

		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (Constants.displayWidth * 0.06f + 0.5f),
				(int) (Constants.displayHeight * 0.06f + 0.5f),
				(int) (Constants.displayWidth * 0.06f + 0.5f), 0);
		getcodebtn.setLayoutParams(params);

		LinearLayout nickNamePart = (LinearLayout) findViewById(R.id.nick_name_part); // 昵称输入框
		LinearLayout codeInputPart = (LinearLayout) findViewById(R.id.code_input_part);

		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (Constants.displayHeight * 0.05f + 0.5f), 0,
				0);
		nickNamePart.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		psw02.setLayoutParams(params); // 确认密码部分
		psw01.setLayoutParams(params); // 设置密码部分
		codeInputPart.setLayoutParams(params); // 输入验证码部分

		Button registerbtn = (Button) findViewById(R.id.registerbtn); // 提交按钮
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (Constants.displayWidth * 0.06f + 0.5f),
				(int) (Constants.displayHeight * 0.06f + 0.5f),
				(int) (Constants.displayWidth * 0.06f + 0.5f), 0);
		registerbtn.setLayoutParams(params);

		LinearLayout inputPart = (LinearLayout) findViewById(R.id.input_part); // 手机号码输入框部分
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
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
	 * /跳转到注册界面
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
