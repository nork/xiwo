package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.net.URLEncoder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.alipay.Keys;
import com.android.xiwao.washcar.alipay.Result;
import com.android.xiwao.washcar.alipay.Rsa;
import com.android.xiwao.washcar.data.FeeData;
import com.android.xiwao.washcar.httpconnection.AccountQuery;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.PlaceOrder;
import com.android.xiwao.washcar.httpconnection.RateQuery;
import com.android.xiwao.washcar.listadapter.RechargeAdapter;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.FileUtil;
import com.android.xiwao.washcar.utils.StringUtils;

public class RechargeActivity extends Activity {

	private RelativeLayout alipayLayout;

	private TextView curMoney;
	private Button backBtn;
	private ImageView userHeadImg;
	private TextView phone;
	private ListView rechargeServiceList;

	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private Bitmap userHeadBitMap;
	
	private long orderId;
	
	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;

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
		if(MainActivity.feeDataList.size() <= 0){
			rateQuery();
		}else{
			setRechargeView();
		}
	}

	private void initContentView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.recharge);
		backBtn = (Button) findViewById(R.id.backbtn);

		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		TextView nickName = (TextView) findViewById(R.id.nick_name);
		phone = (TextView) findViewById(R.id.phone);
		userHeadImg = (ImageView) findViewById(R.id.custom_img);
		curMoney = (TextView) findViewById(R.id.amt_title);
		
		nickName.setText("昵称：" + mLocalSharePref.getNickName());
		phone.setText("我的手机：" + mLocalSharePref.getUserName());
		String userHeadBase64 = mLocalSharePref.getUserHead();
		if(!userHeadBase64.equals("") && userHeadBase64 != null && !userHeadBase64.equals("null")){
			userHeadBitMap = FileUtil.base64ToBitmap(userHeadBase64);
			Drawable drawable = new BitmapDrawable(userHeadBitMap);
			userHeadImg.setBackgroundDrawable(drawable);
//			customerImg.setBackground(drawable);
		}
		
//		alipayLayout = (RelativeLayout) findViewById(R.id.alipay_layout);
//		alipayLayout.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				placeOrder();
//			}
//		});
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
		
		// 头像和添加头像按钮的宽高度设置
		RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
				(int) (displayHeight * 0.12f + 0.5f),
				(int) (displayHeight * 0.12f + 0.5f));
		imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
		imgParams.addRule(RelativeLayout.RIGHT_OF, R.id.head_title);
		userHeadImg.setLayoutParams(imgParams);
	}

	private void placeOrder(FeeData feeData){
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getPlaceOrder(mLocalSharePref.getUserId(), feeData.getFeeType(), mLocalSharePref.getUserName(), 0
						, 0, 0, "00", null, null
						, feeData.getFeeTypeMi(), feeData.getFee(), 1);

		mExecuter.execute(carRegister, mPlaceOrderRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onPlaceOrderSuccess(int saleFee, long orderId){
		((XiwaoApplication)getApplication()).setIfNeedRefreshOrder(true);
		try {	
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(orderId, saleFee);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(RechargeActivity.this, mHandlerForAlipay);

					String result = alipay.pay(orderInfo);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandlerForAlipay.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(RechargeActivity.this, "连接服务器失败",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getNewOrderInfo(long orderId, int saleFee) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(orderId);
		sb.append("\"&subject=\"");
		sb.append("上海喜沃汽车服务有限公司充值服务");
		sb.append("\"&body=\"");
		sb.append("上海喜沃汽车服务有限公司充值服务");
		sb.append("\"&total_fee=\"");
		sb.append(StringUtils.getPriceStr(saleFee));
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://washmycar.sinaapp.com/washmycar/notifyurl.do"));
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
	
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	/**
	 * 查询费用
	 */
	public void rateQuery(){
		BaseCommand login = ClientSession.getInstance().getCmdFactory()
				.getRateQuery();

		mExecuter.execute(login, mRespHandler);
		dialogUtils.showProgress();
	}
	/**
	 * 处理服务器返回的查询结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveRateQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			RateQuery.Response rateQueryRsp = (RateQuery.Response) rsp;
			if (rateQueryRsp.responseType.equals("N")) {
				MainActivity.feeDataList = rateQueryRsp.briefs;
				getServiceCls();
				setRechargeView();
			} else {
				dialogUtils.showToast(rateQueryRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveRateQueryResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceivePlaceOrderResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			PlaceOrder.Response placeOrder = (PlaceOrder.Response) rsp;
			if (placeOrder.responseType.equals("N")) {
				onPlaceOrderSuccess(placeOrder.saleFee, placeOrder.orderId);
//				dialogUtils.showToast(placeOrder.errorMessage);
			} else {
				dialogUtils.showToast(placeOrder.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mPlaceOrderRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceivePlaceOrderResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	private void getBalance() {
		BaseCommand accountQuery = ClientSession.getInstance().getCmdFactory()
				.getAccountQuery(mLocalSharePref.getUserId());

		mExecuter.execute(accountQuery, mAccountQueryRespHandler);

		dialogUtils.showProgress();
	}

	public void onAccountQuerySuccess(long accountInfo) {
		String accountStr = StringUtils.getPriceStr((int)accountInfo);//Long.toString(accountInfo);
//		accountStr = accountStr.substring(0, accountStr.length() - 2) 
//				+ "." + accountStr.substring(accountStr.length() - 2);
		curMoney.setText("账户金额：" + accountStr + "元");
	}

	/**
	 * 处理服务器返回的车辆注册结果
	 * 
	 * @param rsp
	 *            服务返回的车辆注册结果信息
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
				// dialogUtils.showToast(accountQueryRsp.errorMessage);
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
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};

	/**
	 * 服务分类
	 */
	private void getServiceCls(){
		MainActivity.singleServiceList.clear();
		MainActivity.monthlyServiceList.clear();
		MainActivity.rechargeServiceList.clear();
		for(FeeData feeData : MainActivity.feeDataList){
			if(feeData.getFeeType().equals("A")){//单次服务
				MainActivity.singleServiceList.add(feeData);
			}
			if(feeData.getFeeType().equals("B")){//包月服务
				MainActivity.monthlyServiceList.add(feeData);
			}
			if(feeData.getFeeType().equals("C")){//充值服务
				MainActivity.rechargeServiceList.add(feeData);
			}
		}
	}
	
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
		userHeadImg.setImageBitmap(null);	//此处将ImageView的背景bitmap设置为空，断开setImageBitmap对bitmap的引用，然后才能回收bitmap，否则后面回收方法将不起效果
		if(userHeadBitMap != null && !userHeadBitMap.isRecycled()){
			userHeadBitMap.recycle();
			userHeadBitMap = null;
		}
		System.gc();
	}

	private void setRechargeView(){
		rechargeServiceList = (ListView) findViewById(R.id.recharge_server_list);
		RechargeAdapter rechargeServiceAdapter = new RechargeAdapter(this, false
				, R.layout.recharge_adapter, MainActivity.rechargeServiceList);
		
		rechargeServiceList.setAdapter(rechargeServiceAdapter);
		
		rechargeServiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				placeOrder(MainActivity.rechargeServiceList.get(arg2));
			}
		});
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
	
	Handler mHandlerForAlipay = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:

				result.parseResult();
				String message = result.getResult();
				if(result.resultCode.equals("9000")){
					message += "账户充值成功！您可到客户信息中查询！";
//					dialogUtils.showToast(message);
					getBalance();
				}else{
					dialogUtils.showToast(message);
				}			
				break;
			case RQF_LOGIN: {
				Toast.makeText(RechargeActivity.this, result.getResult(),
						Toast.LENGTH_SHORT).show();
			}
				break;
			default:
				break;
			}
		};
	};

}
