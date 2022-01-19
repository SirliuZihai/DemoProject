package com.zihai.h2Client.util;


import com.zihai.h2Client.core.DTOMapper;
import com.zihai.h2Client.dto.BusinessException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class BeaUtil {
    static HashMap<String, Method> cacheMap= new HashMap<>();
    static{
        for(Method method: DTOMapper.INSTANCE.getClass().getMethods()){
            if(method.getParameterCount() == 1)
                cacheMap.put(method.getReturnType().getSimpleName()+method.getParameterTypes()[0].getSimpleName(),method);
        }
    }

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
        Method method = cacheMap.get(target.getClass().getSimpleName()+source.getClass().getSimpleName());
        if(method!=null){
            try {
                target = (T)method.invoke(DTOMapper.INSTANCE,source);
            } catch (Exception e){
                throw new BusinessException(method.getName()+ " DTO转换异常:"+e.getMessage());
            }
        }else {
            BeanUtils.copyProperties(source,target);
        }
        return target;
    }

}
