package com.zihai.h2Client.test;

import java.io.Closeable;
import java.io.IOException;

public class TestClass implements Closeable {
    private String name;

    private SubClouse subClouse;


    TestClass(String name,SubClouse subClouse){
        this.name = name;
        this.subClouse = subClouse;
    }

    void deal(){
        System.out.println(name+"work");
    }

    @Override
    public void close() throws IOException {
        System.out.println(name+"close");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubClouse getSubClouse() {
        return subClouse;
    }

    public void setSubClouse(SubClouse subClouse) {
        this.subClouse = subClouse;
    }

    public static void main(String[] args) {
        try (SubClouse s = new SubClouse();TestClass c = new TestClass("liu",s)){
            c.deal();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
