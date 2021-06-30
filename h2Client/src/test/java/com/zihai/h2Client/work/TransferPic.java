package com.zihai.h2Client.work;

import com.zihai.h2Client.util.ImageTools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class TransferPic {
    public static void main(String[] args) throws IOException {
        System.out.println("start work");
        ImageTools.amendRotatePhoto("C:\\Users\\ros\\Pictures\\2.png","C:\\Users\\ros\\Pictures\\2.png");
        //System.out.println(tiffTurnJpg("C:\\Users\\ros\\Pictures\\2.jpg"));
    }
    public static String tiffTurnJpg(String filePath){
        try {
            BufferedImage file = ImageIO.read(new FileInputStream(filePath));//读取图片文件
            String newPath = filePath.substring(0,filePath.lastIndexOf("."))+".jpg";
            OutputStream ops  = new FileOutputStream(newPath);
            //文件存储输出流
            ImageIO.write(file, "jpg", ops);
            return filePath;
        } catch (FileNotFoundException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
