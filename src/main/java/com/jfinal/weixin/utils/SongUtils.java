package com.jfinal.weixin.utils;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 用于歌曲的工具类
 * @author chenhongyang
 *
 */
public class SongUtils {
	
    /**
     * 获取 token
     *
     * @return token值
     */
    public static String getToken() {
        String token = "";
        try {
//            方法一
            URL url = new URL("http://www.kuwo.cn");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String setCookies = httpURLConnection.getHeaderField("Set-Cookie");
//            System.out.println(setCookies);
            String[] splits = setCookies.split(";");
            for (int i = 0; i < splits.length; i++) {
                if (splits[i].contains("kw_token")) {
                    token = splits[i].split("=")[1];
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
    

    /**
     * searchName   查询该歌手的歌曲
     *
     * @param searchName 歌手的名字
     * @param page       查询的页数
     */
    public static String musicList(String searchName, int page) {
    	
    	// 解决搜索内容为中文时报错的问题
    	searchName = URLEncoder.encode(searchName, StandardCharsets.UTF_8);
    	
    	String rn = "5";
    	String result = "";
        String urlString = "https://kuwo.cn/api/www/search/searchMusicBykeyWord?" +
                "key=" + searchName + "&pn=" + page + "&rn=" + rn + "&httpsStatus=1&reqId=f94cbc40-2488-11eb-bdad-1b8a24fee8b5";

        System.out.println("urlString:" + urlString);
        
        Connection.Response response = null;

        try {
            String token = SongUtils.getToken();  // 获取 token
            System.out.println("token:" + token);
            
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestProperty("Cookie", "kw_token="+token);
            httpURLConnection.setRequestProperty("csrf", token);
            httpURLConnection.setRequestProperty("Referer", "https://kuwo.cn/search/list?key=%E5%91%A8%E6%9D%B0%E4%BC%A6");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
            result = bufferedReader.readLine();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    /** 判断调用 SongUtils.musicList 返回的结果是有效
     * 例如:    result:{"success":false,"message":"CSRF token Invalid!","now":"2020-11-18T11:17:14.103Z"}
     * @return 正常: true ; 不正常: false
     */
    public static Boolean musicListWhetherTrue(String result) {
    	JSONObject jsonObject = JSONObject.parseObject(result);
    	String message = jsonObject.getString("message");
    	if("CSRF token Invalid!".equals(message) || "CSRF Token Not Found!".equals(message)) {
    		return false;
    	} 
    	return true;
    }
    
    /**
     * 根据 rid 获取歌曲下载链接
     *
     * @param ridString 歌曲对应的 rid
     * @return 歌曲下载链接
     */
    public static String get_url_by_rid(String ridString) {
        String mp3URLString = null;  // 歌曲下载链接
        BufferedReader bufferedReader = null;
        String urlString = "http://kuwo.cn/url?format=mp3&rid=" + ridString + "&response=url&type=convert_url3&br=320kmp3&from=web&t=1604991207945&httpsStatus=1&reqId=6fad66a1-2321-11eb-beba-c93a68b45841";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String downloadInfo = bufferedReader.readLine();
            // 把字符串转换为 JSON
            JSONObject downloadJson = JSONObject.parseObject(downloadInfo);
            mp3URLString = downloadJson.getString("url");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mp3URLString;
    }
    
    public static void main(String[] args) {
    	String result = musicList("%E5%91%A8%E6%9D%B0%E4%BC%A6", 1);
    	System.out.println(result);
    	
    
    }
    
}
