/**
 * 
 */
package com.chenhj.mytool.service;

import javax.servlet.http.HttpServletRequest;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: IPdfTaskService.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月14日 下午4:03:28 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月14日     chenhj          v1.0.0               修改原因
*/
public interface IPdfTaskService {
	
	public String pdf2Img(HttpServletRequest request);
	
	public String Img2pdf(HttpServletRequest request);

}
