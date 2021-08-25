package com.wei.ysx.exception;

import com.wei.ysx.entity.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
	public Response exceptionHandler(Exception e){
       	return Response.error("服务异常" +e);
    }
}