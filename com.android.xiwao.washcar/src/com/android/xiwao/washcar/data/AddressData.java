package com.android.xiwao.washcar.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressData implements Parcelable{
	private long addressId;
	private String addressDetail;
	private long distractId;
	private long customerId;
	private String addressType;
	private String postCode;

	
	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public long getDistractId() {
		return distractId;
	}

	public void setDistractId(long distractId) {
		this.distractId = distractId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
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
		dest.writeString(this.addressType);
		dest.writeString(this.postCode);
		dest.writeLong(this.addressId);
		dest.writeLong(this.customerId);
		dest.writeLong(this.distractId);
	}
	
	public static final Parcelable.Creator<AddressData> CREATOR = new Parcelable.Creator<AddressData>(){

		public AddressData createFromParcel(Parcel source) {
			AddressData brief = new AddressData();
			brief.addressDetail = source.readString();
			brief.addressType = source.readString();
			brief.postCode = source.readString();
			brief.addressId = source.readLong();
			brief.customerId = source.readLong();
			brief.distractId = source.readLong();
			return brief;
		}

		public AddressData[] newArray(int size) {

			return new AddressData[size];
		}
		
	};
}
