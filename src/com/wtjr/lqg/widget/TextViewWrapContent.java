package com.wtjr.lqg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.wtjr.lqg.utils.DensityUtils;


/**
 * 控件内部内容根据控件的宽度自动适应
 * @author JoLie 
 * 
 */
public class TextViewWrapContent extends TextView {
	/**
	 * 字体大小
	 */
	private int fontSize;
	private Context mContext;
	
	private int selfWidth;

	public TextViewWrapContent(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TextViewWrapContent(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		getSelfSize();
	}

	public TextViewWrapContent(Context context) {
		super(context);
	}
	


//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		if (getWidth() > 0 && isWidth) {
//			isWidth = false;
//			float textWidth = getPaint().measureText(getText().toString());
//			setFontSize(textWidth, getWidth());
//		}
//	}
	
	private void getSelfSize() {
		ViewTreeObserver vto = TextViewWrapContent.this.getViewTreeObserver();  
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			public boolean onPreDraw() {
				TextViewWrapContent.this.getViewTreeObserver().removeOnPreDrawListener(
						this);

				selfWidth = TextViewWrapContent.this.getMeasuredWidth();
				
				float textWidth = getPaint().measureText(getText().toString());
				setFontSize(textWidth, selfWidth);

				return true;
			}
		});
	}


	/**
	 * 设置字体大小
	 * 
	 * @param textWidth
	 * @param getWidth
	 */
	private void setFontSize(float textWidth, int selfWidth) {
	    if(textWidth < selfWidth) {
	        return;
	    }
		float beishu = (float) selfWidth / textWidth;
		fontSize = (int) (getTextSize() * beishu) - 1;
		setTextSize(DensityUtils.px2sp(mContext, fontSize));
	}

	/**
	 * 重新设置字体大小
	 */
	public void resetSetFontSize() {
		float textWidth = getPaint().measureText(getText().toString());
		setFontSize(textWidth, getWidth());
	}
}
