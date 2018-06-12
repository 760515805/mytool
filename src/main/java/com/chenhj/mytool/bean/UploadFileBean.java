package com.chenhj.mytool.bean;

import com.alibaba.fastjson.JSONObject;

public class UploadFileBean {
//	{
//		  "code": 0
//		  ,"msg": ""
//		  ,"data": {
//		    "src": "http://cdn.abc.com/123.jpg"
//		  }
//		}     
	private Integer code;
	private String  msg="";
	private String src;

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String toString(){
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		json.put("src", src);
		return json.toJSONString();
	}
}
