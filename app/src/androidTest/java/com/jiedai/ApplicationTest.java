package com.jiedai;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.jiedai.utils.HttpUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void testGet(){

        HttpUtils httpUtils = HttpUtils.getInstance();


        String response = httpUtils.getSyn("http://www.baidu.com");

        System.out.println(response);


    }


}