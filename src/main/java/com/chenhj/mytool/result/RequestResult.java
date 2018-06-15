package com.chenhj.mytool.result;

import com.chenhj.mytool.util.StringUtils;

/**
 * 
* Title: RequestResult
* Description:
* 请求对象 
* Version:1.0.0  
* @author pancm
* @date 2018年3月23日
 */
public class RequestResult{
	
	/** 消息序列号 */
	private String seqid;
	 /** 时间戳 */
	private String timestamp;
	/** 鉴权认证字段 */
	private String token;
	/** 加密标记 0 不加密;1 加密*/
	private int encryptsign;
	/** 请求报文体内容 */
	private String svccont;
	
	
	
	

	public RequestResult(){
	}
	
	public RequestResult(String seqid, String timestamp,
			String token, int encryptsign,String svccont) {
		super();
		this.seqid = seqid;
		this.timestamp = timestamp;
		this.token = token;
		this.encryptsign = encryptsign;
		this.svccont = svccont;
	}

	/**  
	 * 获取消息序列号  
	 * @return  seqid  
	 */
	public String getSeqid() {
		return seqid;
	}

	


	/**  
	 * 设置消息序列号  
	 * @param String seqid  
	 */
	public void setSeqid(String seqid) {
		this.seqid = seqid;
	}




	/**  
	 * 获取时间戳  
	 * @return  timestamp  
	 */
	public String getTimestamp() {
		return timestamp;
	}




	/**  
	 * 设置时间戳  
	 * @param String timestamp  
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	/**  
	 * 获取鉴权认证字段  
	 * @return  token  
	 */
	public String getToken() {
		return token;
	}


	/**  
	 * 设置鉴权认证字段  
	 * @param String token  
	 */
	public void setToken(String token) {
		this.token = token;
	}


	/**  
	 * 获取加密标记0不加密;1加密  
	 * @return  encryptsign  
	 */
	public int getEncryptsign() {
		return encryptsign;
	}

	/**  
	 * 设置加密标记0不加密;1加密  
	 * @param int encryptsign  
	 */
	public void setEncryptsign(int encryptsign) {
		this.encryptsign = encryptsign;
	}

	/**  
	 * 获取请求报文体内容  
	 * @return  svccont  
	 */
	public String getSvccont() {
		return svccont;
	}



	/**  
	 * 设置请求报文体内容  
	 * @param String svccont  
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
