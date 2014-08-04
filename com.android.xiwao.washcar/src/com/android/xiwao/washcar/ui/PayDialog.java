package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class PayDialog extends Activity{
	private Context mContext;
	private Button sureBtn;
	private RelativeLayout alpayLayout;
	private RelativeLayout accountPayLayout;
	private RelativeLayout activityPayLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		mContext = this;

		setContentView(R.layout.pay_dialog);
		initContentView();
		setHwView();
	}
	
	private void initContentView(){
		alpayLayout = (RelativeLayout)findViewById(R.id.alipay_layout);
		accountPayLayout = (RelativeLayout)findViewById(R.id.account_money_layout);
		activityPayLayout = (RelativeLayout)findViewById(R.id.activity_pay_layout);
		sureBtn = (Button)findViewById(R.id.sure_btn);
		sureBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void setHwView(){
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (displayWidth * 0.8f + 0.5f),
                (int) (displayHeight * 0.06f + 0.5f));
		alpayLayout.setLayoutParams(params);
		accountPayLayout.setLayoutParams(params);
		activityPayLayout.setLayoutParams(params);
	}
}
