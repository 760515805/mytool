/**
 * 
 */
package com.chenhj.mytool.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsConditionController.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月20日 下午5:45:31 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月20日     chenhj          v1.0.0               修改原因
*/
@Controller
@RequestMapping("/es")
public class EsConditionController {
	@RequestMapping("/condition") 
	public String  condition() {
		return "es/index";
	}
}
