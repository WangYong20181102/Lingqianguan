package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
/**
 * 通用样式2
 * @author Myf
 *
 */
public class UsuallyLayoutStyle2 extends RelativeLayout{
	/**
	 * 图标
	 */
	private ImageView imageView;
	/**
	 * 上面的文字
	 */
	private TextView tvContent;
	/**
	 * 打开或者关闭，默认是打开iade
	 */
	private boolean isCloseAndOpen=true;


	public UsuallyLayoutStyle2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		initData(context, attrs);
	}


	/**
	 * 初始化控件
	 * @param context
	 */
	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.widget_usually_layout_style2, this, true);
		imageView = (ImageView) view.findViewById(R.id.iamge);
		tvContent = (TextView) view.findViewById(R.id.tv_content);
	}
	
	private void initData(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UsuallyLayoutStyle2);
		//上面文字
		CharSequence strTvContent = a.getText(R.styleable.UsuallyLayoutStyle2_com_tv_content2);
		//图片
		Drawable image = a.getDrawable(R.styleable.UsuallyLayoutStyle2_com_image2);
		
		setTextContent(strTvContent);
		setImageDrawable(image);
	}
	
	/**
	 * 设置文字上面内容
	 */
	public void setTextContent(CharSequence strTvContent) {
		tvContent.setText(strTvContent+"");
	}
	
	/**
	 * 设置图片
	 */
	public void setImageDrawable(Drawable drawable) {
		imageView.setImageDrawable(drawable);
	}
}
