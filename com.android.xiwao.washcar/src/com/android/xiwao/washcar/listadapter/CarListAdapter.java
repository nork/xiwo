package com.android.xiwao.washcar.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.ui.AddCarActivity;
import com.android.xiwao.washcar.ui.ModifyCarActivity;

public class CarListAdapter extends BaseAdapter{
	Context mContext;
//	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<CarInfo> mList;	
	int mlayout;
	public CarListAdapter(Context paramContext, Boolean paramBoolean, int paramInt){
		mContext = paramContext;
//		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<CarInfo>();
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}
	
	public void addBriefs(List<CarInfo> mList){
		this.mList = mList;
		
		notifyDataSetChanged();
	}
	
	public void changeListData(List<CarInfo> mList) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int pos = position;
		ViewHolder viewHolder;
//		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.car_info_list_adapter, null);
			viewHolder.carImg = (ImageView) convertView.findViewById(R.id.car_img);
			viewHolder.carInfo = (RelativeLayout) convertView.findViewById(R.id.car_info);
			viewHolder.carNumTitle = (TextView) convertView.findViewById(R.id.car_num_title);
			viewHolder.carAddr = (TextView) convertView.findViewById(R.id.car_addr);
			viewHolder.money = (TextView) convertView.findViewById(R.id.money);
			viewHolder.addCar = (TextView) convertView.findViewById(R.id.add_car);
			viewHolder.carNum = (TextView) convertView.findViewById(R.id.car_num);
			viewHolder.editBtn = (Button) convertView.findViewById(R.id.edit_btn);
			
			viewHolder.itemLeft = (RelativeLayout)convertView.findViewById(R.id.item_left);
			viewHolder.itemRightDelete = (RelativeLayout)convertView.findViewById(R.id.item_right_delete);
			viewHolder.itemRightWash = (RelativeLayout)convertView.findViewById(R.id.item_right_wash);
			convertView.setTag(viewHolder);
//		}else{
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		
		CarInfo singleCarInfo = this.mList.get(position);
		viewHolder.carNum.setText(singleCarInfo.getCarCode());
//		viewHolder.carAddr.setText(singleCarInfo.getCarAddr());
		
		if(singleCarInfo.getCarCode().equals("-1")){
			viewHolder.carImg.setVisibility(View.GONE);
			viewHolder.carInfo.setVisibility(View.GONE);
			viewHolder.money.setVisibility(View.GONE);
			viewHolder.addCar.setVisibility(View.VISIBLE);
			viewHolder.editBtn.setVisibility(View.GONE);
		}
	
		
		viewHolder.addCar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, AddCarActivity.class);
				((Activity)mContext).startActivityForResult(i, Constants.ADD_CAR_RESULT_CODE);
			}
		});
		
		viewHolder.money.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, ModifyCarActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(Constants.CAR_INFO_SEND, mList.get(pos));
				i.putExtras(bundle);
				((Activity)mContext).startActivityForResult(i, Constants.MODIFY_CAR_RESULT_CODE);
			}
		});
        
		return convertView;
	}
	
	static class ViewHolder{
		ImageView carImg;
		RelativeLayout carInfo;
		TextView carNumTitle;
		TextView washBtn;
		TextView waxBtn;
		TextView carAddr;
		TextView money;
		TextView addCar;
		TextView carNum;
		Button editBtn;
		
		RelativeLayout itemLeft;
    	RelativeLayout itemRightDelete;
    	RelativeLayout itemRightWash;
	}
	
	/**
     * µ¥»÷ÊÂ¼þ¼àÌýÆ÷
     */
    private onRightItemClickListener mListener = null;
    
    public void setOnRightItemClickListener(onRightItemClickListener listener){
    	mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position, int option);
    }

}
