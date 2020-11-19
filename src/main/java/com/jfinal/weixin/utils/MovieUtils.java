package com.jfinal.weixin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.render.Render;
import com.jfinal.weixin.entity.Movie;

import org.jsoup.nodes.Document;

import org.jsoup.Jsoup;

/**
 * 用于电影的工具类
 * @author chenhongyang
 *
 */
public class MovieUtils {

    /**
     * 从 content 中分割出用户的 搜索内容
     * @param content 用户发送的信息
     * @return 用户的搜索内容
     */
    public static String splitPlayName(String content) {
    	return content.substring(content.indexOf("#")+1);
    }
    
    /**
     *  根据电影名和页码搜索电影
     * @param searchName 电影名
     * @param pages 页码 
     * @return  存放 电影信息的 HashMap对象
     */
    public static HashMap searchMovie(String searchName, String pages){
    	HashMap hashMap = new HashMap();
    	ArrayList<Movie> arrayList = new ArrayList<Movie>();  // 存放 Movie 实体
    	
    	try {
			String string = "https://www.97soo.com/search.php?page="+pages+"&searchword=" + URLEncoder.encode(searchName, "UTF-8");
			
			Document document = Jsoup.connect(string).get();
//			System.out.println("document: " + document);
			
			Elements els_list = document.select("#main > div.pagelist > div.movielist > ul > li");
			for (Element element : els_list) {
				Movie movie = new Movie();
				movie.setName(element.select("div > h2 > a").attr("title"));
				movie.setGengxin(element.select("div > p:nth-child(6) > i").text());
				movie.setJishu(element.select("a > em").text());
				movie.setLeixing(element.select("div > p:nth-child(5) > i").text());
				movie.setZhuyan(element.select("div > p:nth-child(4) > i:nth-child(1)").text());
				movie.setZhuangtai(element.select("div > p:nth-child(3)").text());
				movie.setDiqu(element.select("div > p:nth-child(4) > i:nth-child(2)").text());
				movie.setNianfen(element.select("div > h2 > em").text());
				movie.setPingfen(element.select("a > i").text());
				movie.setImg(element.select("a > img").attr("src"));
				movie.setDetailUrl(element.select("a").attr("href"));  // /movie/70117.html
				movie.setXingxing(element.select("div > em").attr("class"));
				movie.setNianfen(element.select("div > h2 > em").text());
				
				arrayList.add(movie);
			}
			hashMap.put("mvList", arrayList);
			
			// 获取总页数
			String pageCount=document.select("#pages > span").text();
			System.out.println("pageCount:" + pageCount);
			pageCount=pageCount.substring(pageCount.lastIndexOf("/")+1,pageCount.length()-1);
			hashMap.put("pageCount", pageCount);  // 总页数
			
			hashMap.put("pages", pages);  // 当前页
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hashMap;
	}
    
    
    /**
     * @return 返回视频实际播放地址
     */
    public static String getMvPlayUrl(String vid,String vfrom,String vpart)
	{
    	
    	//           https://www.97soo.com/ass.php?vid=71025&vfrom=3&vpart=0
		String urls="https://www.97soo.com/ass.php?vid="+vid+"&vfrom="+vfrom+"&vpart="+vpart;
		 
		try {
			Document doc = Jsoup.connect(urls).ignoreContentType(true).get();
			
			String body=doc.body().text();
			
			//  <body>({"q":"69939","p":false,"s":{"title":"\u6218\u72fc2017","vid":"69939","num":1,"part":0,
			//  "name":["HD1280\u9ad8\u6e05\u56fd\u8bed\u4e2d\u5b57\u7248"],"url":"https:\/\/wanmei.xiaocong-zuida.com\/
			//  20170801\/2WfvgZbV\/index.m3u8","video":["https:\/\/wanmei.xiaocong-zuida.com\/20170801\/2WfvgZbV\/index.m3u8"],
			//  "cfg":[]}}); </body>
			
			System.out.println("==============doc.body():" + doc.body());
			
			// ({"q":"69939","p":false,"s":{"title":"\u6218\u72fc2017","vid":"69939","num
			//  ":1,"part":0,"name":["HD1280\u9ad8\u6e05\u56fd\u8bed\u4e2d\u5b57\u7248"],"url":
			// "https:\/\/wanmei.xiaocong-zuida.com\/20170801\/2WfvgZbV\/index.m3u8","video":["htt
			// ps:\/\/wanmei.xiaocong-zuida.com\/20170801\/2WfvgZbV\/index.m3u8"],"cfg":[]}});
			System.out.println("==============doc.body().text():" + doc.body().text());
			
			body=body.substring(1,body.length()-2);
			
			JSONObject json=JSONObject.parseObject(body).getJSONObject("s");
			System.out.println(json);
			if(json.containsKey("url"))
			{
				return json.getString("url");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
}
    
    public static void main(String[] args) {
//        String pageCount = "共6791条数据 页次:2/324页";
//
//        pageCount= pageCount.split("/")[1];
//        
//        System.out.println(pageCount.substring(0, pageCount.length()-1));
    	
    	HashMap hashMap = searchMovie("雷", "1");
    	System.out.println(hashMap);
    	
    }
}





















