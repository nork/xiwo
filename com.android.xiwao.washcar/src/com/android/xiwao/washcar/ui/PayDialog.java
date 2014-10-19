package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.android.xiwao.washcar.alipay.Result;
import com.android.xiwao.washcar.alipay.Rsa;
import com.android.xiwao.washcar.httpconnection.AccountConsume;
import com.android.xiwao.washcar.httpconnection.AccountQuery;
import com.android.xiwao.washcar.httpconnection.ActivityConsume;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.CustomActivityQuery;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.StringUtils;

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
	// ����
	private DialogUtils dialogUtils;

	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	// ���������ض���
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private int payWay;	//���ʽ��1Ϊ����2Ϊ����
//	private OrderData orderData;
	private String fee; //����ԭ��
	private String accountFee;//���֧���۸�
	private long orderId;
	private String serverType;//��������
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
		accountFee = getIntent().getStringExtra("order_account_fee");
		orderId = getIntent().getLongExtra("order_id", 0);
		serverType = getIntent().getStringExtra("server_type");
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
		
		TextView alipayAmt = (TextView) findViewById(R.id.alipay_amt);
		TextView accountPayAmt = (TextView) findViewById(R.id.account_pay_amt);
		alipayAmt.setText(fee);
		accountPayAmt.setText(accountFee);
		
		if(serverType.contains("��ֵ")){
			accountPayLayout.setVisibility(View.GONE);
			activityPayLayout.setVisibility(View.GONE);
		}
		if(serverType.contains("����")){
			activityPayLayout.setVisibility(View.GONE);
		}
		sureBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(sureBtn.getText().toString().contains("��ֵ")){
					Intent intent = new Intent(PayDialog.this, RechargeActivity.class);
					startActivity(intent);
					finish();
					return;
				}
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
//					AppLog.v(TAG, "����ɹ�");
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
				payMoneyTxt.setVisibility(View.VISIBLE);
				payMoneyTxt.setText("����ǰʣ��Ļ����:  " + activityTime);
				payWay = 1;
				if(activityTime <= 0){
					payMoneyTxt.setText("����ǰʣ��Ļ����:  " + activityTime + "\n�޷�ʹ����ѻ����\n��ʹ���������ʽ");
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
				payMoneyTxt.setVisibility(View.VISIBLE);
//				String accountBalanceStr = Integer.toString(accountBalance);
				String accountBalanceStr = StringUtils.getPriceStr(accountBalance);
//				try{
//				accountBalanceStr = accountBalanceStr.substring(0, accountBalanceStr.length() - 2) 
//						+ "." + accountBalanceStr.substring(accountBalanceStr.length() - 2);
//				}catch(Exception e){
//					accountBalanceStr = accountBalanceStr + ".00";
//					e.printStackTrace();
//				}
				payMoneyTxt.setText("���ĵ�ǰ���:  " + accountBalanceStr);
				payWay = 2;
				int feeInt = Integer.parseInt(fee.replace(".", ""));
				if(accountBalance < feeInt){
					payMoneyTxt.setText("���ĵ�ǰ���:  " + accountBalanceStr + "\n����\n���ֵ����ʹ���������ʽ");
//					sureBtn.setVisibility(View.GONE);
					sureBtn.setText("��ֵ");
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
							AliPay alipay = new AliPay(PayDialog.this, mHandlerForAlipay);

							String result = alipay.pay(orderInfo);
//							alipay.setSandBox(true);
							Log.i(TAG, "result = " + result);
							Message msg = new Message();
							msg.what = RQF_PAY;
							msg.obj = result;
							mHandlerForAlipay.sendMessage(msg);
						}
					}.start();

				} catch (Exception ex) {
					ex.printStackTrace();
					Toast.makeText(PayDialog.this, "���ӷ�����ʧ��",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private String getNewOrderInfo() {
		String alipayBodyMessage = "�Ϻ�ϲ�������������޹�˾" + serverType + "����";
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(orderId);
		sb.append("\"&subject=\"");
		sb.append(alipayBodyMessage);
		sb.append("\"&body=\"");
		sb.append(alipayBodyMessage);
		sb.append("\"&total_fee=\"");
		sb.append(fee);
		sb.append("\"&notify_url=\"");

		// ��ַ��Ҫ��URL����
		sb.append(URLEncoder.encode("http://washmycar.sinaapp.com/washmycar/notifyurl.do"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// ���show_urlֵΪ�գ��ɲ���
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
	
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	/**
	 * ����ѿ۳�
	 */
	private void activityConsume(){	
		BaseCommand activityConsume = ClientSession.getInstance().getCmdFactory()
				.getActivityConsume(mLocalSharePref.getUserId(),  orderId);

		mExecuter.execute(activityConsume, mActivityConsumeRespHandler);

		dialogUtils.showProgress();
	}
	public void onActivityConsumeSuccess(int accountInfo, String payMessage){
		activityTime = accountInfo;
		payMoneyTxt.setText(payMessage + "\n����ǰʣ��Ļ����:  " + activityTime);
		cancelBtn.setText("֪����");
		sureBtn.setVisibility(View.GONE);
		ifPaySuc = true;
	}
	
	/**
	 * ������������صĳ���ע����
	 * @param rsp ���񷵻صĳ���ע������Ϣ
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
	 * ������ѿ۳�
	 */
	private void accountConsume(){
//		String fee = orderData.getFee();
		
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getAccountConsume(mLocalSharePref.getUserId(), Integer.parseInt(accountFee.replace(".", "")), orderId);

		mExecuter.execute(carRegister, mAccountConsumeRespHandler);

		dialogUtils.showProgress();
	}
	public void onAccountConsumeSuccess(int accountInfo, String payMessage){
		accountBalance = accountInfo;
		String accountBalanceStr = StringUtils.getPriceStr(accountBalance);//Integer.toString(accountBalance);
//		accountBalanceStr = accountBalanceStr.substring(0, accountBalanceStr.length() - 2) 
//				+ "." + accountBalanceStr.substring(accountBalanceStr.length() - 2);
		payMoneyTxt.setText(payMessage + "\n���ĵ�ǰ���:  " + accountBalanceStr);
		cancelBtn.setText("֪����");
		sureBtn.setVisibility(View.GONE);
		ifPaySuc = true;
	}
	
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
		if(activityTime <= 0){	//���û�л���Զ����λ֧��
			activityPayLayout.setVisibility(View.GONE);
		}
	}

	private void onReceiveActivityQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
//			dialogUtils.showToast(error);
		} else {
			CustomActivityQuery.Response customActivityQuery = (CustomActivityQuery.Response) rsp;

			onActivityQuerySuccess(customActivityQuery.time);

		}
	}
	
	private CommandExecuter.ResponseHandler mActivityQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveActivityQueryResponse(rsp);
		}

		public void handleException(IOException e) {
//			dialogUtils.showToast(getString(R.string.connection_error));
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
	 * ������������صĳ���ע����
	 * @param rsp ���񷵻صĳ���ע������Ϣ
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
//				dialogUtils.showToast(accountQueryRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mAccountQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveAccountQueryResponse(rsp);
		}

		public void handleException(IOException e) {
//			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
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
			if(serverType.contains("����")){
				mLocalSharePref.setUserType("01");
			}
			AppLog.v(TAG, "����ɹ�");
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
	
	Handler mHandlerForAlipay = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
				payListPart.setVisibility(View.GONE);
				sureBtn.setVisibility(View.VISIBLE);
				cancelBtn.setVisibility(View.VISIBLE);

				result.parseResult();
				String message = result.getResult();
				if(result.resultCode.equals("9000")){
					ifPaySuc = true;
					message += ",���Ķ������ᾡ�촦��";
				}
				
				payMoneyTxt.setText(message);
				cancelBtn.setText("֪����");
				sureBtn.setVisibility(View.GONE);
				payMoneyTxt.setVisibility(View.VISIBLE);
				
				break;
			case RQF_LOGIN: {
				Toast.makeText(PayDialog.this, result.getResult(),
						Toast.LENGTH_SHORT).show();
			}
				break;
			default:
				break;
			}
		};
	};
}
