package com.zihai.h2Client.test;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageTest {
    public static void main(String[] args) {
        Locale locale3 =  Locale.getDefault();
        System.out.println(locale3.toString());
        //Locale locale = new Locale("zh","US");
        ResourceBundle rb = ResourceBundle.getBundle("1180.hello", locale3);//获取本地化配置
        String result = rb.getString("1");
        System.out.println(result);
        Locale locale2 = new Locale("zh","CN");
        ResourceBundle rb2 = ResourceBundle.getBundle("1180.hello", locale2);//获取本地化配置
        String result2 = rb2.getString("1");
        System.out.println(result2);
        for(String key :rb.keySet()){
            System.out.println(key+" "+rb.getString(key));
        }
    }
}
