package com.wei.ysx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private long code;
    private String message;
    private Object Data;

    public static Response success(String message){
        return new Response(200, message,null);
    }

    public static Response success(String message, Object data){
        return new Response(200, message,data);
    }

    public static Response success(Object data){
        return new Response(200, "请求成功",data);
    }


    public static Response error(String message){
        return new Response(500, message,null);
    }

    public static Response error(String message, Object data){
        return new Response(500, message,data);
    }
}
