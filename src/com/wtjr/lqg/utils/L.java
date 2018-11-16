package com.wtjr.lqg.utils;

import android.util.Log;

import com.wtjr.lqg.constants.Constant;

public class L {
	
	public static <T> void hintV(T t){
		if(Constant.isLog)
		Log.v("wtjr", t + "");
	}
	
	public static <T> void hintD(T t){
		if(Constant.isLog)
		Log.d("wtjr", t + "");
	}
	 
	public static <T> void hintW(T t){
		if(Constant.isLog)
		Log.w("wtjr", t + "");
	}
	
	public static <T> void hintI(T t){
		if(Constant.isLog)
		Log.i("wtjr", t + "");
	}
	
	public static <T> void hintE(T t){
		if(Constant.isLog)
		Log.e("wtjr", t + "");
	}
}
