package com.android.xiwao.washcar.data;

public class CarInfo {
	private String carNum;	//	���ƺ�
	private String carAddr;	//����ַ
	private String washFlag;//ϴ����־
	private String waxFlag;	//������־

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
