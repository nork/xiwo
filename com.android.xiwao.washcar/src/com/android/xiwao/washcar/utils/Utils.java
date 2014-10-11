package com.android.xiwao.washcar.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.protocol.HttpContext;

import com.android.xiwao.washcar.update.VersionInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Utils {

	private static String virsioncode;
	private static String macadress;

	/**
	 * ªÒ»°∞Ê±æ∫≈
	 * 
	 * @param mcontext
	 * @return
	 */
	public static String getVersion(Context mcontext) {

		if (virsioncode == null) {

			try {
				virsioncode = mcontext.getPackageManager().getPackageInfo(
						VersionInfo.PACKAGE_NAME, 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return virsioncode;
	}

	public static String getMacAdress(Context context) {

		if (macadress == null) {
			WifiManager mWifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			macadress = mWifiManager.getConnectionInfo().getMacAddress();
		}
		// Log.i(TAG, "-----macadress="+macadress);
		return macadress;
	}
}
