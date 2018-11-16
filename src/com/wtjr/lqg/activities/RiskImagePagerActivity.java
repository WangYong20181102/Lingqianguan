/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.wtjr.lqg.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;

/**
 * ViewPager页面显示Activity
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class RiskImagePagerActivity extends BaseActivity {

    private static final String STATE_POSITION = "STATE_POSITION";

    ViewPager pager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_image_pager);

        Bundle bundle = getIntent().getExtras();
        String[] imageUrls = bundle.getStringArray("ImageUrls");
        // 当前显示View的位置
        int pagerPosition = bundle.getInt("ShowPosition", 0);

        // 如果之前有保存用户数据
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImagePagerAdapter(imageUrls));
        pager.setCurrentItem(pagerPosition);    // 显示当前位置的View

        pager.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        app.setOptions(R.drawable.risk_iamge_load);
    }

    public void onSaveInstanceState(Bundle outState) {
        // 保存用户数据
        outState.putInt(STATE_POSITION, pager.getCurrentItem());
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private String[] images;
        private LayoutInflater inflater;

        ImagePagerAdapter(String[] images) {
            this.images = images;
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);


            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });


            app.setDisplayImage(images[position], imageView, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) { // 获取图片失败类型
                        case IO_ERROR: // 文件I/O错误
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR: // 解码错误
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED: // 网络延迟
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY: // 内存不足
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN: // 原因不明
                            message = "Unknown error";
                            break;
                    }
//                    ToastUtil.showToastShort(RiskImagePagerActivity.this, message);                
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    spinner.setVisibility(View.GONE);
                }
            });

            ((ViewPager) view).addView(imageLayout, 0);        // 将图片增加到ViewPager
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }
    }
}