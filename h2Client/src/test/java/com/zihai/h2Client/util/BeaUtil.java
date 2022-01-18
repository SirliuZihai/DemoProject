package com.zihai.h2Client.util;


import com.zihai.h2Client.dto.BusinessException;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class BeaUtil {
    public static <S,T> List<T> transList(List<S> source,List<T> target,Class<T> tClass) {
        try {
            if(source!=null&&source.size()>0)
                for(Object o:source){
                    T a = tClass.getDeclaredConstructor().newInstance();
                    target.add(transObject(o,a));
                }
        } catch (Exception e) {
            throw new BusinessException("类型转换异常："+e.getMessage());
        }
        return target;
    }
    public static <S,T> T transObject(S source,T target){
        BeanUtils.copyProperties(source,target);
        return target;
    }

}
