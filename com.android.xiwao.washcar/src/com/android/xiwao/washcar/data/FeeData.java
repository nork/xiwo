package com.android.xiwao.washcar.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class FeeData implements Parcelable, Serializable{

	private static final long serialVersionUID = 1L;
	private long feeId;
	private String feeType;
	private int fee;

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
	}
	
	public static final Parcelable.Creator<FeeData> CREATOR = new Parcelable.Creator<FeeData>(){

		public FeeData createFromParcel(Parcel source) {
			FeeData brief = new FeeData();
			brief.feeId = source.readLong();
			brief.feeType = source.readString();
			brief.fee = source.readInt();
			return brief;
		}

		public FeeData[] newArray(int size) {

			return new FeeData[size];
		}
		
	};
}
