package com.zihai.h2Client.springTest;

import com.zihai.h2Client.annotation.BusMethod;
import com.zihai.h2Client.annotation.BusService;
import com.zihai.h2Client.service.AdviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import static com.zihai.h2Client.util.BeanScanUtil.scanClass;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai.h2Client")
@SpringBootTest
public class SpringServiceTest {
    @Autowired
    AdviceService adviceService;
    HashMap<String,Method> methodMap = new HashMap<>();
    HashMap<String,Object> beanMap = new HashMap<>();

    @Test
    public void TestAdvice(){
        adviceService.test();
    }

    @Test
    public void Test() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Class> list = scanClass("com.zihai",BusService.class);
        for (Class serviceBean : list) {
            Method[] methods= serviceBean.getDeclaredMethods();
            for (Method method : methods) {
                BusMethod annotation = method.getAnnotation(BusMethod.class);
                if(annotation != null){
                    methodMap.put(annotation.value(), method);
                    beanMap.put(method.getDeclaringClass().getSimpleName(),serviceBean.newInstance());
                }
            }
        }
        Method method = methodMap.get("test");
        String s = (String)method.invoke(beanMap.get(method.getDeclaringClass().getSimpleName()),"hello beijing");
        System.out.println(s);
    }

}

