package com.zihai.h2Client.util;

import com.zihai.h2Client.config.DTOMapper;
import com.zihai.h2Client.dto.BusinessException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class BeanTransferUtil {
    static HashMap<String, Method> cacheMap = new HashMap<>();

    static {
        for (Method method : DTOMapper.INSTANCE.getClass().getMethods()) {
            if (method.getParameterCount() == 1)
                cacheMap.put(method.getReturnType().getSimpleName() + method.getParameterTypes()[0].getSimpleName(), method);
        }
    }

    public static <S, T> List<T> transList(List<S> source, List<T> target, Class<T> tClass) {
        if (source != null && source.size() > 0)
            for (Object o : source) {
                target.add(transObject(o, tClass));
            }

        return target;
    }

    public static <S, T> T transObject(S source, Class<T> targetClazz) {
        try {
            Method method = cacheMap.get(targetClazz.getSimpleName() + source.getClass().getSimpleName());
            T target;
            if (method != null) {
                try {
                    target = (T) method.invoke(DTOMapper.INSTANCE, source);
                } catch (Exception e) {
                    throw new BusinessException(method.getName() + " DTO转换异常:" + e.getMessage());
                }
            } else {
                target = targetClazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, target);
            }
            return target;
        } catch (Exception e) {
            throw new BusinessException("类型转换异常：" + e.getMessage());
        }
    }

}
