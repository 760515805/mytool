package com.chenhj.mytool.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/")
public class IndexController {
	@RequestMapping("/") 
	public String  index() {
		return "/index";
	}
	@RequestMapping(value="/download") 
	public String downLoad(HttpServletRequest request,HttpServletResponse response){
	        //String filename="2.jpg";
	        //String filePath = "F:/test" ;
			String path = request.getParameter("src");
	        File file = new File(path);
	        if(file.exists()){ //判断文件父目录是否存在
	            response.setContentType("application/force-download");
	            response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());
	            byte[] buffer = new byte[1024];
	            FileInputStream fis = null; //文件输入流
	            BufferedInputStream bis = null;
	            
	            OutputStream os = null; //输出流
	            try {
	                os = response.getOutputStream();
	                fis = new FileInputStream(file); 
	                bis = new BufferedInputStream(fis);
	                int i = bis.read(buffer);
	                while(i != -1){
	                    os.write(buffer);
	                    i = bis.read(buffer);
	                }
	                
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            System.out.println("----------file download" + file.getName());
	            try {
	            	if(bis!=null)bis.close();
	                if(bis!=null)fis.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	        return null;
	    }
}