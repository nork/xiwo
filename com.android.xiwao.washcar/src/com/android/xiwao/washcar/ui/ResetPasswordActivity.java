package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.android.xiwao.washcar.httpconnection.PasswordModify;
import com.android.xiwao.washcar.utils.DialogUtils;

/**
 * 重置密码
 * 
 * @author admin
 * 
 */
public class ResetPasswordActivity extends Activity {

	public Context mcontext;
	public View view;
//	private String TAG = "ResetPsw ";

	private EditText oldpsw;
	private EditText psw01;
	private EditText psw02;
	
	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;
	// 工具
	private DialogUtils dialogUtils;
		
	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mcontext = this;
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);

		mLocalSharePref = new LocalSharePreference(this);
		
		LayoutInflater inflater = LayoutInflater.from(mcontext);
		view = inflater.inflate(R.layout.resetpswview, null);

		setContentView(view); // R.layout.managemoneyview
		initExecuter();
		initUtils();
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

				if (oldpswstr == null || oldpswstr.length() == 0) {

					dialogUtils.showToast(getString(R.string.old_pwd_null_error));
					return;

				} else if (psw1 == null || psw1.length() == 0) {

					dialogUtils.showToast(getString(R.string.new_pwd_null_error));
					return;

				} else if (psw1.length() < 6 || psw1.length() > 16
						|| Pattern.matches("[0-9]+", psw1)
						|| Pattern.matches("[a-zA-Z]+", psw1)
						|| Pattern.matches("[_]+", psw1)) {
					dialogUtils.showToast(getString(R.string.pwd_format_erro));
					return;
				}

				else if (psw2 == null || psw2.length() == 0) {
					dialogUtils.showToast(getString(R.string.pwd_dif_erro));
					return;
				} else if (!psw1.equals(psw2)) {

					dialogUtils.showToast(getString(R.string.pwd_dif_erro));
					return;
				}
				 
				resetPwd(mLocalSharePref.getUserId(), oldpswstr, psw2);
			}
		});

	}
	/**
	 * 登录成功之后的处理
	 */
	private void onModifySuccess(){
		mLocalSharePref.setLoginState(false);	//保存登录状态		
    	Intent intent = new Intent(this, LoginActivity.class);
    	startActivity(intent);
    	finish();
    }
	/**
	 * 处理服务器返回的登录结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveModifyResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			PasswordModify.Response passwordModifyRsp = (PasswordModify.Response) rsp;
			if (passwordModifyRsp.responseType.equals("N")) {
				onModifySuccess();
				dialogUtils.showToast(passwordModifyRsp.errorMessage);
			} else {
				dialogUtils.showToast(passwordModifyRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveModifyResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};

	/**
	 * 密码重置
	 * 
	 * @param mobile
	 * @param oldpwd
	 * @param newpwd
	 */
	public void resetPwd(long id, String oldpwd, String newpwd) {
		BaseCommand modifyPassword = ClientSession.getInstance().getCmdFactory()
				.getPasswordModify(id, oldpwd, newpwd);

		mExecuter.execute(modifyPassword, mRespHandler);

		dialogUtils.showProgress();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}
	
}
