package com.zihai.h2Client.work;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Test {
    public static void main(String[] args) {
      /*  TestDto dto = new TestDto();
        List<People> arrayList = new ArrayList<>();
        dto.setPeoples(arrayList);
        People p = new People("liu",23);
        arrayList.add(p);
        p = new People("liu",24);
        System.out.println(JsonUtils.toJsonString(arrayList));*/
        BigDecimal x = new BigDecimal(5);
        BigDecimal y = new BigDecimal(50);
        BigDecimal m = new BigDecimal(0);
        BigDecimal total;
        for (int i = 1; i < 100; i++) {
            BigDecimal z0 = total(x.add(BigDecimal.ONE), y, m);
            BigDecimal z1 = total(x, y.add(BigDecimal.valueOf(2)), m);
            BigDecimal z3;
            if (m.compareTo(BigDecimal.valueOf(46)) < 0) {
                z3 = total(x, y, m.add(BigDecimal.valueOf(1.5)));
            } else {
                z3 = total(x, y, m);
            }
            if (z0.compareTo(z1) >= 0 && z0.compareTo(z3) >= 0) {
                x = x.add(BigDecimal.ONE);
                total = z0;
            } else if (z1.compareTo(z0) >= 0 && z1.compareTo(z3) >= 0) {
                y = y.add(BigDecimal.valueOf(2));
                total = z1;
            } else {
                if (m.compareTo(BigDecimal.valueOf(46)) < 0) {
                    m = m.add(BigDecimal.valueOf(1.5));
                }

                total = z3;
            }


            System.out.println(String.format("time:%s,x:%s,y:%s,m:%s,total:%s,benefit:%s", i, x, y, m, total, total.divide(new BigDecimal(i), 4, RoundingMode.HALF_UP)));
        }

    }

    public static BigDecimal total(BigDecimal x, BigDecimal y, BigDecimal m) {
        return divide(m, BigDecimal.valueOf(100)).add(BigDecimal.ONE).multiply(divide(y.multiply(x), BigDecimal.valueOf(10000)).add(BigDecimal.ONE));
    }

    public static BigDecimal divide(BigDecimal x, BigDecimal y) {
        return x.divide(y, 4, RoundingMode.HALF_UP);
    }
}
