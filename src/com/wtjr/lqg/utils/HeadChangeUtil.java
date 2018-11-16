package com.wtjr.lqg.utils;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.HeadAmendActivity;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.base.AccountData;
import com.wtjr.lqg.constants.Constant;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 更换网络头像工具 
 * @author JoLie
 *
 */
public class HeadChangeUtil {
    private static String loadingCompleteheadUrl = "";
    /**
     * 请求头像
     */
    public static void requestHeadImage(final LqgApplication app,final ImageView imageView) {
        if(app.mAccountData == null || app.mAccountData.headPortraitUrl == null) {
            imageView.setImageResource(R.drawable.qiurenshi);
            return;
        }
        
        if(app.mAccountData.headPortraitUrl.length() <= 2) {//用的是系统头像
            int parseInt = Integer.parseInt(app.mAccountData.headPortraitUrl);
            String fileName = "head"+(parseInt+1)+".png";
            Bitmap imageFromAssetsFile = ImageTools.getImageFromAssetsFile(app,"head_portrait/"+fileName);
            imageView.setImageBitmap(imageFromAssetsFile);
            return;
        }
        
        final String path= Constant.PHOTO_PATH + app.mAccountData.uId + ".png";
        String headURL = Constant.IMAGE_ADDRESS + app.mAccountData.headPortraitUrl;
        
        if(loadingCompleteheadUrl.equals(headURL)) {
            Bitmap bitmap = ImageTools.getBitmap(app, path);
            
            if(bitmap == null) {//没有本地头像图片
                imageView.setImageResource(R.drawable.qiurenshi);
                loadingCompleteheadUrl = "";
            }else {
                imageView.setImageBitmap(bitmap);
                return;
            }
        }
        
        app.setOptions(R.drawable.qiurenshi);
        app.setDisplayImage(headURL, imageView, new ImageLoadingListener() {
            
            @Override
            public void onLoadingStarted(String imageUri, View view) {
//                L.hintD("=============onLoadingStarted============");                        
            }
            
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                L.hintE("=============onLoadingFailed============");  
               
                Bitmap bitmap = ImageTools.getBitmap(app, path);
                if(bitmap == null) {//没有本地头像图片
                    imageView.setImageResource(R.drawable.qiurenshi);
                }else {
                    imageView.setImageBitmap(bitmap);
                }
            }
            
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                L.hintI("=============onLoadingComplete============"+imageUri);  
                loadingCompleteheadUrl = imageUri;
                
                File file = new File(Constant.PHOTO_PATH);
                if(!file.exists()){
                    file.mkdirs();
                }
                
                String path = Constant.PHOTO_PATH + app.mAccountData.uId + ".png";
                ImageTools.savePhotoToSDCard(loadedImage, path);
            }
            
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
//                L.hintD("=============onLoadingCancelled============");                                 
            }
        });
    }
}
