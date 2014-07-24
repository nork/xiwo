package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class CustomInfoActivity extends Activity{
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;

		setContentView(R.layout.customer_info);

		initContentView();
		setViewHw();
	}
	
	public void initContentView(){
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(R.string.custom_info);
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
		listParams.setMargins(0, (int)(displayHeight * 0.02f + 0.5f), 0, 0);
		head.setLayoutParams(listParams);
		listParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		listParams.setMargins(0, (int)(displayHeight * 0.001f + 0.5f), 0, 0);
		name.setLayoutParams(listParams);
		user.setLayoutParams(listParams);
		email.setLayoutParams(listParams);
		phone.setLayoutParams(listParams);
	}
}
