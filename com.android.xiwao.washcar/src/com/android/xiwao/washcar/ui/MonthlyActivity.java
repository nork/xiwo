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
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.MonthlyCarData;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.VIPInfoQuery;
import com.android.xiwao.washcar.listadapter.MonthlyCarInfoListAdapter;
import com.android.xiwao.washcar.ui.widget.SwipeListView;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.FileUtil;

/**
 * 包月界面
 * @author hpq
 *
 */
public class MonthlyActivity extends Activity{
	private Context mContext;
	private SwipeListView carList;
	private ImageView customerImg;
	private MonthlyCarInfoListAdapter carInfoListAdapter;
	private List<MonthlyCarData> monthlyCarDataList = new ArrayList<MonthlyCarData>();

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
		carList.setRightViewWidth(0);	//设置列表删除键宽度
		backBtn = (Button) findViewById(R.id.backbtn);
		backBtn.setVisibility(View.VISIBLE);
		title = (TextView) findViewById(R.id.title);
		title.setText("包月信息");
		TextView carInfoTitle = (TextView) findViewById(R.id.car_info_title);
		carInfoTitle.setText(mLocalSharePref.getNickName() + getString(R.string.monthly_info_title));
		
		customerImg = (ImageView) findViewById(R.id.custom_img);
		
		String userHeadBase64 = mLocalSharePref.getUserHead();
		if(!userHeadBase64.equals("") && userHeadBase64 != null){
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

		carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "item onclick " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });
	}

	public void refreshInfoList(){
		initAdapter();
		getCarListData();
	}
	/**
	 * 初始化Adapter
	 */
	private void initAdapter() {
		if (monthlyCarDataList.size() > 0) {
			monthlyCarDataList.clear();
		}
		carInfoListAdapter = new MonthlyCarInfoListAdapter(mContext, false,
				R.layout.car_info_list_adapter, carList.getRightViewWidth());
		carInfoListAdapter.setOnRightItemClickListener(new MonthlyCarInfoListAdapter.onRightItemClickListener() {
        	
            @Override
            public void onRightItemClick(View v, int position, int option) {
            	
            	switch(option){
				case 1:		//删除
					break;
				case 2:		//洗车
					Intent intent = new Intent(mContext, MonthlyDetailActivity.class);
					intent.putExtra("service_type", 0);
					intent.putExtra("choice_monthly_car", (Parcelable)monthlyCarDataList.get(position));
					startActivity(intent);
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
//		monthlyCarDataList.remove(1);
//		if(monthlyCarDataList.size() == 1){
//			Intent intent = new Intent(mContext, MonthlyDetailActivity.class);
//			intent.putExtra("service_type", 0);
//			intent.putExtra("choice_monthly_car", (Parcelable)monthlyCarDataList.get(0));
//			startActivity(intent);
//			finish();
//		}
		carInfoListAdapter.addBriefs(monthlyCarDataList);
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
			VIPInfoQuery.Response vipInfoQuery = (VIPInfoQuery.Response) rsp;
			if (vipInfoQuery.responseType.equals("N")) {
				monthlyCarDataList = vipInfoQuery.monthlyCarDataList;
			} else {
				dialogUtils.showToast(vipInfoQuery.errorMessage);
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
				.getVIPInfoQuery(mLocalSharePref.getUserId());

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
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
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
