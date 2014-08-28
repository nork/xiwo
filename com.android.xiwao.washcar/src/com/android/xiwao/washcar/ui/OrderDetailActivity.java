package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.OrderData;

public class OrderDetailActivity extends Activity {
	private Context mContext;
	private Button cannelOrderBtn;
	private Button payNowBtn;
	private TextView orderId;
	private TextView transactionAmount;
	private TextView serviceType;
	private TextView carNum;
	private TextView phone;
	private TextView vipCustomer;
	private TextView address;
	private TextView endTime;
	private TextView orderState;
	private LinearLayout buttonGroup;
	private LinearLayout bottomStatePart;
	private ImageView orderStateImg;
	private TextView bottomOrderState;
	
	private OrderData orderData;
	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;
	
	private boolean ifNeedRefresh = false;	//标记返回时订单列表是否需要刷新
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		mContext = this;
		mLocalSharePref = new LocalSharePreference(this);
		orderData = getIntent().getParcelableExtra("order_detail");
		setContentView(R.layout.order_detail);
		initContentView();
		fetchOrderData();
		setHwView();
	}

	private void initContentView() {
		payNowBtn = (Button) findViewById(R.id.pay_now);
		cannelOrderBtn = (Button) findViewById(R.id.cannel_order);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.order_detail);
		
		orderId = (TextView) findViewById(R.id.serial_number);
		transactionAmount = (TextView) findViewById(R.id.transaction_amount);
		serviceType = (TextView) findViewById(R.id.server_type);
		carNum = (TextView)findViewById(R.id.car_num);
		phone = (TextView) findViewById(R.id.phone);
		vipCustomer = (TextView) findViewById(R.id.vip_customer);
		address = (TextView) findViewById(R.id.address);
		endTime = (TextView) findViewById(R.id.endtime);
		orderState = (TextView) findViewById(R.id.order_state);
		buttonGroup = (LinearLayout) findViewById(R.id.button_group);
		bottomStatePart = (LinearLayout) findViewById(R.id.bottom_state_part);
		orderStateImg = (ImageView) findViewById(R.id.order_state_img);
		bottomOrderState = (TextView) findViewById(R.id.bottom_order_state);
		
		payNowBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, PayDialog.class);
//				intent.putExtra("order_detail", orderData);
				intent.putExtra("order_id", orderData.getOrderId());
				intent.putExtra("order_fee", orderData.getFee());
				startActivityForResult(intent, Constants.PAY_ORDER_RESULT_CODE);
			}
		});
		
		Button backBtn = (Button) findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void fetchOrderData(){
		orderId.setText(Long.toString(orderData.getOrderId()));
		transactionAmount.setText(orderData.getFee());
		serviceType.setText(orderData.getServiceType());
		carNum.setText(orderData.getCarCode());
		phone.setText(orderData.getMobileNum());
		vipCustomer.setText(mLocalSharePref.getNickName());
		address.setText(orderData.getAddressDetail());
		endTime.setText(orderData.getWashEnd());
		String orderStateStr = orderData.getOrderState();
		if(orderStateStr.equals("01")){
			orderStateStr = "未支付";
			buttonGroup.setBackgroundResource(R.color.background);
			payNowBtn.setVisibility(View.VISIBLE);
			cannelOrderBtn.setVisibility(View.VISIBLE);
			bottomStatePart.setVisibility(View.GONE);
		}else if(orderStateStr.equals("02")){
			orderStateStr = "已支付";
			buttonGroup.setBackgroundResource(R.color.white);
			payNowBtn.setVisibility(View.GONE);
			cannelOrderBtn.setVisibility(View.GONE);
			bottomStatePart.setVisibility(View.VISIBLE);
			orderStateImg.setBackgroundResource(R.drawable.yes);
			bottomOrderState.setText("已经支付");
		}else if(orderStateStr.equals("03")){
			orderStateStr = "服务中";
			buttonGroup.setBackgroundResource(R.color.background);
			payNowBtn.setVisibility(View.GONE);
			cannelOrderBtn.setVisibility(View.GONE);
			bottomStatePart.setVisibility(View.VISIBLE);
			orderStateImg.setBackgroundResource(R.drawable.yes);
			bottomOrderState.setText("服务中");
		}else if(orderStateStr.equals("04")){
			orderStateStr = "已完成";
			buttonGroup.setBackgroundResource(R.color.background);
			payNowBtn.setVisibility(View.GONE);
			cannelOrderBtn.setVisibility(View.GONE);
			bottomStatePart.setVisibility(View.VISIBLE);
			orderStateImg.setBackgroundResource(R.drawable.yes);
			bottomOrderState.setText("已经完成");
		}
		orderState.setText(orderStateStr);
	}
	public void setHwView() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		// 按钮宽高
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.3f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int) (displayWidth * 0.1f + 0.5f),
				(int) (displayHeight * 0.04f + 0.5f), 0, 0);
		cannelOrderBtn.setLayoutParams(btnParams);
		btnParams = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.3f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int) (displayWidth * 0.2f + 0.5f),
				(int) (displayHeight * 0.04f + 0.5f), 0, 0);
		payNowBtn.setLayoutParams(btnParams);

		RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		
//		bottomStatePart.setLa(Gravity.BOTTOM);
		bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		bottomStatePart.setLayoutParams(bottomParams);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AppLog.v("TAG", "付款反馈");
		if(resultCode != RESULT_OK){
			return;
		}
		AppLog.v("TAG", "付款成功");
		switch(requestCode){
		case Constants.PAY_ORDER_RESULT_CODE:
			buttonGroup.setBackgroundResource(R.color.white);
			payNowBtn.setVisibility(View.GONE);
			cannelOrderBtn.setVisibility(View.GONE);
			bottomStatePart.setVisibility(View.VISIBLE);
			orderStateImg.setBackgroundResource(R.drawable.yes);
			orderState.setText("已支付");
			bottomOrderState.setText("已经支付");
			ifNeedRefresh = true;
			break;
		}
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(ifNeedRefresh){
			AppLog.v("123", "订单列表需要刷新");
			setResult(RESULT_OK);
		}
		super.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}		
}
