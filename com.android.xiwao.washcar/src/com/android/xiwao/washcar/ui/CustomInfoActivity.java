package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class CustomInfoActivity extends Activity{
	private TextView userTxt;
	private TextView emailTxt;
	private TextView phoneTxt;
	
	private Button backBtn;
	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		mLocalSharePref = new LocalSharePreference(this);

		setContentView(R.layout.customer_info);

		initContentView();
		setViewHw();
	}
	
	public void initContentView(){
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(R.string.custom_info);
		
		userTxt = (TextView)findViewById(R.id.user_name_txt);
		emailTxt = (TextView)findViewById(R.id.email_txt);
		phoneTxt = (TextView)findViewById(R.id.phone_txt);
		
		userTxt.setText(mLocalSharePref.getNickName());
		emailTxt.setText(mLocalSharePref.getUserEmail());
		phoneTxt.setText(mLocalSharePref.getUserName());
		
		backBtn = (Button)findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	public void setViewHw(){
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
//		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		//title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		
		//头像部分
		RelativeLayout head = (RelativeLayout) findViewById(R.id.head_img);
		RelativeLayout name = (RelativeLayout) findViewById(R.id.name);
		RelativeLayout user = (RelativeLayout) findViewById(R.id.user);
		RelativeLayout email = (RelativeLayout) findViewById(R.id.email);
		RelativeLayout phone = (RelativeLayout) findViewById(R.id.phone);
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.24f + 0.5f));
		listParams.setMargins(0, (int)(displayHeight * 0.04f + 0.5f), 0, 0);
		head.setLayoutParams(listParams);
		listParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		listParams.setMargins(0, (int)(displayHeight * 0.001f + 0.5f), 0, 0);
		name.setLayoutParams(listParams);
		user.setLayoutParams(listParams);
		email.setLayoutParams(listParams);
		phone.setLayoutParams(listParams);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}
	
}
