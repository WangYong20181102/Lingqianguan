package com.wtjr.lqg.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.TimeUtil;

public class TestClass extends AndroidTestCase{
	public void testMethods() throws Exception{
//		115.63332429804744,35.29737984214894
//		115.63331595357681,35.2973881702311
	    
	    String str = "150000.01";
	    
	    Log.i("LogContent", Float.parseFloat(str)+"==================="+Double.parseDouble(str));
	    
	    
	    String fullTime = TimeUtil.getFullTime(86400000, "HH:mm:ss");
	    L.hintD("======================="+fullTime);
	}
	
	private <T> void sssss(T t){
		if(t instanceof List){
			int i = ((List) t).size();
			L.hintD(i+"============List============");
		}else{
			L.hintD("============不是============");
		}
	}
}

