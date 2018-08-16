package org.wingstudio.blog.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.util.StringUtil;

public class SecurityUtil {
    public static boolean isOwner(String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication !=null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal().toString())){
            User user= (User) authentication.getPrincipal();
            if (user!=null && StringUtil.equals(user.getUsername(),username)){
                return true;
            }
        }
        return false;
    }
    public static User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if("anonymousUser".equals(authentication.getPrincipal().toString()))
            return null;
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
