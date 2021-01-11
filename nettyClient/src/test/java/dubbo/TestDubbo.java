package dubbo;

import com.zihai.base.service.TestService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes=TestDubbo.class)
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@EnableDubbo
@ComponentScan("com.zihai")
public class TestDubbo {
    @Reference
    private TestService testService;

    @Test
    public void main2(){
        String s = testService.sayHello();
        System.out.println(s);
    }

}
