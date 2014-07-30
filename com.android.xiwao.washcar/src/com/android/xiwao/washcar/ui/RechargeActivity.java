package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class RechargeActivity extends Activity {

	private RelativeLayout rechargeTitle;
	private LinearLayout moneyPart;
	private LinearLayout buttonGroup;
	
	private TextView curMoney;
	private Button btn200;
	private Button btn400;
	private Button btn600;
	private Button btn800;
	private Button btn1000;
	private Button btn1200;
	
	private Button rechargeBtn;
	private Button cancelBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);

		setContentView(R.layout.recharge);
		initContentView();
		setHwView();
	}

	private void initContentView() {		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.recharge);
		
		rechargeTitle = (RelativeLayout) findViewById(R.id.recharge_title);
		moneyPart = (LinearLayout) findViewById(R.id.money_part);
		buttonGroup = (LinearLayout) findViewById(R.id.button_group);
		
		curMoney = (TextView) findViewById(R.id.cur_blance);
		btn200 = (Button) findViewById(R.id.btn200);
		btn400 = (Button) findViewById(R.id.btn400);
		btn600 = (Button) findViewById(R.id.btn600);
		btn800 = (Button) findViewById(R.id.btn800);
		btn1000 = (Button) findViewById(R.id.btn1000);
		btn1200 = (Button) findViewById(R.id.btn1200);
		
		rechargeBtn = (Button) findViewById(R.id.pay_now);
		cancelBtn = (Button) findViewById(R.id.cannel_order);
	}

	private void setHwView() {
		int displayHeight = ((XiwaoApplication) getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getApplication())
				.getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		
		//充值title部分
		LinearLayout.LayoutParams rechargeTitleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.12f + 0.5f));
		rechargeTitle.setLayoutParams(rechargeTitleParams);
		
		//金额按钮部分
		LinearLayout.LayoutParams moneyBtnParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		moneyPart.setLayoutParams(moneyBtnParams);
		
		//付款按钮部分
		LinearLayout.LayoutParams payBtnParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.12f + 0.5f));
		payBtnParams.setMargins(0, (int)(displayHeight * 0.002f + 0.5f), 0, 0);
		buttonGroup.setLayoutParams(payBtnParams);
		
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams((int)(displayWidth * 0.4f + 0.5f), 
				(int)(displayHeight * 0.08f + 0.5f)); 
		btnParams.setMargins((int)(displayWidth * 0.05f + 0.5f), 0, 0, 0);
		cancelBtn.setLayoutParams(btnParams);
		btnParams = new LinearLayout.LayoutParams((int)(displayWidth * 0.4f + 0.5f), 
				(int)(displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int)(displayWidth * 0.1f + 0.5f), 0, 0, 0);
		rechargeBtn.setLayoutParams(btnParams);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
