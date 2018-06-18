package com.chenhj.mytool.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chenhj.mytool.service.IPdfTaskService;
@Controller
@RequestMapping("/pdf")
public class PdfController {
	
	@Autowired
	private IPdfTaskService pdfService;
	
	
	@RequestMapping("/pdf2img") 
	public String  pdf2img() {
		return "word/pdf2img";
	}
	@RequestMapping("/img2pdf") 
	public String  img2pdf() {
		return "word/img2pdf";
	}
	/**
     * 实现多文件上传
     * */
    @RequestMapping(value="/multifileUpload",method=RequestMethod.POST) 
    public @ResponseBody String multifileUpload(HttpServletRequest request){
    	String action = request.getParameter("action");
    	
    	System.out.println(action);
    	
    	if("pdf2img".equals(action)){
        	return pdfService.pdf2Img(request);
    	}else if("img2pdf".equals(action)){
        	return pdfService.Img2pdf(request);
    	}
    	return "0";
    }
    
}
