package com.wtjr.lqg.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class SignUtil {
	
	public static StringBuilder CreateLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuilder prestr = new StringBuilder();
		String key = "";
		String value = "";
		for (int i = 0; i < keys.size(); i++) {
			key = (String) keys.get(i);
			value = (String) params.get(key);
			if ("".equals(value) || value == null
					|| key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			prestr.append(value).append("|");
		}
		return prestr;
	}
	
	public static String BuildAppsign(Map<String, String> sArray, String key) {
		if (sArray != null && sArray.size() > 0) {
			StringBuilder prestr = CreateLinkString(sArray);
			System.out.println("********************代签名字符串为：" + prestr.toString() + key);
			return Md5Utils.md5(prestr.toString() + key, "UTF-8");
		}
		return null;
	}
}
