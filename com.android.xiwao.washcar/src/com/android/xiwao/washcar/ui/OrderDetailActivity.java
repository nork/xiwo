package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.OrderData;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CarRegister;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.UpdateOrderStateCancel;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.StringUtils;

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
	private TextView saleFee;
	private TextView quantity;
	private TextView ifCleanIn;
	private LinearLayout buttonGroup;
	private LinearLayout bottomStatePart;
	private ImageView orderStateImg;
	private TextView bottomOrderState;
	private TableRow payTimeRow;
	private TableRow orderTimeRow;
	private TableRow doneTimeRow;
	private TableRow quantityRow;
	private TableRow ifCleanInRow;
	
	private OrderData orderData;
	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
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
		initExecuter();
		initUtils();
		initContentView();
		fetchOrderData();
		setHwView();
	}

	private void initContentView() {
		payNowBtn = (Button) findViewById(R.id.pay_now);
		cannelOrderBtn = (Button) findViewById(R.id.cannel_order);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("交易详情");
		
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
		payTimeRow = (TableRow) findViewById(R.id.pay_time);
		orderTimeRow = (TableRow) findViewById(R.id.order_time);
		doneTimeRow = (TableRow) findViewById(R.id.done_time);
		saleFee = (TextView) findViewById(R.id.sale_fee);
		ifCleanIn = (TextView) findViewById(R.id.if_clean);
		quantity = (TextView) findViewById(R.id.quantity);
		ifCleanInRow = (TableRow) findViewById(R.id.if_clean_row);
		quantityRow = (TableRow) findViewById(R.id.quantity_row);
		
		payNowBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, PayDialog.class);
//				intent.putExtra("order_detail", orderData);
				intent.putExtra("order_id", orderData.getOrderId());
				intent.putExtra("order_fee", orderData.getFee());
				String saleFeeStr = orderData.getSaleFee();
				saleFeeStr = saleFeeStr.substring(0, saleFeeStr.length() - 2) + "." 
						+ saleFeeStr.substring(saleFeeStr.length() - 2);
				intent.putExtra("order_account_fee", saleFeeStr);
				intent.putExtra("server_type", orderData.getServiceType());
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
		
		cannelOrderBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCannelOrderDialog();
			}
		});
	}

	private void fetchOrderData(){
		orderId.setText(Long.toString(orderData.getOrderId()));
		serviceType.setText(orderData.getServiceType());
		carNum.setText(orderData.getCarCode());
		phone.setText(orderData.getMobileNum());
		vipCustomer.setText(mLocalSharePref.getNickName());
		address.setText(orderData.getAddressDetail());
		endTime.setText(orderData.getWashEnd().replace("T", " "));
		TextView payTime = (TextView) findViewById(R.id.paytime);
		TextView orderTime = (TextView) findViewById(R.id.ordertime);
		payTime.setText(orderData.getPayTime().replace("T", " "));
		orderTime.setText(orderData.getCreateTime().replace("T", " "));
		TextView saleFeeTitle = (TextView) findViewById(R.id.sale_fee_title);
		
		LinearLayout remindTitle = (LinearLayout) findViewById(R.id.remind_title);
		
		String orderStateStr = orderData.getOrderState();
//		String saleFeeStr = orderData.getSaleFee();
//		saleFeeStr = saleFeeStr.substring(0, saleFeeStr.length() - 2) + "." + saleFeeStr.substring(saleFeeStr.length() - 2);
		String saleFeeStr = StringUtils.getPriceStr(Integer.parseInt(orderData.getSaleFee()));
		if(orderStateStr.equals("01")){
			orderStateStr = "未支付";
			buttonGroup.setBackgroundResource(R.color.background);
			payNowBtn.setVisibility(View.VISIBLE);
			cannelOrderBtn.setVisibility(View.VISIBLE);
			bottomStatePart.setVisibility(View.GONE);
			payTimeRow.setVisibility(View.GONE);
			doneTimeRow.setVisibility(View.GONE);
			TableRow saleFeeRow = (TableRow) findViewById(R.id.sale_fee_row);
			saleFeeRow.setVisibility(View.GONE);
			transactionAmount.setText(orderData.getFee() + "      账户支付价格    " + saleFeeStr);
//			String saleFeeStr = orderData.getSaleFee();
//			saleFeeStr = saleFeeStr.substring(0, saleFeeStr.length() - 2) + "." + saleFeeStr.substring(saleFeeStr.length() - 2);
//			saleFee.setText(saleFeeStr);
		}else if(orderStateStr.equals("02")){
			orderStateStr = "已支付";
			buttonGroup.setBackgroundResource(R.color.white);
			payNowBtn.setVisibility(View.GONE);
			cannelOrderBtn.setVisibility(View.GONE);
			bottomStatePart.setVisibility(View.GONE);
			orderStateImg.setBackgroundResource(R.drawable.yes);
			bottomOrderState.setText("已经支付");
			doneTimeRow.setVisibility(View.GONE);
			remindTitle.setVisibility(View.GONE);
			saleFeeTitle.setText("支付方式");
			saleFee.setText(orderData.getPayType());

			transactionAmount.setText(saleFeeStr + "    支付方式    " + orderData.getPayType());
		}else if(orderStateStr.equals("03")){
			orderStateStr = "服务中";
			buttonGroup.setBackgroundResource(R.color.background);
			payNowBtn.setVisibility(View.GONE);
			cannelOrderBtn.setVisibility(View.GONE);
			bottomStatePart.setVisibility(View.GONE);
			orderStateImg.setBackgroundResource(R.drawable.yes);
			bottomOrderState.setText("服务中");
			remindTitle.setVisibility(View.GONE);
			saleFeeTitle.setText("支付方式");
			saleFee.setText(orderData.getPayType());
			
			transactionAmount.setText(saleFeeStr + "    支付方式    " + orderData.getPayType());
		}else if(orderStateStr.equals("04")){
			orderStateStr = "已完成";
			buttonGroup.setBackgroundResource(R.color.background);
			payNowBtn.setVisibility(View.GONE);
			cannelOrderBtn.setVisibility(View.GONE);
			bottomStatePart.setVisibility(View.GONE);
			orderStateImg.setBackgroundResource(R.drawable.yes);
			bottomOrderState.setText("已经完成");
			remindTitle.setVisibility(View.GONE);
			saleFeeTitle.setText("支付方式");
			saleFee.setText(orderData.getPayType());
			transactionAmount.setText(saleFeeStr + "    支付方式    " + orderData.getPayType());
		}else if(orderStateStr.equals("05")){
			orderStateStr = "已取消";
			payNowBtn.setVisibility(View.GONE);
			cannelOrderBtn.setVisibility(View.GONE);
			bottomStatePart.setVisibility(View.GONE);
			payTimeRow.setVisibility(View.GONE);
			doneTimeRow.setVisibility(View.GONE);
			remindTitle.setVisibility(View.GONE);
			transactionAmount.setText(orderData.getSaleFee());
		}
		orderState.setText(orderStateStr);
		quantity.setText(orderData.getQuantity() + "");
		String cleanInStr = "否";
		if(orderData.getServiceTypeMi().equals("01")){
			ifCleanIn.setText("是");
			cleanInStr = "是";
		}
		if(orderData.getServiceType().equals("包月")){
			ifCleanInRow.setVisibility(View.GONE);		
			serviceType.setText(orderData.getServiceType() + "    数量     " + orderData.getQuantity());
		}else if(orderData.getServiceType().equals("打蜡") || orderData.getServiceType().equals("洗车")){
			quantityRow.setVisibility(View.GONE);
			serviceType.setText(orderData.getServiceType() + "    清洗内饰     " + cleanInStr);
		}
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
	
	private void cannelOrder(){
		BaseCommand cannelOrder = ClientSession.getInstance().getCmdFactory()
				.updateOrderStateCancel((int)orderData.getOrderId());

		mExecuter.execute(cannelOrder, mRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onCannelOrderSuccess() {
		ifNeedRefresh = true;
		dialogUtils.dismissProgress();
		finish();
	}

	/**
	 * 处理服务器返回的车辆注册结果
	 * 
	 * @param rsp
	 *            服务返回的车辆注册结果信息
	 */
	private void onReceiveCannelOrderResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			UpdateOrderStateCancel.Response updateOrderStateCancel = (UpdateOrderStateCancel.Response) rsp;
			if (updateOrderStateCancel.responseType.equals("N")) {
				onCannelOrderSuccess();
				dialogUtils.showToast(updateOrderStateCancel.errorMessage);
			} else {
				dialogUtils.showToast(updateOrderStateCancel.errorMessage);
			}
		}
	}

	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCannelOrderResponse(rsp);
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

	public void showCannelOrderDialog(){
		new AlertDialog.Builder(mContext)
		.setTitle(mContext.getString(R.string.remind))
		.setMessage("确定取消订单？")
		.setPositiveButton(mContext.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				cannelOrder();
			}
		})
		.setNegativeButton(mContext.getString(R.string.no),
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
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
			setResult(RESULT_OK);
		}
		dialogUtils.dismissProgress();
		super.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}		
}
