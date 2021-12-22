package com.zihai.h2Client.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import java.util.List;

public class SpringSecurityTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityTest.class);
    static ObjectMapper mapper = new ObjectMapper();

    static {
        ClassLoader loader = SpringSecurityTest.class.getClassLoader();
        List<Module> modules = SecurityJackson2Modules.getModules(loader);
        mapper.registerModules(modules);
    }
    public static void main(String[] args) throws JsonProcessingException {
        SecurityContext context1 = SecurityContextHolder.createEmptyContext();
        Authentication authentication1 =
                new UsernamePasswordAuthenticationToken("zihaijun", "haha", AuthorityUtils.commaSeparatedStringToAuthorityList("root,department"));
        context1.setAuthentication(authentication1);
        SecurityContextHolder.setContext(context1);

        SimpleAsyncTaskExecutor delegateExecutor =
                new SimpleAsyncTaskExecutor();
        //ThreadPoolExecutor executor1 = new ThreadPoolExecutor(1,5,5, TimeUnit.MINUTES,new ArrayBlockingQueue<>(5));
        DelegatingSecurityContextExecutor executor =
                new DelegatingSecurityContextExecutor(delegateExecutor, context1);

        LOGGER.info(mapper.writeValueAsString(context1));
        for(int i=0;i<50;i++){
            int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    SecurityContext context = SecurityContextHolder.getContext();
                    Authentication authentication = context.getAuthentication();
                    String username = authentication.getName();
                    Object principal = authentication.getPrincipal();
                    //Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                    LOGGER.info("index:{} username:{},principal:{}",finalI,username,principal);
                }
            });
        }
    }
}
