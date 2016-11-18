package com.jiedai.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.jiedai.app.MyApp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/17.
 */
public class HttpUtils {


    private static HttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler handler;

    private HttpUtils() {

        mOkHttpClient = new OkHttpClient().newBuilder().readTimeout(20,TimeUnit.SECONDS)
                .connectTimeout(20,TimeUnit.SECONDS)
                .build();

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case SHOW_TOAST:
                        String str = (String) msg.obj;
                        Toast.makeText(MyApp.app, str, Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

    }

    private static final int SHOW_TOAST = 100;


    public static synchronized HttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils();
                }
            }
        }
        return mInstance;
    }

    // 下面的是提供的工具

    /**
     * 异步的get请求
     *
     * @param url 联网地址
     * @param callback 回调对象，如需自己处理联网错误，重写 onFailure 方法
     */
    public void get(String url, ResultCallback callback) {
        Request request = new Request.Builder().url(url).method("GET",null).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 同步的get请求
     *
     *  @param url 联网地址
     *  @return  联网获得响应的字符串
     *      如果联网出错，抛出异常
     */
    public String getSyn(String url) throws IOException {
        String result = null;
        Request request = new Request.Builder().url(url).method("GET",null).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        result =  response.body().string();
        return result;
    }

    /**
     * 异步的post请求
     *
     * @param url 联网地址
     * @param callback 回调
     * @param params 参数
     */
    public void post(String url, Map<String, String> params,
                     ResultCallback callback) {

        FormBody.Builder bodyBuild = new FormBody.Builder();

        Set<String> keySet = params.keySet();
        for(String keyStr : keySet){
            String value = params.get(keyStr);
            bodyBuild.add(keyStr,value);
        }

        RequestBody requestBody = bodyBuild.build();
        Request request = new Request.Builder().url(url).method("POST",requestBody).build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(callback); // 执行
    }

    /**
     * post 请求
     * @param url
     * @param callback
     */
    public void post(String url, PostParams callback) {

        FormBody.Builder bodyBuild = new FormBody.Builder();

        bodyBuild =  callback.addParams(bodyBuild);// 添加参数

        RequestBody requestBody = bodyBuild.build();

        Request request = new Request.Builder().url(url).method("POST",requestBody).build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(callback); // 执行
    }





    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param fileCallBack
     */
    public void downloadFile( String url, final String destFileDir,
                             final DownLoadFileCallBack fileCallBack) {

        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new ResultCallback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                File file = FileUtils.saveStreamToFile(inputStream, destFileDir);
                fileCallBack.download(file);
            }
        });
    }

    public void showToast(String s) {
        Message msg = Message.obtain();
        msg.what = SHOW_TOAST;
        msg.obj = s;
        handler.sendMessage(msg);
    }

    /**
     * 下载文件成功后调用
     */
    public interface DownLoadFileCallBack{
        /**
         * 下载文件成功时，调用此方法
         * @param file
         */
        abstract void download(File file);

    }


    /**
     * post 请求时，添加参数
     */
    public abstract class PostParams extends ResultCallback{
        /**
         * 在此方法中添加 post 请求的参数
         *      builder.add(key,value);
         * @param builder
         * @return
         */
        public  abstract FormBody.Builder addParams(FormBody.Builder builder);
    }

    /**
     * 联网获得结果时，错误情况统一处理，正确的情况由子类处理
     */
    public abstract class ResultCallback implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
           showToast("联网失败，请稍候重试:"+e.getMessage());
        }
    }

}
