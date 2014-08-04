package com.android.xiwao.washcar.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.data.WebSiteData;

public class UsefulAddressListAdapter extends BaseAdapter{

	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<WebSiteData> mList;
	int mlayout;
	public UsefulAddressListAdapter(Context paramContext, Boolean paramBoolean, int paramInt){
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<WebSiteData>();
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}
	
	public void addBriefs(List<WebSiteData> mList){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.useful_address_adapter, null);
			viewHolder.websiteName = (TextView) convertView.findViewById(R.id.website_name);
			viewHolder.addressDetail = (TextView) convertView.findViewById(R.id.address_detail);
			viewHolder.editBtn = (Button) convertView.findViewById(R.id.edit_btn);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		WebSiteData singleOrderData = this.mList.get(position);
		viewHolder.websiteName.setText(singleOrderData.getBranchName());
		viewHolder.addressDetail.setText(singleOrderData.getStreet());
		
		return convertView;
	}

	static class ViewHolder{
		TextView websiteName;
		TextView addressDetail;
		Button editBtn;
	}
}
