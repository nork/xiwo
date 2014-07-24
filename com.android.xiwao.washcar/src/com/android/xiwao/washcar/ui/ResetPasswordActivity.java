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
 * ��������
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
				oldPwd = oldpswstr;
				newPwd = psw2;

				if (oldpswstr == null || oldpswstr.length() == 0) {

					Toast.makeText(mcontext, "�����벻��Ϊ��", Toast.LENGTH_LONG)
							.show();
					return;

				} else if (psw1 == null || psw1.length() == 0) {

					Toast.makeText(mcontext, "�����벻��Ϊ��", Toast.LENGTH_LONG)
							.show();
					return;

				} else if (psw1.length() < 6 || psw1.length() > 16
						|| Pattern.matches("[0-9]+", psw1)
						|| Pattern.matches("[a-zA-Z]+", psw1)
						|| Pattern.matches("[_]+", psw1)) {
					Toast.makeText(mcontext, "������6-16����ĸ�����ֻ��»������",
							Toast.LENGTH_LONG).show();
					return;
				}

				else if (psw2 == null || psw2.length() == 0) {
					Toast.makeText(mcontext, "ȷ�����벻��Ϊ��", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (!psw1.equals(psw2)) {

					Toast.makeText(mcontext, "������������벻һ�£�������������",
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
	 * ��������
	 * 
	 * @param mobile
	 * @param oldpwd
	 * @param newpwd
	 */
	public void resetPsw(final String mobile, String oldpwd, final String newpwd) {

		// ������� json={��mobile��:����,�� oldpwd��:����,�� newpwd��:���� }
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
		// // {��status��:0,��respDesc��:���ɹ��� }
		// String status = jsonobj.getString("status");
		// String respDesc = jsonobj.getString("respDesc");
		//
		// if("0".equals(status)){ //�ɹ�
		//
		// Toast.makeText(mcontext, "�����޸ĳɹ�", 0).show();
		// // System.setProperty(password, newpwd);
		// Account.saveLoginStatus(mcontext, "1", mobile, newpwd, "");
		// finish();
		//
		// }else if("-10".equals(status)){
		// AppLog.v(TAG, "session����");
		// mHandler.sendEmptyMessage(UnLogin);
		// }else{
		// Toast.makeText(mcontext, "�������������", 0).show();
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
	 * �߳�ͨ��handler
	 */
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case ReloginSuc:
			// AppLog.v(TAG, "�ص�¼�ɹ�");
			// resetPsw(moble, Utils.MD5(oldPwd), Utils.MD5(newPwd));
			// break;
			// case ReloginFail:
			// AppLog.v(TAG, "�ص�¼ʧ��");
			// Toast.makeText(mcontext, "�����޸�ʧ�ܣ����Ժ�����", 0).show();
			// break;
			// case UnLogin:
			// AppLog.v(TAG, "δ��¼�������ص�¼");
			// ReloginHelper relogin = new ReloginHelper((Activity)mcontext,
			// mHandler);
			// relogin.relogin(true);
			// break;
			}
		}
	};

}
