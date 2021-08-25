package com.wei.ysx.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wei.ysx.entity.Goods;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class URLConnectionUtils {

    public static String sendRequest(String urlParam) {

        URLConnection con = null;  

        BufferedReader buffer = null; 
        StringBuffer resultBuffer = null;  

        try {
             URL url = new URL(urlParam); 
             con = url.openConnection();  

            //设置请求需要返回的数据类型和字符集类型
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            //允许写出
            con.setDoOutput(true);
            //允许读入
            con.setDoInput(true);
            //不使用缓存
            con.setUseCaches(false);
            //得到响应流
            InputStream inputStream = con.getInputStream();
            //将响应流转换成字符串
            resultBuffer = new StringBuffer();
            String line;
            buffer = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((line = buffer.readLine()) != null) {
                resultBuffer.append(line);
            }
            return resultBuffer.toString();

        }catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) throws JsonProcessingException {
        String url ="http://route.showapi.com/66-22?showapi_appid=572140&showapi_sign=2196b3a9cefa4ae3bcaeb2e5aab56c3e&code=6958137689039";

        String s = sendRequest(url);
        Goods parse = JsonParser.parseGoods(s, Goods.class);

        System.out.println(parse);
    }
}
