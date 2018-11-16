package com.wtjr.lqg.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.PayPasswordType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.BankImageUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HeadChangeUtil;
import com.wtjr.lqg.utils.ImageTools;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.PermissionUtils;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.CircleImageView;
import com.wtjr.lqg.widget.UsuallyLayoutItem;

/**
 * 修改头像
 *
 * @author Myf
 */
public class HeadAmendActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnItemClickListener {
    private static final int REQUEST_CAMERA = 0;
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 头像
     */
    private RelativeLayout rlHead;
    /**
     * 昵称
     */
    private UsuallyLayoutItem uliNickname;
    /**
     * 银行
     */
    private UsuallyLayoutItem uliBank;
    private CircleImageView civHeadPicture;
    private ImageView image_next;

    private Dialog dialog;
    private String photoSavePath;
    private String photoSaveName;
    private ViewStub vsSetHead;
    /**
     * 当前选择的是哪个图片
     */
    private int selectPosition = -1;
    private boolean isFirst = true;
    /**
     * 选择头像布局是否显示
     */
    public boolean headIsShow = false;
    /**
     * 负责显示和关闭头像动画的父控件
     */
    private RelativeLayout rl1;
    private SetHeadBaseAdapter<String> adapter;
    /**
     * 保存头像文件名称的集合
     */
    private ArrayList<String> arrayList = new ArrayList<String>();
    /**
     * 选择头像时候头像控件的大小
     */
    private int headImageViewSize;

    private static final int GET_IMAGE_VIA_CAMERA = 10;
    private static final int GET_IMAGE_CIA_CLIP = 20;
    private static final int GET_IMAGE_CIA_PHOTO = 30;
    private static final int HEAD_PORTRAIT_SELECT = 40;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_head_amend);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        rlHead = (RelativeLayout) findViewById(R.id.rl_head);
        uliNickname = (UsuallyLayoutItem) findViewById(R.id.uli_nickname);
        uliBank = (UsuallyLayoutItem) findViewById(R.id.uli_bank);

        civHeadPicture = (CircleImageView) findViewById(R.id.civ_head_picture);

        image_next = (ImageView) findViewById(R.id.image_next);

        vsSetHead = (ViewStub) findViewById(R.id.vs_set_head);
    }

    @Override
    public void onStart() {
        if (isLogin) {
            uliNickname.setTextTvContentR(app.mAccountData.nickName, 0);
            HeadChangeUtil.requestHeadImage(app, civHeadPicture);
        }
        super.onStart();
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        rlHead.setOnClickListener(this);
        uliNickname.setOnClickListener(this);
        uliBank.setOnClickListener(this);

        httpUtil.setHttpRequesListener(this);

    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:修改头像
        tvTitleName.setText(R.string.head_amend_name);
    }

    @Override
    public void initData() {
        File file = new File(Constant.PHOTO_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        File file2 = new File(Constant.PENDING_PATH);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        uliNickname.setImageContentrResourceL(0);
        uliBank.setImageContentrResourceL(0);

        if (app.mAccountData.bankName != null) {
            BankImageUtil.setBankImage(app.mAccountData.bankName, uliBank.getNextLeftImage());
        }

        float dimension20DP = app.getResources().getDimension(R.dimen.d20);
        float dimension40DP = app.getResources().getDimension(R.dimen.d40);

        //计算出选择头像的时候每个头像的大小
        headImageViewSize = (int) (app.mScreenWidth - 4 * dimension20DP - 2 * dimension40DP) / 3;

        for (int i = 0; i < 9; i++) {
            arrayList.add("head" + (i + 1) + ".png");
        }

        adapter = new SetHeadBaseAdapter<String>(this, arrayList);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.rl_head:// 头像

                headChangeDialog();
                break;
            case R.id.uli_nickname:// 昵称
                intent = new Intent(HeadAmendActivity.this, NickNameActivity.class);
                startActivity(intent);
                break;
            case R.id.uli_bank:// 银行
                intent = new Intent(HeadAmendActivity.this, MyBankActivity.class);
                intent.putExtra(PayPasswordType.class.getName(), PayPasswordType.PayPasswordNotUnequivocal);
                startActivity(intent);

                break;
            case R.id.bt_set_head_popmenu_exit:// 点击取消
                dialog.dismiss();
                break;
            case R.id.bt_set_head_popmenu_photograph:// 拍照
                startCamera();
                dialog.dismiss();
                break;
            case R.id.bt_set_head_popmenu_electPhoto:// 点击选择一张图片
                PermissionUtils.writerReadSDcard(HeadAmendActivity.this);
                Intent intent3 = new Intent(Intent.ACTION_PICK);
                intent3.setType("image/*");
                startActivityForResult(intent3, GET_IMAGE_CIA_PHOTO);
                dialog.dismiss();
                break;

            case R.id.bt_set_head_popmenu_wentong:// 稳通头像

                if (app.mAccountData.headPortraitUrl != null && app.mAccountData.headPortraitUrl.length() <= 2) {
                    Constant.SELECT = Integer.parseInt(app.mAccountData.headPortraitUrl);
                    Constant.isSelect = true;
                }
                Constant.isEnableClick = true;
                Intent intent4 = new Intent(this, HeadAmendWenTongActivity.class);
                intent4.setAction(Intent.ACTION_VIEW);
                startActivityForResult(intent4, HEAD_PORTRAIT_SELECT);
                dialog.dismiss();


                break;
            case R.id.iv_close:// 关闭选择头像的布局
                closeHead();
                break;

            case R.id.bt_yesSelect:// 确认选择头像
                if (selectPosition != -1) {//此时证明已经选择图片了

                    sendChangeHeadReques(selectPosition + "");
                    closeHead();
                } else {
                    DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, getString(R.string.please_select_head_portrait));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 关闭头像布局
     */
    public void closeHead() {
        if (vsSetHead != null) {
            headIsShow = false;
            isFirst = true;
//          selectPosition = -1;
            closeHeadAnimation(rl1);
        }
    }

    /**
     * 关闭头像动画
     *
     * @param view
     */
    private void closeHeadAnimation(View view) {
        if (view != null) {
            TranslateAnimation mHiddenAction = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f);
            mHiddenAction.setDuration(200);
            view.startAnimation(mHiddenAction);
            view.setVisibility(View.GONE);

            mHiddenAction.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    vsSetHead.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 显示头像动画
     *
     * @param view
     */
    private void showHeadAnimation(View view) {
        if (view != null) {

            TranslateAnimation mShowAction = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(500);
            view.startAnimation(mShowAction);
            view.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 选择头像的GridView的适配器
     *
     * @param <T>
     * @author JoLie
     */
    public class SetHeadBaseAdapter<T> extends NewBaseAdapter<T> {

        public SetHeadBaseAdapter(Context context, List<T> list) {
            super(context, list);
        }

        @Override
        public int getContentView() {
            return R.layout.item_head_portrait;
        }

        @Override
        public void onInitView(View view, int position) {

            CircleImageView headPortrait = getViewChild(view, R.id.iv_item_head_portrait);

            LayoutParams params = (LayoutParams) headPortrait.getLayoutParams();
            params.width = headImageViewSize;
            params.height = headImageViewSize;
            headPortrait.setLayoutParams(params);


            if (selectPosition == position) {//被选择的图片要显示选择状态
                headPortrait.setBorderWidth((int) app.getResources().getDimension(R.dimen.d3));
                headPortrait.setBorderColorResource(R.color.FC_SELECT);

            } else {//没被选择的图片要隐藏选择状态
                headPortrait.setBorderWidth((int) app.getResources().getDimension(R.dimen.d1));
                headPortrait.setBorderColorResource(R.color.FC_333333);
            }

            if (isFirst) {
                String string = arrayList.get(position);
                Bitmap imageFromAssetsFile = ImageTools.getImageFromAssetsFile(HeadAmendActivity.this, "head_portrait/" + string);
                headPortrait.setImageBitmap(imageFromAssetsFile);
            }
        }
    }


    /**
     * 启动拍照
     */
    private void startCamera() {
        photoSavePath = Constant.PENDING_PATH;
        photoSaveName = System.currentTimeMillis() + ".png";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            //有权限，直接拍照
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            takePhoto();
        }
    }

    private void takePhoto() {
        Uri imageUri = null;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
        try {
            if (Build.VERSION.SDK_INT < 24) {
                openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, GET_IMAGE_VIA_CAMERA);
            } else {  //Android7.0以上版本需要以下方式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, new File(photoSavePath, photoSaveName).getAbsolutePath());
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(openCameraIntent, GET_IMAGE_VIA_CAMERA);
            }
        } catch (Exception e) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "启动相机失败");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = null;

            switch (requestCode) {

                case GET_IMAGE_CIA_PHOTO:// 相册选择一张图片后返回的
                    uri = getPictureUri(data); // 方法调用
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor == null) {
                        return;
                    }

                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);// 图片在的路径


                    Intent intent3 = new Intent(HeadAmendActivity.this, ClipHeadActivity.class);
                    intent3.putExtra("path", path);
                    startActivityForResult(intent3, GET_IMAGE_CIA_CLIP);
                    break;

                case HEAD_PORTRAIT_SELECT:// 选择稳通头像完成返回的
                    sendChangeHeadReques(Constant.SELECT + "");
                    break;
                case GET_IMAGE_CIA_CLIP:// 剪裁完图片完照片返回的
                    sendSetingHeadRequest(RefreshType.RefreshNoPull);

                    break;
                case GET_IMAGE_VIA_CAMERA:// 相机拍完照片返回的
                    String path2 = photoSavePath + photoSaveName;
                    uri = Uri.fromFile(new File(path2));
                    Intent intent2 = new Intent(HeadAmendActivity.this, ClipHeadActivity.class);
                    intent2.putExtra("path", path2);
                    startActivityForResult(intent2, GET_IMAGE_CIA_CLIP);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 解决小米手机无法调用相册
     *
     * @param intent
     * @return
     */
    public Uri getPictureUri(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
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
     */
    public void sendChangeHeadReques(String which) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_SETHEADPORTRAIT);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("headPortraitType", which);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);
    }


    /**
     * 发送设置头像请求
     *
     * @param refreshType
     */
    public void sendSetingHeadRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_SETHEADPORTRAIT);
        params.addBodyParameter("user_userunid", mUid);
        String path = Constant.PHOTO_PATH + mUid + ".png";
        params.addBodyParameter("avatar", new File(path));
        httpUtil.sendRequest(params, refreshType, this);
    }


    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        String urlHP = jsonObject.getString("HeadPortraitUrl");

        L.hintI("urlHP=" + urlHP);

        app.mAccountData.headPortraitUrl = urlHP;
        app.updateData(app.mAccountData);

        HeadChangeUtil.requestHeadImage(app, civHeadPicture);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        isFirst = false;
        selectPosition = position;
        adapter.notifyDataSetChanged();
    }
}
