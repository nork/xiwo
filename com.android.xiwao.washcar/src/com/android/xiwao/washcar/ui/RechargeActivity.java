package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.httpconnection.AccountCharge;
import com.android.xiwao.washcar.httpconnection.AccountQuery;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.utils.DialogUtils;

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
	private Button backBtn;
	
	private Button rechargeBtn;
	private Button cancelBtn;

	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);

		mLocalSharePref = new LocalSharePreference(this);
		
		setContentView(R.layout.recharge);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		getBalance();
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
		backBtn = (Button) findViewById(R.id.backbtn);
		
		rechargeBtn = (Button) findViewById(R.id.pay_now);
		cancelBtn = (Button) findViewById(R.id.cannel_order);
		
		rechargeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				accountRecharge(200);
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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

	private void getBalance(){
		BaseCommand accountQuery = ClientSession.getInstance().getCmdFactory()
				.getAccountQuery(mLocalSharePref.getUserId());

		mExecuter.execute(accountQuery, mAccountQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAccountQuerySuccess(long accountInfo){
		curMoney.setText(Long.toString(accountInfo));
	}
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceiveAccountQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			AccountQuery.Response accountQueryRsp = (AccountQuery.Response) rsp;
			if (accountQueryRsp.responseType.equals("N")) {
				onAccountQuerySuccess(accountQueryRsp.accountInfo);
//				dialogUtils.showToast(accountQueryRsp.errorMessage);
			} else {
				dialogUtils.showToast(accountQueryRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mAccountQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveAccountQueryResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	private void accountRecharge(int money){
		BaseCommand accountRecharge = ClientSession.getInstance().getCmdFactory()
				.getAccountRecharge(mLocalSharePref.getUserId(), money);

		mExecuter.execute(accountRecharge, mAccountRechargeRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAccountRechargeSuccess(long accountInfo){
		curMoney.setText(Long.toString(accountInfo));
	}
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceiveAccountRechargeResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			AccountCharge.Response accountChargeRsp = (AccountCharge.Response) rsp;
			if (accountChargeRsp.responseType.equals("N")) {
				onAccountRechargeSuccess(accountChargeRsp.accountInfo);
				dialogUtils.showToast(accountChargeRsp.errorMessage);
			} else {
				dialogUtils.showToast(accountChargeRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mAccountRechargeRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveAccountRechargeResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
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
		ActivityManage.getInstance().setCurContext(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
