package com.android.xiwao.washcar.httpconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends BaseCommand {
	private static final String LOGIN_CMD = "m=Api&a=dologin";
	
	private static final String PARAM_USER = "username";
	private static final String PARAM_PASSWD = "passwd";
	
	public static final String JSON_ID = "id";
	public static final String JSON_USERNAME = "username";
	public static final String JSON_FACE = "face";
	public static final String JSON_DEFAULT_START = "default_start";
	public static final String JSON_DEFAULT_DEST = "default_dest";
	public static final String JSON_COMPANY = "company";
	public static final String JSON_COM_ADDR = "com_addr";
	public static final String JSON_COM_CONTACT = "com_contact";
	public static final String JSON_COM_PHONE = "com_phone";
	public static final String JSON_PERSON_ID = "person_id";
	public static final String JSON_PERSON_ADDR = "person_addr";
	public static final String JSON_BYBROWSE = "bybrowse";
	public static final String JSON_IS_RECOMMEND = "is_recommend";
	public static final String JSON_RECOMTIME = "recomtime";
	public static final String JSON_IS_DISABLE = "is_disable";
	public static final String JSON_ADMIN = "admin";
	public static final String JSON_TIME = "time";	
	public static final String JSON_ISSUC = "issuc";
	public static final String JSON_STATUS = "state";
	
	private String mUser;
	private String mPasswd;
	
	public static class Response extends BaseResponse {

		public static int ISSUC_FAILED = 0;
		public static int ISSUC_SUCC = 1;
		
		public long id;
		public String name;
		public String face;
		public String default_start;
		public String default_dest;
		public String company;
		public String com_addr;
		public String com_contact;
		public String com_phone;
		public String person_id;
		public String person_addr;
		public int bybrowse;
		public int is_recommend;
		public String recomtime;
		public int is_disable;
		public int admin;
		public String  time;
		public int  issuc;
		public String state;
	}
		
	public Login() {
		super();
	}
	
	public void setUser(String user){
		mUser = user;
	}
	
	public void setPasswd(String passwd){
		mPasswd = passwd;
	}


	@Override
	protected List<NameValuePair> getParameters() {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add( new BasicNameValuePair(PARAM_USER, mUser));
		nvps.add( new BasicNameValuePair(PARAM_PASSWD, mPasswd));
	
		return nvps;
	}

	@Override
	protected BaseResponse parseResponse(String content) {
		Response login = new Response();
		
		try {
			JSONObject jsonObj = new JSONObject(content);
			login.issuc = jsonObj.getInt(JSON_ISSUC);
			login.admin = jsonObj.getInt(JSON_ADMIN);
			login.okey = true;
			
			login.id = jsonObj.getLong(JSON_ID);
			//login.name = jsonObj.getString(JSON_USERNAME);
			//login.face = jsonObj.getString(JSON_FACE);
			//login.default_start = jsonObj.getString(JSON_DEFAULT_START);
			//login.default_dest = jsonObj.getString(JSON_DEFAULT_DEST);
			//login.company = jsonObj.getString(JSON_COMPANY);
			//login.com_addr = jsonObj.getString(JSON_COM_ADDR);
			//login.com_contact = jsonObj.getString(JSON_COM_CONTACT);
			login.com_phone = jsonObj.getString(JSON_COM_PHONE);
			//login.person_id = jsonObj.getString(JSON_PERSON_ID);
			//login.person_addr = jsonObj.getString(JSON_PERSON_ADDR);
			//login.bybrowse = jsonObj.getInt(JSON_BYBROWSE);
			//login.is_recommend = jsonObj.getInt(JSON_IS_RECOMMEND);
			//login.recomtime = jsonObj.getString(JSON_RECOMTIME);
			//login.is_disable = jsonObj.getInt(JSON_IS_DISABLE);
			//login.time = jsonObj.getString(JSON_TIME);			
			//login.state = jsonObj.getString(JSON_STATUS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return login;
	}

	@Override
	protected String getComand() {
		return LOGIN_CMD;
	}
}
