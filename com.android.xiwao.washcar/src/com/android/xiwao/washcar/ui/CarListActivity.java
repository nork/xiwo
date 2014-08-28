package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import com.android.xiwao.washcar.ui.widget.SwipeListView;
import com.android.xiwao.washcar.utils.DialogUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CarListActivity extends Activity{
	private static final String TAG = "CarListActivity";
	private Context mContext;
	private SwipeListView carList;
	private CarInfoListAdapter carInfoListAdapter;
	private List<CarInfo> carInfoListData = new ArrayList<CarInfo>();

	private TextView title;
	private Button backBtn;

	// ����
	private DialogUtils dialogUtils;

	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	// ���������ض���
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
		
		setContentView(R.layout.car_list);
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
		title = (TextView) findViewById(R.id.title);
		title.setText(getString(R.string.car_info));
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
		AppLog.v(TAG, "�յ�����22");
		
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
	 * ��ʼ��Adapter
	 */
	private void initAdapter() {
		if (carInfoListData.size() > 0) {
			carInfoListData.clear();
		}
		carInfoListAdapter = new CarInfoListAdapter(mContext, false,
				R.layout.car_info_list_adapter, carList.getRightViewWidth());
		carInfoListAdapter.setOnRightItemClickListener(new CarInfoListAdapter.onRightItemClickListener() {
        	
            @Override
            public void onRightItemClick(View v, int position, int option) {
            	
            	switch(option){
				case 1:		//ɾ��
					carInfoListData.remove(position);
					carInfoListAdapter.addBriefs(carInfoListData);
					break;
				case 2:		//ϴ��
					Intent intent = new Intent();
					intent.putExtra("choice_car", (Parcelable)carInfoListData.get(position));
					setResult(RESULT_OK, intent);
					finish();
					break;
				case 3:		//����༭��ť
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
	 * �������
	 */
	private void fetchList() {

		/*
		 * �˴�����һ����Ӱ�ť����ϴ�����Ϊ-1ʱ�� Ĭ�ϼ�����Ӱ�ť
		 */
		if(carInfoListData.size() < 50){
			CarInfo last = new CarInfo();	
			last.setCarCode("-1");
			carInfoListData.add(last);
		}
		carInfoListAdapter.addBriefs(carInfoListData);
	}

	/**
	 * ������������صĵ�¼���
	 * @param rsp ���񷵻صĵ�¼��Ϣ
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
	
	public void setHwView() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
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
