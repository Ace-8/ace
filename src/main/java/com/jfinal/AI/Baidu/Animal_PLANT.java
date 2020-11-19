package com.jfinal.AI.Baidu;

import java.awt.List;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.jfinal.weixinproperty.Property;

import com.jfinal.weixin.utils.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.AI.Baidu.utils.Utiles;
import com.jfinal.weixin.utils.Base64Util;
import com.jfinal.weixin.utils.HttpUtil;

/**
 * 动物识别
 * @author chenhongyang
 *
 */
public class Animal_PLANT {
	/**
	    * 重要提示代码中所需工具类
	    * FileUtil,Base64Util,HttpUtil,GsonUtils请从
	    * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
	    * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
	    * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
	    * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
	    * 下载
	    */
	
	
		/**
		 *  识别图片返回json数据
		 * @param imagePath  需要识别的图片的链接
		 * @return  识别的json数据+图片的本地路径
		 */
	    public static String identify(byte[] imgData, String imagePath, String url) {
	        String imageLocalPath = "";  // 图片在本地的绝对路径
	        String result = "";  // 识别图片返回的json数据
	        
	        ArrayList<String> list = new ArrayList<String>();
	        
	        try {
	            String imgStr = Base64Util.encode(imgData);
	            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

	            String param = "image=" + imgParam;

	            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
	            String accessToken = AuthService.getAuth();
	            
	            result = HttpUtil.post(url, accessToken, param);
	            System.out.println(result);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        return result;
	    }
	    
	    /**
	     *  对 Animal.IdentifyAnimal() 的返回值进行解析
	     * @param jsonResult  Animal.IdentifyAnimal() 返回的结果
	     * @return 解析后的数据(html格式<便于页面展示>)
	     */
	    public static ArrayList<String> parseJsonResult(String jsonResult) {
	    	
	    	ArrayList<String> list = new ArrayList<String>();
	    	
	    	//  {"log_id": 2621635846185612051, "result": [{"score": "0.342515", "name": "孟加拉虎"}, 
	        // {"score": "0.289474", "name": "西伯利亚虎"}, {"score": "0.238095", "name": "苏门答腊虎"}, 
	    	// {"score": "0.0592241", "name": "华南虎"}, {"score": "0.0533629", "name": "印度支那虎"}, {"score": "0.00217554", "name": "巴厘虎"}]}

	    	JSONObject jsonObject = JSONObject.parseObject(jsonResult);
	    	JSONArray jsonArray = jsonObject.getJSONArray("result");

	    	String parseResult = "";  // 识别成功后 返回的信息
	    	String result = "";  // 是否识别成功的标记
	    	
	    	for(int i=0; i<jsonArray.size();i++) {
	    		
	    		JSONObject ele_res = (JSONObject) jsonArray.get(i);
	    		
	    		// {"log_id": 4996398811437213107, "result": [{"score": "0.999721", "name": "非动物"}]}
	    		result = ele_res.getString("name");
	    				
	    		if(i==jsonArray.size()-1) {
	    			parseResult += "名字:" + ele_res.getString("name") + "\n" + "置信度:" + ele_res.getString("score");
	    		} else {
	    			parseResult += "名字:" + ele_res.getString("name") + "\n" + "置信度:" + ele_res.getString("score") + "\n\n";
	    		}
	    	}
	    	
	    	list.add(parseResult);
	    	list.add(result);
	    	return list;
	    }
}















