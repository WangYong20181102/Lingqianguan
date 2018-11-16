package com.wtjr.lqg.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.Bill;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;

import java.util.List;

public class BillExpandableListAdapter extends BaseExpandableListAdapter {

    private List<Bill> mBills;

    private Context mContext;

    public BillExpandableListAdapter(Context context, List<Bill> bills) {
        this.mContext = context;
        this.mBills = bills;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mBills.get(groupPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return 6;
    }

    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {

        Bill bill = (Bill) getChild(groupPosition, childPosition);

        HolderView holder = null;
        if (convertView == null) {
            holder = new HolderView();
            // 为视图对象指定布局
            convertView = (RelativeLayout) RelativeLayout.inflate(mContext,R.layout.bill_child, null);
            // 图标
            holder.tv_deal_record_icon = (TextView) convertView.findViewById(R.id.tv_deal_record_icon);
            // 类型名
            holder.tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);
            // 钱
            holder.tv_child_money = (TextView) convertView.findViewById(R.id.tv_child_money);
            // item下面的横线
            holder.v_heng_xian = convertView.findViewById(R.id.v_heng_xian);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        switch (childPosition) {
        case 0:// 收益
            holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_shou);
            holder.tv_child_name.setText("收益");
            moneyShow(true, holder.tv_child_money, bill.income);
            holder.v_heng_xian.setVisibility(View.VISIBLE);
            break;
        case 1:// 充值
            holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_chong);
            holder.tv_child_name.setText("充值");
            moneyShow(true, holder.tv_child_money, bill.charge);
            holder.v_heng_xian.setVisibility(View.VISIBLE);
            break;
        case 2:// 转入
            holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_ru);
            holder.tv_child_name.setText("转入");
            moneyShow(true, holder.tv_child_money, bill.rollIn);
            holder.v_heng_xian.setVisibility(View.VISIBLE);
            break;
        case 3:// 转出
            holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_chu);
            holder.tv_child_name.setText("转出");
            moneyShow(false, holder.tv_child_money, bill.rollOut);
            holder.v_heng_xian.setVisibility(View.VISIBLE);
            break;
        case 4:// 提现
            holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_xian);
            holder.tv_child_name.setText("提现");
            moneyShow(false, holder.tv_child_money, bill.withdraw);
            holder.v_heng_xian.setVisibility(View.VISIBLE);
            break;
        case 5:// 手续费
            holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_fei);
            holder.tv_child_name.setText("手续费");
            moneyShow(false, holder.tv_child_money, bill.poundage);
            // 最后一条的横线要隐藏
            holder.v_heng_xian.setVisibility(View.GONE);
            break;
//        case 11:// 冲正
//            holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_chong);
//            holder.tv_child_name.setText("冲正");
//            moneyShow(false, holder.tv_child_money, bill.poundage);
//            // 最后一条的横线要隐藏
//            holder.v_heng_xian.setVisibility(View.GONE);
//            break;
        }

        return convertView;
    }
    
    private void moneyShow(boolean zf,TextView textView,double money) {
        if(money == 0) {
            textView.setText(MoneyFormatUtil.format(money));
        }else {
            if(zf) {
                textView.setText("+" + MoneyFormatUtil.format(money));
            }else {
                textView.setText("-" + MoneyFormatUtil.format(money));
            }
        }
    }

    public Object getGroup(int groupPosition) {
        return mBills.get(groupPosition);
    }

    public int getGroupCount() {
        return mBills.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {

        Bill bill = (Bill) getGroup(groupPosition);

        HolderView holder = null;
        if (convertView == null) {
            holder = new HolderView();
            // 为视图对象指定布局
            convertView = View.inflate(mContext, R.layout.bill_group, null);
            //日期标题
            holder.group_title = (TextView) convertView.findViewById(R.id.group_title);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        holder.group_title.setText(TimeUtil.getY_M_D_Type2(bill.date));

        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    private class HolderView {
        private TextView tv_deal_record_icon;
        private TextView tv_child_money;
        private TextView tv_child_name;
        private TextView group_title;
        private View v_heng_xian;
    }
}