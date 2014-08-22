package com.android.xiwao.washcar.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.data.AddressData;
import com.android.xiwao.washcar.data.WebSiteData;

public class UsefulAddressListAdapter extends BaseAdapter{

	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<AddressData> mList;
	public List<WebSiteData> websitList;
	int mlayout;
	public UsefulAddressListAdapter(Context paramContext, Boolean paramBoolean, int paramInt, List<WebSiteData> websitList){
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<AddressData>();
		mInternetpic = paramBoolean;
		mlayout = paramInt;
		this.websitList = websitList;
	}
	
	public void addBriefs(List<AddressData> mList){
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.address_list_adapter, null);
			viewHolder.websiteName = (TextView) convertView.findViewById(R.id.website_name);
			viewHolder.addressDetail = (TextView) convertView.findViewById(R.id.address_detail);
			viewHolder.editBtn = (Button) convertView.findViewById(R.id.default_edit_btn);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		AddressData singleOrderData = this.mList.get(position);
		String branchName = "";
		for(WebSiteData websitData : websitList){
			if(websitData.getDistractId() == singleOrderData.getDistractId()){
				branchName = websitData.getBranchName();
				break;
			}
		}
		final String branchNameforClick = branchName;
		viewHolder.websiteName.setText(branchName);
		viewHolder.addressDetail.setText(singleOrderData.getAddressDetail());
		viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("choice_address", mList.get(position));
				intent.putExtra("branch_name", branchNameforClick);
				((Activity)mContext).setResult(Activity.RESULT_OK, intent);
				((Activity)mContext).finish();
			}
		});
		return convertView;
	}

	static class ViewHolder{
		TextView websiteName;
		TextView addressDetail;
		Button editBtn;
	}
}
