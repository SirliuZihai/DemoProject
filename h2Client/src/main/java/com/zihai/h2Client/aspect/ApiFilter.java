package com.zihai.h2Client.aspect;

import com.google.gson.JsonObject;
import com.zihai.h2Client.util.JsValidate;
import com.zihai.h2Client.util.JsonHelp;
import com.zihai.h2Client.util.MyHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.script.ScriptException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Component
public class ApiFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(ApiFilter.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init----ApiFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest res = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper(res);

        //参数校验
        if(StringUtils.isEmpty(requestWrapper.getBodyStr())){
            JsonObject obj = new JsonObject();
            Enumeration<String> enumeration =  res.getParameterNames();
            while (enumeration.hasMoreElements()){
                String key = enumeration.nextElement();
                obj.addProperty(key,res.getParameter(key));
            }
            requestWrapper.setBody(obj.toString().getBytes());
        }
        InputStream in = new ClassPathResource("validate/iim_validate.json").getInputStream();
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        String str = new String(bytes, StandardCharsets.UTF_8);
        JsonObject map = JsonHelp.gson.fromJson(str,JsonObject.class);
        if(map.get(res.getServletPath()).getAsString()!=null){
            try {
                String result = (String) JsValidate.engine.invokeFunction(map.get(res.getServletPath()).getAsString(), requestWrapper.getBodyStr());
                if(JsonHelp.gson.fromJson(result,JsonObject.class).get("code").getAsInt() != 1){
                    resp.getOutputStream().write(result.getBytes());
                    return;
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("校验异常,校验方法不存在");
            } catch (ScriptException e) {
                throw new RuntimeException("js校验脚本异常");
            }
        }
        // 白名单 no login
        for(String key :WhiteList.whiteMap.keySet()){
            if(res.getServletPath().contains(key)) {
                chain.doFilter(requestWrapper, resp);
                return;
            }
        }
        chain.doFilter(requestWrapper, resp);
    }

    @Override
    public void destroy() {

    }

    public static Object getCurrentUser(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request.getAttribute("curUser");
    }

}
