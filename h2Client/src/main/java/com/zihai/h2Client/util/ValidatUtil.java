package com.zihai.h2Client.util;

public class ValidatUtil {
    public static boolean isEmail(String email){
        String regex ="\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
        return email.matches(regex);
    }
    public static boolean isPhone(String phone){
        String regex = "(^(\\d{2,4}[-_－—]?)?\\d{3,8}([-_－—]?\\d{3,8})?([-_－—]?\\d{1,7})?$)|(^0?1[35]\\d{9}$)" ;
        return phone.matches(regex);
    }

    public static boolean hasOrignProp(String text){
        String regex = "\\b(state|createBy|createTime|updateBy|updateTime|id)\\b";
        return text.matches(regex);
    }


    public static boolean isIdentity(String identity) {
        String regex = "^\\d{15}|\\d{18}$";
        return identity.matches(regex);
    }

    public static boolean isHK(String stock) {
        String regex = "\\d+";
        return stock.matches(regex);
    }


    public static void main(String[] args) {
        /*System.out.println(isPhone("86+13386126649"));
        System.out.println(isPhone("133gve861249"));
        System.out.println(isEmail("xx@ss.com"));
        System.out.println(isEmail("xs.com"));*/
        System.out.println(isHK("PBTS"));
    }
}
