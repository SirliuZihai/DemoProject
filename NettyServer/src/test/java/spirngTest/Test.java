/*
package spirngTest;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test.class)
public class Test {
    private String active;
    @Value("${spring.profiles.active}")
    public void setActive(String evn){
        if("test".equals(evn)) {
            this.active = "fase";
        }else {
            this.active = evn;
        }

    }
    @org.junit.Test
    public void bAusiness(){
        System.out.println(active);
    }
}
*/
