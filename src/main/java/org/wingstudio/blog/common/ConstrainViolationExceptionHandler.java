package org.wingstudio.blog.common;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ConstrainViolationExceptionHandler {

    public static String getMessage(ConstraintViolationException e){
        StringBuffer buffer=new StringBuffer();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            buffer.append(constraintViolation.getMessage());
            buffer.append(",");
        }
        buffer.replace(buffer.length()-1,buffer.length(),"");
        return buffer.toString();
    }

}
