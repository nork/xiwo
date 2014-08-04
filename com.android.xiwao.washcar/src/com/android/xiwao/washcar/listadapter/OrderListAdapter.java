package com.android.xiwao.washcar.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.data.OrderData;
import com.android.xiwao.washcar.listadapter.CarInfoListAdapter.ViewHolder;

public class OrderListAdapter extends BaseAdapter{

	private static String TAG = "CarInfoListAdapter";
	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<OrderData> mList;
	int mlayout;
	public OrderListAdapter(Context paramContext, Boolean paramBoolean, int paramInt){
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<OrderData>();
		mInternetpic = paramBoolean;
		mlayout = paramInt;
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
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		OrderData singleOrderData = this.mList.get(position);
		viewHolder.serialNumber.setText(Long.toString(singleOrderData.getOrderId()));
//		viewHolder.money.setText(singleOrderData.getMoney());
		viewHolder.payState.setText(singleOrderData.getOrderState());
		
//		AbsListView.LayoutParams params;			
//		params = new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, (int)(Constants.displayHeight * 0.1f + 0.5f));
//		convertView.setLayoutParams(params);
		
		return convertView;
	}

	static class ViewHolder{
		TextView serialNumber;
		TextView money;
		TextView payState;
	}
}
