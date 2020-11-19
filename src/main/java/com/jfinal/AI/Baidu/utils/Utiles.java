package com.jfinal.AI.Baidu.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

/**
 * 工具类
 * @author chenhongyang
 *
 */

public class Utiles {

	/**
	 *  获取指定长度的随机字符串
	 * @param length  获取随机字符串的长度
	 * @return 对应长度的随机字符串
	 */
	public static String getRandomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	
	
//	public static InputStream inStream = null;
	/**
	 *  
	 * @param path  "F:\\img\\"    图片存放在本地的路径
	 * @param imgUrl     图片链接
	 */
    public static String downloadPicture(String path, String imgUrl) {
    	
    	File file = new File(path);
    	if(!file.exists()) {
    		try {
				file.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
        URL url = null;
        int imageNumber = 0;
        String imagePath = "";  // 图片在本地的绝对路径
        try {
            url = new URL(imgUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            String name = Utiles.getRandomString(60);
//            String suffix = imgUrl.substring(imgUrl.lastIndexOf("."), imgUrl.length());  // 获取图片后缀
            imagePath =  path + name + ".jpg";

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imagePath));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }
    
    
    
    public static void main(String[] args) {
    	
    	// 测试 Utiles.downloadPicture()
        String imgUrl = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2389924891,2442295862&fm=26&gp=0.jpg";
        downloadPicture("F:\\img\\", imgUrl);
        System.out.println("ok");
    }
}



















