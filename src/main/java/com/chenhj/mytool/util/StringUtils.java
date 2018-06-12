package com.chenhj.mytool.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 
 * @Title: MyTools 
 * @Description:常用工具类
 * @Version:1.0.1
 * @author pancm
 * @date 2017年9月26日
 */
public final class StringUtils {

	/** 检查是否为整型 */
	private static  Pattern p=Pattern.compile("^\\d+$");

	/**
	 * 判断String类型的数据是否为空 
	 * null,""," " 为true 
	 * "A"为false
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return (null == str || str.trim().length() == 0);
	}

	/**
	 * 判断String类型的数据是否为空 
	 * null,"", " " 为false 
	 * "A", 为true
	 * 
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断list类型的数据是否为空 
	 * null,[] 为 true
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty(List<?> list) {
		return (null == list || list.size() == 0);
	}

	/**
	 * 判断list类型的数据是否为空 
	 * null,[] 为 false
	 * 
	 * @return boolean
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}

	/**
	 * 判断Map类型的数据是否为空 
	 * null,[] 为true 
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty(Map<?,?> map) {
		return (null == map || map.size()==0);
	}
	
	/**
	 * 判断map类型的数据是否为空 
	 * null,[] 为 false
	 * 
	 * @return boolean
	 */
	public static boolean isNotEmpty(Map<?,?> map) {
		return !isEmpty(map);
	}
	
	/**
	 * 判断JSONObject类型的数据是否为空 
	 * null,[] 为true 
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty(JSONObject json) {
		return (null == json || json.size()==0);
	}
	
	/**
	 * 判断json类型的数据是否为空 
	 * null,[] 为 false
	 * 
	 * @return boolean
	 */
	public static boolean isNotEmpty(JSONObject json) {
		return !isEmpty(json);
	}
	
	/**
	 * 字符串反转
	 * 如:入参为abc，出参则为cba
	 * @param str 
	 * @return  
	 */
	public static String reverse(String str) {
        if(isEmpty(str)){ 
            return str;
        }
        return reverse(str.substring(1)) + str.charAt(0);
    }

	/**
     * 判断是否为整型
     * @param String           
     * @return boolean 
     */   
    public static boolean isInteger(String str) {
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	/**
	 * 自定义位数产生随机数字
	 * @param int
	 * @return String
	 */
	public static String random(int count) {
		char start = '0';
		char end = '9';
		Random rnd = new Random();
		char[] result = new char[count];
		int len = end - start + 1;
		while (count-- > 0) {
			result[count] = (char) (rnd.nextInt(len) + start);
		}
		return new String(result);
	}

	/**   
    * 获取自定义长度的随机数(含字母)
    * @param len  长度
    * @return String
    */
    public static String random2(int len){ 
    	int random= Integer.parseInt(random(5));
        Random rd = new Random(random);  
        final int  maxNum = 62;  
        StringBuffer sb = new StringBuffer();  
        int rdGet;//取得随机数  
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',  
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',  
                'x', 'y', 'z', 'A','B','C','D','E','F','G','H','I','J','K',  
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
                'X', 'Y' ,'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
        int count=0;  
        while(count < len){  
            rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1  
            if (rdGet >= 0 && rdGet < str.length) {  
                sb.append(str[rdGet]);  
                count ++;  
            }  
        }  
        return sb.toString();  
    }
	
	
	/**
	 * 获取本机ip
	 * 
	 * @return String
	 * @throws UnknownHostException
	 */
	public static String getLocalHostIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	
	/**
	 * Object 转换为 String
	 * @param list
	 * @return String
	 */
	public static String toString(Object obj){
		 return JSON.toJSONString(obj);  
	}
	
	
	/**
	 *  JSON 转换为 JavaBean
	 * @param json
	 * @param t
	 * @return <T>
	 */
	public static <T> T toBean(JSONObject json,Class<T> t){
		 return JSON.toJavaObject(json,t);  
	}
	
	/**
	 *  JSON 字符串转换为 JavaBean
	 * @param str
	 * @param t
	 * @return <T>
	 */
	public static <T> T toBean(String str,Class<T> t){
		 return JSON.parseObject(str,  t);  
	}
	
	/**
	 * JSON 字符串 转换成JSON格式 
	 * @param obj
	 * @return JSONObject
	 */
	public static JSONObject toJson(String str){
		if(isEmpty(str)){
			return new JSONObject();
		}
		return JSON.parseObject(str);
			
	}
	
	/**
	 *  JavaBean 转化为JSON
	 * @param t
	 * @return
	 */
	public static  JSONObject toJson(Object t){
		if(null==t||"".equals(t)){
			return new JSONObject();
		}
		return (JSONObject) JSON.toJSON(t);
	}
	
	/**
	 * JSON 字符串转换为 HashMap
	 * 
	 * @param json
	 *            - String
	 * @return Map
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMap(String json) {
		if (isEmpty(json)) {
			return new HashMap();
		}
		return JSON.parseObject(json, HashMap.class);
	}
	
	/**  
     * 将map转化为string  
     * @param m  
     * @return  
     */  
    @SuppressWarnings("rawtypes")
	public static String toString(Map m) {  
        return JSONObject.toJSONString(m);  
    }  
    
      /**
       *  String转换为数组 
       * @param text
       * @return
       */
    public static <T> Object[] toArray(String text) {  
        return toArray(text, null);  
    }  
    
    /**
     *  String转换为数组 
     * @param text
     * @return
     */
    public static <T> Object[] toArray(String text, Class<T> clazz) {  
        return JSON.parseArray(text, clazz).toArray();  
    }  
	
	/**
	 * name1=value1&name2=value2格式的数据转换成json数据格式
	 * @param str
	 * @return
	 */
    public static JSONObject str2Json(String str){
    	if(isEmpty(str)){
    		return new JSONObject();
    	}
    	JSONObject json=new JSONObject();
    	String [] str1=str.split("&");
    	String str3="",str4="";
    	if(null==str1||str1.length==0){
    		return new JSONObject();
    	}
    	for(String str2:str1){
    		str3=str2.substring(0, str2.lastIndexOf("="));
    		str4=str2.substring(str2.lastIndexOf("=")+1, str2.length());
    		json.put(str3, str4);
    	}
		return json;
    }
    
    
    /**
	 * json数据格式 转换成name1=value1&name2=value2格式
	 * @param str
	 * @return
	 */
    @SuppressWarnings("rawtypes")
	public static String json2Str(JSONObject json){
    	 if(isEmpty(json)){
    		return null;
    	 }
    	  StringBuffer sb=new StringBuffer();
		  Iterator it=json.entrySet().iterator(); //定义迭代器
		  while(it.hasNext()){
			 Map.Entry  er= (Entry) it.next();
			 sb.append(er.getKey());
			 sb.append("=");
			 sb.append(er.getValue());
			 sb.append("&");
		  }
		  sb.delete(sb.length()-1, sb.length()); //去掉最后的&
		return sb.toString();
    }
	
	 
  /**
     * MD5加密
     * @param message
     * @return
     */
    public static String md5Encode(String message) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    message.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        int length=32 - md5code.length();
        for (int i = 0; i <length ; i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
    /**
     * 输入流转字符串
     */
	 public static String inputstr2Str(InputStream in, String encode)  
	   {  
	       StringBuffer sb = new StringBuffer();  
	       byte[] b = new byte[1024];  
	       int len = 0;  
	       try  
	       {  
	    	  // 默认以utf-8形式
	           if (encode == null || encode.equals(""))encode = "utf-8";
	           while ((len = in.read(b)) != -1)  
	           	 {  
	               sb.append(new String(b, 0, len, encode));  
	            }  
	           return sb.toString();  
	       }  
	       catch (IOException e)  
	       {  
	           e.printStackTrace();  
	       }catch (NullPointerException e) {
	    	   return "";
	       }  
	       return "";  
	   }  
	 
	/**
	 * 本方法的测试示例
	 * @param args
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) throws ParseException {
		/*
		 * String 和List 空数据判断
		 */
		String str1="";
		String str2=" ";
		String str3=null;
		String str4="a";
		List list=null;
		List<String> list2=new ArrayList<String>();
		List<Object> list3=new ArrayList<Object>();
		list3.add("a");
		
		System.out.println("str1 :"+isEmpty(str1));		//str1 :true
		System.out.println("str2 :"+isEmpty(str2));		//str2 :true
		System.out.println("str3 :"+isEmpty(str3));		//str3 :true
		System.out.println("str4 :"+isEmpty(str4));		//str4 :false
		System.out.println("list :"+isEmpty(list));		//list :true
		System.out.println("list2 :"+isEmpty(list2));	//list2 :true
		System.out.println("list3 :"+isEmpty(list3)); //list3 :false
		
		

		
		
		
		/*
		 * 整型判断
		 */
		String st="258369";
		String st2="258369A!@";
		String st3="258  369 ";
		System.out.println("st:"+isInteger(st));  //st:true
		System.out.println("st2:"+isInteger(st2)); //st2:false
		System.out.println("st3:"+isInteger(st3)); //st3:false
		
		/*
		 * 字符串反转
		 */
		String re="abcdefg";
		System.out.println("字符串反转:"+reverse(re)); //字符串反转:gfedcba
		
		
		/*
		 * 本机IP
		 */
		try {
			System.out.println("本机IP:"+getLocalHostIp()); //本机IP:192.168.1.111
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		/*
		 * 随机数
		 */
		
		System.out.println("6位随机数:"+random(6));	//6位随机数:222488
		System.out.println("10位随机数:"+random2(10));  //10位随机数:ZwW0pmofjW
		
		/*
		 * JSON数据转换
		 */
		
		String value="name1=value1&name2=value2&name3=value3";
		JSONObject json=new JSONObject();
		json.put("name1", "value1");
		json.put("name2", "value2");
		json.put("name3", "value3");
		System.out.println("value:"+value);				  //value:name1=value1&name2=value2&name3=value3
		System.out.println("str2Json:"+str2Json(value));  //str2Json:{"name1":"value1","name2":"value2","name3":"value3"}
		System.out.println("json:"+json.toJSONString());  //json:{"name1":"value1","name2":"value2","name3":"value3"}
		System.out.println("json2Str:"+json2Str(json));  //json2Str:name3=value3&name1=value1&name2=value2
		
		String jsonString=json.toJSONString();
		System.out.println("jsonString:"+jsonString);		//{"name1":"value1","name2":"value2","name3":"value3"}
		System.out.println("toJson(jsonString):"+toJson(jsonString)); //toJson(jsonString):{"name1":"value1","name2":"value2","name3":"value3"}
	     

		
	}
	
}
