package com.android.xiwao.washcar.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class FeeData implements Parcelable, Serializable{

	private static final long serialVersionUID = 1L;
	private long feeId;
	private String feeType;//服务大类
	private int fee;	//服务费用
	private String feeTypeMi; //服务小类
	private int productId;	//产品ID
	private String productName;	//产品名称
	private String productInfo; //产品描述

	public long getFeeId() {
		return feeId;
	}

	public void setFeeId(long feeId) {
		this.feeId = feeId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}	

	public String getFeeTypeMi() {
		return feeTypeMi;
	}

	public void setFeeTypeMi(String feeTypeMi) {
		this.feeTypeMi = feeTypeMi;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
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
		dest.writeLong(this.feeId);
		dest.writeString(this.feeType);
		dest.writeInt(this.fee);
		dest.writeInt(this.productId);
		dest.writeString(this.feeTypeMi);
		dest.writeString(this.productName);
		dest.writeString(this.productInfo);
	}
	
	public static final Parcelable.Creator<FeeData> CREATOR = new Parcelable.Creator<FeeData>(){

		public FeeData createFromParcel(Parcel source) {
			FeeData brief = new FeeData();
			brief.feeId = source.readLong();
			brief.feeType = source.readString();
			brief.fee = source.readInt();
			brief.productId = source.readInt();
			brief.feeTypeMi = source.readString();
			brief.productName = source.readString();
			brief.productInfo = source.readString();
			return brief;
		}

		public FeeData[] newArray(int size) {

			return new FeeData[size];
		}
		
	};
}
