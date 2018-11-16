package com.wtjr.lqg.activities;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.wtjr.lqg.base.AccountData;
import com.wtjr.lqg.base.LqbData;
import com.wtjr.lqg.base.SevenDayEarnings;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.sharedpreferences.SaveCurrentPhoneUtil;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.sharedpreferences.SaveLoginState;
import com.wtjr.lqg.utils.CrashHandler;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.ScreenUtils;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class LqgApplication extends Application {
    /**
     * 状态栏高度
     */
    public int mStatusHeight;

    /**
     * 屏幕宽度
     */
    public int mScreenWidth;

    /**
     * 屏幕高度
     */
    public int mScreenHeight;

    /**
     * 屏幕中内容的高度
     */
    public int mScreenContentHeight;

    /**
     * 获得像素密度
     */
    public float mDensity;

    /**
     * 账号个人信息数据
     */
    public AccountData mAccountData;

    /**
     * 零钱宝数据
     */
    public LqbData mLqbData;

    /**
     * 数据库管理类
     */
    private DbManager db;

    /**
     * ImageLoader图片加载工具
     */
    private ImageLoader mImageLoader = ImageLoader.getInstance();

    /**
     * ImageLoader的配置
     */
    private DisplayImageOptions mOptions;


    /**
     * 是否更新版本(true是，false不是--非强制更新APP使用该参数)
     */
    public boolean isUpdate = true;

    public Location location;

    // LqgApplication单例对象
    private static LqgApplication mLqgApplication;

    /**
     * QQ app id
     */
    private static final String QQAppId = "1105163229";

    /**
     * QQ app key
     */
    private static final String QQAppKey = "5N4Kuzbxfh81jmYh";

    // 注意：在微信授权的时候，必须传递appSecret
    // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
    private final String WXAppId = "wxe1a74d394122d096";

    private final String WXAppSecret = "0c87a0cc6cdc30cad6df0e9f32e6c4c0";

    // sina新浪微博的appid App Secret

    private final String SinaAppId = "424949664";

    private final String SinaAppSecret = "f787076c3e30c50fe6517690cc2cf507";

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化
        x.Ext.init(this);
        // 设置是否输出debug
        x.Ext.setDebug(false);

        if (!Constant.isLog) {
            // 初始化全局异常
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }


        mLqgApplication = this;

        /**
         * 初始化友盟sdk
         */
        UMShareAPI.get(this);
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "156a5ac01175ba745617cb87a50f7dc5");
        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(true);

        /**
         *  友盟分享配置三方平台的appkey
         */
        PlatformConfig.setWeixin(WXAppId, WXAppSecret);
        PlatformConfig.setSinaWeibo(SinaAppId, SinaAppSecret, "http://sns.whalecloud.com");
        PlatformConfig.setQQZone(QQAppId, QQAppKey);
        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        MobclickAgent.enableEncrypt(true);
        /*场景类型设置接口*/
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);//普通统计场景类型

        /**
         * 初始化极光推送jpush
         */
        JPushInterface.setDebugMode(false);// 调试模式会打印日志
        JPushInterface.init(this);

        /**
         * 第一次进入APP传极光的RegistrationID给后台
         * 个推需要别名Alias 放在登录成功之后使用Uid注册 LoginOrExitUtils.loginSuccess();中
         */

        /**
         * 极光推送服务会被停止掉
         */
//        JPushInterface.stopPush(this);
//        /**
//         * 极光推送服务会恢复正常工作
//         */
        JPushInterface.resumePush(this);

        initDb();
        initImageLoader(this);

        mDensity = getResources().getDisplayMetrics().density;
        mStatusHeight = ScreenUtils.getStatusHeight(this);
        mScreenWidth = ScreenUtils.getScreenWidth(this);
        mScreenHeight = ScreenUtils.getScreenHeight(this);

        // isLogin = SaveLoginState.getLoginState(this);

        getAccountData();
        getLqbData();

    }

    protected void initDb() {
        // 本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("lqg_db") // 设置数据库名
                .setDbVersion(1) // 设置数据库版本,每次启动应用时将会检查该版本号,
                // 发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true)// 设置是否开启事务,默认为false关闭事务
                .setTableCreateListener(new DbManager.TableCreateListener() {

                    @Override
                    public void onTableCreated(DbManager arg0, TableEntity<?> arg1) {
                        L.hintW("onTableCreated===================" + arg1.getName());
                    }

                })// 设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        L.hintW(oldVersion + "============onUpgrade=======" + newVersion);
                    }
                });// 设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        // .setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下
        db = x.getDb(daoConfig);
    }

    /**
     * 获得数据库操作对象
     *
     * @return
     */
    public DbManager getDb() {
        return db;
    }

    /**
     * 更新数据
     *
     * @param t 数据对象 (只能是账号数据和零钱宝数据)
     */
    public <T> void updateData(T t) {
        if (t == null) {
            return;
        }

        if (t instanceof AccountData) {
            mAccountData = (AccountData) t;
            List<SevenDayEarnings> recent_income = mAccountData.recent_income;
            if (recent_income != null) {
                for (int i = 0; i < recent_income.size(); i++) {
                    if (i == 0) {
                        mAccountData.sevenDayEarnings = "";
                        mAccountData.sevenDayEarnings += recent_income.get(i).dayProfit;
                    } else {
                        mAccountData.sevenDayEarnings += "," + recent_income.get(i).dayProfit;
                    }
                }
            }

            try {
                db.saveOrUpdate(mAccountData);
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else if (t instanceof LqbData) {
            String currentUid = SaveCurrentUidUtil.getCurrentUid(this);
            mLqbData = (LqbData) t;
            mLqbData.uId = currentUid;

            try {
                db.saveOrUpdate(mLqbData);
            } catch (DbException e) {
                e.printStackTrace();
                L.hintD(e.toString());
            }
        }
    }

    /**
     * 查询数据库中的账户数据
     */
    private void getAccountData() {
        boolean loginState = SaveLoginState.getLoginState(this);
        if (!loginState) {
            return;
        }

        String currentPhone = SaveCurrentPhoneUtil.getCurrentPhone(this);
        try {
            List<AccountData> accountDatas = db.selector(AccountData.class).where("phone", "=", currentPhone).findAll();
            if (accountDatas != null && accountDatas.size() > 0) {
                mAccountData = accountDatas.get(0);
            }

            L.hintD("mAccountData=" + mAccountData);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询数据库中的零钱宝数据
     */
    private void getLqbData() {
        boolean loginState = SaveLoginState.getLoginState(this);
        if (!loginState) {
            return;
        }

        String currentUid = SaveCurrentUidUtil.getCurrentUid(this);
        try {
            List<LqbData> lqbDatas = db.selector(LqbData.class).where("uId", "=", currentUid).findAll();
            if (lqbDatas != null && lqbDatas.size() > 0) {
                mLqbData = lqbDatas.get(0);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    // m

    // try {
    // List<PersonalInformation> findAll =
    // db.findAll(PersonalInformation.class);
    // for (PersonalInformation personalInformation2 : findAll) {
    // L.hintV(personalInformation2);
    //
    // List<Aaaaa> aaa = personalInformation2.getAaaaa(db);
    // for (Aaaaa aaaaa3 : aaa) {
    // L.hintI(aaaaa3);
    // }
    //
    // }
    // //
    // } catch (DbException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    // /**
    // * 当前用户信息，更具当前的手机号码，查询当前登录账号存储在数据库里面是所有的信息
    // *
    // * @param context
    // */
    // public PersonalInformation getUserInformation() {
    // String account =
    // SaveCurrentAccountState.getCurrentAccount(getApplicationContext());
    // if (!TextUtils.isEmpty(account)) {
    // try {
    // List<PersonalInformation> findAll =
    // db.findAll(PersonalInformation.class);
    // } catch (DbException e) {
    // e.printStackTrace();
    // }
    // }
    // return null;
    // }

    // =======================================ImageLoader=======================================

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                // for
                // release
                // app
                .build();
        // Initialize ImageLoader with configuration.
        mImageLoader.init(config);
    }

    /**
     * 初始化imageLoader配置，加载中，失败，成功
     */
    public void setOptions(int iamgeResources) {
        mOptions = new DisplayImageOptions.Builder().showStubImage(iamgeResources).showImageForEmptyUri(iamgeResources)
                .showImageOnFail(iamgeResources).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .build();
    }

    /**
     * 设置图片
     *
     * @param imageUrl
     * @param imageView
     */
    public void setDisplayImage(String imageUrl, ImageView imageView) {
        mImageLoader.displayImage(imageUrl, imageView, mOptions);
    }

    /**
     * 设置图片
     *
     * @param imageUrl
     * @param imageView
     */
    public void setDisplayImage(String imageUrl, ImageView imageView, ImageLoadingListener listener) {
        mImageLoader.displayImage(imageUrl, imageView, mOptions, listener);
    }

    /**
     * 清除内存缓存
     */
    public void clearMemoryCache() {
        mImageLoader.clearMemoryCache();
    }

    /**
     * 清除SD卡中的缓存
     */
    public void clearDiscCache() {
        mImageLoader.clearDiscCache();
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 获取本地缓存图片
     *
     * @param uri
     * @return
     */
    public Bitmap getDiscCacheImage(String uri) {//这里的uri一般就是图片网址
        File file = DiscCacheUtil.findInCache(uri, mImageLoader.getDiscCache());
        try {

            String path = file.getPath();
            return BitmapFactory.decodeFile(path);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return null;
    }
    // =========================================================================================

    /**
     * 获取LqgApplication单例对象的方法
     *
     * @return
     * @author wangxu
     */
    public static LqgApplication getInstance() {

        return mLqgApplication;
    }

}
