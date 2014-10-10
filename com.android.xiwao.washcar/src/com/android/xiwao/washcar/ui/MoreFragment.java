package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.VIPInfoQuery;
import com.android.xiwao.washcar.utils.DialogUtils;

public class MoreFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private RelativeLayout customInfo;
	private RelativeLayout password;
	private RelativeLayout integralManage;
	private RelativeLayout help;
	private RelativeLayout tellFriend;
	private RelativeLayout about;
	private RelativeLayout carInfo;
	private RelativeLayout allMonthInfo;
	private RelativeLayout contactInfo;
	private Button quitBtn;

	// 工具
	private DialogUtils dialogUtils;
	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;
	
	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;

//	private List<MonthlyCarData> monthlyCarDataList = new ArrayList<MonthlyCarData>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		view = inflater.inflate(R.layout.more, null);
		mLocalSharePref = new LocalSharePreference(this.getActivity());
		initUtils();
		initExecuter();
		initContentView();
		setHwView();
		return view;
	}

	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		customInfo = (RelativeLayout) view.findViewById(R.id.custom_info);
		password = (RelativeLayout) view.findViewById(R.id.password);
		integralManage = (RelativeLayout) view
				.findViewById(R.id.integral_manage);
		help = (RelativeLayout) view.findViewById(R.id.help);
		tellFriend = (RelativeLayout) view.findViewById(R.id.tell_friend);
		about = (RelativeLayout) view.findViewById(R.id.about);
		carInfo = (RelativeLayout) view.findViewById(R.id.car_info);
		allMonthInfo = (RelativeLayout) view.findViewById(R.id.allmonth_info);
		contactInfo = (RelativeLayout) view.findViewById(R.id.contact_info);
		TextView title = (TextView)view.findViewById(R.id.title);
		title.setText(R.string.more);

		quitBtn = (Button) view.findViewById(R.id.quit);
		
		if(mLocalSharePref.getUserType().equals("01")){
			allMonthInfo.setVisibility(View.VISIBLE);
		}
		
		if(!mLocalSharePref.getLoginState()){
			customInfo.setVisibility(View.GONE);
			password.setVisibility(View.GONE);
			integralManage.setVisibility(View.GONE);
			carInfo.setVisibility(View.GONE);
			allMonthInfo.setVisibility(View.GONE);
			quitBtn.setText("登录");
		}
	
		quitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(quitBtn.getText().equals("登录")){
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
					return;
				}
				showExitDialog();
			}
		});

		customInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CustomInfoActivity.class);
				startActivity(intent);
			}
		});

		password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						ResetPasswordActivity.class);
				startActivity(intent);
			}
		});
		
		integralManage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						RechargeActivity.class);
				startActivity(intent);
			}
		});
		
		carInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						CarInfoActivity.class);
				startActivity(intent);
			}
		});
		
		contactInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCallDialog();
			}
		});
		
		allMonthInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext,
//						MonthlyActivity.class);
//				startActivity(intent);
				getCarListData();
			}
		});
		help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						UserProtocalActivity.class);
				startActivity(intent);
			}
		});
	}

	public void showCallDialog(){
		final String phone = "4008-591-577";
		new AlertDialog.Builder(mContext)
		.setTitle(mContext.getResources().getString(R.string.remind))
		.setMessage(phone)
		.setPositiveButton("取消", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1)
			{
				dialog.dismiss();
			}
		})
		.setNegativeButton("呼叫", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try{
					Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
					mContext.startActivity(phoneIntent);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).show();
	}

	public void showExitDialog(){
		mLocalSharePref.setLoginState(false);
		new AlertDialog.Builder(mContext)
		.setTitle(mContext.getString(R.string.remind))
		.setMessage(mContext.getString(R.string.sure_exit))
		.setPositiveButton(mContext.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent = new Intent(mContext, MainActivity.class);
				mContext.startActivity(intent);
				getActivity().finish();
			}
		})
		.setNegativeButton(mContext.getString(R.string.no),
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
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
	
	private void setHwView() {
		int displayHeight = ((XiwaoApplication)getActivity().getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getActivity().getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		// 客户信息
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0,
				0);
		allMonthInfo.setLayoutParams(params);
		if(mLocalSharePref.getUserType().equals("00")){
			customInfo.setLayoutParams(params);
		}
		// 修改密码
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f),
				0, 0);
		if(mLocalSharePref.getUserType().equals("01")){
			customInfo.setLayoutParams(params);
		}
		password.setLayoutParams(params);
		// 积分管理
		integralManage.setLayoutParams(params);
		// 帮助
		help.setLayoutParams(params);
		// 告诉朋友
		tellFriend.setLayoutParams(params);
		// 关于
		about.setLayoutParams(params);
		carInfo.setLayoutParams(params);
		//联系我们
		contactInfo.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		quitBtn.setLayoutParams(params);
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
//				monthlyCarDataList = vipInfoQuery.monthlyCarDataList;
				if(vipInfoQuery.monthlyCarDataList.size() == 1){
					Intent intent = new Intent(mContext, MonthlyDetailActivity.class);
					intent.putExtra("service_type", 0);
					intent.putExtra("choice_monthly_car", (Parcelable)vipInfoQuery.monthlyCarDataList.get(0));
					startActivity(intent);
				}else{
					Intent intent = new Intent(mContext,
							MonthlyActivity.class);
					startActivity(intent);
				}
			} else {
				dialogUtils.showToast(vipInfoQuery.errorMessage);
			}
//			fetchList();
		}
		dialogUtils.dismissProgress();
	}
	
	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCarListResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
//			fetchList();
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
}
