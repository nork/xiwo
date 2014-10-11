package com.android.xiwao.washcar.update;


import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.android.xiwao.washcar.utils.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.util.Log;

public class CustomerHttpClient {
	
//	private static String TAG = "CustomerHttpClient";
	private static Context mcontext;
	private static final String CHARSET = "HTTP.UTF_8";
	private static DefaultHttpClient customerHttpClient;

    private static final String USERAGENT =  "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1";
    
    static NetworkChangeReceiver mNetworkChangeReceiver = new NetworkChangeReceiver();
    private static boolean broadcast_resisted = false;
    private static boolean networkchanged = false;
    
    /**
     * ���HttpClient
     * @param context
     * @return HttpClient
     */
	public static synchronized DefaultHttpClient getHttpClient(Context context) {
		
		mcontext = context;
//		registNetChangeBroadcast();
	
		if (null == customerHttpClient  || networkchanged) {
			
			HttpParams params = new BasicHttpParams();
			
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);//
			HttpProtocolParams.setUserAgent(params, USERAGENT);  //			
			
			ConnManagerParams.setTimeout(params, 15000); //			
			HttpConnectionParams.setConnectionTimeout(params, 15000);		
			HttpConnectionParams.setSoTimeout(params, 15000);

		
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);  //new DefaultHttpClient(); // 
			customerHttpClient.getParams().setParameter("netmac", Utils.getMacAdress(mcontext));
			customerHttpClient.getParams().setParameter("version", Utils.getVersion(mcontext));
			//���֤��
			enableSSL(customerHttpClient);
			

			
//			String host=Proxy.getDefaultHost();//�˴�ProxyԴ��android.net   
//			int port = Proxy.getDefaultPort();//ͬ��   
//			Log.i(TAG, "-----------host="+host+"------port="+port);    //Proxy��10.0.0.200 port��80  
			
			ConnectivityManager conManager= (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo info = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
			String currentAPN = info.getExtraInfo(); 
//			Log.i(TAG, "-----     ConnectivityManager ------currentAPN="+currentAPN);  
			
			if(!isWifiConnected(mcontext)){
				
				//���ô���  
				if("ctwap".equalsIgnoreCase(currentAPN)){  //����
					HttpHost httpHost = new HttpHost("10.0.0.200", 80);  			 
					customerHttpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,  httpHost); 
					Credentials defaultcreds = new UsernamePasswordCredentials("ctwap@mycdma.cn", "vnet.mobi");
					( (DefaultHttpClient) customerHttpClient).getCredentialsProvider().setCredentials(new AuthScope("10.0.0.200", 80, AuthScope.ANY_REALM), defaultcreds);
				}else if("3gwap".equalsIgnoreCase(currentAPN)){  //��ͨ
					HttpHost httpHost = new HttpHost("10.0.0.172", 80);  	
					customerHttpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,  httpHost); 
				}else if("cmwap".equalsIgnoreCase(currentAPN)){  //�ƶ�
					HttpHost httpHost = new HttpHost("10.0.0.172", 80);  			 
					customerHttpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,  httpHost); 
				}
			}else{
//				Log.i(TAG, "----------isWifiConnected-------- ");
			}
					
		}

		
           networkchanged= false;
		return customerHttpClient;
	}
	
	/**
	 * �ж�WIFI�Ƿ�����
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {

		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			
			if (mWiFiNetworkInfo != null) {  
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	



	/**
	 * ע��㲥���ж������Ƿ�ı�
	 */
	private static void registNetChangeBroadcast(){
		
		if(!broadcast_resisted){
			IntentFilter intentfilter = new IntentFilter();
			intentfilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			mcontext.registerReceiver(mNetworkChangeReceiver, intentfilter);
			broadcast_resisted = true;
		}
	}
	
	    public static class NetworkChangeReceiver extends BroadcastReceiver { 
	    	
	        public void onReceive(Context context, Intent intent) { 
	        	
	            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) { 
	            	 networkchanged= true;
//	            	 Log.i(TAG, "-------------------------"+intent.getAction());
	            } 
	        } 
	    } 

	

	/**
	 * �����֤
	 * @param httpclient
	 */
     public static void enableSSL(HttpClient httpclient) {
		
		try {

			Scheme sche = new Scheme("https",new EasySSLSocketFactory(mcontext), 443);
				
			//��ӡkeys
//			KeyStore ks = KeyStore.getInstance("PKCS12"); //
//			InputStream fis = mcontext.getResources().openRawResource(R.raw.client);  //mcontext.getAssets().open("client.p12");
//			char[] pwd = "EIWnsA1n6tGs".toCharArray();
//			ks.load(fis, pwd);
//			fis.close();
//			
//			Enumeration enume = ks.aliases();
//			String keyAlias = null;
//			if (enume.hasMoreElements()) {
//			 keyAlias = (String) enume.nextElement();
//			}
//			PrivateKey pk = (PrivateKey) ks.getKey(keyAlias, pwd);
//			Log.i("test", "-------"+pk);

			httpclient.getConnectionManager().getSchemeRegistry().register(sche);
			           
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	}
	

}
