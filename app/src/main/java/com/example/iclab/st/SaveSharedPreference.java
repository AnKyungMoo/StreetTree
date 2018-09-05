package com.example.iclab.st;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_USER_NAME = "username";
    static final String PREF_USER_FULL = "userfull";
    static final String PREF_USER_DATA = "userdata";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserName(Context ctx, String userName, String userFull) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_USER_FULL, userFull);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    // 저장된 정보 가져오기
    public static String getUserFull(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_FULL, "");
    }

    // 리스트 저장
    public static void setUserData(Context ctx, String userData) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_DATA, userData);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserData(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_DATA, "");
    }

    // 로그아웃
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}