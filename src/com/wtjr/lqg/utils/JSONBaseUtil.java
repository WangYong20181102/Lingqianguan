package com.wtjr.lqg.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.VersionBase;

/**
 * 基类解析工具
 *
 * @author JoLie
 */
public class JSONBaseUtil {
    public static JSONBase getJSONBase(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return null;
        }

        String message = null;
        String invoking = null;
        String alertCode = null;
        String disposeResult = null;
        String versionInfo = null;

        try {
            disposeResult = jsonObject.getString("disposeResult");
            versionInfo = jsonObject.getString("versionInfo");

            String invokingResult = jsonObject.getString("invokingResult");
            JSONObject jsonObject2 = new JSONObject(invokingResult);

            message = jsonObject2.getString("message");
            invoking = jsonObject2.getString("invoking");
            alertCode = jsonObject2.getString("alertCode");
        } catch (JSONException e) {
            e.printStackTrace();
            L.hintE("基类解析错误-->" + e.toString());
            return null;
        }

        JSONBase jsonBase = new JSONBase();
        jsonBase.setMessage(message);
        jsonBase.setInvoking(invoking);
        jsonBase.setAlertCode(alertCode);
        jsonBase.setDisposeResult(disposeResult);
        jsonBase.setVersionInfo(versionInfo);

        return jsonBase;
    }


    public static VersionBase getVersionBase(String versionInfo) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(versionInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return null;
        }

        int forceUpdate = 0;
        int AndroidIsDo = 1;
        String updateContent = "";
        String AndroidVersion = "2.0";
        String url = "";
        String AndroidPromptContent = "";

        try {
            updateContent = jsonObject.getString("AndroidUpdateContent");
            forceUpdate = jsonObject.getInt("AndroidForceUpdate");
            AndroidIsDo = jsonObject.getInt("AndroidIsDo");
            url = jsonObject.getString("AndroidUrl");
            AndroidVersion = jsonObject.getString("AndroidVersion");
            AndroidPromptContent = jsonObject.getString("AndroidPromptContent");
        } catch (JSONException e) {
            e.printStackTrace();
            L.hintE("基类解析错误-->" + e.toString());
        }

        VersionBase versionBase = new VersionBase();
        versionBase.setForceUpdate(forceUpdate);
        versionBase.setUpdateContent(updateContent);
        versionBase.setUrl(url);
        versionBase.setAndroidVersion(AndroidVersion);
        versionBase.setAndroidPromptContent(AndroidPromptContent);
        versionBase.setAndroidIsDo(AndroidIsDo);

        return versionBase;
    }
}
