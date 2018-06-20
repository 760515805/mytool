package com.chenhj.mytool.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: ConditionEsUtil.java
* @Description: 该类的功能描述
*ES条件转化类
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月14日 下午3:05:40 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月14日     chenhj          v1.0.0               修改原因
 */
@SuppressWarnings("all")
public class ConditionEsUtil{
	
	private QueryBuilder queryBuilder;
	
	Map<String,Object> map;
	//private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";	
	protected QueryBuilder conditionUtil(Map<String,Object> map){
		if(map==null||map.isEmpty()){
			return null;
		}
		this.map=map;
		condetionValidPhone();
		return queryBuilder;
	}
	//如果需要排序把filter换回must
	private void condetionValidPhone(){
		

		
		//SQLTranslate
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		 for (Entry<String, Object> s : map.entrySet()) {
			 String key = s.getKey();
			 Object value = s.getValue();
			 if(value!=null){
				 String valueToS = value.toString().trim();
				 //这一步必须要求对接方按接口来,对区间进行查询
				 if(valueToS.startsWith("{")&&valueToS.endsWith("}")&&(valueToS.contains("gt")||valueToS.contains("lt"))){
					 JSONObject json = null;
					 try {
						 json = JSON.parseObject(valueToS);
					} catch (RuntimeException e) {
						throw new IllegalArgumentException("请检查参数"+key+"是否需要区间查询,区间查询格式是否填写正确,参数内容:"+value, e);
					}
					 if(json.containsKey("gt"))boolQueryBuilder.filter(QueryBuilders.rangeQuery(key).gt(json.get("gt")));
					 if(json.containsKey("gte"))boolQueryBuilder.filter(QueryBuilders.rangeQuery(key).gte(json.get("gte")));
					 if(json.containsKey("lt"))boolQueryBuilder.filter(QueryBuilders.rangeQuery(key).lt(json.get("lt")));
					 if(json.containsKey("lte"))boolQueryBuilder.filter(QueryBuilders.rangeQuery(key).lte(json.get("lte")));
					 
				 }
				 //这一步是查询
				 else if("existsMustNotQuery".equals(key)){
					 String fields[]=valueToS.split(",");
					 for(String field:fields){
						 boolQueryBuilder.mustNot(QueryBuilders.existsQuery(field));
					 }
				 }else if("existsMustQuery".equals(key)){
					 String fields[]=valueToS.split(",");
					 for(String field:fields){
						 boolQueryBuilder.must(QueryBuilders.existsQuery(field));
					 }
				 }
				 //这一步是查询
				 else if("existsShouldQuery".equals(key)){
					 String fields[]=valueToS.split(",");
					 for(String field:fields){
						 boolQueryBuilder.should(QueryBuilders.existsQuery(field));
					 }
				 }
				 else{
					 //term是代表完全匹配，即不进行分词器分析，文档中必须包含整个搜索的词汇
					 if(value instanceof List){
						 //这个if里面的方法相当于SQL的IN语句
						 List list = (List) value;
						 for(Object obj:list){
							 if(obj instanceof Map){
								 Map map = (Map) obj;
								 boolQueryBuilder.should(QueryBuilders.termQuery(key,map.get(key)));
							 }else{
								 boolQueryBuilder.should(QueryBuilders.termQuery(key,obj));
							 }
						 }
					 }else{
						 boolQueryBuilder.filter(QueryBuilders.termQuery(key,value));
					 }

				 	}
			 }else{
				 //term是代表完全匹配，即不进行分词器分析，文档中必须包含整个搜索的词汇
				 boolQueryBuilder.mustNot(QueryBuilders.existsQuery(key));
			 }
			
	     }			

		this.queryBuilder=boolQueryBuilder;
	}
	public static void main(String[] args) throws IOException {
		 java.util.Map<String, Object> map = new HashMap<>();
		 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		 
		 
		 JSONObject js = new JSONObject();
		 
		 js.put("gte","2018-04-24 00:00:00");
		 js.put("lte","2018-04-24 23:59:59");
		// List<String> list = new ArrayList<>();
		// map.put("recvtm",js.toJSONString());
		// map.put("imMsgid",6132040000041l);
		// list.add("d4:5a:7a:7c:9c:b9");
		// list.add("a2:d7:9f:b4:7e:d7");
		// list.add("a5:d0:3c:6b:b5:7b");
		// list.add("a1:1a:1a:2e:a5:3e");
		// map.put("devUid", list);
		 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		 java.util.Map<String, Object> map1 = new HashMap<>();
		 
		 map1.put("devUid", 123);
		 java.util.Map<String, Object> map2 = new HashMap<>();
		 
		 map2.put("devUid", 3454);
		 list.add(map1);
		 list.add(map2);
		 map.put("devUid", list);
	     QueryBuilder aa = new ConditionEsUtil().conditionUtil(map);//subAggregation(AggregationBuilders.filter("messages", aa)
	    // AggregationBuilder aggregation =  AggregationBuilders.terms("agg").field("phone");
		// AggregationBuilder aggregation =  AggregationBuilders.avg("menot").field("fields").field("mont");
	     //Map<String, Object> params = new HashMap<>();
	     //Script script = new Script(ScriptType.INLINE,"painless","return doc['sendtm'].value.secondOfDay - doc['recvtm'].value.secondOfDay" , params);
	    //searchSourceBuilder.scriptField("chazhi", script);
	 
	     //searchSourceBuilder.aggregation(aggregation);
	     //NestedAggregationBuilder  nested = AggregationBuilders.nested("agg",""); 
	    //searchSourceBuilder.aggregation(aggregation);
		//System.out.println(map.get("a").toString());
	     searchSourceBuilder.query(aa);
		 System.out.println(searchSourceBuilder.toString());
		
	}
}
