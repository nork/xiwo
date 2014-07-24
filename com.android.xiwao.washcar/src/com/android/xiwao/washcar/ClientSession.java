package com.android.xiwao.washcar;

import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import com.android.xiwao.washcar.httpconnection.CommandFactory;
import com.android.xiwao.washcar.httpconnection.DefaultCommandFactory;

public class ClientSession {
	
	private static final String TAG = "ClientSession";
	
	// create mock command objects for ui demo
	public static boolean sMockConnection = false; 

	private static ClientSession sIntance;

	private CommandFactory mDefaultCmdFactory;
	
	private List<Cookie> mSessionCookies;
	
	private long mUserId;
	private String diverStatus = null;
	
	private ClientSession() {
		mDefaultCmdFactory = new DefaultCommandFactory();
	}
	
	public static ClientSession getInstance(){
		if(sIntance == null){
			sIntance = new ClientSession();
		}	
		return sIntance; 
	}
	
	public void setSessionCookies(List<Cookie> cookies){
		mSessionCookies = cookies;
	}
	
	private static final String USER_ID_COOKIE_NAME = "user_id";
			
	public CookieStore getChallenge(){
		
		if(mSessionCookies == null){
			return null;
		}
		
		CookieStore cookieStore = new BasicCookieStore();
		Date date =  new Date(System.currentTimeMillis());
		
		boolean isUserIdCookieFound = false;
		
		for(Cookie c : mSessionCookies){
			if(c.isExpired(date)){
				// sessoin expired
				mSessionCookies = null;
				return null;
			}
			
			if(USER_ID_COOKIE_NAME.equals(c.getName())){
				isUserIdCookieFound = true;
			}
			
			cookieStore.addCookie(c);
		}
		
		if(isUserIdCookieFound == false){
			// user id cookie expired
			return null;
		}
		
		if(cookieStore.getCookies().size() == 0){
			return null;
		}
		
		return cookieStore;
	}
	
	public void setUserId(long id){
		mUserId = id;
	}
	
	public long getUserId(){		 
		return mUserId;
	}
	
	public void setDiverStatus(String status){
		diverStatus = status;
	}
	
	public String getDiverStatus(){
		return diverStatus;
	}
	
	public CommandFactory getCmdFactory(){
		return mDefaultCmdFactory;
	}
	
}
