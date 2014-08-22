package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.alipay.Keys;
import com.android.xiwao.washcar.alipay.Rsa;
import com.android.xiwao.washcar.data.OrderData;
import com.android.xiwao.washcar.httpconnection.AccountConsume;
import com.android.xiwao.washcar.httpconnection.AccountQuery;
import com.android.xiwao.washcar.httpconnection.ActivityConsume;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.CustomActivityQuery;
import com.android.xiwao.washcar.utils.DialogUtils;

public class PayDialog extends Activity{
	private static final String TAG = "PayDialog";
	private Context mContext;
	private Button sureBtn;
	private Button cancelBtn;
	private RelativeLayout alpayLayout;
	private RelativeLayout accountPayLayout;
	private RelativeLayout activityPayLayout;
	private LinearLayout payListPart;
	private TextView payMoneyTxt;
	
	private int accountBalance;
	private int activityTime;
	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private int payWay;	//付款方式，1为活动付款，2为余额付款
//	private OrderData orderData;
	private String fee;
	private long orderId;
	private boolean ifPaySuc = false;
	
	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		mLocalSharePref = new LocalSharePreference(this);
		mContext = this;
//		orderData = getIntent().getParcelableExtra("order_detail");
		fee = getIntent().getStringExtra("order_fee");
		orderId = getIntent().getLongExtra("order_id", 0);
		setContentView(R.layout.pay_dialog);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		activityQuery();
	}
	
	private void initContentView(){
		payListPart = (LinearLayout) findViewById(R.id.pay_list_part);
		alpayLayout = (RelativeLayout)findViewById(R.id.alipay_layout);
		accountPayLayout = (RelativeLayout)findViewById(R.id.account_money_layout);
		activityPayLayout = (RelativeLayout)findViewById(R.id.activity_pay_layout);
		sureBtn = (Button)findViewById(R.id.sure_btn);
		cancelBtn = (Button) findViewById(R.id.cannel_btn);
		payMoneyTxt = (TextView) findViewById(R.id.pay_money);
		payMoneyTxt.setText(fee);
		sureBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch(payWay){
				case 1:
					activityConsume();
					break;
				case 2:
					accountConsume();
					break;
				}
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				if(ifPaySuc){
//					AppLog.v(TAG, "付款成功");
//					setResult(RESULT_OK);
//				}
				finish();
			}
		});
		
		activityPayLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				payListPart.setVisibility(View.GONE);
				sureBtn.setVisibility(View.VISIBLE);
				cancelBtn.setVisibility(View.VISIBLE);
				payMoneyTxt.setText("您当前剩余的活动次数:  " + activityTime);
				payWay = 1;
				if(activityTime <= 0){
					payMoneyTxt.setText("您当前剩余的活动次数:  " + activityTime + "\n无法使用免费活动付款\n请使用其他付款方式");
					sureBtn.setVisibility(View.GONE);
				}
			}
		});
		
		accountPayLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				payListPart.setVisibility(View.GONE);
				sureBtn.setVisibility(View.VISIBLE);
				cancelBtn.setVisibility(View.VISIBLE);
				payMoneyTxt.setText("您的当前余额:  " + accountBalance);
				payWay = 2;
				int feeInt = Integer.parseInt(fee.split("\\.")[0]);
				if(accountBalance < feeInt){
					payMoneyTxt.setText("您的当前余额:  " + accountBalance + "\n余额不足\n请使用其他付款方式");
					sureBtn.setVisibility(View.GONE);
				}
			}
		});
		
		alpayLayout.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Log.i("ExternalPartner", "onItemClick");
					String info = getNewOrderInfo();
					String sign = Rsa.sign(info, Keys.PRIVATE);
					sign = URLEncoder.encode(sign);
					info += "&sign=\"" + sign + "\"&" + getSignType();
					Log.i("ExternalPartner", "start pay");
					// start the pay.
					Log.i(TAG, "info = " + info);

					final String orderInfo = info;
					new Thread() {
						public void run() {
							AliPay alipay = new AliPay(PayDialog.this, mHandler);
							
							//设置为沙箱模式，不设置默认为线上环境
							//alipay.setSandBox(true);

							String result = alipay.pay(orderInfo);

							Log.i(TAG, "result = " + result);
							Message msg = new Message();
							msg.what = RQF_PAY;
							msg.obj = result;
							mHandler.sendMessage(msg);
						}
					}.start();

				} catch (Exception ex) {
					ex.printStackTrace();
					Toast.makeText(PayDialog.this, "连接服务器失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(getOutTradeNo());
		sb.append("\"&subject=\"");
		sb.append("上海洗沃公司洗车服务费");
		sb.append("\"&body=\"");
		sb.append("上海洗沃公司洗车服务费");
		sb.append("\"&total_fee=\"");
		sb.append("0.01");
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		Log.d(TAG, "outTradeNo: " + key);
		return key;
	}
	
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	/**
	 * 活动消费扣除
	 */
	private void activityConsume(){	
		BaseCommand activityConsume = ClientSession.getInstance().getCmdFactory()
				.getActivityConsume(mLocalSharePref.getUserId(),  orderId);

		mExecuter.execute(activityConsume, mActivityConsumeRespHandler);

		dialogUtils.showProgress();
	}
	public void onActivityConsumeSuccess(int accountInfo, String payMessage){
		activityTime = accountInfo;
		payMoneyTxt.setText(payMessage + "\n您当前剩余的活动次数:  " + activityTime);
		cancelBtn.setText("知道了");
		sureBtn.setVisibility(View.GONE);
		ifPaySuc = true;
	}
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceiveActivityConsumeResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			ActivityConsume.Response activityConsume = (ActivityConsume.Response) rsp;
			if (activityConsume.responseType.equals("N")) {
				onActivityConsumeSuccess((int)activityConsume.accountInfo, activityConsume.errorMessage);
//				dialogUtils.showToast(activityConsume.errorMessage);
			} else {
				dialogUtils.showToast(activityConsume.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mActivityConsumeRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveActivityConsumeResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * 余额消费扣除
	 */
	private void accountConsume(){
//		String fee = orderData.getFee();
		
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getAccountConsume(mLocalSharePref.getUserId(), Integer.parseInt(fee.split("\\.")[0]), orderId);

		mExecuter.execute(carRegister, mAccountConsumeRespHandler);

		dialogUtils.showProgress();
	}
	public void onAccountConsumeSuccess(int accountInfo, String payMessage){
		accountBalance = accountInfo;
		payMoneyTxt.setText(payMessage + "\n您的当前余额:  " + accountBalance);
		cancelBtn.setText("知道了");
		sureBtn.setVisibility(View.GONE);
		ifPaySuc = true;
	}
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceiveAccountConsumeResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			AccountConsume.Response accountConsume = (AccountConsume.Response) rsp;
			if (accountConsume.responseType.equals("N")) {
				onAccountConsumeSuccess((int)accountConsume.accountInfo, accountConsume.errorMessage);
//				dialogUtils.showToast(accountConsume.errorMessage);
			} else {
				dialogUtils.showToast(accountConsume.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mAccountConsumeRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveAccountConsumeResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	
	private void activityQuery(){
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getActivityQuery(mLocalSharePref.getUserId());

		mExecuter.execute(carRegister, mActivityQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onActivityQuerySuccess(int time){
		activityTime = time;
		getBalance();
	}
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceiveActivityQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			CustomActivityQuery.Response customActivityQuery = (CustomActivityQuery.Response) rsp;
			if (customActivityQuery.responseType.equals("N")) {
				onActivityQuerySuccess(customActivityQuery.time);
//				dialogUtils.showToast(customActivityQuery.errorMessage);
			} else {
				dialogUtils.showToast(customActivityQuery.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mActivityQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveActivityQueryResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	private void getBalance(){
		BaseCommand accountQuery = ClientSession.getInstance().getCmdFactory()
				.getAccountQuery(mLocalSharePref.getUserId());

		mExecuter.execute(accountQuery, mAccountQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAccountQuerySuccess(long accountInfo){
		accountBalance = (int) accountInfo;
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
	private void setHwView(){
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (displayWidth * 0.8f + 0.5f),
                (int) (displayHeight * 0.06f + 0.5f));
		alpayLayout.setLayoutParams(params);
		accountPayLayout.setLayoutParams(params);
		activityPayLayout.setLayoutParams(params);
		
		LinearLayout.LayoutParams paramsPay = new LinearLayout.LayoutParams((int) (displayWidth * 0.8f + 0.5f),
				LayoutParams.WRAP_CONTENT);
		payMoneyTxt.setLayoutParams(paramsPay);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(ifPaySuc){
			AppLog.v(TAG, "付款成功");
			setResult(RESULT_OK);
//			((XiwaoApplication)getApplication()).setIfNeedRefreshOrder(true);
		}
		super.finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}	
}
