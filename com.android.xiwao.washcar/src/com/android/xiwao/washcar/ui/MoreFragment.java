package com.android.xiwao.washcar.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;

public class MoreFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private RelativeLayout customInfo;
	private RelativeLayout password;
	private RelativeLayout integralManage;
	private RelativeLayout help;
	private RelativeLayout tellFriend;
	private RelativeLayout about;
	private Button quitBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		view = inflater.inflate(R.layout.more, null);
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
		TextView title = (TextView)view.findViewById(R.id.title);
		title.setText(R.string.more);

		quitBtn = (Button) view.findViewById(R.id.quit);
		quitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
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
	}

	private void setHwView() {

		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		// 客户信息
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (Constants.displayHeight * 0.04f + 0.5f), 0,
				0);
		customInfo.setLayoutParams(params);
		// 修改密码
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (Constants.displayHeight * 0.001f + 0.5f),
				0, 0);
		password.setLayoutParams(params);
		// 积分管理
		integralManage.setLayoutParams(params);
		// 帮助
		help.setLayoutParams(params);
		// 告诉朋友
		tellFriend.setLayoutParams(params);
		// 关于
		about.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(
				(int) (Constants.displayWidth * 0.94f + 0.5f),
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (Constants.displayWidth * 0.03f + 0.5f),
				(int) (Constants.displayHeight * 0.08f + 0.5f),
				(int) (Constants.displayWidth * 0.03f + 0.5f), 0);
		quitBtn.setLayoutParams(params);
	}
}
