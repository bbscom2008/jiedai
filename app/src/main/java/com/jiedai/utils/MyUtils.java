package com.jiedai.utils;

/**
 * Created by Administrator on 2016/11/17.
 */
public class MyUtils {

    /**
     * 返回当前时间
     * @return
     */
    public static String now(){
        return java.text.DateFormat.getTimeInstance().format(System.currentTimeMillis());

    }

}
