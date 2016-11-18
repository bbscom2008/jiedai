package com.jiedai.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/11/17.
 */
public class LogUtils {

    private static boolean isShow = true;

    public static void logleo(String msg){
        if(isShow){
            Log.i("leo",msg);
        }
    }

}
