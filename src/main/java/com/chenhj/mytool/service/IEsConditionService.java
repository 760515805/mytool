/**
 * 
 */
package com.chenhj.mytool.service;

import javax.servlet.http.HttpServletRequest;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: IEsConditionService.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月20日 下午6:55:14 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月20日     chenhj          v1.0.0               修改原因
*/
public interface IEsConditionService {
		
	public String condition(HttpServletRequest request);
}
