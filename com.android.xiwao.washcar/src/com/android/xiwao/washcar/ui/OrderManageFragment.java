package com.android.xiwao.washcar.ui;

import java.io.IOException;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
import com.android.xiwao.washcar.ui.widget.PullToRefreshBase.OnRefreshListener;
import com.android.xiwao.washcar.ui.widget.PullToRefreshListView;
import com.android.xiwao.washcar.utils.DialogUtils;

public class OrderManageFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private PullToRefreshListView paidListView;
	private PullToRefreshListView waitListView;
	private PullToRefreshListView doneListView;
	private PullToRefreshListView closeListView;
	ListView paidList;
	ListView waitList;
	ListView doneList;
	ListView closeList;
	private Button paidBtn;
	private Button waitPayBtn;
	private Button completedBtn;
	private Button closedBtn;
	private TextView title;
	private TextView noOrder;
//	private Button backBtn;

	private OrderListAdapter paidOrderListAdapter;
	private OrderListAdapter waitOrderListAdapter;
	private OrderListAdapter doneOrderListAdapter;
	private OrderListAdapter closeOrderListAdapter;
	
	// ����
	private DialogUtils dialogUtils;

	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	// ���������ض���
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private String orderClass;
	private int curOrderClass;	//��ǰѡ�еĶ�������  0 ��֧�� 1 δ֧�� 2 ������� 3���׹ر�
	private boolean ifNeedShowProg; //�Ƿ���Ҫ��ʾ������
	private int pagePaid = 0;	//��֧�������б�ǰҳ��
	private int countPagePaid = 0;	//��֧�������б���ҳ��
	private int pageWait = 0;	//δ֧�������б�ǰҳ��
	private int countPageWait = 0;	//��֧�������б���ҳ��
	private int pageDone = 0;	//����ɶ����б�ǰҳ��
	private int countPageDone = 0;	//��֧�������б���ҳ��
	private int pageClose = 0;	//�ѹرն����б�ǰҳ��
	private int countPageClose = 0;	//��֧�������б���ҳ��
	

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
		curOrderClass = 1;
		orderClass = "01";
		getOrderListData();
		return view;
	}

	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		mContext = this.getActivity();
		paidListView = (PullToRefreshListView) view.findViewById(R.id.paid_order_list);
		waitListView = (PullToRefreshListView) view.findViewById(R.id.waitting_order_list);
		doneListView = (PullToRefreshListView) view.findViewById(R.id.done_order_list);
		closeListView = (PullToRefreshListView) view.findViewById(R.id.close_order_list);
		paidBtn = (Button) view.findViewById(R.id.paid_btn);
		waitPayBtn = (Button) view.findViewById(R.id.wait_pay_btn);
		completedBtn = (Button) view.findViewById(R.id.completed_btn);
		closedBtn = (Button) view.findViewById(R.id.closed_btn);
		title = (TextView) view.findViewById(R.id.title);
		noOrder = (TextView) view.findViewById(R.id.no_order);

		setListViewOnEvent();
		title.setText(getString(R.string.order_manage)); 	
		setSelected(waitPayBtn);//���μ���ʱ�Ѿ�֧����ťĬ��ѡ��
		setListDisplay(waitListView);
		paidBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setSelected(paidBtn);	
				setListDisplay(paidListView);
				curOrderClass = 0;
				if(paidOrderListAdapter.isEmpty()){
					getOrderListData();
				}
			}
		});
		waitPayBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setSelected(waitPayBtn);
				setListDisplay(waitListView);
				curOrderClass = 1;
				if(waitOrderListAdapter.isEmpty()){
					getOrderListData();
				}
			}
		});
		completedBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setSelected(completedBtn);
				setListDisplay(doneListView);
				curOrderClass = 2;
				if(doneOrderListAdapter.isEmpty()){
					getOrderListData();
				}
			}
		});
		closedBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setSelected(closedBtn);
				setListDisplay(closeListView);
				curOrderClass = 3;
				if(closeOrderListAdapter.isEmpty()){
					getOrderListData();
				}
			}
		});
		
		//��֧���б�������
		paidListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount);
				if(loadMore){
					if(pagePaid + 1 <= countPagePaid){
						pagePaid++;
						getOrderListData();
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//δ֧���б�������
		waitListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount);
				if(loadMore){
					if(pageWait + 1 <= countPageWait){
						pageWait++;
						getOrderListData();
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		});		
		//��������б�������
		doneListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount);
				if(loadMore){
					if(pageDone + 1 <= countPageDone){
						pageDone++;
						getOrderListData();
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		});		
		//���׹ر��б�������
		closeListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount);
				if(loadMore){
					if(pageClose + 1 <= countPageClose){
						pageClose++;
						getOrderListData();
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	private void setSelected(View view){
		switch(curOrderClass){
		case 0:
			paidBtn.setSelected(false);
			break;
		case 1:
			waitPayBtn.setSelected(false);
			break;
		case 2:
			completedBtn.setSelected(false);
			break;
		case 3:
			closedBtn.setSelected(false);
			break;
		}
		view.setSelected(true);
	}
	/**
	 * �����б�Ŀ�߶��Դﵽ���غ���ʾ�б��ܣ��˺���ֻ����4������ˢ���б�������4���б��setVisibility������Ч��δ�ҵ�ԭ�򣩣�������ʱ������������棩
	 * @param view
	 */
	private void setListDisplay(PullToRefreshListView view){	
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(0, 0);
		switch(curOrderClass){
		case 0:
			paidListView.setLayoutParams(listParams);
			break;
		case 1:
			waitListView.setLayoutParams(listParams);
			break;
		case 2:
			doneListView.setLayoutParams(listParams);
			break;
		case 3:
			closeListView.setLayoutParams(listParams);
			break;
		}
		
		listParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(listParams);
	}

	private void initAdapter() {
		paidOrderListAdapter = new OrderListAdapter(mContext, false,
				R.layout.order_list_adapter);
		paidList.setAdapter(paidOrderListAdapter);
		
		waitOrderListAdapter = new OrderListAdapter(mContext, false,
				R.layout.order_list_adapter);
		waitList.setAdapter(waitOrderListAdapter);
		
		doneOrderListAdapter = new OrderListAdapter(mContext, false,
				R.layout.order_list_adapter);
		doneList.setAdapter(doneOrderListAdapter);
		
		closeOrderListAdapter = new OrderListAdapter(mContext, false,
				R.layout.order_list_adapter);
		closeList.setAdapter(closeOrderListAdapter);
	}

	public void refreshInfoList(){
		initAdapter();
		pagePaid = 0;
		pageWait = 0;
		pageDone = 0;
		pageClose = 0;
		getOrderListData();
	}
	
	private void getOrderListData(){
		int page = 0;
		switch(curOrderClass){
		case 0:
			orderClass = "02";
			page = pagePaid;
			break;
		case 1:
			orderClass = "01";
			page = pageWait;
			break;
		case 2:
			orderClass = "03";
			page = pageDone;
			break;
		case 3:
			orderClass = "04";
			page = pageClose;
			break;
		}
		AppLog.v("TAG", "page:" + page);
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getOrderQuery(mLocalSharePref.getUserId(), orderClass, page * 20, 20);

		mExecuter.execute(carRegister, mOrderQueryRespHandler);

		if(!ifNeedShowProg){
			dialogUtils.showProgress();
		}
	}
	
	public void onOrderQuerySuccess(List<OrderData> listOrderData, int count){
		fetchList(listOrderData, count);
	}
	
	/**
	 * ������������صĳ���ע����
	 * @param rsp ���񷵻صĳ���ע������Ϣ
	 */
	private void onReceiveOrderQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			OrderQuery.Response orderQuery = (OrderQuery.Response) rsp;
			if (orderQuery.responseType.equals("N")) {
//				paidListOrderData = orderQuery.orderDataList;
				onOrderQuerySuccess(orderQuery.orderDataList, orderQuery.orderCount);
//				dialogUtils.showToast(orderQuery.errorMessage);
			} else {
				dialogUtils.showToast(orderQuery.errorMessage); 
				ifNeedShowProg = false;
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mOrderQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveOrderQueryResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
			ifNeedShowProg = false;
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
			switch(curOrderClass){
			case 0:
				paidListView.onRefreshComplete();
				break;
			case 1:
				waitListView.onRefreshComplete();
				break;
			case 2:
				doneListView.onRefreshComplete();
				break;
			case 3:
				closeListView.onRefreshComplete();
				break;
			}
//			ifNeedShowProg = false;
		}
	};
	
	private void fetchList(List<OrderData> listOrderData, int orderCount) {
		switch(curOrderClass){
		case 0:
			if(ifNeedShowProg){//�˴��������е���ʾ�������ı�������ֱ��Ƿ���Ҫˢ���б�
				paidOrderListAdapter.refreshBriefs(listOrderData);
				ifNeedShowProg = false;		//���֮����Ҫ���˱����Ϊfalse����ֹ����һ�ε�ˢ�²���Ӱ��
			}else{
				paidOrderListAdapter.addBriefs(listOrderData);
			}
			if(paidOrderListAdapter.isEmpty()){
				paidListView.setVisibility(View.GONE);
				noOrder.setVisibility(View.VISIBLE);
			}
			countPagePaid = orderCount / 20;
			break;
		case 1:
			if(ifNeedShowProg){//�˴��������е���ʾ�������ı�������ֱ��Ƿ���Ҫˢ���б�
				waitOrderListAdapter.refreshBriefs(listOrderData);
				ifNeedShowProg = false;
			}else{
				waitOrderListAdapter.addBriefs(listOrderData);
			}
			if(waitOrderListAdapter.isEmpty()){
				waitListView.setVisibility(View.GONE);
				noOrder.setVisibility(View.VISIBLE);
			}
			countPageWait = orderCount / 20;
			break;
		case 2:
			if(ifNeedShowProg){//�˴��������е���ʾ�������ı�������ֱ��Ƿ���Ҫˢ���б�
				doneOrderListAdapter.refreshBriefs(listOrderData);
				ifNeedShowProg = false;
			}else{
				doneOrderListAdapter.addBriefs(listOrderData);
			}
			if(doneOrderListAdapter.isEmpty()){
				doneListView.setVisibility(View.GONE);
				noOrder.setVisibility(View.VISIBLE);
			}
			countPageDone = orderCount / 20;
			break;
		case 3:
			if(ifNeedShowProg){		//�˴��������е���ʾ�������ı�������ֱ��Ƿ���Ҫˢ���б�
				closeOrderListAdapter.refreshBriefs(listOrderData);
				ifNeedShowProg = false;
			}else{
				closeOrderListAdapter.addBriefs(listOrderData);
			}
			if(closeOrderListAdapter.isEmpty()){
				closeListView.setVisibility(View.GONE);
				noOrder.setVisibility(View.VISIBLE);
			}
			countPageClose = orderCount / 20;
			break;
		}
	}

	/**
	 * ��ʼ����Ҫ�Ĺ���
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

	private void setListViewOnEvent(){
		paidList = paidListView.getRefreshableView();
		waitList = waitListView.getRefreshableView();
		doneList = doneListView.getRefreshableView();
		closeList = closeListView.getRefreshableView();
		
		paidListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				ifNeedShowProg = true;
				pagePaid = 0;
				getOrderListData();
			}
		});
		
		waitListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				ifNeedShowProg = true;
				pageWait = 0;
				getOrderListData();
			}
		});
		
		doneListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				ifNeedShowProg = true;
				pageDone = 0;
				getOrderListData();
			}
		});
		
		closeListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				ifNeedShowProg = true;
				pageClose = 0;
				getOrderListData();
			}
		});
		paidList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, OrderDetailActivity.class);
				intent.putExtra("order_detail", (OrderData)paidOrderListAdapter.getItem(arg2 - 1));
				startActivityForResult(intent, Constants.CHECK_ORDER_RESULT_CODE);
			}
		});
		
		waitList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, OrderDetailActivity.class);
				intent.putExtra("order_detail", (OrderData)waitOrderListAdapter.getItem(arg2 - 1));
				startActivityForResult(intent, Constants.CHECK_ORDER_RESULT_CODE);
			}
		});
		
		doneList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, OrderDetailActivity.class);
				intent.putExtra("order_detail", (OrderData)doneOrderListAdapter.getItem(arg2 - 1));
				startActivityForResult(intent, Constants.CHECK_ORDER_RESULT_CODE);
			}
		});
		
		closeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, OrderDetailActivity.class);
				intent.putExtra("order_detail", (OrderData)closeOrderListAdapter.getItem(arg2 - 1));
				startActivityForResult(intent, Constants.CHECK_ORDER_RESULT_CODE);
			}
		});
	}
	
	public void setHwView() {
		int displayHeight = ((XiwaoApplication) getActivity().getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getActivity().getApplication())
				.getDisplayWidth();
		// title�߶�
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
		AppLog.v(getTag(), "�յ�����22");
		
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
