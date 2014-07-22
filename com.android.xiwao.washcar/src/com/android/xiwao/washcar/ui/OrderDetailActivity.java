package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;

public class OrderDetailActivity extends Activity {
	private Context mContext;
	private Button cannelOrderBtn;
	private Button payNowBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.order_detail);
		initContentView();
		setHwView();
	}

	private void initContentView() {
		payNowBtn = (Button) findViewById(R.id.pay_now);
		cannelOrderBtn = (Button) findViewById(R.id.cannel_order);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.order_detail);

		payNowBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, PayDialog.class);
				startActivity(intent);
			}
		});
	}

	public void setHwView() {
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		// 按钮宽高
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
				(int) (Constants.displayWidth * 0.3f + 0.5f),
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int) (Constants.displayWidth * 0.1f + 0.5f),
				(int) (Constants.displayHeight * 0.04f + 0.5f), 0, 0);
		cannelOrderBtn.setLayoutParams(btnParams);
		btnParams = new LinearLayout.LayoutParams(
				(int) (Constants.displayWidth * 0.3f + 0.5f),
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int) (Constants.displayWidth * 0.2f + 0.5f),
				(int) (Constants.displayHeight * 0.04f + 0.5f), 0, 0);
		payNowBtn.setLayoutParams(btnParams);
	}
}
