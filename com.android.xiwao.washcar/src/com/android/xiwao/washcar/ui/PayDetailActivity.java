package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.AddressData;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.utils.DialogUtils;

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
	private int fee;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);

		mLocalSharePref = new LocalSharePreference(this);
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.pay_detail, null);
		setContentView(view);
		
		fee = getIntent().getIntExtra("fee", 0);
		orderId = getIntent().getLongExtra("order_id", 0);
		initContentView();
		setHwView();
	}

	public void initContentView() {
		// TODO Auto-generated method stub
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(R.string.car_order);
		
		cannelOrderBtn = (Button) view.findViewById(R.id.cannel_order);
		payNowBtn = (Button) view.findViewById(R.id.pay_now);
		agreebtn = (Button) view.findViewById(R.id.agreebtn);
		agreebtn.setSelected(true); // 默认选中

		payNowBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String feeStr = Integer.toString(fee / 100) + ".00";
				Intent intent = new Intent(mContext, PayDialog.class);
				intent.putExtra("order_id", orderId);
				intent.putExtra("order_fee", feeStr);
				startActivityForResult(intent, Constants.PAY_ORDER_RESULT_CODE);
			}
		});
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
