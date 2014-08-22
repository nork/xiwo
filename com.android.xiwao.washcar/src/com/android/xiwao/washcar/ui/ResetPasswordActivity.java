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
 * ��������
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
	
	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;
	// ����
	private DialogUtils dialogUtils;
		
	// ���������ض���
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
	 * ���ÿؼ���߶�
	 */
	public void setViewHw() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		// title�߶�
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

		Button submitbtn = (Button) view.findViewById(R.id.okbtn); // ��ť����
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.06f + 0.5f),
				(int) (displayHeight * 0.06f + 0.5f),
				(int) (displayWidth * 0.06f + 0.5f), 0);
		submitbtn.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		oldpsw.setLayoutParams(params); // �����������
		psw01.setLayoutParams(params); // �����������
		psw02.setLayoutParams(params); // ����ȷ�������
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
	
	/**
	 * ��ʼ��������������Ӧ�¼�
	 */
	private void initView() {

		Button backbtn = (Button) view.findViewById(R.id.backbtn);

		oldpsw = (EditText) findViewById(R.id.pswedt_old);
		psw01 = (EditText) findViewById(R.id.pswedt01);
		psw02 = (EditText) findViewById(R.id.pswedt02);
		Button submitbtn = (Button) view.findViewById(R.id.okbtn);

		TextView title = (TextView)findViewById(R.id.title);
		title.setText(R.string.modify_pwd);
		// ����
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		// ȷ����ť �޸�����
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
	 * ��¼�ɹ�֮��Ĵ���
	 */
	private void onModifySuccess(){
		mLocalSharePref.setLoginState(false);	//�����¼״̬		
    	Intent intent = new Intent(this, LoginActivity.class);
    	startActivity(intent);
    	finish();
    }
	/**
	 * ������������صĵ�¼���
	 * @param rsp ���񷵻صĵ�¼��Ϣ
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
	 * ��������
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
