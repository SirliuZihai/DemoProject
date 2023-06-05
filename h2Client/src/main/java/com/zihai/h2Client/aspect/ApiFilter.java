package com.zihai.h2Client.aspect;

import com.google.gson.JsonObject;
import com.zihai.h2Client.dto.BusinessException;
import com.zihai.h2Client.util.JsValidate;
import com.zihai.h2Client.util.JsonHelp;
import com.zihai.h2Client.util.MyHttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.script.ScriptException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class ApiFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(ApiFilter.class);
    private static JsonObject validate_map;

    static {
        InputStream in = null;
        try {
            in = new ClassPathResource("validate/iim_validate.json").getInputStream();
            String str = IOUtils.toString(in,StandardCharsets.UTF_8);
            validate_map = JsonHelp.gson.fromJson(str,JsonObject.class);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init----ApiFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest res = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper(res);
        //打印入参
        if(!WhiteList.noLogSet.contains(res.getServletPath()))
            logger.info("reqUrl=={},requestBody=={}", res.getServletPath(), requestWrapper.getBodyStr());
        //参数校验
        if(validate_map.get(res.getServletPath())!=null){
            try {
                String result = (String) JsValidate.engine.invokeFunction(validate_map.get(res.getServletPath()).getAsString(), requestWrapper.getBodyStr());
                if(JsonHelp.gson.fromJson(result,JsonObject.class).get("code").getAsInt() != 1){
                    resp.getOutputStream().write(result.getBytes());
                    return;
                }
            } catch (NoSuchMethodException e) {
                throw new BusinessException("校验异常,校验方法不存在");
            } catch (ScriptException e) {
                logger.error(e.getMessage(),e);
                throw new BusinessException("js校验脚本异常 error="+e.getMessage());
            }
        }
        // 白名单 no login
        for(String key :WhiteList.whiteMap.keySet()){
            if(res.getServletPath().contains(key)) {
                chain.doFilter(requestWrapper, resp);
                return;
            }
        }

        //res.setAttribute("curUser",user);
        MDC.put("USER_ID", "357");
        chain.doFilter(requestWrapper, resp);
        MDC.clear();
    }

    @Override
    public void destroy() {

    }

    /*public static User getCurrentUser(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return (User)request.getAttribute("curUser");
    }*/

}
