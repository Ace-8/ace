package com.jfinal.AI.Tencent;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.http.HttpUtil;

/**
 * 语音识别
 * @author chenhongyang
 *
 */

public class Conversation {
	/**
     * 随机字符串
     *
     * @param length x
     * @return x
     */
    public String getRandomString(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    
	/**
     * 参数组合
     *@param 组合 
     */
    public static Map<String, Object> getKeybyvalue(String msg){
    	
    	String app_Id = "2159826663";
		String session = "10000";
		String question = msg;
		Conversation sign = new Conversation();
		Map<String, Object> params = new HashMap<>();
		params.put("app_id", app_Id);
		params.put("time_stamp", new Date().getTime() / 1000);
		params.put("nonce_str", sign.getRandomString(16));
		params.put("session", session);
		params.put("question", question);

		// {nonce_str=8ZYYMBO4CD9R3RBV, time_stamp=1605666042, question=你在哪里上学, session=10000, app_id=2159826663}
		System.out.println("params:" + params);
        return params;
    }
    
    
    /**
     * SIGN签名生成算法-JAVA版本 通用。默认参数都为UTF-8适用
     *
     * @param params 请求参数集，所有参数必须已转换为字符串类型
     * @return 签名
     * @throws IOException
     */
    public static Map<String, Object> getSignature(Map<String, Object> params,String CONFIG ) throws IOException {
        Map<String, Object> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, Object> param : entrys) {
            if (param.getValue() != null && !"".equals(param.getKey().trim()) &&
                    !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue())) {
                baseString.append(param.getKey().trim()).append("=")
                        .append(URLEncoder.encode(param.getValue().toString(), "UTF-8")).append("&");
            }
        }
        if (baseString.length() > 0) {
            baseString.deleteCharAt(baseString.length() - 1).append("&app_key=")
                    .append(CONFIG);
        }
        try {
            String sign = MD5Utils.getMD5String(baseString.toString());
            System.out.println("sign:" + sign.toUpperCase());
            sortedParams.put("sign", sign);
            
        } catch (Exception ex) {
            throw new IOException(ex);
        }
        
        // {app_id=2159826663, nonce_str=8ZYYMBO4CD9R3RBV, question=你在哪里上学, session=10000, sign=449BC3AE6B7949AFDFADC752C3A1CA76, time_stamp=1605666042}
        System.out.println("sortedParams: " + sortedParams);
        return sortedParams;
    }
    
    /**
     * 对外调用 接口
     * @param content
     * @return
     * @throws IOException 
     */
    public static String ConversationCall(String content) {
		String a;
		String result = "";
		try {
			a = HttpUtil.get("https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat",Conversation.getSignature(Conversation.getKeybyvalue(content), "SqwrMKu2ISnzL8IR"));
			
			// {"ret":0,"msg":"ok","data":{"answer":"毕业了","session":"10000"}}
			JSONObject jsonObject = JSONObject.parseObject(a);
	    	
	    	JSONObject jsonObject2 = jsonObject.getJSONObject("data");
	    	result = jsonObject2.getString("answer");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return result;
	}
    
	public static void main(String[] args) throws IOException {
		
//		String app_Id = "2159826663";
//		Map<String, Object> params = new HashMap<>();
//		params.put("app_id", app_Id);
//		params.put("time_stamp", 1605666042);
//		params.put("nonce_str", "8ZYYMBO4CD9R3RBV");
//		params.put("session", "10000");
//		params.put("question", "你好");
//		params.put("sign", "449BC3AE6B7949AFDFADC752C3A1CA76");
//		
//		String a = HttpUtil.get("https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat", params);
		
		String a = HttpUtil.get("https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat",Conversation.getSignature(Conversation.getKeybyvalue("疫情怎么样"), "SqwrMKu2ISnzL8IR"));
    	JSONObject jsonObject = JSONObject.parseObject(a);
    	
    	// {"ret":0,"msg":"ok","data":{"answer":"毕业了","session":"10000"}}
    	System.out.println(jsonObject);
	}
	
}



















