package com.android.xiwao.washcar.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.AddressData;
import com.android.xiwao.washcar.ui.AddAddressActivity;

public class UsefulAddressListAdapter extends BaseAdapter{

	Context mContext;
//	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<AddressData> mList;
	private int mRightWidth = 0;
	int mlayout;
	public UsefulAddressListAdapter(Context paramContext, Boolean paramBoolean, int paramInt, int rightWidth){
		mContext = paramContext;
//		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<AddressData>();
		mInternetpic = paramBoolean;
		mlayout = paramInt;
		mRightWidth = rightWidth;
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
//		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.address_list_adapter, null);
			viewHolder.websiteName = (TextView) convertView.findViewById(R.id.website_name);
			viewHolder.addressDetail = (TextView) convertView.findViewById(R.id.address_detail);
			viewHolder.editBtn = (Button) convertView.findViewById(R.id.default_edit_btn);
			viewHolder.itemLeft = (RelativeLayout)convertView.findViewById(R.id.item_left);
			viewHolder.itemRightDelete = (RelativeLayout)convertView.findViewById(R.id.item_right_delete);
			viewHolder.itemRightSure = (RelativeLayout)convertView.findViewById(R.id.item_right_wash);
			viewHolder.addAddress = (TextView) convertView.findViewById(R.id.add_address);
			convertView.setTag(viewHolder);
//		}else{
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		
		AddressData singleOrderData = this.mList.get(position);
//		String branchName = "";
//		for(WebSiteData websitData : websitList){
//			if(websitData.getDistractId() == singleOrderData.getDistractId()){
//				branchName = websitData.getBranchName();
//				break;
//			}
//		}
//		final String branchNameforClick = branchName;
		if(singleOrderData.getAddressId() == -1){
			viewHolder.websiteName.setVisibility(View.GONE);
			viewHolder.addressDetail.setVisibility(View.GONE);
			viewHolder.addAddress.setVisibility(View.VISIBLE);
			viewHolder.itemLeft.setBackgroundResource(R.drawable.xu_line_box);
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(mList.size() > 5){
						Toast.makeText(mContext, "常用地址信息不能超过5条，请删除后再添加！", Toast.LENGTH_LONG).show();
						return;
					}
					Intent intent = new Intent(mContext, AddAddressActivity.class);
					((Activity)mContext).startActivityForResult(intent, Constants.ADD_ADDRESS_RESULT_CODE);
				}
			});
		}else{
			viewHolder.websiteName.setText(singleOrderData.getBranchName());
			viewHolder.addressDetail.setText(singleOrderData.getAddressDetail());

	        convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mListener.onRightItemClick(arg0, position, 2);
				}
			});
		}
		int displayHeight = ((XiwaoApplication)((Activity)mContext).getApplication()).getDisplayHeight();
		
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int)(displayHeight * 0.1f + 0.5f));
		viewHolder.itemLeft.setLayoutParams(lp1);
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, (int)(displayHeight * 0.1f + 0.5f));      
        viewHolder.itemRightSure.setLayoutParams(lp2);   
        viewHolder.itemRightDelete.setLayoutParams(lp2);
        viewHolder.itemRightSure.setVisibility(View.GONE);
        
        viewHolder.itemRightDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position, 1);
                }
            }
        });
        
        viewHolder.itemRightSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position, 2);
                }
            }
        });
       
		return convertView;
	}

	static class ViewHolder{
		TextView websiteName;
		TextView addressDetail;
		TextView addAddress;
		Button editBtn;
		
		RelativeLayout itemLeft;
    	RelativeLayout itemRightDelete;
    	RelativeLayout itemRightSure;
	}
	
	/**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;
    
    public void setOnRightItemClickListener(onRightItemClickListener listener){
    	mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position, int option);
    }
}
