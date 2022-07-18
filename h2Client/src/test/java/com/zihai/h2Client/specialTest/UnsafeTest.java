package com.zihai.h2Client.specialTest;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTest {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        long addr = unsafe.allocateMemory(4);
        unsafe.setMemory(addr, 4, (byte) 1);
        System.out.println(unsafe.getInt(addr));  //00000001 00000001 00000001 00000001  ->16843009
        unsafe.freeMemory(addr);
    }
}
