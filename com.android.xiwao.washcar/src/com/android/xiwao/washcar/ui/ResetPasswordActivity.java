package com.android.xiwao.washcar.ui;

import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

/**
 * 重置密码
 * 
 * @author admin
 * 
 */
public class ResetPasswordActivity extends Activity {

	public Context mcontext;
	public View view;
	private String TAG = "ResetPsw ";

	private EditText oldpsw;
	private EditText psw01;
	private EditText psw02;

	// private Map<String, String> params;
	private JSONObject params;
	private String results;
	private String oldPwd;
	private String newPwd;
	private String moble;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mcontext = this;

		LayoutInflater inflater = LayoutInflater.from(mcontext);
		view = inflater.inflate(R.layout.resetpswview, null);

		setContentView(view); // R.layout.managemoneyview

		initView();
		setViewHw();
	}

	/**
	 * 设置控件宽高度
	 */
	public void setViewHw() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		LinearLayout.LayoutParams params;
		LinearLayout getcodeview = (LinearLayout) findViewById(R.id.getcodeview);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int) (displayHeight * 0.05f + 0.5f), 0,
				0);
		getcodeview.setLayoutParams(params);

		Button submitbtn = (Button) view.findViewById(R.id.okbtn); // 按钮部分
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.06f + 0.5f),
				(int) (displayHeight * 0.06f + 0.5f),
				(int) (displayWidth * 0.06f + 0.5f), 0);
		submitbtn.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		oldpsw.setLayoutParams(params); // 旧密码输入框
		psw01.setLayoutParams(params); // 新密码输入框
		psw02.setLayoutParams(params); // 密码确认输入框
	}

	/**
	 * 初始化各组件并添加相应事件
	 */
	private void initView() {

		Button backbtn = (Button) view.findViewById(R.id.backbtn);

		oldpsw = (EditText) findViewById(R.id.pswedt_old);
		psw01 = (EditText) findViewById(R.id.pswedt01);
		psw02 = (EditText) findViewById(R.id.pswedt02);
		Button submitbtn = (Button) view.findViewById(R.id.okbtn);

		TextView title = (TextView)findViewById(R.id.title);
		title.setText(R.string.modify_pwd);
		// 返回
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		// 确定按钮 修改密码
		submitbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String oldpswstr = oldpsw.getText().toString();
				String psw1 = psw01.getText().toString();
				String psw2 = psw02.getText().toString();
				oldPwd = oldpswstr;
				newPwd = psw2;

				if (oldpswstr == null || oldpswstr.length() == 0) {

					Toast.makeText(mcontext, "旧密码不能为空", Toast.LENGTH_LONG)
							.show();
					return;

				} else if (psw1 == null || psw1.length() == 0) {

					Toast.makeText(mcontext, "新密码不能为空", Toast.LENGTH_LONG)
							.show();
					return;

				} else if (psw1.length() < 6 || psw1.length() > 16
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
				}

				// String mobile =
				// Account.getSharedPreOfLogin(mcontext).getString(mobileno,
				// "");
				// moble = mobile;
				// resetPsw(mobile, oldpswstr, psw2);
				// resetPsw(mobile, Utils.MD5(oldpswstr), Utils.MD5(psw2));
			}
		});

	}

	/**
	 * 密码重置
	 * 
	 * @param mobile
	 * @param oldpwd
	 * @param newpwd
	 */
	public void resetPsw(final String mobile, String oldpwd, final String newpwd) {

		// 请求参数 json={“mobile”:””,” oldpwd”:””,” newpwd”:”” }
		// params = new HashMap<String, String>();
		// params = new JSONObject();
		// try {
		// params.put("mobile", mobile);
		// params.put("oldpwd", oldpwd);
		// params.put("newpwd", newpwd);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// new Thread(new Runnable() {
		// public void run() {
		//
		// results = HttpUtil.getContent(mcontext, UrlStrings.updatePassword,
		// params, null);
		// if(results==null){
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
		// // {“status”:0,”respDesc”:”成功” }
		// String status = jsonobj.getString("status");
		// String respDesc = jsonobj.getString("respDesc");
		//
		// if("0".equals(status)){ //成功
		//
		// Toast.makeText(mcontext, "密码修改成功", 0).show();
		// // System.setProperty(password, newpwd);
		// Account.saveLoginStatus(mcontext, "1", mobile, newpwd, "");
		// finish();
		//
		// }else if("-10".equals(status)){
		// AppLog.v(TAG, "session过期");
		// mHandler.sendEmptyMessage(UnLogin);
		// }else{
		// Toast.makeText(mcontext, "旧密码输入错误", 0).show();
		// }
		//
		//
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// });
		//
		// }
		// }).start();
	}

	/**
	 * 线程通信handler
	 */
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case ReloginSuc:
			// AppLog.v(TAG, "重登录成功");
			// resetPsw(moble, Utils.MD5(oldPwd), Utils.MD5(newPwd));
			// break;
			// case ReloginFail:
			// AppLog.v(TAG, "重登录失败");
			// Toast.makeText(mcontext, "密码修改失败，请稍后再试", 0).show();
			// break;
			// case UnLogin:
			// AppLog.v(TAG, "未登录，进行重登录");
			// ReloginHelper relogin = new ReloginHelper((Activity)mcontext,
			// mHandler);
			// relogin.relogin(true);
			// break;
			}
		}
	};

}
