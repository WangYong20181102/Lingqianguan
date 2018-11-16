package com.wtjr.lqg.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.ImageTools;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
/**
 * 设置头像
 * @author JoLie
 *
 */
public class HeadSetingActivity extends BaseActivity implements OnClickListener,HttpRequesListener, OnItemClickListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 显示风控图片的GridView
	 */
    private GridView gvHeadPicture;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 风控图片的宽度
     */
    private int mRiskPictureWidth;
    private HeadGridViewAdapter<String> adapter;
    private Button button1;
    private ImageView imageView1;
    
    private Dialog dialog;
    private String photoSavePath;
    private String photoSaveName;
    
    private static final int GET_IMAGE_VIA_CAMERA = 10;
    private static final int GET_IMAGE_CIA_CLIP = 20;
    private static final int GET_IMAGE_CIA_PHOTO = 30;
    private static final int HEAD_PORTRAIT_SELECT = 40;
    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_seting_head);

		mScreenWidth = app.mScreenWidth;
		
		float dimension1 = getResources().getDimension(R.dimen.d15);
		float dimension2 = getResources().getDimension(R.dimen.d10);
		
        mRiskPictureWidth = (int) ((mScreenWidth - dimension1 * 2 - dimension2 * 2)/3);
        
        start();

        File file = new File(Constant.PHOTO_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
        
        File file2 = new File(Constant.PENDING_PATH);
        if(!file2.exists()){
            file2.mkdirs();
        }
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		
		
		gvHeadPicture = (GridView) findViewById(R.id.gv_headPicture);
		
		button1 = (Button) findViewById(R.id.button1);
		
		
		imageView1 = (ImageView) findViewById(R.id.imageView1);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
		
		gvHeadPicture.setOnItemClickListener(this);
	}

	@Override
	public void setTitle() {
		// 设置名字为:活动
		tvTitleName.setText("设置头像");
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);

	}

	@Override
	public void initData() {
	    ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            list.add(i+"");
        }
        
        adapter = new HeadGridViewAdapter<String>(this, list);
        gvHeadPicture.setAdapter(adapter);
	}
	
	/**
     * 风控信息界面的用GridView的适配器
     * 
     * @author dell 
     * 
     * @param <T>
     * 
     */
    public class HeadGridViewAdapter<T> extends NewBaseAdapter<T> {

        public HeadGridViewAdapter(Context context, List<T> list) {
            super(context, list);
        }

        // item布局
        public int getContentView() {
            return R.layout.item_risk_message;
        }

        public void onInitView(View view, int position) {
//            // 获得显示风控图片的控件
            ImageView pictureIV = getViewChild(view, R.id.iv_picture);
            RelativeLayout layout = getViewChild(view, R.id.rl_picture);
            layout.getLayoutParams().width = mRiskPictureWidth;
            layout.getLayoutParams().height = mRiskPictureWidth;
            
            
            String path= Constant.PHOTO_PATH + mUid + ".png";
            Bitmap bitmap = ImageTools.getBitmap(app,path);
            pictureIV.setImageBitmap(bitmap);
            
//            
//            RiskPicture picture = (RiskPicture) getItem(position);
//            // 将图片显示任务增加到执行池，图片将被显示到ImageView当轮到此ImageView
//            app.setDisplayImage(picture.img_url, riskPictureIV);
        }
    }
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
		case R.id.bt_set_head_popmenu_exit:// 点击取消
            dialog.dismiss();
            break;
        case R.id.bt_set_head_popmenu_photograph:// 拍照
            startCamera();
            dialog.dismiss();
            break;
        case R.id.bt_set_head_popmenu_electPhoto:// 点击选择一张图片
            Intent intent3 = new Intent(Intent.ACTION_PICK);
            intent3.setType("image/*");
            startActivityForResult(intent3, GET_IMAGE_CIA_PHOTO);
            dialog.dismiss();
            break;

        case R.id.bt_set_head_popmenu_wentong:// 稳通头像
//             Intent intentWenTongHead = new Intent(this,
//             WenTongHeadPortraitActivity.class);
            // startActivityForResult(intentWenTongHead, HEAD_PORTRAIT_SELECT);
            // dialog.dismiss();
            break;
		default:
			break;
		}
	}
	
	/**
     * 启动拍照
     */
    private void startCamera() {
        photoSavePath = Constant.PENDING_PATH;
        photoSaveName = System.currentTimeMillis()+ ".png";

        Uri imageUri = null;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(new File(photoSavePath,photoSaveName));
        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        
        try {
            startActivityForResult(openCameraIntent, GET_IMAGE_VIA_CAMERA);
        } catch (Exception e) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "启动相机失败");
        }
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = null;
            
            switch (requestCode) {
            
            case GET_IMAGE_CIA_PHOTO:// 相册选择一张图片后返回的
                uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(uri, proj, null, null,null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);// 图片在的路径
                
                
                Intent intent3=new Intent(HeadSetingActivity.this, ClipHeadActivity.class);
                intent3.putExtra("path", path);
                startActivityForResult(intent3, GET_IMAGE_CIA_CLIP);
                break;
                
            case HEAD_PORTRAIT_SELECT:// 选择稳通头像完成返回的
            case GET_IMAGE_CIA_CLIP:// 剪裁完图片完照片返回的
                sendSetingHeadRequest(RefreshType.RefreshNoPull);
                
//                Bitmap headPictureBitmap = ImageTools.getHeadPictureBitmap(this,app.information.phone);
//                Intent mIntent = new Intent(Constant.HEADPICTURECHANGE);
//                // 发送头像变动广播
//                sendBroadcast(mIntent,Manifest.permission.receiver_permission);
//                changeHeadBitmap(headPictureBitmap);
                
                break;
            case GET_IMAGE_VIA_CAMERA:// 相机拍完照片返回的
                String path2=photoSavePath+photoSaveName;
                uri = Uri.fromFile(new File(path2));
                Intent intent2=new Intent(HeadSetingActivity.this, ClipHeadActivity.class);
                intent2.putExtra("path", path2);
                startActivityForResult(intent2, GET_IMAGE_CIA_CLIP);
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	

    /**
     * 头像更改弹出的菜单
     */
    private void headChangeDialog() {
        dialog = DialogUtil.upSlideDialog(this, R.layout.dialog_set_head_popmenu);
        
        Button bt_exit = (Button) dialog.findViewById(R.id.bt_set_head_popmenu_exit);
        Button bt_photograph = (Button) dialog.findViewById(R.id.bt_set_head_popmenu_photograph);
        Button bt_electPhoto = (Button) dialog.findViewById(R.id.bt_set_head_popmenu_electPhoto);
        Button bt_wenTong = (Button) dialog.findViewById(R.id.bt_set_head_popmenu_wentong);
        
        bt_wenTong.setOnClickListener(this);
        bt_exit.setOnClickListener(this);// 取消按钮的监听
        bt_photograph.setOnClickListener(this);// 拍照按钮的监听
        bt_electPhoto.setOnClickListener(this);// 选择一张图片按钮的监听
    }
	
	/**
     * 发送设置头像请求
     * @param refreshType
     */
    public void sendSetingHeadRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_SETHEADPORTRAIT);
        params.addBodyParameter("user_userunid", mUid);
        String path= Constant.PHOTO_PATH + mUid + ".png";
        params.addBodyParameter("avatar", new File(path));
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        String urlHP = jsonObject.getString("HeadPortraitUrl");
        
        L.hintI("urlHP="+urlHP);
        
        app.mAccountData.headPortraitUrl = urlHP;
        app.updateData(app.mAccountData);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        
    }

}
