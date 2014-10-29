package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.alipay.Keys;
import com.android.xiwao.washcar.alipay.Result;
import com.android.xiwao.washcar.alipay.Rsa;
import com.android.xiwao.washcar.data.FeeData;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.PlaceOrder;
import com.android.xiwao.washcar.httpconnection.RateQuery;
import com.android.xiwao.washcar.listadapter.ServiceAdapter;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.StringUtils;

public class ServerFragment extends BaseFragment{
	private Context mContext;
	private Activity activity;
	private TextView title;
		
	private View view;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;
	
	// 工具
	private DialogUtils dialogUtils;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	
	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		mContext = getActivity();
		activity = this.getActivity();
		mLocalSharePref = new LocalSharePreference(this.getActivity());
		
        view = inflater.inflate(R.layout.server, null); 
        initContentView();
        setHwView();
        initExecuter();
		initUtils();
        if(MainActivity.feeDataList.size() <= 0){
			rateQuery();
		}else{
			setServiceClsView();
		}
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
	/**
	 * 服务分类
	 */
	private void getServiceCls(){
		MainActivity.singleServiceList.clear();
		MainActivity.monthlyServiceList.clear();
		MainActivity.rechargeServiceList.clear();
		for(FeeData feeData : MainActivity.feeDataList){
			if(feeData.getFeeType().equals("A")){//单次服务
				MainActivity.singleServiceList.add(feeData);
			}
			if(feeData.getFeeType().equals("B")){//包月服务
				MainActivity.monthlyServiceList.add(feeData);
			}
			if(feeData.getFeeType().equals("C")){//充值服务
				MainActivity.rechargeServiceList.add(feeData);
			}
		}
	}
	private void setServiceClsView(){
		ListView singleServiceList = (ListView) view.findViewById(R.id.single_server_list);
		ListView monthlyServiceList = (ListView) view.findViewById(R.id.monthly_server_list);
		ListView rechargeServiceList = (ListView) view.findViewById(R.id.recharge_server_list);
		ServiceAdapter singleServiceAdapter = new ServiceAdapter(mContext, false
				, R.layout.service_adapter, MainActivity.singleServiceList);
		ServiceAdapter monthlyServiceAdapter = new ServiceAdapter(mContext, false
				, R.layout.service_adapter, MainActivity.monthlyServiceList);
		ServiceAdapter rechargeServiceAdapter = new ServiceAdapter(mContext, false
				, R.layout.service_adapter, MainActivity.rechargeServiceList);
		
		singleServiceList.setAdapter(singleServiceAdapter);
		monthlyServiceList.setAdapter(monthlyServiceAdapter);
		rechargeServiceList.setAdapter(rechargeServiceAdapter);
		
		setListViewHeightBasedOnChildren(singleServiceList);
		setListViewHeightBasedOnChildren(monthlyServiceList);
		setListViewHeightBasedOnChildren(rechargeServiceList);
		
		singleServiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!mLocalSharePref.getLoginState()){
        			Intent intent = new Intent(mContext, LoginActivity.class);
        			startActivity(intent);
        		}else{
        			Intent intent = new Intent(mContext, CarInfoEditActivity.class);
        			intent.putExtra("service_type", MainActivity.singleServiceList.get(arg2).getFeeType());
        			intent.putExtra("server_cls", (Parcelable)MainActivity.singleServiceList.get(arg2));
        			startActivity(intent);
        		}
			}
		});
		
		monthlyServiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!mLocalSharePref.getLoginState()){
        			Intent intent = new Intent(mContext, LoginActivity.class);
        			startActivity(intent);
        		}else{
        			Intent intent = new Intent(mContext, CarInfoEditActivity.class);
        			intent.putExtra("service_type", "B");
        			intent.putExtra("server_cls", (Parcelable)MainActivity.monthlyServiceList.get(arg2));
        			startActivity(intent);
        		}
			}
		});
		
		rechargeServiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!mLocalSharePref.getLoginState()){
        			Intent intent = new Intent(mContext, LoginActivity.class);
        			startActivity(intent);
        		}else{
        			placeOrder(MainActivity.rechargeServiceList.get(arg2));
        		}
			}
		});
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
				getServiceCls();
				setServiceClsView();
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
	
	private void placeOrder(FeeData feeData){
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getPlaceOrder(mLocalSharePref.getUserId(), feeData.getFeeType(), mLocalSharePref.getUserName(), 0
						, 0, 0, "00", null, null
						, feeData.getFeeTypeMi(), feeData.getFee(), 1);

		mExecuter.execute(carRegister, mPlaceOrderRespHandler);

		dialogUtils.showProgress();
	}
	private void onReceivePlaceOrderResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			PlaceOrder.Response placeOrder = (PlaceOrder.Response) rsp;
			if (placeOrder.responseType.equals("N")) {
				onPlaceOrderSuccess(placeOrder.saleFee, placeOrder.orderId);
//				dialogUtils.showToast(placeOrder.errorMessage);
			} else {
				dialogUtils.showToast(placeOrder.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mPlaceOrderRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceivePlaceOrderResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	public void onPlaceOrderSuccess(int saleFee, long orderId){
		((XiwaoApplication)this.getActivity().getApplication()).setIfNeedRefreshOrder(true);
		try {	
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(orderId, saleFee);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(activity, mHandlerForAlipay);

					String result = alipay.pay(orderInfo);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandlerForAlipay.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(mContext, "连接服务器失败",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getNewOrderInfo(long orderId, int saleFee) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(orderId);
		sb.append("\"&subject=\"");
		sb.append("上海喜沃汽车服务有限公司充值服务");
		sb.append("\"&body=\"");
		sb.append("上海喜沃汽车服务有限公司充值服务");
		sb.append("\"&total_fee=\"");
		sb.append(StringUtils.getPriceStr(saleFee));
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://washmycar.sinaapp.com/washmycar/notifyurl.do"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
	
	private String getSignType() {
		return "sign_type=\"RSA\"";
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
	
	/**
	* 动态设置ListView的高度
	* @param listView
	*/
	public void setListViewHeightBasedOnChildren(ListView listView) { 
		 // 获取ListView对应的Adapter   
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);
	}
	
	Handler mHandlerForAlipay = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:

				result.parseResult();
				String message = result.getResult();
				if(result.resultCode.equals("9000")){
					message += "账户充值成功！您可到客户信息中查询！";
//					dialogUtils.showToast(message);
				}else{
					dialogUtils.showToast(message);
				}			
				break;
			case RQF_LOGIN: {
				Toast.makeText(mContext, result.getResult(),
						Toast.LENGTH_SHORT).show();
			}
				break;
			default:
				break;
			}
		};
	};
}
