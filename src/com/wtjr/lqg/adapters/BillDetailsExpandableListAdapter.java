package com.wtjr.lqg.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.BillDetails;
import com.wtjr.lqg.base.BillDetailsTime;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.widget.UpDownTextView;
import com.wtjr.lqg.widget.pullRefresh.PullableExpandableListView;

/**
 * 账单详情
 *
 * @author JoLie
 */
public class BillDetailsExpandableListAdapter extends BaseExpandableListAdapter {
    // 用来标识是否设置二級item背景色为绿色,初始值为-1既为选中状态
    public int child_groupId = -1;
    public int child_childId = -1;
    private List<BillDetailsTime> logTimes;
    private Context context;
    private PullableExpandableListView pullableExpListView;

    public BillDetailsExpandableListAdapter(Context context, List<BillDetailsTime> logTimes, PullableExpandableListView pullableExpListView) {
        this.logTimes = logTimes;
        this.context = context;
        this.pullableExpListView = pullableExpListView;
    }

    /**
     * 获取一级标签总数
     */
    @Override
    public int getGroupCount() {
        return logTimes.size();
    }

    /**
     * 获取一级标签内容
     */
    @Override
    public Object getGroup(int groupPosition) {
        return logTimes.get(groupPosition);
    }

    /**
     * 获取一级标签的ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取一级标签下二级标签的总数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return logTimes.get(groupPosition).getListSize();
    }

    /**
     * 获取一级标签下二级标签的内容
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return logTimes.get(groupPosition).getContactItemByIndex(childPosition);
    }

    /**
     * 获取二级标签的ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 指定位置相应的组视图
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 对一级标签进行设置
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // pullableExpListView.expandGroup(groupPosition);//展开
        BillDetailsTime logTime = (BillDetailsTime) getGroup(groupPosition);
        // 为视图对象指定布局
        convertView = (RelativeLayout) RelativeLayout.inflate(context, R.layout.bill_group, null);
        // 新建一个TextView对象，用来显示一级标签上的标题信息
        TextView group_title = (TextView) convertView.findViewById(R.id.group_title);
        // 设置标题上的文本信息
        group_title.setText(logTime.getTime());
        return convertView;
    }

    /**
     * 对一级标签下的二级标签进行设置
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        BillDetails dealLogDetails = (BillDetails) getChild(groupPosition, childPosition);

        HolderView holder = null;
        if (convertView == null) {
            holder = new HolderView();
            // 为视图对象指定布局
            convertView = (RelativeLayout) RelativeLayout.inflate(context, R.layout.bill_details_child, null);
            // item下面的横线
            holder.v_heng_xian = convertView.findViewById(R.id.v_heng_xian);
            // 钱
            holder.tv_deal_record_icon = (TextView) convertView.findViewById(R.id.tv_deal_record_icon);
            // 钱
            holder.tv_child_money = (TextView) convertView.findViewById(R.id.tv_child_money);
            // 时间
            holder.tv_child_time = (UpDownTextView) convertView.findViewById(R.id.udtv_child_time);
            // 注释
            holder.tv_child_remark = (TextView) convertView.findViewById(R.id.tv_child_remark);
            // ---------------------------------------隐藏部分----------------------------------------------
            // 负责显示隐藏的
            holder.layout = (LinearLayout) convertView.findViewById(R.id.show);
            // 交易流水号
            holder.child_dealNumber_content = (TextView) convertView.findViewById(R.id.child_dealNumber_content);
            // 交易时间
            holder.child_dealTime_content = (TextView) convertView.findViewById(R.id.child_dealTime_content);
            // 备注
            holder.child_remark_content = (TextView) convertView.findViewById(R.id.child_remark_content);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        // 每组中最后一个item
        if (getChildrenCount(groupPosition) == childPosition + 1) {
            holder.v_heng_xian.setVisibility(View.GONE);
        } else {
            holder.v_heng_xian.setVisibility(View.VISIBLE);
        }

        // //设置点击展开的
        // if (child_groupId == groupPosition&& child_childId == childPosition)
        // {
        // holder.layout.setVisibility(View.VISIBLE);
        // } else {
        // holder.layout.setVisibility(View.GONE);
        // }

        holder.layout.setOnClickListener(null);
        holder.child_dealNumber_content.setText(dealLogDetails.detailOptTime);
        holder.child_dealTime_content.setText(TimeUtil.getY_M_D_Type2(dealLogDetails.time));
        holder.child_remark_content.setText(dealLogDetails.remark);
        holder.tv_child_remark.setText(dealLogDetails.remark);

        switch (dealLogDetails.type) {
            case 1:// 充值
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_chong);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 2:// 提现
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_xian);
                setMoneyUpandDown(false, holder.tv_child_money, dealLogDetails.money);
                break;
            case 3:// 转入零钱宝
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_ru);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 4:// 转出零钱宝
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_chu);
                setMoneyUpandDown(false, holder.tv_child_money, dealLogDetails.money);
                break;
            case 5:// 投资定期标
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_biao);
                setMoneyUpandDown(false, holder.tv_child_money, dealLogDetails.money);
                break;
            case 6:// 定期标到期收益
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 7:// 零钱宝获得收益
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 8:// 定期标获得收益
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 9:// 银行卡转入零钱宝
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_chong);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 10:// 定期标到期还本
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_biao);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 11:// 金额冲正
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_chongzhen);
                if (dealLogDetails.money < 0) {
                    holder.tv_child_money.setText(MoneyFormatUtil.format(dealLogDetails.money));
                } else {
                    setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                }
                break;
            case 12:// 现金奖励
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 13:// 新手标到期收益
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 14:// 新手标到期还本
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_xinshoubiao);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 15:// 投资新手标
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_xinshoubiao);
                setMoneyUpandDown(false, holder.tv_child_money, dealLogDetails.money);
                break;
            case 16:// 新手标获得收益
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 17:// 活动现金奖励
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_zeng);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 22:// 邀请奖励
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_xian);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 23:// 邀请奖励
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_xian);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 24:// 周周升
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_zhou);
                setMoneyUpandDown(false, holder.tv_child_money, dealLogDetails.money);
                break;
            case 25:// 周周升获得收益
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 26:// 提前结清
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            case 27:// 周周升到期还本
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_shou);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
            default:
                holder.tv_deal_record_icon.setBackgroundResource(R.drawable.bill_details_xian);
                setMoneyUpandDown(true, holder.tv_child_money, dealLogDetails.money);
                break;
        }

        // 设置时间
        String md = TimeUtil.getFullTime(dealLogDetails.time, "MM-dd");
        String hm = TimeUtil.getFullTime(dealLogDetails.time, "HH:mm");
        holder.tv_child_time.setUpTextContent(md);
        holder.tv_child_time.setDownTextContent(hm);
        // 返回一个布局对象
        return convertView;

    }

    /**
     * 设置钱数是加还是减
     *
     * @param b true表示加，false减
     */
    private void setMoneyUpandDown(boolean b, TextView view, double money) {
        if (b) {
            view.setText("+" + MoneyFormatUtil.format(money));
            // view.setTextColor(Color.parseColor("#ff6434"));
        } else {
            view.setText("-" + MoneyFormatUtil.format(money));
            // view.setTextColor(Color.parseColor("#b7de7d"));
        }
    }

    /**
     * 当选择子节点的时候，调用该方法
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    private class HolderView {
        private TextView tv_deal_record_icon;
        private TextView tv_child_money;
        private UpDownTextView tv_child_time;
        private TextView tv_child_remark;
        private LinearLayout layout;
        private TextView child_dealNumber_content;
        private TextView child_dealTime_content;
        private TextView child_remark_content;
        private View v_heng_xian;
    }

}
