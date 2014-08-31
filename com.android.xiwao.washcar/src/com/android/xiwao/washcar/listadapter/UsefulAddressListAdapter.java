package com.android.xiwao.washcar.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.AddressData;
import com.android.xiwao.washcar.data.WebSiteData;
import com.android.xiwao.washcar.listadapter.CarInfoListAdapter.onRightItemClickListener;

public class UsefulAddressListAdapter extends BaseAdapter{

	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<AddressData> mList;
	public List<WebSiteData> websitList;
	private int mRightWidth = 0;
	int mlayout;
	public UsefulAddressListAdapter(Context paramContext, Boolean paramBoolean, int paramInt, List<WebSiteData> websitList, int rightWidth){
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<AddressData>();
		mInternetpic = paramBoolean;
		mlayout = paramInt;
		this.websitList = websitList;
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
			convertView.setTag(viewHolder);
//		}else{
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		
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
//		viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.putExtra("choice_address", (Parcelable)mList.get(position));
//				intent.putExtra("branch_name", branchNameforClick);
//				((Activity)mContext).setResult(Activity.RESULT_OK, intent);
//				((Activity)mContext).finish();
//			}
//		});

		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
		viewHolder.itemLeft.setLayoutParams(lp1);
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth / 2, LayoutParams.MATCH_PARENT);      
        viewHolder.itemRightSure.setLayoutParams(lp2);   
        viewHolder.itemRightDelete.setLayoutParams(lp2);
        
        viewHolder.itemRightDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position, 1, null);
                }
            }
        });
        
        viewHolder.itemRightSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position, 2, branchNameforClick);
                }
            }
        });
        final View view = convertView;
        viewHolder.editBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				view.scrollTo(400, 0);
				if (mListener != null) {
					mListener.onRightItemClick(view, position, 3, null);
				}
			}
		});
        
		return convertView;
	}

	static class ViewHolder{
		TextView websiteName;
		TextView addressDetail;
		Button editBtn;
		
		RelativeLayout itemLeft;
    	RelativeLayout itemRightDelete;
    	RelativeLayout itemRightSure;
	}
	
	/**
     * µ¥»÷ÊÂ¼þ¼àÌýÆ÷
     */
    private onRightItemClickListener mListener = null;
    
    public void setOnRightItemClickListener(onRightItemClickListener listener){
    	mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position, int option, String branchName);
    }
}
