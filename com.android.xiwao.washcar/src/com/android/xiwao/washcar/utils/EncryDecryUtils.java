package com.android.xiwao.washcar.utils;
/**
 * ����ӽ��ܹ�����
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryDecryUtils {
	public final static String str2Md5(String s)
	{
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		// char decDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7'};
		try{ 
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp); byte[] md = mdTemp.digest();
			int j = md.length; char str[] = new char[j * 3]; 
			int k = 0; 
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
				str[k++] = hexDigits[byte0 & 3];
				// str[k++] = decDigits[byte0 >>>5 &7]; 
				// str[k++] = decDigits[byte0 >>>2&7];
				// str[k++] = decDigits[byte0 &3]; 
			} 
			return new String(str); 
		}
		catch (Exception e){ 
			e.printStackTrace(); 
			return null; 
			}
	}
	

	public static String encryptToMD5(String info) {
		byte[] digesta = null;
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			alga.update(info.getBytes());
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String rs = byte2hex(digesta);
		return rs;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
	
	
	/**   
	 * �ַ���ת����ʮ�������ַ���  
	 * @param String str ��ת����ASCII�ַ���  
	 * @return String ÿ��Byte֮��ո�ָ�����: [61 6C 6B]  
	 */      
	public static String str2HexStr(String str)    
	{      
	  
	    char[] chars = "0123456789ABCDEF".toCharArray();      
	    StringBuilder sb = new StringBuilder("");    
	    byte[] bs = str.getBytes();      
	    int bit;      
	        
	    for (int i = 0; i < bs.length; i++)    
	    {      
	        bit = (bs[i] & 0x0f0) >> 4;      
	        sb.append(chars[bit]);      
	        bit = bs[i] & 0x0f;      
	        sb.append(chars[bit]);    
	        sb.append(' ');    
	    }      
	    return sb.toString().trim();      
	}    
	    
	/**   
	 * ʮ������ת���ַ���  
	 * @param String str Byte�ַ���(Byte֮���޷ָ��� ��:[616C6B])  
	 * @return String ��Ӧ���ַ���  
	 */      
	public static String hexStr2Str(String hexStr)    
	{      
	    String str = "0123456789ABCDEF";      
	    char[] hexs = hexStr.toCharArray();      
	    byte[] bytes = new byte[hexStr.length() / 2];      
	    int n;      
	  
	    for (int i = 0; i < bytes.length; i++)    
	    {      
	        n = str.indexOf(hexs[2 * i]) * 16;      
	        n += str.indexOf(hexs[2 * i + 1]);      
	        bytes[i] = (byte) (n & 0xff);      
	    }      
	    return new String(bytes);      
	}
	
	/**  
	 * bytesת����ʮ�������ַ���  
	 * @param byte[] b byte����  
	 * @return String ÿ��Byteֵ֮��ո�ָ�  
	 */    
	public static String byte2HexStr(byte[] b)    
	{    
	    String stmp="";    
	    StringBuilder sb = new StringBuilder("");    
	    for (int n=0;n<b.length;n++)    
	    {    
	        stmp = Integer.toHexString(b[n] & 0xFF);    
	        sb.append((stmp.length()==1)? "0"+stmp : stmp);    
	        sb.append(" ");    
	    }    
	    return sb.toString().toUpperCase().trim();    
	}
	
	/**  
	 * bytes�ַ���ת��ΪByteֵ  
	 * @param String src Byte�ַ�����ÿ��Byte֮��û�зָ���  
	 * @return byte[]  
	 */    
	public static byte[] hexStr2Bytes(String src)    
	{    
	    int m=0,n=0;    
	    int l=src.length()/2;    
	    System.out.println(l);    
	    byte[] ret = new byte[l];    
	    for (int i = 0; i < l; i++)    
	    {    
	        m=i*2+1;    
	        n=m+1;    
	        ret[i] = Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n));    
	    }    
	    return ret;    
	}
}
