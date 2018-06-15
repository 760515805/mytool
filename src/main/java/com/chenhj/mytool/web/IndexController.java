package com.chenhj.mytool.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.chenhj.mytool.util.RedisUtil;
import com.chenhj.mytool.util.StringUtils;
@Controller
@RequestMapping("/")
public class IndexController {
	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);
	@Resource
	private RedisUtil redisUtil;
	@RequestMapping("/") 
	public String  index() {
		return "/index";
	}
	@RequestMapping(value="/download") 
	public void downLoad(HttpServletRequest request,HttpServletResponse response){
			String fileid = request.getParameter("src");
			LOG.info("redis ID:"+fileid);
			String path =(String) redisUtil.get(fileid);
			if(StringUtils.isEmpty(path)){
				return;
			}
	        File file = new File(path);
	        if(file.exists()){ //判断文件父目录是否存在
	        	String name = file.getName().replaceAll(" ","");
	            response.setContentType("application/force-download");
	            response.setHeader("Content-Disposition", "attachment;fileName=" + name);
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
	            	LOG.error(e+"");
	                e.printStackTrace();
	            }
	            LOG.info("----------file download" + file.getName());
	            try {
	            	if(bis!=null)bis.close();
	                if(bis!=null)fis.close();
	            } catch (IOException e) {
	            	LOG.error(e+"");
	                e.printStackTrace();
	            }
	        }
	    }
}
