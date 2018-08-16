package org.wingstudio.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends AbstractErrorController {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @ExceptionHandler(Exception.class)
    public Object handle(Exception e,HttpServletRequest request){
        String remoteAddr = request.getRemoteAddr();
        log.error("host {} 访问 {} 出现 {} 错误",remoteAddr,request.getServletPath(),e.getMessage());
        return "访问出错";
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}
