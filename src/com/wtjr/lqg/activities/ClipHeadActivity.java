package com.wtjr.lqg.activities;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.ImageTools;
import com.wtjr.lqg.widget.ClipImageLayout;
import com.wtjr.lqg.widget.ClipZoomImageView;

/**
 * 剪切头像
 * @author JoLie
 *
 */
public class ClipHeadActivity extends BaseActivity{
	private ClipImageLayout mClipImageLayout;
	private String path;
	private ProgressDialog loadingDialog;
	
	private Bitmap bitmap;
	
	private int x = 0;
	private int y = 0;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip_head);
		//这步必须要加 
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog=new ProgressDialog(this);
        loadingDialog.setMessage("图片处理中,请稍后...");
        loadingDialog.setCancelable(false);
        
		path=getIntent().getStringExtra("path");
		
		if(TextUtils.isEmpty(path)||!(new File(path).exists())){
			DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "图片加载失败");
			return;
		}
		
		bitmap=ImageTools.getBitmap(this,path );
		
		if(bitmap==null){
			DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "图片加载失败");
			return;
		}
		
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		mClipImageLayout.setBitmap(bitmap);
		
		
		((Button)findViewById(R.id.id_action_clip)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadingDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Bitmap bitmap = mClipImageLayout.clip();
						String path= Constant.PHOTO_PATH + mUid + ".png";
						//压缩图片后保存
						ImageTools.compressImageAndSave(app, bitmap, 500 ,path);
						
						loadingDialog.dismiss();
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
				}).start();
			}
		});
		
		
		((Button)findViewById(R.id.id_action_rotate)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Matrix matrix = new Matrix();

				x = bitmap.getWidth();
				y = bitmap.getHeight();
				
				// 设置旋转角度
				matrix.setRotate(90);
				
				// 重新绘制Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0,0, x, y,
						matrix, false);
				mClipImageLayout.setBitmap(bitmap);
				
				ClipZoomImageView zoomImageView = mClipImageLayout.getZoomImageView();
				MotionEvent event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 200, 500, 0);
				zoomImageView.onTouch(zoomImageView, event);
			}
		});
		
		
		tvTitle = (TextView) findViewById(R.id.tv_title_name);
		tvTitle.setText("头像剪裁");
	}
}
