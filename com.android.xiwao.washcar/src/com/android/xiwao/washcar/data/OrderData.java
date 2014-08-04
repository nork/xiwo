package com.android.xiwao.washcar.data;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderData implements Parcelable{
	private long orderId;
	private long custmerId;
	private String serviceType;
	private String mobileNum;
	private String carCode;
	private String addressDetail;
	private String createTime;
	private String payTime;
	private String washBegin;
	private String washEnd;
	private String OrderState;
	private String note;
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public long getCustmerId() {
		return custmerId;
	}
	public void setCustmerId(long custmerId) {
		this.custmerId = custmerId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getCarCode() {
		return carCode;
	}
	public void setCarCode(String carCode) {
		this.carCode = carCode;
	}
	public String getAddressDetail() {
		return addressDetail;
	}
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getWashBegin() {
		return washBegin;
	}
	public void setWashBegin(String washBegin) {
		this.washBegin = washBegin;
	}
	public String getWashEnd() {
		return washEnd;
	}
	public void setWashEnd(String washEnd) {
		this.washEnd = washEnd;
	}
	public String getOrderState() {
		return OrderState;
	}
	public void setOrderState(String orderState) {
		OrderState = orderState;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 此处注意下面两个方法中数据写入的顺序一定要相同，否则在传输时会出现数据错误问题
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.addressDetail);
		dest.writeString(this.carCode);
		dest.writeString(this.mobileNum);
		dest.writeString(this.note);
		dest.writeString(this.OrderState);
		dest.writeString(this.serviceType);
		dest.writeLong(this.custmerId);
		dest.writeLong(this.orderId);
		dest.writeString(this.createTime);
		dest.writeString(this.payTime);
		dest.writeString(this.washBegin);
		dest.writeString(this.washEnd);
	}
	
	public static final Parcelable.Creator<OrderData> CREATOR = new Parcelable.Creator<OrderData>(){

		public OrderData createFromParcel(Parcel source) {
			OrderData brief = new OrderData();
			brief.addressDetail = source.readString();
			brief.carCode = source.readString();
			brief.mobileNum = source.readString();
			brief.note = source.readString();
			brief.OrderState = source.readString();
			brief.serviceType = source.readString();
			brief.custmerId = source.readLong();
			brief.orderId = source.readLong();
			brief.createTime = source.readString();
			brief.payTime = source.readString();
			brief.washBegin = source.readString();
			brief.washEnd = source.readString();
			return brief;
		}

		public OrderData[] newArray(int size) {

			return new OrderData[size];
		}
		
	};
}
