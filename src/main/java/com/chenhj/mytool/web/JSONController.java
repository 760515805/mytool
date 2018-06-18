package com.chenhj.mytool.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/json")
public class JSONController {
	@RequestMapping("/index") 
	public String  pdf2img() {
		return "json/index";
	}
}
