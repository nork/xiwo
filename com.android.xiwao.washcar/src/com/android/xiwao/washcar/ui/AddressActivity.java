package com.android.xiwao.washcar.ui;

import java.io.IOException;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.AddressData;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.httpconnection.AddressDelete;
import com.android.xiwao.washcar.httpconnection.AddressQuery;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.listadapter.UsefulAddressListAdapter;
import com.android.xiwao.washcar.ui.widget.SwipeListView;
import com.android.xiwao.washcar.utils.DialogUtils;

public class AddressActivity extends Activity {
	private Context mContext;
	private Button backBtn;
	private SwipeListView addressList;
	private Button addAddressBtn;
	private TextView noAddressTxt;
	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
//	private List<WebSiteData> websitListData = new ArrayList<WebSiteData>();
	private List<AddressData> addressListData = new ArrayList<AddressData>();
	
	private UsefulAddressListAdapter usefulAddressListAdapter;
	
	private View mCurrentDisplayItemView;
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
//		getWebsitInfo();
		getAddressInfo();
	}
	
	private void initContentView(){
		noAddressTxt = (TextView) findViewById(R.id.no_address_txt);
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
				AppLog.v("TAG", "地址数量:" + addressListData.size());
				if(addressListData.size() >= 5){
					Toast.makeText(mContext, "常用地址信息不能超过5条，请删除后再添加！", Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = new Intent(mContext, AddAddressActivity.class);
//				intent.putExtra("websit_list", (Serializable)websitListData);
				startActivityForResult(intent, Constants.ADD_ADDRESS_RESULT_CODE);
			}
		});
		addressList = (SwipeListView) findViewById(R.id.useful_address_list);
		addressList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("choice_address", (Parcelable)addressListData.get(arg2));
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
		
		
//		LinearLayout.LayoutParams addressBtnParams = new LinearLayout.LayoutParams(
//				(int) (displayWidth * 0.94f + 0.5f),
//				(int) (displayHeight * 0.08f + 0.5f));
//		addressBtnParams.setMargins((int) (displayWidth * 0.03f + 0.5f),
//				(int) (displayHeight * 0.02f + 0.5f),
//				(int) (displayWidth * 0.03f + 0.5f), 0);
//		addAddressBtn.setLayoutParams(addressBtnParams);
		
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.7f + 0.5f));
//		addressList.setLayoutParams(listParams);
		noAddressTxt.setLayoutParams(listParams);
//		
		addressList.setRightViewWidth((int) (displayWidth * 0.15f + 0.5f));	//设置列表删除键宽度
	}
	/**
	 * 填充地址列表信息
	 * @param addressDataList
	 */
	private void fetchListAdapter(List<AddressData> addressDataList){
		usefulAddressListAdapter = new UsefulAddressListAdapter(mContext, false,
				R.layout.car_info_list_adapter, addressList.getRightViewWidth());
		usefulAddressListAdapter.setOnRightItemClickListener(new UsefulAddressListAdapter.onRightItemClickListener() {

			@Override
			public void onRightItemClick(View v, int position, int option) {
				// TODO Auto-generated method stub
				switch(option){
				case 1:		//删除
					deleteAddress(addressListData.get(position).getAddressId());
					break;
				case 2:		//选中
					Intent intent = new Intent();
					intent.putExtra("choice_address", (Parcelable)addressListData.get(position));
//					intent.putExtra("branch_name", branchName);
					((Activity)mContext).setResult(Activity.RESULT_OK, intent);
					((Activity)mContext).finish();
					break;
				case 3:		//点击编辑按钮
					if(mCurrentDisplayItemView != v){
						addressList.showRightOnClick(v);
						mCurrentDisplayItemView = v;
					}else{
						mCurrentDisplayItemView = null;
					}
					break;
				}
			}
        });
		//添加一个按钮项
		AddressData singleOrderData = new AddressData();
		singleOrderData.setAddressId(-1);
		addressListData.add(singleOrderData);
		usefulAddressListAdapter.addBriefs(addressDataList);
		addressList.setAdapter(usefulAddressListAdapter);
		if(addressDataList.size() <= 0 || addressDataList == null){
			AppLog.v("AddressActivity", "地址为空");
			noAddressTxt.setVisibility(View.VISIBLE);
			addressList.setVisibility(View.GONE);
		}
	}
	
	private void getAddressInfo(){
		BaseCommand addressQuery = ClientSession.getInstance().getCmdFactory()
				.getAddressQuery(mLocalSharePref.getUserId());	//网点ID，此处暂时设置为1，为玉桥路网点

		mExecuter.execute(addressQuery, mAddressQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAddressQuerySuccess(){
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
			noAddressTxt.setVisibility(View.VISIBLE);
			addressList.setVisibility(View.GONE);
		} else {
			AddressQuery.Response addressQueryRsp = (AddressQuery.Response) rsp;
			if (addressQueryRsp.responseType.equals("N")) {
				addressListData = addressQueryRsp.addressDataList;
//				addrNum = addressQueryRsp.addressDataList.size();
				onAddressQuerySuccess();
				if(addressQueryRsp.addressDataList.size() <= 0){
					noAddressTxt.setVisibility(View.VISIBLE);
					addressList.setVisibility(View.GONE);
				}
//				dialogUtils.showToast(addressQueryRsp.errorMessage);
			} else {
//				dialogUtils.showToast(addressQueryRsp.errorMessage);
				addressListData.clear();
				fetchListAdapter(addressListData);
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
	
	private void deleteAddress(long addressId){
		BaseCommand addressDelete = ClientSession.getInstance().getCmdFactory()
				.getAddressDelete(addressId);	//网点ID，此处暂时设置为1，为玉桥路网点

		mExecuter.execute(addressDelete, mAddressDeleteRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAddressDeleteSuccess(){
		getAddressInfo();
	}
	
	private void onReceiveAddressDeleteResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			AddressDelete.Response addressDeleteRsp = (AddressDelete.Response) rsp;
			if (addressDeleteRsp.responseType.equals("N")) {
				onAddressDeleteSuccess();
//				dialogUtils.showToast(addressQueryRsp.errorMessage);
			} else {
				dialogUtils.showToast(addressDeleteRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mAddressDeleteRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveAddressDeleteResponse(rsp);
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
//			getAddressInfo();
			AddressData newAddress = new AddressData();
			newAddress = data.getParcelableExtra("new_address");
			Intent intent = new Intent();
			intent.putExtra("choice_address", (Parcelable)newAddress);
			setResult(RESULT_OK, intent);
			finish();
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
