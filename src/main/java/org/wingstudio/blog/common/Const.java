package org.wingstudio.blog.common;

public class Const {
    public static final String REMEMBER_ME_KEY="remember-me";
    public interface ROLES{
        Long ROLE_ADMIN_AUTHORITY_ID=1L;
        Long ROLE_USER_AUTHORITY_ID=2L;
        String ROLE_ADMIN_DESC="ROLE_ADMIN";
        String ROLE_USER_DESC="ROLE_USER";
    }
    public interface ORDER_PROPERITY{
        String HOT="hot";
        String NEW="new";
    }
}
