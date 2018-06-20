package com.chenhj.mytool.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/front")
public class FrontController {
	@RequestMapping("/json") 
	public String  pdf2img() {
		return "json/index";
	}
	@RequestMapping("/js_beautify") 
	public String  jsbeautify() {
		return "jsdecode/index";
	}
}
