package com.jfinal.weixin.demo;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jfinal.core.Controller;
import com.jfinal.weixin.entity.Movie;
import com.jfinal.weixin.utils.MovieUtils;

public class MovieController extends Controller{
	
	// 访问该链接会自动请求该函数: http://127.0.0.1/movie
	// 等同于                    http://127.0.0.1/movie/index
	@SuppressWarnings("unchecked")
	public void index() {
		
		String searchName = this.getPara("searchName");
		String pages = this.getPara("pages");
		
		// 默认访问第一页的数据
		if(pages==null || "".equals(pages)) {
			pages = "1";
		}
		
		// http://127.0.0.1/movie?searchName=%E5%85%AB%E4%BD%B0
		this.setAttr("searchName", searchName);
		this.setAttr("pages", Integer.valueOf(pages));
		
		HashMap hashMap = MovieUtils.searchMovie(searchName, pages);
		this.setAttr("pageCount", hashMap.get("pageCount").toString());  // 总页数
		this.setAttr("mvList", hashMap.get("mvList"));  // 电影信息
		
		System.out.println("hashMap.get()==null" + "=================" + hashMap.get("mvList"));
		
		// 判断电影不存在的情况
		if(((ArrayList<Element>) hashMap.get("mvList")).isEmpty()) {
			render("ResourceNotExist.html");
		} else {
			render("index.html");
		}
	}
	
	/**
	 * 获取电影播放链接
	 */
	public void getDetailUrl() {
    	String detailUrl = this.getPara("detailUrl");  // /movie/70117.html
		ArrayList list = new ArrayList();
		try {
			System.out.println("detailUrl:" + detailUrl);
			Document doc=Jsoup.connect("https://www.97soo.com"+detailUrl).get();
			Elements els_play = doc.select("#main > div.endpage.clearfix > div[class=mox]");  // 搜索 class=mox 的 div 块
			
			for (Element element : els_play) {
				// 确保西瓜视频不会被爬取到
				if(element.select("div.title > span > img").indexOf("xigua")>0) {
					continue;
				}else {
					Elements els_li = element.select("div:nth-child(2) > ul > li >a");
					// 遍历所有的 a 标签
					for (Element element2 : els_li) {
						Movie mv = new Movie();
						mv.setDetailTitle(element2.attr("title"));
						mv.setDetailPlayUrl(element2.attr("href"));
						list.add(mv);
					}
				}
			}
			this.setAttr("mvlist", list);
			render("detail.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 获取电影播放链接
	 */
	public void getPlayUrl() {
		String url=this.getPara("url");  // /play/69939-0-0.html
		
		url=url.substring(url.indexOf("/")+1);
		url=url.substring(0,url.lastIndexOf("."));
		String[] aa=url.split("-");
		
		String dyplayurl = MovieUtils.getMvPlayUrl(aa[0], aa[1], aa[2]);
		this.setAttr("playurl", dyplayurl);
		
		if(dyplayurl == null) {
			render("ResourceNotExist.html");
		} else {
			render("dm.html");
		}
	}
	
}
