package com.zihai.h2Client.aspect;

import com.zihai.h2Client.dto.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.zihai.h2Client.util.JsonHelp.gson;

@Component
@Aspect
public class ControllerAOP {
    private static Logger logger = LoggerFactory.getLogger(ControllerAOP.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Pointcut("execution(public * com.zihai.h2Client.controller..*.*(..))")
    public void doWeb() {
    }

    @Around("doWeb()")
    public Object doWeb(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        //滑动窗口
        Long cur_time = System.currentTimeMillis();
        BoundZSetOperations operations = redisTemplate.boundZSetOps(request.getRemoteHost());
        operations.removeRangeByScore(0, cur_time - 60 * 1000); //窗口一小时  3600*1000
        Long time = operations.size();
        if (time >= 100) {
            throw new BusinessException("1分钟限制100次请求");
        }

        // 获得当前访问的class
        Class<?> className = joinPoint.getTarget().getClass();
        // 获得访问的方法名
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.debug("{}.{}输入参数=={}", className, methodName, gson.toJson(args));

        //*******************************校验层*****************************
        String msToken = request.getHeader("msToken");

        //放入代码，不作反射
        //*******************************校验层*****************************
        Object res = joinPoint.proceed();
        logger.debug("{}.{}输出参数=={}", className, methodName, gson.toJson(res));
        return res;


    }
}
