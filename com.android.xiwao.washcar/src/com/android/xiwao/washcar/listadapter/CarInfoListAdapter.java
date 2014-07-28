package com.android.xiwao.washcar.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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
import com.android.xiwao.washcar.ui.AddCarActivity;

public class CarInfoListAdapter extends BaseAdapter{

	private static String TAG = "CarInfoListAdapter";
	Context mContext;
	private LayoutInflater mInflater;
	Boolean mInternetpic;
	public List<CarInfo> mList;
	int mlayout;
	public CarInfoListAdapter(Context paramContext, Boolean paramBoolean, int paramInt){
		mContext = paramContext;
		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<CarInfo>();
		mInternetpic = paramBoolean;
		mlayout = paramInt;
	}
	
	public void addBriefs(List<CarInfo> mList){
		for(int i = 0; i < mList.size() || i>=5; i++){
			this.mList.add(mList.get(i));
		}
		/*
		 * 此处设置一个添加按钮，将洗车标记为-1时， 默认加载添加按钮
		 */
		CarInfo last = new CarInfo();	
		last.setCarCode("-1");
		this.mList.add(last);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.car_info_list_adapter, null);
			viewHolder.carImg = (ImageView) convertView.findViewById(R.id.car_img);
			viewHolder.carInfo = (RelativeLayout) convertView.findViewById(R.id.car_info);
			viewHolder.carNumTitle = (TextView) convertView.findViewById(R.id.car_num_title);
			viewHolder.washBtn = (TextView) convertView.findViewById(R.id.wash_button);
			viewHolder.waxBtn = (TextView) convertView.findViewById(R.id.wax_button);
			viewHolder.carAddr = (TextView) convertView.findViewById(R.id.car_addr);
			viewHolder.money = (TextView) convertView.findViewById(R.id.money);
			viewHolder.addCar = (TextView) convertView.findViewById(R.id.add_car);
			viewHolder.carNum = (TextView) convertView.findViewById(R.id.car_num);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		CarInfo singleCarInfo = this.mList.get(position);
		viewHolder.carNum.setText(singleCarInfo.getCarCode());
//		viewHolder.carAddr.setText(singleCarInfo.getCarAddr());
		
		if(singleCarInfo.getCarCode().equals("-1")){
			viewHolder.carImg.setVisibility(View.GONE);
			viewHolder.carInfo.setVisibility(View.GONE);
			viewHolder.money.setVisibility(View.GONE);
			viewHolder.addCar.setVisibility(View.VISIBLE);
		}
		
		viewHolder.addCar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, AddCarActivity.class);
				mContext.startActivity(i);
			}
		});
		
		AbsListView.LayoutParams params;			
		params = new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		convertView.setLayoutParams(params);
		
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
	}

}
