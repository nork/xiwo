package com.android.xiwao.washcar.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.data.OrderData;
import com.android.xiwao.washcar.utils.StringUtils;

public class OrderListAdapter extends BaseAdapter{
	Context mContext;
//	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<OrderData> mList;
	int mlayout;
	public OrderListAdapter(Context paramContext, Boolean paramBoolean, int paramInt){
		mContext = paramContext;
//		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<OrderData>();
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}
	
	public void refreshBriefs(List<OrderData> mList){
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	public void addBriefs(List<OrderData> mList){
		for(int i = 0; i<mList.size(); i++){
			this.mList.add(mList.get(i));
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0L;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.order_list_adapter, null);
			viewHolder.serialNumber = (TextView) convertView.findViewById(R.id.serial_number);
			viewHolder.money = (TextView) convertView.findViewById(R.id.money);
			viewHolder.payState = (TextView) convertView.findViewById(R.id.pay_state);
			viewHolder.carNum = (TextView) convertView.findViewById(R.id.car_num);
			viewHolder.serverType = (TextView) convertView.findViewById(R.id.server_type);
			viewHolder.orderDate = (TextView) convertView.findViewById(R.id.order_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		OrderData singleOrderData = this.mList.get(position);
		viewHolder.serialNumber.setText(Long.toString(singleOrderData.getOrderId()));
		
		String saleFeeStr = singleOrderData.getSaleFee();
		try{
			saleFeeStr = StringUtils.getPriceStr(Integer.parseInt(singleOrderData.getSaleFee()));
		}catch(Exception e){
			saleFeeStr = singleOrderData.getFee();
			e.printStackTrace();
		}
		
		viewHolder.money.setText(saleFeeStr);
		String orderState = singleOrderData.getOrderState();
		if(orderState.equals("01")){
			orderState = "未支付";
		}else if(orderState.equals("02")){
			orderState = "已支付";
		}else if(orderState.equals("03")){
			orderState = "服务中";
		}else if(orderState.equals("04")){
			orderState = "交易完成";
		}else if(orderState.equals("05")){
			orderState = "已取消";
		}
		viewHolder.payState.setText(orderState);
		viewHolder.orderDate.setText(singleOrderData.getCreateTime().substring(0, 10));
		viewHolder.carNum.setText(singleOrderData.getCarCode());
		viewHolder.serverType.setText(singleOrderData.getServiceType());	
		if(singleOrderData.getServiceType().equals("充值")){
			viewHolder.carNum.setVisibility(View.GONE);
		}else{
			viewHolder.carNum.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	static class ViewHolder{
		TextView serialNumber;
		TextView money;
		TextView payState;
		TextView serverType;
		TextView carNum;
		TextView orderDate;
	}
}
