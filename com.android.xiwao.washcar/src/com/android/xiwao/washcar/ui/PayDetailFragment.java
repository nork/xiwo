package com.android.xiwao.washcar.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;

public class PayDetailFragment extends BaseFragment {

	private Context mContext;
	private View view;
	private Button cannelOrderBtn;
	private Button payNowBtn;
	private Button agreebtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		view = inflater.inflate(R.layout.pay_detail, null);
		initContentView();
		setHwView();
		return view;
	}

	@Override
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
				Intent intent = new Intent(mContext, PayDialog.class);
				startActivity(intent);
			}
		});
	}

	public void setHwView() {
		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				(int) (Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		
		//队列图片高度
		ImageView payTitle = (ImageView) view.findViewById(R.id.pay_title_img);
		LinearLayout.LayoutParams payTitleParams = new LinearLayout.LayoutParams((int)((Constants.displayHeight * 0.16f + 0.5f) * 4.27), 
				(int)(Constants.displayHeight * 0.16f + 0.5f)); 
		payTitle.setLayoutParams(payTitleParams);
		
		//按钮宽高
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams((int)(Constants.displayWidth * 0.3f + 0.5f), 
				(int)(Constants.displayHeight * 0.08f + 0.5f)); 
		btnParams.setMargins((int)(Constants.displayWidth * 0.1f + 0.5f), (int)(Constants.displayHeight * 0.04f + 0.5f), 0, 0);
		cannelOrderBtn.setLayoutParams(btnParams);
		btnParams = new LinearLayout.LayoutParams((int)(Constants.displayWidth * 0.3f + 0.5f), 
				(int)(Constants.displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int)(Constants.displayWidth * 0.2f + 0.5f), (int)(Constants.displayHeight * 0.04f + 0.5f), 0, 0);
		payNowBtn.setLayoutParams(btnParams);
	}
}
