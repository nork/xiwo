package com.android.xiwao.washcar.ui;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.FeeData;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.RateQuery;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.StringUtils;

public class ServerFragment extends BaseFragment{
	private Context mContext;
	private TextView title;
		
	private View view;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;
	
	// 工具
	private DialogUtils dialogUtils;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		mContext = getActivity();
		mLocalSharePref = new LocalSharePreference(this.getActivity());
		
        view = inflater.inflate(R.layout.server, null); 
        initContentView();
        setHwView();
        initExecuter();
		initUtils();
        if(MainActivity.feeDataList.size() <= 0){
			rateQuery();
		}
        setPriceView();
        return view;  
    }
	
	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		RelativeLayout washNoIn = (RelativeLayout) view.findViewById(R.id.wash_no_in);
		RelativeLayout washIn = (RelativeLayout) view.findViewById(R.id.wash_in);
		RelativeLayout wax = (RelativeLayout) view.findViewById(R.id.wax);
		RelativeLayout monthly = (RelativeLayout) view.findViewById(R.id.monthly);
		RelativeLayout recharge = (RelativeLayout) view.findViewById(R.id.recharge);
		
		recharge.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, RechargeActivity.class);
				startActivity(intent);
			}
		});
		
		monthly.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CarInfoEditActivity.class);
				intent.putExtra("service_type", 2);
				startActivity(intent);
			}
		});
		
		wax.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CarInfoEditActivity.class);
				intent.putExtra("service_type", 1);
				startActivity(intent);
			}
		});
		
		washNoIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CarInfoEditActivity.class);
				intent.putExtra("service_type", 0);
				startActivity(intent);
			}
		});
		
		washIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CarInfoEditActivity.class);
				intent.putExtra("service_type", 0);
				intent.putExtra("if_clean_in", true);
				startActivity(intent);
			}
		});
	}
	
	private void setPriceView(){
		int cleanInPrice = 0;
		int waxPrice = 0;
		int washPrice = 0;
		int monthlyPrice = 0;
		int rechargePrice = 0;
		for(FeeData feeData : MainActivity.feeDataList){
			if(feeData.getFeeType().equals("01")){
				cleanInPrice = feeData.getFee();
			}
			if(feeData.getFeeType().equals("B")){
				waxPrice = feeData.getFee();
			}
			if(feeData.getFeeType().equals("A")){
				washPrice = feeData.getFee();
			}
			if(feeData.getFeeType().equals("C")){
				monthlyPrice = feeData.getFee();
			}
			if(feeData.getFeeType().equals("D")){
				rechargePrice = feeData.getFee();
			}
		}
		TextView washNoIn = (TextView) view.findViewById(R.id.wash_no_in_price);
		TextView washIn = (TextView) view.findViewById(R.id.wash_in_price);
		TextView wax = (TextView) view.findViewById(R.id.wax_price);
		TextView monthly = (TextView) view.findViewById(R.id.monthly_price);
		TextView recharge = (TextView) view.findViewById(R.id.rechager_price);
		
		washNoIn.setText(StringUtils.getPriceIntStr(washPrice) + "元");
		washIn.setText(StringUtils.getPriceIntStr(washPrice + cleanInPrice) + "元");
		wax.setText(StringUtils.getPriceIntStr(waxPrice) + "元");
		monthly.setText(StringUtils.getPriceIntStr(monthlyPrice) + "元");
		recharge.setText(StringUtils.getPriceIntStr(rechargePrice) + "元");
	}
	public void setHwView(){
		int displayHeight = ((XiwaoApplication)getActivity().getApplication()).getDisplayHeight();
//		int displayWidth = ((XiwaoApplication)getActivity().getApplication()).getDisplayWidth();		
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
	}
	
	/**
	 * 查询费用
	 */
	public void rateQuery(){
		BaseCommand login = ClientSession.getInstance().getCmdFactory()
				.getRateQuery();

		mExecuter.execute(login, mRespHandler);
		dialogUtils.showProgress();
	}
	
	/**
	 * 处理服务器返回的查询结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveRateQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			RateQuery.Response rateQueryRsp = (RateQuery.Response) rsp;
			if (rateQueryRsp.responseType.equals("N")) {
				MainActivity.feeDataList = rateQueryRsp.briefs;
				setPriceView();
			} else {
				dialogUtils.showToast(rateQueryRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveRateQueryResponse(rsp);
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
}
