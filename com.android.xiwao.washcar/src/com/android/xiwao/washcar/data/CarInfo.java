package com.android.xiwao.washcar.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车辆信息类
 * @author hpq
 *
 */
public class CarInfo implements Parcelable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long customerId;
	private long carId;
	private String carCode;
	private String carColor;
	private String carBrand;
	private String carType;
	private String carPic;
	private String regDate;
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getCarId() {
		return carId;
	}
	public void setCarId(long carId) {
		this.carId = carId;
	}
	public String getCarCode() {
		return carCode;
	}
	public void setCarCode(String carCode) {
		this.carCode = carCode;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getCarPic() {
		return carPic;
	}
	public void setCarPic(String carPic) {
		this.carPic = carPic;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
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
		dest.writeString(this.carBrand);
		dest.writeString(this.carCode);
		dest.writeString(this.carColor);
		dest.writeString(this.carPic);
		dest.writeString(this.carType);
		dest.writeString(this.regDate);
		dest.writeLong(this.carId);
		dest.writeLong(this.customerId);
	}
	
	public static final Parcelable.Creator<CarInfo> CREATOR = new Parcelable.Creator<CarInfo>(){

		public CarInfo createFromParcel(Parcel source) {
			CarInfo brief = new CarInfo();
			brief.carBrand = source.readString();
			brief.carCode = source.readString();
			brief.carColor = source.readString();
			brief.carPic = source.readString();
			brief.carType = source.readString();
			brief.regDate = source.readString();
			brief.carId = source.readLong();
			brief.customerId = source.readLong();
			return brief;
		}

		public CarInfo[] newArray(int size) {

			return new CarInfo[size];
		}
		
	};
}
