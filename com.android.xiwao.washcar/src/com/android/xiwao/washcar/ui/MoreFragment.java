package com.android.xiwao.washcar.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.utils.DialogUtils;

public class MoreFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private RelativeLayout customInfo;
	private RelativeLayout password;
	private RelativeLayout integralManage;
	private RelativeLayout help;
	private RelativeLayout tellFriend;
	private RelativeLayout about;
	private RelativeLayout carInfo;
	private RelativeLayout allMonthInfo;
	private RelativeLayout contactInfo;
	private Button quitBtn;

	// ����
	private DialogUtils dialogUtils;
	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		view = inflater.inflate(R.layout.more, null);
		mLocalSharePref = new LocalSharePreference(this.getActivity());
		initUtils();
		initContentView();
		setHwView();
		return view;
	}

	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		customInfo = (RelativeLayout) view.findViewById(R.id.custom_info);
		password = (RelativeLayout) view.findViewById(R.id.password);
		integralManage = (RelativeLayout) view
				.findViewById(R.id.integral_manage);
		help = (RelativeLayout) view.findViewById(R.id.help);
		tellFriend = (RelativeLayout) view.findViewById(R.id.tell_friend);
		about = (RelativeLayout) view.findViewById(R.id.about);
		carInfo = (RelativeLayout) view.findViewById(R.id.car_info);
		allMonthInfo = (RelativeLayout) view.findViewById(R.id.allmonth_info);
		contactInfo = (RelativeLayout) view.findViewById(R.id.contact_info);
		TextView title = (TextView)view.findViewById(R.id.title);
		title.setText(R.string.more);

		quitBtn = (Button) view.findViewById(R.id.quit);
		
		if(!mLocalSharePref.getLoginState()){
			customInfo.setVisibility(View.GONE);
			password.setVisibility(View.GONE);
			integralManage.setVisibility(View.GONE);
			carInfo.setVisibility(View.GONE);
			allMonthInfo.setVisibility(View.GONE);
			quitBtn.setText("��¼");
		}
		
		if(mLocalSharePref.getUserType().equals("01")){
			allMonthInfo.setVisibility(View.VISIBLE);
		}
		quitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(quitBtn.getText().equals("��¼")){
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
					return;
				}
				showExitDialog();
			}
		});

		customInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CustomInfoActivity.class);
				startActivity(intent);
			}
		});

		password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						ResetPasswordActivity.class);
				startActivity(intent);
			}
		});
		
		integralManage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						RechargeActivity.class);
				startActivity(intent);
			}
		});
		
		carInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						CarInfoActivity.class);
				startActivity(intent);
			}
		});
		
		contactInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCallDialog();
			}
		});
		
		allMonthInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						MonthlyActivity.class);
				startActivity(intent);
			}
		});
	}

	public void showCallDialog(){
		final String phone = "4008-591-577";
		new AlertDialog.Builder(mContext)
		.setTitle(mContext.getResources().getString(R.string.remind))
		.setMessage(phone)
		.setPositiveButton("ȡ��", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1)
			{
				dialog.dismiss();
			}
		})
		.setNegativeButton("����", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try{
					Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
					mContext.startActivity(phoneIntent);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).show();
	}

	public void showExitDialog(){
		mLocalSharePref.setLoginState(false);
		new AlertDialog.Builder(mContext)
		.setTitle(mContext.getString(R.string.remind))
		.setMessage(mContext.getString(R.string.sure_exit))
		.setPositiveButton(mContext.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent = new Intent(mContext, MainActivity.class);
				mContext.startActivity(intent);
				getActivity().finish();
			}
		})
		.setNegativeButton(mContext.getString(R.string.no),
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
	
	/**
	 * ��ʼ����Ҫ�Ĺ���
	 */
	public void initUtils() {
		dialogUtils = new DialogUtils();
	}
	
	private void setHwView() {
		int displayHeight = ((XiwaoApplication)getActivity().getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getActivity().getApplication()).getDisplayWidth();
		// title�߶�
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		// �ͻ���Ϣ
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0,
				0);
		allMonthInfo.setLayoutParams(params);
		if(mLocalSharePref.getUserType().equals("00")){
			customInfo.setLayoutParams(params);
		}
		// �޸�����
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f),
				0, 0);
		if(mLocalSharePref.getUserType().equals("01")){
			customInfo.setLayoutParams(params);
		}
		password.setLayoutParams(params);
		// ���ֹ���
		integralManage.setLayoutParams(params);
		// ����
		help.setLayoutParams(params);
		// ��������
		tellFriend.setLayoutParams(params);
		// ����
		about.setLayoutParams(params);
		carInfo.setLayoutParams(params);
		//��ϵ����
		contactInfo.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		quitBtn.setLayoutParams(params);
	}
}
