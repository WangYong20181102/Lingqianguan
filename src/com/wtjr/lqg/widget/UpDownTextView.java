package com.wtjr.lqg.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.widget.risenumber.RiseNumberTextView;

public class UpDownTextView extends LinearLayout{
    /**
     * 上面TextView控件
     */
    private TextView tvUp;
    /**
     * 下面TextView控件
     */
    private RiseNumberTextView tvDown;
    /**
     * 上面TextView控件文本内容
     */
    private String mUpTextContent;
    /**
     * 下面TextView控件文本内容
     */
    private String mDownTextContent;
    /**
     * 上面TextView控件文字的大小
     */
    private float mUpTextSize;
    /**
     * 下面TextView控件文字的大小
     */
    private float mDownTextSize;
    /**
     * 上面TextView控件文字的颜色
     */
    private int mUpTextColor;
    /**
     * 下面TextView控件文字的颜色
     */
    private int mDownTextColor;
    /**
     * 上下TextView之间的间距
     */
    private float mUpDownMargin;
    /**
     * 控件的对齐方式
     */
    private int mTextGravity;

    public UpDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UpDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context, attrs);
    }

    public UpDownTextView(Context context) {
        super(context);
    }
    
    /**
     * 初始化控件
     * @param context
     */
    private void initView(Context context) {
        if(isInEditMode()) {
            return;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.widget_up_down_textview, this, true);
        tvUp = (TextView) view.findViewById(R.id.tv_up);
        tvDown = (RiseNumberTextView) view.findViewById(R.id.tv_down);
    }
    
    @SuppressLint("RtlHardcoded")
    private void initData(Context context, AttributeSet attrs) {
        if(isInEditMode()) {
            return;
        }
        
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UpDownTextView);
        mUpTextContent = ta.getString(R.styleable.UpDownTextView_up_text_content);
        mDownTextContent = ta.getString(R.styleable.UpDownTextView_down_text_content);
        mUpTextSize = ta.getDimension(R.styleable.UpDownTextView_up_text_size, 25); 
        mDownTextSize = ta.getDimension(R.styleable.UpDownTextView_down_text_size, 20); 
        mUpTextColor = ta.getColor(R.styleable.UpDownTextView_up_text_color, Color.parseColor("#000000"));
        mDownTextColor = ta.getColor(R.styleable.UpDownTextView_down_text_color, Color.parseColor("#000000"));
        mUpDownMargin = ta.getDimension(R.styleable.UpDownTextView_up_down_margin, 0);
        mTextGravity= ta.getInt(R.styleable.UpDownTextView_text_gravity, Gravity.CENTER);
        ta.recycle();
        
        tvUp.setText(mUpTextContent);
        tvDown.setText(mDownTextContent);
        
        tvUp.setTextSize(TypedValue.COMPLEX_UNIT_PX, mUpTextSize);
        tvDown.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDownTextSize);
        
        tvUp.setGravity(mTextGravity);
        tvDown.setGravity(mTextGravity);
       
        tvUp.setTextColor(mUpTextColor);
        tvDown.setTextColor(mDownTextColor);
        
        LayoutParams layoutParams = (LayoutParams) tvDown.getLayoutParams();
        layoutParams.topMargin = (int) mUpDownMargin;
        tvDown.setLayoutParams(layoutParams);//设置上下文本之间的间距
    }
    
    /**
     * 设置上面文本的字体大小
     * @param upTextSize
     */
    public void setUpTextSize(float upTextSize) {
        tvUp.setTextSize(TypedValue.COMPLEX_UNIT_PX,upTextSize);
    }
    
    /**
     * 设置下面文本的字体大小
     * @param downTextSize
     */
    public void setDownTextSize(float downTextSize) {
        tvDown.setTextSize(TypedValue.COMPLEX_UNIT_PX,downTextSize);
    }
    
    /**
     * 设置上面文本的字体颜色
     * @param upTextColor
     */
    public void setUpTextColor(int upTextColor) {
        tvUp.setTextColor(upTextColor);
    }
    
    /**
     * 设置下面文本的字体颜色
     * @param downTextColor
     */
    public void setDownTextColor(int downTextColor) {
        tvDown.setTextColor(downTextColor);
    }
    
    
    /**
     * 设置上下面文本的间距
     * @param upDownMargin
     */
    public void setUpDownMargin(int upDownMargin) {
        LayoutParams layoutParams = (LayoutParams) tvDown.getLayoutParams();
        layoutParams.topMargin = (int) upDownMargin;
        tvDown.setLayoutParams(layoutParams);
    }
    
    /**
     * 设置上面文本的内容
     * @param upTextContent
     */
    public void setUpTextContent(String upTextContent) {
        tvUp.setText(upTextContent);
    }
    
    /**
     * 设置下面文本的内容
     * @param string
     */
    public void setDownTextContent(String downTextContent) {
        tvDown.setText(downTextContent);
    }
    
    /**
	 * 设置数字跑动
	 * 
	 * @param startNumber 起始数
	 * @param endNumber 终止数
	 * @param duration 持续时间(毫秒)
	 */
	public void withNumber(String startNumber,String endNumber) {
		tvDown.withNumber(startNumber, endNumber).start();
	}
	
	public String getDownTextContent(){
	    String content = tvDown.getText().toString();
		return content.replace(",", "");
	}
}