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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.xiwao.washcar.httpconnection.AccountQuery;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.PlaceOrder;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.FileUtil;

public class RechargeActivity extends Activity {

	private RelativeLayout alipayLayout;

	private TextView curMoney;
	private Button backBtn;
	private ImageView userHeadImg;
	private TextView phone;

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
		if(!userHeadBase64.equals("") && userHeadBase64 != null){
			userHeadBitMap = FileUtil.base64ToBitmap(userHeadBase64);
			Drawable drawable = new BitmapDrawable(userHeadBitMap);
			userHeadImg.setBackgroundDrawable(drawable);
//			customerImg.setBackground(drawable);
		}
		
		alipayLayout = (RelativeLayout) findViewById(R.id.alipay_layout);
		alipayLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				placeOrder();
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
		
		// 头像和添加头像按钮的宽高度设置
		RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
				(int) (displayHeight * 0.12f + 0.5f),
				(int) (displayHeight * 0.12f + 0.5f));
		imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
		imgParams.addRule(RelativeLayout.RIGHT_OF, R.id.head_title);
		userHeadImg.setLayoutParams(imgParams);
	}

	private void placeOrder(){
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getPlaceOrder(mLocalSharePref.getUserId(), "D", mLocalSharePref.getUserName(), 0
						, 0, 0, "00", null, null
						, null, 50000);

		mExecuter.execute(carRegister, mPlaceOrderRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onPlaceOrderSuccess(int saleFee, long orderId){
		try {	
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo();
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
	
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(orderId);
		sb.append("\"&subject=\"");
		sb.append("上海洗沃公司洗车服务费");
		sb.append("\"&body=\"");
		sb.append("上海洗沃公司洗车服务费");
		sb.append("\"&total_fee=\"");
		sb.append("0.01");
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
		curMoney.setText("账户金额：" + Long.toString(accountInfo) + "元");
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
