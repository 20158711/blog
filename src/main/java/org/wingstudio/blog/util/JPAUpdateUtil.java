package org.wingstudio.blog.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JPAUpdateUtil {

    public static void copyNotNullProperity(Object src,Object dest){
        BeanUtils.copyProperties(src,dest,getNullProperties(src));
    }

    private static String[] getNullProperties(Object src){
        BeanWrapper srcBean=new BeanWrapperImpl(src);
        PropertyDescriptor[] descriptors = srcBean.getPropertyDescriptors();
        Set<String> emptyName=new HashSet<>();
        Arrays.stream(descriptors).forEach(desc->{
            Object o = srcBean.getPropertyValue(desc.getName());
            if (o == null) {
                emptyName.add(desc.getName());
            }
        });
        String[] result=new String[emptyName.size()];
        return emptyName.toArray(result);
    }

}
