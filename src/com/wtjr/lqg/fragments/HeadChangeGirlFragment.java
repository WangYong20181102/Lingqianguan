package com.wtjr.lqg.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.HeadAmendWenTongActivity;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.utils.ImageTools;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myf on 2016/12/6.
 */

public class HeadChangeGirlFragment extends Fragment implements AdapterView.OnItemClickListener {
    /**
     * 九宫格视图
     *
     * @param arg0
     */
    private GridView gridView;
    private static SetHeadBaseAdapter<String> adapter;
    private LqgApplication app;
    /**
     * 当前选择的是哪个图片
     */
    private int selectPosition = -1;
    /**
     * 保存头像文件名称的集合
     */
    private ArrayList<String> arrayList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.head_gradview_layout, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        gridView = (GridView) view.findViewById(R.id.gv_head);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    public void initData() {
        app = (LqgApplication) x.app();
        for (int i = 0; i < 9; i++) {
            arrayList.add("head" + (i + 1) + ".png");
        }
        adapter = new SetHeadBaseAdapter<String>(getActivity(), arrayList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    //刷新适配器
    public static void updateData() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectPosition = position;
        if (Constant.SELECT >= 9) {
            HeadChangeManFragment.updateData();
        }
        Constant.SELECT = position;
        if (app.mAccountData.headPortraitUrl != null && app.mAccountData.headPortraitUrl.length() <= 2) {
            if (position == Integer.parseInt(app.mAccountData.headPortraitUrl)) {
                Constant.isEnableClick = true;
                HeadAmendWenTongActivity.updateData();
            } else if (Constant.isEnableClick) {
                Constant.isEnableClick = false;
                HeadAmendWenTongActivity.updateData();
            }
        } else if (Constant.isEnableClick) {
            Constant.isEnableClick = false;
            HeadAmendWenTongActivity.updateData();
        }
        adapter.notifyDataSetChanged();
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
            return R.layout.item_head_portrait1;
        }

        @Override
        public void onInitView(View view, int position) {
            ImageView headPortrait = getViewChild(view, R.id.iv_item_head_portrait1);
            ImageView iv_item_head_portrait_select = getViewChild(view, R.id.iv_item_head_portrait_select);

            if (Constant.isSelect) {
                if (Constant.SELECT < 9) {
                    selectPosition = Constant.SELECT;
                    Constant.isSelect = false;
                }
            }
            if (selectPosition == position) {//被选择的图片要显示选择状态
                if (Constant.SELECT >= 9) {
                    iv_item_head_portrait_select.setVisibility(View.GONE);
                } else {
                    iv_item_head_portrait_select.setVisibility(View.VISIBLE);
                }
            } else {
                iv_item_head_portrait_select.setVisibility(View.GONE);
            }


            String string = arrayList.get(position);
            Bitmap imageFromAssetsFile = ImageTools.getImageFromAssetsFile(getActivity(), "head_portrait/" + string);
            headPortrait.setImageBitmap(imageFromAssetsFile);
        }
    }
}
