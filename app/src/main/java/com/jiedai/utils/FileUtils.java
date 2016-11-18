package com.jiedai.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    /**
     * 将输入流保存至文件
     * @param input 输入流
     * @param filePath 文件路径
     * @return
     */
    public static File saveStreamToFile(InputStream input, String filePath){

        byte [] buffer = new byte[1024];
        int len  =-1;
        File file =  new File(filePath);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            while((len =input.read(buffer))!=-1 ){
                fout.write(buffer,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }
        return file;
    }

}
