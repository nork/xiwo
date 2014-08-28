package com.android.xiwao.washcar.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.xiwao.washcar.AppLog;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;


public class FileUtil {
	
	private static final String TAG = "FileUtil";

	/**
	 * <p>将文件转成base64 字符�?/p>
	 * @param path 文件路径
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64File(String path) throws Exception {
		File  file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int)file.length()];
		inputFile.read(buffer);
        inputFile.close();
        return  Base64.encodeToString(buffer, Base64.DEFAULT);
	}
	/**
	 * <p>将base64字符解码保存文件</p>
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static String decoderBase64File(String base64Code,String targetPath) throws Exception {
		byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
		
		return targetPath;
	}
//	/**
//	 * <p>将base64字符保存文本文件</p>
//	 * @param base64Code
//	 * @param targetPath
//	 * @throws Exception
//	 */
//	public static void toFile(String base64Code,String targetPath) throws Exception {
//		byte[] buffer = base64Code.getBytes();
//		FileOutputStream out = new FileOutputStream(targetPath);
//		out.write(buffer);
//		out.close();
//	}
	public static void test() {
		try {
			String base64Code =encodeBase64File("D:\\1.jpg");
			System.out.println(base64Code);
			decoderBase64File(base64Code, "D:\\2.jpg");
//			toFile(base64Code, "D:\\three.txt");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 将图片压缩并转换成Base64字串
	 * @param path 图片路径
	 * @return
	 * @throws Exception
	 */
	public static String img2Base64(String path) throws Exception {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 2;//图片大小，设置越大，图片越不清晰，占用空间越�?
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		float realWidth = options.outWidth;
		float realHeight = options.outHeight;
		int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100); //600
		if (scale <= 0) {
			scale = 1;
		}
		Log.i(TAG, "---------img2Base64-------------------scale="+scale);
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, options);
		
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //200
				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		return Base64.encodeToString(buffer, Base64.DEFAULT);
		return result;
	}

	/**
	 * 将Base64字串转化为Bitmap
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static Bitmap base642Img(String content) throws Exception {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(content, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}


	/**
	 * 将Bitmap保存
	 * @param bitmap //要保存的图片
	 * @param path 保存路径
	 * @return
	 * @throws Exception
	 */
	public static void saveBitMap(Bitmap bitmap , String path) throws Exception {

		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //200
				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				
				FileOutputStream out = new FileOutputStream(path);
				out.write(bitmapBytes);
				out.flush();
				out.close();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将图片转换成Base64字串 (没有压缩)
	 * @param path 图片路径
	 * @return
	 * @throws Exception
	 */
	public static String bitMap2Base64(String path) throws Exception {


		Bitmap bitmap = BitmapFactory.decodeFile(path);
		
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //200
				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
    /**
     * 创建文件及文件夹
     * @param _folder
     * @param _file
     * @return
     */
	public static String createFileOnSD(String _folder, String _file) {
		
		String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
		_folder = sdpath +_folder;

		AppLog.v(TAG, "_folder");
		File file = new File(_folder + _file);
		File fileFolder = new File(_folder);
		if (!fileFolder.exists())
			fileFolder.mkdirs();
		// 这里不做文件是否存在的判�?
		try {
			if(!file.exists()){
				file.createNewFile();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
	/**
	 * 压缩图片并保存，此方法不会改变图片尺寸，只修改压缩率
	 * @param path
	 * @param targetPath
	 * @throws Exception
	 */
	public static void compressAndSaveNoHw(String path, String targetPath) throws Exception{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();

		newOpts.inJustDecodeBounds = false;
		newOpts.inSampleSize = 1;
		newOpts.inDither = false;
		newOpts.inPurgeable = true;
		newOpts.inInputShareable = true;
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
		
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				int options = 80;
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
				while(baos.toByteArray().length / 1024 > 500 && options > 20){
					baos.reset();
					options -= 10;
					bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos); //30 压缩�? 100不压�?
				}
				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				Log.i(TAG, " compressAndSave  after   bitmapBytes:"+bitmapBytes.length);
				FileOutputStream out = new FileOutputStream(targetPath);
				out.write(bitmapBytes);
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
				bitmap.recycle();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 压缩图片并保�?此方法会改变图片尺寸
	 * @param path
	 * @param targetPath
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public static void compressAndSave(String path, String targetPath) throws Exception {
		// options.inSampleSize = 2;//图片大小，设置越大，图片越不清晰，占用空间越�?
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// �?��读入图片，此时把options.inJustDecodeBounds 设回true�?
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，�?��高和宽我们设置为
		float hh = 100f;
		float ww = 100f;
		// 缩放比�?由于是固定比例缩放，只用高或者宽其中�?��数据进行计算即可
		int be = 1;// be=1表示不缩�?
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩�?
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩�?
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
		bitmap = BitmapFactory.decodeFile(path, newOpts);
		
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos); //30 压缩�? 100不压�?
				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				Log.i(TAG, " compressAndSave  after   bitmapBytes:"+bitmapBytes.length);
				FileOutputStream out = new FileOutputStream(targetPath);
				out.write(bitmapBytes);
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
				bitmap.recycle();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/** 
	 * bitmap转为base64 
	 * @param bitmap 
	 * @return 
	 */  
	public static String bitmapToBase64(Bitmap bitmap) {  
	  
	    String result = null;  
	    ByteArrayOutputStream baos = null;  
	    try {  
	        if (bitmap != null) {  
	            baos = new ByteArrayOutputStream();  
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
	  
	            baos.flush();  
	            baos.close();  
	  
	            byte[] bitmapBytes = baos.toByteArray();  
	            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);  
	        }  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (baos != null) {  
	                baos.flush();  
	                baos.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return result;  
	}  
	  
	/** 
	 * base64转为bitmap 
	 * @param base64Data 
	 * @return 
	 */  
	public static Bitmap base64ToBitmap(String base64Data) {  
	    byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
	}  
}