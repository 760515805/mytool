/**
 * 
 */
package com.chenhj.mytool.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.chenhj.mytool.bean.UploadFileBean;
import com.chenhj.mytool.config.Constant;
import com.chenhj.mytool.service.IPdfTaskService;
import com.chenhj.mytool.util.PdfUtil;
import com.chenhj.mytool.util.RedisUtil;
import com.chenhj.mytool.util.StringUtils;
import com.chenhj.mytool.util.UUIDUtils;
/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: PdfTaskServiceImpl.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月14日 下午5:42:27 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月14日     chenhj          v1.0.0               修改原因
*/
@Service
public class PdfTaskServiceImpl implements IPdfTaskService{
	private static final Logger LOG = LoggerFactory.getLogger(PdfTaskServiceImpl.class);
	@Autowired
	private Constant constant;
	
	private static final int FAIL=1;
	private static final int SUCCESS=0;
	@Resource
	private RedisUtil redisUtil;
	
	JSONObject redisJ =null;

	@Override
	public String pdf2Img(HttpServletRequest request) {
        
    	UploadFileBean upload = new UploadFileBean();
    	try {
    		List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
    		if(files.isEmpty()){
    			upload.setCode(FAIL);
    		}
        String path = constant.getPdfImgPath();
        for(MultipartFile file:files){
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            LOG.info(fileName + "-->" + size);
            if(file.isEmpty()){
            	upload.setCode(FAIL);
            }else{        
                File dest = new File(path +File.separator+"temp"+File.separator+ fileName);
                if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                file.transferTo(dest);
                String src = PdfUtil.pdf2Image(dest,path, 296);
                LOG.info("原始路径:"+src.trim());
                String redisKey = UUIDUtils.getUUID();
                
                redisJ = new JSONObject();
                redisJ.put("src", src);
                //把路径保存进Redis中
				redisUtil.set(redisKey,redisJ);
				LOG.info("存入成功");
				LOG.info(redisUtil.get(redisKey)+"");
				
                upload.setSrc(redisKey);
                upload.setCode(SUCCESS);
                //删除临时包
                dest.delete();
              }
           }
		} catch (Exception e) {
			upload.setCode(FAIL);
			upload.setMsg(e.getMessage());
		}
    	LOG.info(upload.toString());
        return upload.toString();
    
	}
	@Override
	public String Img2pdf(HttpServletRequest request) {
    	UploadFileBean upload = new UploadFileBean();
    	try {
    		List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
    		if(files.isEmpty()){
    			upload.setCode(FAIL);
    		}
        String path = constant.getPdfImgPath();
        List<File> imgFileList = new ArrayList<File>();
        for(MultipartFile file:files){
        	String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            File dest = new File(path +File.separator+"temp"+File.separator+ fileName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            file.transferTo(dest);
            imgFileList.add(dest);
            LOG.info(fileName + "-->" + size);
        }	
               // String src = PdfUtil.pdf2Image(dest,path, 296);
                String src = PdfUtil.toPdf(imgFileList, path);
                if(!StringUtils.isEmpty(src)){
                	String redisKey = UUIDUtils.getUUID();
                    redisJ = new JSONObject();
                    redisJ.put("src", src);
                    //把路径保存进Redis中
    				redisUtil.set(redisKey,redisJ);
    				LOG.info("存入成功");
    				LOG.info(redisUtil.get(redisKey)+"");
                    upload.setSrc(redisKey);
                    upload.setCode(SUCCESS);
                }else{
                	upload.setCode(FAIL);
                }
                        
		} catch (Exception e) {
			upload.setCode(FAIL);
			upload.setMsg(e.getMessage());
		}
    	LOG.info(upload.toString());
        return upload.toString();
	}

}
