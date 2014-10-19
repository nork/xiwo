package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CarDelete;
import com.android.xiwao.washcar.httpconnection.CarQuery;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.listadapter.CarInfoListAdapter;
import com.android.xiwao.washcar.ui.widget.SwipeListView;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.FileUtil;

public class CarInfoActivity extends Activity{
	private static final String TAG = "CarInfoActivity";
	private Context mContext;
	private SwipeListView carList;
	private ImageView customerImg;
	private CarInfoListAdapter carInfoListAdapter;
	private List<CarInfo> carInfoListData = new ArrayList<CarInfo>();

	private TextView title;
	private Button backBtn;

	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private View mCurrentDisplayItemView;
	private int displayHeight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		mLocalSharePref = new LocalSharePreference(this);
		
		setContentView(R.layout.car_info_list);
		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		initAdapter();
		getCarListData();
	}

	public void initContentView() {
		// TODO Auto-generated method stub
		mContext = this;
		carList = (SwipeListView) findViewById(R.id.car_list);
		backBtn = (Button) findViewById(R.id.backbtn);
		backBtn.setVisibility(View.VISIBLE);
		title = (TextView) findViewById(R.id.title);
		title.setText(getString(R.string.car_info));
		TextView carInfoTitle = (TextView) findViewById(R.id.car_info_title);
		carInfoTitle.setText(mLocalSharePref.getNickName() + getString(R.string.car_info_title));
		
		customerImg = (ImageView) findViewById(R.id.custom_img);
		
		String userHeadBase64 = mLocalSharePref.getUserHead();
		if(!userHeadBase64.equals("") && userHeadBase64 != null && !userHeadBase64.equals("null")){
			Bitmap userHeadBitMap = FileUtil.base64ToBitmap(userHeadBase64);
			Drawable drawable = new BitmapDrawable(userHeadBitMap);
			customerImg.setBackgroundDrawable(drawable);
		}
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
//		carList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.putExtra("choice_car", (Parcelable)carInfoListData.get(arg2));
//				setResult(RESULT_OK, intent);
//				finish();
//			}
//		});
		carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "item onclick " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AppLog.v(TAG, "收到反馈22");
		
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
				R.layout.car_info_list_adapter, carList.getRightViewWidth(), displayHeight);
		carInfoListAdapter.setOnRightItemClickListener(new CarInfoListAdapter.onRightItemClickListener() {
        	
            @Override
            public void onRightItemClick(View v, int position, int option) {
            	
            	switch(option){
				case 1:		//删除
					deleteCar(carInfoListData.get(position).getCarId());
					break;
				case 2:		//洗车
					Intent intent = new Intent(mContext, CarInfoEditActivity.class);
					intent.putExtra("service_type", MainActivity.singleServiceList.get(0).getFeeType());
					intent.putExtra("server_cls", (Parcelable)MainActivity.singleServiceList.get(0));
					intent.putExtra("choice_car", (Parcelable)carInfoListData.get(position));
					intent.putExtra("is_need_last_car", true);
					startActivity(intent);
					finish();
					break;
				case 3:		//点击编辑按钮
					if(mCurrentDisplayItemView != v){
						carList.showRightOnClick(v);
						mCurrentDisplayItemView = v;
					}else{
						mCurrentDisplayItemView = null;
					}
					break;
				}
            }
        });
		carList.setAdapter(carInfoListAdapter);
	}

	/**
	 * 填充数据
	 */
	private void fetchList() {

		/*
		 * 此处设置一个添加按钮，将洗车标记为-1时， 默认加载添加按钮
		 */
		if(carInfoListData.size() < 50){
			CarInfo last = new CarInfo();	
			last.setCarCode("-1");
			carInfoListData.add(last);
		}
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
//				dialogUtils.showToast(carQueryRsp.errorMessage);
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
	
	private void deleteCar(long carId){
		BaseCommand deleteCar = ClientSession.getInstance().getCmdFactory()
				.getCarDelete(carId);

		mExecuter.execute(deleteCar, mCarDeleteRespHandler);

		dialogUtils.showProgress();
	}
	
	/**
	 * 处理服务器返回的登录结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveCarDeleteResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			CarDelete.Response carDeleteRsp = (CarDelete.Response) rsp;
			if (carDeleteRsp.responseType.equals("N")) {
				refreshInfoList();
			} else {
				dialogUtils.showToast(carDeleteRsp.errorMessage);
			}
		}
		dialogUtils.dismissProgress();
	}
	
	private CommandExecuter.ResponseHandler mCarDeleteRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCarDeleteResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
			fetchList();
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
	
	public void setHwView() {
		displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		
		// 头像和添加头像按钮的宽高度设置
		RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
				(int) (displayHeight * 0.12f + 0.5f),
				(int) (displayHeight * 0.12f + 0.5f));
		customerImg.setLayoutParams(imgParams);
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
