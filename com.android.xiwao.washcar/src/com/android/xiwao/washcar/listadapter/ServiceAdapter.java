package com.android.xiwao.washcar.listadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.data.FeeData;
import com.android.xiwao.washcar.utils.StringUtils;

public class ServiceAdapter extends BaseAdapter {

	Context mContext;
	Boolean mInternetpic;	//判断是否为弹出框adapter
	public List<FeeData> mList;
	int mlayout;

	public ServiceAdapter(Context paramContext, Boolean paramBoolean,
			int paramInt, List<FeeData> mList) {
		mContext = paramContext;
		// mInflater = LayoutInflater.from(mContext);
		this.mList = mList;
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}

	public void addBriefs(List<FeeData> mList) {
		this.mList = mList;

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
		final int pos = position;
		ViewHolder viewHolder;
		viewHolder = new ViewHolder();
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.service_adapter, null);
		viewHolder.serviceDescribe = (TextView) convertView
				.findViewById(R.id.server_describe);
		viewHolder.servicePrice = (TextView) convertView
				.findViewById(R.id.server_price);
		viewHolder.serviceInfo = (TextView) convertView.findViewById(R.id.server_info);
		convertView.setTag(viewHolder);
		
		FeeData singleFeeData = mList.get(pos);
		viewHolder.serviceDescribe.setText(singleFeeData.getProductName());
		viewHolder.servicePrice.setText(StringUtils.getPriceIntStr(singleFeeData.getFee()) + "元");
		viewHolder.serviceInfo.setText(singleFeeData.getProductInfo());
		if(mInternetpic){
			viewHolder.servicePrice.setVisibility(View.GONE);
			if(singleFeeData.getProductInfo().equals("no info")){
				viewHolder.serviceInfo.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	static class ViewHolder {
		TextView serviceDescribe;
		TextView servicePrice;
		TextView serviceInfo;
	}
}
