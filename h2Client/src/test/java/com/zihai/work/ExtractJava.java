package com.zihai.work;

import java.io.File;
import java.io.IOException;

public class ExtractJava {
    static String rootPath = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\h2Client\\target\\test-classes\\";
    public static void main(String[] args) throws IOException {
        //String rootPath  = "D:\\dev\\work_1.9jdk\\DemoProject\\h2Client\\target\\h2Client-1.0";
        extract(new File(rootPath));
    }
    public static void extract(File f) throws IOException {
        if(f.isFile()&&(f.getName().endsWith(".class")||f.getName().endsWith(".jar"))){
            String outPath = "D:\\dexout\\h2Client\\" +f.getName().replace(".class",".jar");
            File outfile = new File(outPath.substring(0,outPath.lastIndexOf("\\")));
            if(!outfile.exists()){
                outfile.mkdirs();
            }
            String exect = "dx --dex --output="+outPath + " "+f.getPath().replace(rootPath,"");
            //Runtime.getRuntime().exec(exect);
            System.out.println(exect);
        }
        else if(f.isDirectory()){
            for(File f1: f.listFiles()){
                extract(f1);
            }
        }
    }
}
