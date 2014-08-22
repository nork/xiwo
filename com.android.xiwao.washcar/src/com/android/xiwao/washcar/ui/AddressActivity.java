package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.io.ObjectOutputStream.PutField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.AddressData;
import com.android.xiwao.washcar.data.WebSiteData;
import com.android.xiwao.washcar.httpconnection.AddressQuery;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.DistractQuery;
import com.android.xiwao.washcar.listadapter.UsefulAddressListAdapter;
import com.android.xiwao.washcar.utils.DialogUtils;

public class AddressActivity extends Activity {
	private Context mContext;
	private Button backBtn;
	private ListView addressList;
	private Button addAddressBtn;
	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private List<WebSiteData> websitListData = new ArrayList<WebSiteData>();
	private List<AddressData> addressListData = new ArrayList<AddressData>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		mContext = this;
		
		mLocalSharePref = new LocalSharePreference(this);
		setContentView(R.layout.address_list);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		getWebsitInfo();
	}
	
	private void initContentView(){
		backBtn = (Button) findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		addAddressBtn = (Button) findViewById(R.id.add_address);
		addAddressBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, AddAddressActivity.class);
				intent.putExtra("websit_list", (Serializable)websitListData);
				startActivityForResult(intent, Constants.ADD_ADDRESS_RESULT_CODE);
			}
		});
		addressList = (ListView) findViewById(R.id.useful_address_list);
		addressList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("choice_address", addressListData.get(arg2));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	private void setHwView() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		//title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
	}
	/**
	 * 填充地址列表信息
	 * @param addressDataList
	 */
	private void fetchListAdapter(List<AddressData> addressDataList){
		UsefulAddressListAdapter usefulAddressListAdapter = new UsefulAddressListAdapter(mContext, false,
				R.layout.car_info_list_adapter, websitListData);
		usefulAddressListAdapter.addBriefs(addressDataList);
		addressList.setAdapter(usefulAddressListAdapter);
	}
	
	private void getWebsitInfo(){
		BaseCommand distractQuery = ClientSession.getInstance().getCmdFactory()
				.getDistractQuery();	//网点ID，此处暂时设置为1，为玉桥路网点

		mExecuter.execute(distractQuery, mDistractQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	/**
	 * 处理服务器返回的地址查询
	 * @param rsp 服务返回的地址查询结果信息
	 */
	private void onReceiveDistractQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			DistractQuery.Response distractQuery = (DistractQuery.Response) rsp;
			if (distractQuery.responseType.equals("N")) {
				websitListData = distractQuery.webDataList;
				getAddressInfo();
//				dialogUtils.showToast(addressQueryRsp.errorMessage);
			} else {
				dialogUtils.showToast(distractQuery.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mDistractQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveDistractQueryResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	
	private void getAddressInfo(){
		BaseCommand addressQuery = ClientSession.getInstance().getCmdFactory()
				.getAddressQuery(mLocalSharePref.getUserId(), 1);	//网点ID，此处暂时设置为1，为玉桥路网点

		mExecuter.execute(addressQuery, mAddressQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAddressQuerySuccess(){
//		setResult(RESULT_OK);
//		finish();
		fetchListAdapter(addressListData);
	}
	
	/**
	 * 处理服务器返回的地址查询
	 * @param rsp 服务返回的地址查询结果信息
	 */
	private void onReceiveAddressQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			AddressQuery.Response addressQueryRsp = (AddressQuery.Response) rsp;
			if (addressQueryRsp.responseType.equals("N")) {
				addressListData = addressQueryRsp.addressDataList;
				onAddressQuerySuccess();
//				dialogUtils.showToast(addressQueryRsp.errorMessage);
			} else {
				dialogUtils.showToast(addressQueryRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mAddressQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveAddressQueryResponse(rsp);
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
		if(resultCode != RESULT_OK){
			return;
		}
		switch(requestCode){
		case Constants.ADD_ADDRESS_RESULT_CODE:
			getAddressInfo();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
	}

}
