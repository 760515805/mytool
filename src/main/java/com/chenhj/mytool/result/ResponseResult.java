package com.chenhj.mytool.result;

import com.chenhj.mytool.util.StringUtils;

/**
 * 
* Title: ResponseResult
* Description:
* 应答对象 
* Version:1.0.0  
* @author pancm
* @date 2018年3月23日
 */
public class ResponseResult  {
	
	public ResponseResult(){
		
	}
	/**
	 * 
	 * @param seqid   	  消息序列号
	 * @param timestamp	   时间戳
	 * @param resultcode 返回结果代码
	 * @param resultmsg	  返回结果消息描述
	 * @param svccont	 请求报文体内容
	 * @param
	 */
	public ResponseResult(String seqid, String timestamp,
			 String resultcode, String resultmsg, String svccont) {
		this.seqid = seqid;
		this.timestamp = timestamp;
		this.resultcode = resultcode;
		this.resultmsg = resultmsg;
		this.svccont = svccont;
	}

	/** 消息序列号 */
	private String seqid;
	/** 时间戳 */
	private String timestamp;
	/** 返回结果代码 */
	private String resultcode;
	/** 返回结果消息描述 */
	private String resultmsg;
	/** 请求报文体内容 */
	private String svccont;


	/**
	 * 获取消息序列号
	 * 
	 * @return seqid
	 */
	public String getSeqid() {
		return seqid;
	}

	/**
	 * 设置消息序列号
	 * 
	 * @param String
	 *            seqid
	 */
	public void setSeqid(String seqid) {
		this.seqid = seqid;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * 设置时间戳
	 * 
	 * @param String
	 *            timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 获取返回结果代码
	 * @return resultcode
	 */
	public String getResultcode() {
		return resultcode;
	}

	/**
	 * 设置返回结果代码
	 * 
	 * @param String
	 *            resultcode
	 */
	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}

	/**
	 * 获取返回结果消息描述
	 * 
	 * @return resultmsg
	 */
	public String getResultmsg() {
		return resultmsg;
	}

	/**
	 * 设置返回结果消息描述
	 * 
	 * @param String
	 *            resultmsg
	 */
	public void setResultmsg(String resultmsg) {
		this.resultmsg = resultmsg;
	}

	/**
	 * 获取请求报文体内容
	 * 
	 * @return svccont
	 */
	public String getSvccont() {
		return svccont;
	}

	/**
	 * 设置请求报文体内容
	 * 
	 * @param String
	 *            svccont
	 */
	public void setSvccont(String svccont) {
		this.svccont = svccont;
	}
	
	/** 
	 * 
	 */
	@Override
	public String toString() {
		return StringUtils.toString(this);
	}


}
