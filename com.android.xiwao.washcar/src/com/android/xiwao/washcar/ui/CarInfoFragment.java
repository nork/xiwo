package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CarQuery;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.listadapter.CarInfoListAdapter;
import com.android.xiwao.washcar.utils.DialogUtils;

public class CarInfoFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private ListView carList;
	private CarInfoListAdapter carInfoListAdapter;
	private List<CarInfo> carInfoListData = new ArrayList<CarInfo>();

	private TextView title;
	private Button backBtn;
	private TextView carInfoTitle;

	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppLog.v(getTag(), "fragment onCreate");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		mLocalSharePref = new LocalSharePreference(this.getActivity());
		
		view = inflater.inflate(R.layout.car_info_list, null);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		initAdapter();
		getCarListData();
		return view;
	}

	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		mContext = this.getActivity();
		carList = (ListView) view.findViewById(R.id.car_list);
		backBtn = (Button) view.findViewById(R.id.backbtn);
		title = (TextView) view.findViewById(R.id.title);
		title.setText(getString(R.string.car_info));
		carInfoTitle = (TextView) view.findViewById(R.id.car_info_title);
		carInfoTitle.setText(mLocalSharePref.getNickName() + getString(R.string.car_info_title));
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AppLog.v(getTag(), "收到反馈22");
		
		switch(requestCode){
		case Constants.ADD_CAR_RESULT_CODE:
			refreshInfoList();
			break;
		case Constants.MODIFY_CAR_RESULT_CODE:
			refreshInfoList();
			break;
		}
	}

	public void refreshInfoList(){
		initAdapter();
		getCarListData();
	}
	/**
	 * 初始化Adapter
	 */
	private void initAdapter() {
		if (carInfoListData.size() > 0) {
			carInfoListData.clear();
		}
		carInfoListAdapter = new CarInfoListAdapter(mContext, false,
				R.layout.car_info_list_adapter);
		carList.setAdapter(carInfoListAdapter);
	}

	/**
	 * 填充数据
	 */
	private void fetchList() {
		carInfoListAdapter.addBriefs(carInfoListData);
	}

	/**
	 * 处理服务器返回的登录结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveCarListResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			CarQuery.Response carQueryRsp = (CarQuery.Response) rsp;
			if (carQueryRsp.responseType.equals("N")) {
				carInfoListData = carQueryRsp.briefs;
			} else {
				dialogUtils.showToast(carQueryRsp.errorMessage);
			}
			fetchList();
		}
		dialogUtils.dismissProgress();
	}
	
	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCarListResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
			fetchList();
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	private void getCarListData(){
		BaseCommand login = ClientSession.getInstance().getCmdFactory()
				.getCarQuery(mLocalSharePref.getUserId());

		mExecuter.execute(login, mRespHandler);

		dialogUtils.showProgress();
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
	
	public void setHwView() {
		int displayHeight = ((XiwaoApplication)getActivity().getApplication()).getDisplayHeight();
//		int displayWidth = ((XiwaoApplication)getActivity().getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
	}
}
