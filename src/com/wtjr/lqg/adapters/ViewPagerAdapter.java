package com.wtjr.lqg.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.umeng.socialize.utils.Log;
import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.activities.WebViewActivity;
import com.wtjr.lqg.base.ActivityData;
import com.wtjr.lqg.constants.Constant;

import java.util.List;

/**
 * @author kincai
 * @company KCS互联网有限公司
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * @description viewpager适配器
 * @project Zhuanti_Custom_View_Adv_ViewPager
 * @package com.kincai.zhuanti_custom_view_adv_viewpager
 * @time 2015-7-25 上午10:42:53
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<ActivityData> activityDatas;
    private Context mContext;
    private LqgApplication app;

    public ViewPagerAdapter(Context context, List<ActivityData> activityDatas) {
        this.mContext = context;
        this.activityDatas = activityDatas;
        app = (LqgApplication) context.getApplicationContext();
    }

    /**
     * 返回多少page
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public void setActivityBases(List<ActivityData> activityDatas) {
        this.activityDatas = activityDatas;
    }

    /**
     * 判断当前滑动view等不等进来的对象
     * <p>
     * true: 表示不去创建，使用缓存 false:去重新创建 view： 当前滑动的view
     * object：将要进入的新创建的view，由instantiateItem方法创建
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 类似于BaseAdapger的getView方法 用了将数据设置给view 由于它最多就3个界面，不需要viewHolder
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 得到控件
        View view = View.inflate(mContext, R.layout.investment_view_pager_item, null);
        int size = activityDatas.size();
        // 如果小于1表示没有数据直接返回
        if (size < 1) {
            return view;
        }
        // 有数据
        final ActivityData activityData = activityDatas.get(position % size);

        ImageView imageView = (ImageView) view.findViewById(R.id.view_image);

        app.setOptions(R.drawable.home_activity_load);
        String url = Constant.IMAGE_ADDRESS + activityData.activeImage;
        app.setDisplayImage(url, imageView);

        container.addView(view);// 一定不能少，将view加入到viewPager中

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LogContent", activityData.toString());

                if (TextUtils.isEmpty(activityData.activeUrl) && TextUtils.isEmpty(activityData.activeImageUrl)) {
                    return;
                }

                Intent intent2 = new Intent(mContext, WebViewActivity.class);
                intent2.putExtra(ActivityData.class.getName(), activityData);
                mContext.startActivity(intent2);
            }
        });

        return view;
    }

    /**
     * 销毁page position： 当前需要消耗第几个page object:当前需要消耗的page
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
