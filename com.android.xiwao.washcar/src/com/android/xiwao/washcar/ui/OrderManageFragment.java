package com.android.xiwao.washcar.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.OrderData;
import com.android.xiwao.washcar.listadapter.OrderListAdapter;

public class OrderManageFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private ListView listView;
	private Button paidBtn;
	private Button waitPayBtn;
	private Button completedBtn;
	private Button closedBtn;
	private TextView title;
	private Button backBtn;

	private OrderListAdapter orderListAdapter;
	private List<OrderData> listOrderData = new ArrayList<OrderData>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.order_list, null);
		initContentView();
		initAdapter();
		fetchList();
		setHwView();
		return view;
	}

	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		mContext = this.getActivity();
		listView = (ListView) view.findViewById(R.id.order_list);
		paidBtn = (Button) view.findViewById(R.id.paid_btn);
		waitPayBtn = (Button) view.findViewById(R.id.wait_pay_btn);
		completedBtn = (Button) view.findViewById(R.id.completed_btn);
		closedBtn = (Button) view.findViewById(R.id.closed_btn);
		title = (TextView) view.findViewById(R.id.title);
		backBtn = (Button) view.findViewById(R.id.backbtn);

		title.setText(getString(R.string.order_manage));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, OrderDetailActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initAdapter() {
		if (listOrderData.size() > 0) {
			listOrderData.clear();
		}
		orderListAdapter = new OrderListAdapter(mContext, false,
				R.layout.order_list_adapter);
		listView.setAdapter(orderListAdapter);
	}

	private void fetchList() {
		OrderData orderData = new OrderData();
		for (int i = 0; i < 5; i++) {
			orderData.setMoney("88");
			orderData.setPayState("已支付");
			orderData.setSerialNumber("0025655122");
			listOrderData.add(orderData);
		}

		orderListAdapter.addBriefs(listOrderData);
	}

	public void setHwView() {
		int displayHeight = ((XiwaoApplication)getActivity().getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getActivity().getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		
		LinearLayout buttonGroup = (LinearLayout) view.findViewById(R.id.button_group);
		LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams((int)(displayWidth * 0.8f + 0.5f), 
				(int)(displayHeight * 0.06f + 0.5f));
		buttonParams.setMargins(0, (int)(displayHeight * 0.04f + 0.5f), 0, 0);
		buttonGroup.setLayoutParams(buttonParams);
	}
}
