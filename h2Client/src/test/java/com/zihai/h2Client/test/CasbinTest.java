package com.zihai.h2Client.test;


import org.apache.commons.io.IOUtils;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CasbinTest {
    public static void main(String[] args) throws IOException {
        String text = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("rbac_model.conf"), Charset.defaultCharset());
        Model model = Enforcer.newModel(text);
        List<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        model.addPolicy("p", "p", list);
        Enforcer enforcer = new Enforcer(model);
        Assert.isTrue(enforcer.enforce("1", "2"));
        enforcer.clearPolicy();
        List list2 = new ArrayList();
        list2.add("2");
        list2.add("3");
        model.addPolicy("p", "p", list2);
        Assert.isTrue(enforcer.enforce("1", "2") == false);
        Assert.isTrue(enforcer.enforce("2", "3"));

        enforcer.addRoleForUser("lyz", "admin");
        enforcer.addRoleForUser("lyz", "guester");
        List lis = enforcer.getRolesForUser("lyz");
        System.out.println(lis);
        Collections.sort(list, (x, y) -> Integer.parseInt(x) > Integer.parseInt(y) == true ? 1 : -1);
        System.out.println(list);
        Collections.sort(list, (x, y) -> Integer.parseInt(x) > Integer.parseInt(y) == false ? 1 : -1);
        System.out.println(list);
    }
}
