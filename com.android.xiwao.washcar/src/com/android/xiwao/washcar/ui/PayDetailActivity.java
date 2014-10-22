package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.UpdateOrderStateCancel;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.StringUtils;

public class PayDetailActivity extends Activity {

	private Context mContext;
	private View view;
	private Button cannelOrderBtn;
	private Button payNowBtn;
	private Button agreebtn;
	
	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private long orderId;
	private int fee;//订单价格
	private int saleFee; //账户支付价格
	private String feeStr;
	private String saleFeeStr;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);

		mLocalSharePref = new LocalSharePreference(this);
		initExecuter();
		initUtils();
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.pay_detail, null);
		setContentView(view);
		
		fee = getIntent().getIntExtra("fee", 0);
		orderId = getIntent().getLongExtra("order_id", 0);
		saleFee = getIntent().getIntExtra("sale_fee", 0);
		initContentView();
		setHwView();
	}

	public void initContentView() {
		// TODO Auto-generated method stub
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText("订单支付");
		
		cannelOrderBtn = (Button) view.findViewById(R.id.cannel_order);
		payNowBtn = (Button) view.findViewById(R.id.pay_now);
		agreebtn = (Button) view.findViewById(R.id.agreebtn);
		agreebtn.setSelected(true); // 默认选中

		String feeStr = StringUtils.getPriceStr(fee);//Integer.toString(fee / 100) + ".00";
		saleFeeStr = StringUtils.getPriceStr(saleFee);//Integer.toString(saleFee);
//		saleFeeStr = saleFeeStr.substring(0, saleFeeStr.length() - 2) + "." + saleFeeStr.substring(saleFeeStr.length() - 2);
		payNowBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String feeStr = Integer.toString(fee / 100) + ".00";
				Intent intent = new Intent(mContext, PayDialog.class);
				intent.putExtra("order_id", orderId);
				intent.putExtra("order_fee", feeStr);
				intent.putExtra("order_account_fee", saleFeeStr);
				intent.putExtra("server_type", getIntent().getStringExtra("server_type"));
				startActivityForResult(intent, Constants.PAY_ORDER_RESULT_CODE);
			}
		});
		
		cannelOrderBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCannelOrderDialog();
			}
		});
		
		Button backbtn = (Button) view.findViewById(R.id.backbtn);
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		TextView serverType = (TextView)findViewById(R.id.server_type);
		TextView carNum = (TextView) findViewById(R.id.car_num);
		TextView webCenter = (TextView) findViewById(R.id.web_center);
		TextView phone = (TextView) findViewById(R.id.phone);
		TextView fee = (TextView) findViewById(R.id.fee);
		TextView accountPayAmt = (TextView) findViewById(R.id.account_pay_amt);
		TextView ifCleanIn = (TextView) findViewById(R.id.ifclean_in);
		TextView monthlyTime = (TextView) findViewById(R.id.monthly_time);
		TextView remark = (TextView) findViewById(R.id.remark);

		carNum.setText(getIntent().getStringExtra("car_code"));
		webCenter.setText(getIntent().getStringExtra("address"));
		phone.setText(getIntent().getStringExtra("phone"));
		fee.setText(feeStr + "    账户支付价格    " + saleFeeStr);
	
		accountPayAmt.setText(saleFeeStr);
		if(getIntent().getStringExtra("server_type").contains("包月")){
			TableRow ifCleanInRow = (TableRow) findViewById(R.id.clean_in_row);
			TextView ifCleanInLine = (TextView) findViewById(R.id.clean_in_row_line);
			ifCleanInLine.setVisibility(View.GONE);
			ifCleanInRow.setVisibility(View.GONE);
			monthlyTime.setText(getIntent().getIntExtra("monthly_time", 0) + " ");
			serverType.setText(getIntent().getStringExtra("server_type") + "    数量     " + getIntent().getIntExtra("monthly_time", 0));
		}else{
			TableRow monthlyTimeRow = (TableRow) findViewById(R.id.monthly_time_part);
			TextView ifCleanInLine = (TextView) findViewById(R.id.monthly_time_part_line);
			ifCleanInLine.setVisibility(View.GONE);
			monthlyTimeRow.setVisibility(View.GONE);
			if(getIntent().getBooleanExtra("if_clean_in", false)){
				ifCleanIn.setText("是");
				serverType.setText(getIntent().getStringExtra("server_type") + "    清洗内饰     是");
			}else{
				serverType.setText(getIntent().getStringExtra("server_type") + "    清洗内饰     否");
			}
		}
		
		remark.setText(getIntent().getStringExtra("remark"));
	}
	
	public void setHwView() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		
		//队列图片高度
		ImageView payTitle = (ImageView) view.findViewById(R.id.pay_title_img);
		LinearLayout.LayoutParams payTitleParams = new LinearLayout.LayoutParams((int)((displayHeight * 0.16f + 0.5f) * 4.27), 
				(int)(displayHeight * 0.16f + 0.5f)); 
		payTitle.setLayoutParams(payTitleParams);
		
		//按钮宽高
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams((int)(displayWidth * 0.3f + 0.5f), 
				(int)(displayHeight * 0.08f + 0.5f)); 
		btnParams.setMargins((int)(displayWidth * 0.1f + 0.5f), (int)(displayHeight * 0.04f + 0.5f), 0, 0);
		cannelOrderBtn.setLayoutParams(btnParams);
		btnParams = new LinearLayout.LayoutParams((int)(displayWidth * 0.3f + 0.5f), 
				(int)(displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int)(displayWidth * 0.2f + 0.5f), (int)(displayHeight * 0.04f + 0.5f), 0, 0);
		payNowBtn.setLayoutParams(btnParams);
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
	
	private void cannelOrder(){
		BaseCommand cannelOrder = ClientSession.getInstance().getCmdFactory()
				.updateOrderStateCancel((int)orderId);

		mExecuter.execute(cannelOrder, mRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onCannelOrderSuccess() {
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
			finish();
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}
	
}
