package com.android.xiwao.washcar.update;

public class VersionInfo {
	public static final String PACKAGE_NAME = "com.android.xiwao.washcar";
	private int verCode;
	private String verName;
	private String url;
	private String apkName;
	private String updateFunction;
	public boolean force = false;
	
	public VersionInfo(){
		this.verCode = -1;
		this.verName = "";
		this.url = "";
		this.updateFunction = "";
		this.force = false;
	}

	public void setUpdateFunction(String function){
		this.updateFunction = function;
	}
	
	public String getUpdateFunction(){
		return this.updateFunction;
	}
	
	public void setVerCode(int i){
		verCode = i;
	}
	public void setVerName(String name){
		verName = name;
	}
	
	public void setApkName(String name)
	{
		this.apkName = name;
	}
	public String getApkName()
	{
		return this.apkName;
	}
	public void setURL(String u){
		url = u;
	}
	
	public int getVerCode(){
		return verCode;
	}
	public String getVerName(){
		return verName;
	}
	public String getURL(){
		return url;
	}
}
