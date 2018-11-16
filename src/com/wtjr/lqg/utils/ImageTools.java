package com.wtjr.lqg.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import com.wtjr.lqg.constants.Constant;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

/**
 * Tools for handler picture
 * 
 * @author Ryan.Tang
 * 
 */
public final class ImageTools {
    /**
     * 判断SD卡的状态是否可用
     * @return
     */
    public static boolean checkSDCardAvailable(){
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    
    
	/**
	 * 保存photoBitmap图片到SD卡中
	 * @param photoBitmap 需要保存图片的Bitmap
	 * @param photoName 保存的文件名
	 * @param path 保存的路径
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){
		if (checkSDCardAvailable()) {
			savePhotoToSDCard(photoBitmap, path + photoName);
		} 
	}
	
	/**
	 * 保存photoBitmap图片到SD卡中
	 * @param photoBitmap 需要保存图片的Bitmap
	 * @param path 保存的路径
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap,String path){
		if (checkSDCardAvailable()) {
			File photoFile = new File(path);
			
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (Exception e) {
				photoFile.delete();
				e.printStackTrace();
			} finally{
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} 
	}
	
	/**
	 * 压缩图片
	 * @param bitmap
	 * @param context
	 * @param size 压缩到多大(KB)
	 * @param path 保存文件的路径
	 * @return
	 */
    public static Bitmap compressImageAndSave(Context context,Bitmap bitmap,int size,String path) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩       
            baos.reset();//重置baos即清空baos
            options -= 1;//每次都减少1
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        
        
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        
                
        byte[] byteArray = baos.toByteArray();
        
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            fileOutputStream.write(byteArray, 0, byteArray.length);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return getBitmap(context, path);
    }
   
	
//	/**
//	 * 获取头像图片
//	 * @param context 
//	 * @param phone 手机号
//	 * @return
//	 */
//	public static Bitmap getHeadPictureBitmap(Context context,String phone) {
//		String path= Constant.PHOTO_PATH + phone + ".png";
//		try {
//			FileInputStream fis = new FileInputStream(path);
//			return BitmapFactory.decodeStream(fis);
//		} catch (FileNotFoundException e) {
//			return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//		}
//	}
	

	/**
	 * 从Assets中读取头像资源图片
	 */
	public static Bitmap getImageFromAssetsFile(Context context,String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
//			BitmapFactory.Options opt = new BitmapFactory.Options();
//			image = BitmapFactory.decodeStream(is, null, opt);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	
	/** 
	 * 根据图片资源ID获得该图片的Bitmap(已做尺寸压缩处理)
	 * @param context 
	 * @param t SD卡路径 或 图片ID
	 * @return
	 */
	public static <T> Bitmap getBitmap(Context context,T t){   
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// 这个isjustdecodebounds很重要
		opt.inJustDecodeBounds = true;
		
		Bitmap bitmap = null;
		
		if(t instanceof String) {//SD卡图片
		    BitmapFactory.decodeFile((String)t, opt);
		}else if(t instanceof Integer) {//资源ID图片
		    BitmapFactory.decodeResource(context.getResources(), (Integer)t,opt);
		}

		// 获取到这个图片的原始宽度和高度
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		// 获取屏的宽度和高度

		int screenHeight = ScreenUtils.getScreenHeight(context);
		int screenWidth = ScreenUtils.getScreenWidth(context);

		// isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
//		opt.inSampleSize = 1;
		
		// 根据屏的大小和图片大小计算出缩放比例
		if (picWidth > picHeight) {
			if (picWidth > screenWidth)
				opt.inSampleSize = picWidth / screenWidth;
		} else {
			if (picHeight > screenHeight)
				opt.inSampleSize = picHeight / screenHeight;
		}

		// 这次再真正地生成一个有像素的，经过缩放了的bitmap
		opt.inJustDecodeBounds = false;
		
		if(t instanceof String) {//SD卡图片
           bitmap = BitmapFactory.decodeFile((String)t, opt);
        }else if(t instanceof Integer) {//资源ID图片
           bitmap = BitmapFactory.decodeResource(context.getResources(), (Integer)t,opt);
        }

		return bitmap;
	}  
	
	
	
	/**  
	 * 压缩Bitmap
	 * @param context 
	 * @param bitmap 
	 * @return
	 */
	public static Bitmap getResourceBitmap(Context context, Bitmap bitmap){ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bitmapByte = baos.toByteArray();
		
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// 这个isjustdecodebounds很重要
		opt.inJustDecodeBounds = true;
		
		Bitmap bm =BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length, opt);
		opt.inSampleSize = 2;
		opt.inJustDecodeBounds = false;

		bm =BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length, opt);

		return bm;
	}  
	
	/**
	 * drawable转Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}
}
