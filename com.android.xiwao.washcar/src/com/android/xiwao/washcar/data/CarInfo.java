package com.android.xiwao.washcar.data;

public class CarInfo {
	private String carNum;	//	车牌号
	private String carAddr;	//车地址
	private String washFlag;//洗车标志
	private String waxFlag;	//打蜡标志

	public String getCarAddr() {
		return carAddr;
	}

	public void setCarAddr(String carAddr) {
		this.carAddr = carAddr;
	}

	public String getWashFlag() {
		return washFlag;
	}

	public void setWashFlag(String washFlag) {
		this.washFlag = washFlag;
	}

	public String getWaxFlag() {
		return waxFlag;
	}

	public void setWaxFlag(String waxFlag) {
		this.waxFlag = waxFlag;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
}
