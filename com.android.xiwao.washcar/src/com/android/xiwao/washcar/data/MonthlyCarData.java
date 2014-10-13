package com.android.xiwao.washcar.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 包月车辆信息
 * @author Administrator
 *
 */
public class MonthlyCarData implements Parcelable, Serializable{
	private static final long serialVersionUID = 1L;
	private int carId;
	private String carCode;
	private String carColor;
	private String carBrand;
	private int customerId;
	private String startDate;
	private String endDate;
	private int distractFirstId;	//包月网点ID
	private int buffTimes;
	private int washTimes;
	private int interiorTimes;
	private String finalDate;
	private String carPic;
	
	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
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

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getDistractFirstId() {
		return distractFirstId;
	}

	public void setDistractFirstId(int distractFirstId) {
		this.distractFirstId = distractFirstId;
	}

	public int getBuffTimes() {
		return buffTimes;
	}

	public void setBuffTimes(int buffTimes) {
		this.buffTimes = buffTimes;
	}

	public int getWashTimes() {
		return washTimes;
	}

	public void setWashTimes(int washTimes) {
		this.washTimes = washTimes;
	}

	public int getInteriorTimes() {
		return interiorTimes;
	}

	public void setInteriorTimes(int interiorTimes) {
		this.interiorTimes = interiorTimes;
	}

	public String getCarPic() {
		return carPic;
	}

	public void setCarPic(String carPic) {
		this.carPic = carPic;
	}

	public String getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
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
		dest.writeInt(this.carId);
		dest.writeString(this.carCode);
		dest.writeString(this.carColor);
		dest.writeString(this.carBrand);
		dest.writeInt(this.customerId);
		dest.writeString(this.startDate);
		dest.writeString(this.endDate);
		dest.writeInt(this.distractFirstId);
		dest.writeInt(this.buffTimes);
		dest.writeInt(this.washTimes);
		dest.writeInt(this.interiorTimes);
		dest.writeString(this.carPic);
		dest.writeString(this.finalDate);
	}
	
	public static final Parcelable.Creator<MonthlyCarData> CREATOR = new Parcelable.Creator<MonthlyCarData>(){

		public MonthlyCarData createFromParcel(Parcel source) {
			MonthlyCarData brief = new MonthlyCarData();
			brief.carId = source.readInt();
			brief.carCode = source.readString();
			brief.carColor = source.readString();
			brief.carBrand = source.readString();
			brief.customerId = source.readInt();
			brief.startDate = source.readString();
			brief.endDate = source.readString();
			brief.distractFirstId = source.readInt();
			brief.buffTimes = source.readInt();
			brief.washTimes = source.readInt();
			brief.interiorTimes = source.readInt();
			brief.carPic = source.readString();
			brief.finalDate = source.readString();
			return brief;
		}

		public MonthlyCarData[] newArray(int size) {

			return new MonthlyCarData[size];
		}
		
	};

}
