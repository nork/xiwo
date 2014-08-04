package com.android.xiwao.washcar.data;

import android.os.Parcel;
import android.os.Parcelable;

public class WebSiteData implements Parcelable{

	private long distractId;
	private String branchName;
	private String province;
	private String city;
	private String distract;
	private String street;
	private String map;
	
	
	public long getDistractId() {
		return distractId;
	}

	public void setDistractId(long distractId) {
		this.distractId = distractId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistract() {
		return distract;
	}

	public void setDistract(String distract) {
		this.distract = distract;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.branchName);
		dest.writeString(this.city);
		dest.writeString(this.distract);
		dest.writeString(this.map);
		dest.writeString(this.province);
		dest.writeString(this.street);
		dest.writeLong(this.distractId);
	}
	
	public static final Parcelable.Creator<WebSiteData> CREATOR = new Parcelable.Creator<WebSiteData>(){

		public WebSiteData createFromParcel(Parcel source) {
			WebSiteData brief = new WebSiteData();
			brief.branchName = source.readString();
			brief.city = source.readString();
			brief.distract = source.readString();
			brief.map = source.readString();
			brief.province = source.readString();
			brief.street = source.readString();
			brief.distractId = source.readLong();
			return brief;
		}

		public WebSiteData[] newArray(int size) {

			return new WebSiteData[size];
		}
		
	};

}
