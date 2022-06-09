package com.zihai.h2Client.util;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StringUtil {
    // 32个字符，用来表示32进制
    final static char[] digits = {
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' , 'A' , 'B' ,
            'C' , 'D' , 'E' , 'F' , 'G' , 'H' , 'I',
            'J' , 'K' , 'L' , 'M' , 'N' ,'O', 'P' ,
            'Q' , 'R' , 'T' , 'U' , 'V' , 'W' ,
            'X' , 'Y'
    };
    /**
     * long类型转为32进制，指定了使用的字符，参考Long.toUnsignedString0
     * @param val
     * @return
     */
    static String digits32(long val) {
        // 32=2^5=二进制100000
        int shift = 5;
        // numberOfLeadingZeros 获取long值从高位连续为0的个数，比如val=0，则返回64
        // 此处mag=long值二进制减去高位0之后的长度
        int mag = Long.SIZE - Long.numberOfLeadingZeros(val);
        int len = Math.max(((mag + (shift - 1)) / shift), 1);
        char[] buf = new char[len];
        do {
            // &31相当于%32
            buf[--len] = digits[((int) val) & 31];
            val >>>= shift;
        } while (val != 0 && len > 0);
        return new String(buf);
    }

    public static String transfer32(Long l) {
        return digits32(l);
    }

    public static String join(final Iterable<?> target, final String separator) {
        if (separator == null)
            throw new IllegalArgumentException("Separator cannot be null");

        if (target == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        final Iterator<?> it = target.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(separator);
                sb.append(it.next());
            }
        }
        return sb.toString();

    }

    public static void main(String[] args) {
        System.out.println(transfer32(1686216l));
        System.out.println(Long.toString(1686216l, 32).toUpperCase());

        Set<String> o = new HashSet<>();
        o.add("1aasdf");
        o.remove(1);
    }
}
