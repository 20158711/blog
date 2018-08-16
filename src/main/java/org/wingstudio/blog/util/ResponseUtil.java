package org.wingstudio.blog.util;

import org.springframework.http.ResponseEntity;
import org.wingstudio.blog.common.ConstrainViolationExceptionHandler;
import org.wingstudio.blog.common.Response;

import javax.validation.ConstraintViolationException;

public class ResponseUtil {
    public static ResponseEntity<Response> error(Exception e){
        if (e instanceof ConstraintViolationException)
            return ResponseEntity.ok().body(new Response(false,ConstrainViolationExceptionHandler.getMessage((ConstraintViolationException)e)));
        else
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
    }

    public static ResponseEntity<Response> error(String str){
        return ResponseEntity.ok().body(new Response(false,str));
    }

    public static ResponseEntity<Response> success(){
        return ResponseEntity.ok().body(new Response(true,"success"));
    }

    public static ResponseEntity<Response> success(Object o){
        return ResponseEntity.ok().body(new Response(true,"success",o));
    }

    public static ResponseEntity<Response> success(String msg,Object o){
        return ResponseEntity.ok().body(new Response(true,msg,o));
    }
}
