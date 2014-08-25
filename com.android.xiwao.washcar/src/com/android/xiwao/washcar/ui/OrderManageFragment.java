package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.OrderData;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.OrderQuery;
import com.android.xiwao.washcar.listadapter.OrderListAdapter;
import com.android.xiwao.washcar.utils.DialogUtils;

public class OrderManageFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private ListView paidListView;
	private ListView waitListView;
	private ListView doneListView;
	private ListView closeListView;
	private Button paidBtn;
	private Button waitPayBtn;
	private Button completedBtn;
	private Button closedBtn;
	private TextView title;
	private Button backBtn;

	private OrderListAdapter paidOrderListAdapter;
	private OrderListAdapter waitOrderListAdapter;
	private OrderListAdapter doneOrderListAdapter;
	private OrderListAdapter closeOrderListAdapter;
	private List<OrderData> paidListOrderData = new ArrayList<OrderData>();
	private List<OrderData> waitListOrderData = new ArrayList<OrderData>();
	private List<OrderData> doneListOrderData = new ArrayList<OrderData>();
	private List<OrderData> closeListOrderData = new ArrayList<OrderData>();
	
	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private String orderClass;
	private int curOrderClass;	//当前选中的订单类型  0 已支付 1 未支付 2 交易完成 3交易关闭

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLocalSharePref = new LocalSharePreference(this.getActivity());
		view = inflater.inflate(R.layout.order_list, null);
		initExecuter();
		initUtils();
		initContentView();
		initAdapter();
		setHwView();
		curOrderClass = 0;
		orderClass = "02";
		getOrderListData();
		return view;
	}

	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		mContext = this.getActivity();
		paidListView = (ListView) view.findViewById(R.id.paid_order_list);
		waitListView = (ListView) view.findViewById(R.id.waitting_order_list);
		doneListView = (ListView) view.findViewById(R.id.done_order_list);
		closeListView = (ListView) view.findViewById(R.id.close_order_list);
		paidBtn = (Button) view.findViewById(R.id.paid_btn);
		waitPayBtn = (Button) view.findViewById(R.id.wait_pay_btn);
		completedBtn = (Button) view.findViewById(R.id.completed_btn);
		closedBtn = (Button) view.findViewById(R.id.closed_btn);
		title = (TextView) view.findViewById(R.id.title);
		backBtn = (Button) view.findViewById(R.id.backbtn);

		title.setText(getString(R.string.order_manage));

		paidListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, OrderDetailActivity.class);
				intent.putExtra("order_detail", paidListOrderData.get(arg2));
				startActivityForResult(intent, Constants.CHECK_ORDER_RESULT_CODE);
			}
		});

		getFocuse(paidBtn); 	//初次加载时已经支付按钮默认选中
		paidBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFocuse(paidBtn);		
				curOrderClass = 0;
			}
		});
		waitPayBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFocuse(waitPayBtn);
				curOrderClass = 1;
			}
		});
		completedBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFocuse(completedBtn);
				curOrderClass = 2;
			}
		});
		closedBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFocuse(closedBtn);
				curOrderClass = 3;
			}
		});
	}
	
	/**
	 * 控件获取焦点
	 * @param view 需要获取焦点的控件
	 */
	private void getFocuse(View view){
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
		view.requestFocusFromTouch();
	}

	private void initAdapter() {
		if (paidListOrderData.size() > 0) {
			paidListOrderData.clear();
		}
		paidOrderListAdapter = new OrderListAdapter(mContext, false,
				R.layout.order_list_adapter);
		paidListView.setAdapter(paidOrderListAdapter);
	}

	public void refreshInfoList(){
		initAdapter();
		getOrderListData();
	}
	
	private void getOrderListData(){
		switch(curOrderClass){
		case 0:
			orderClass = "02";
			break;
		case 1:
			orderClass = "01";
			break;
		case 2:
			orderClass = "03";
			break;
		case 3:
			orderClass = "04";
			break;
		}
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getOrderQuery(mLocalSharePref.getUserId(), orderClass, 0, 100);

		mExecuter.execute(carRegister, mOrderQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onOrderQuerySuccess(){
		fetchList();
	}
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceiveOrderQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			OrderQuery.Response orderQuery = (OrderQuery.Response) rsp;
			if (orderQuery.responseType.equals("N")) {
				paidListOrderData = orderQuery.orderDataList;
				onOrderQuerySuccess();
//				dialogUtils.showToast(orderQuery.errorMessage);
			} else {
				dialogUtils.showToast(orderQuery.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mOrderQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveOrderQueryResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	private void fetchList() {
		paidOrderListAdapter.addBriefs(paidListOrderData);
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
//	
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		AppLog.v("OrderManageFragment", "OnResume");
//		getOrderListData();
//	}

	public void setHwView() {
		int displayHeight = ((XiwaoApplication) getActivity().getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getActivity().getApplication())
				.getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		LinearLayout buttonGroup = (LinearLayout) view
				.findViewById(R.id.button_group);
		LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.8f + 0.5f),
				(int) (displayHeight * 0.06f + 0.5f));
		buttonParams.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0, 0);
		buttonGroup.setLayoutParams(buttonParams);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AppLog.v(getTag(), "收到反馈22");
		
		if(resultCode != Activity.RESULT_OK){
			return;
		}
		switch(requestCode){
		case Constants.CHECK_ORDER_RESULT_CODE:
			refreshInfoList();
			break;
		}
	}
}
