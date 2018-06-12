package com.chenhj.mytool.web;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.chenhj.mytool.bean.UploadFileBean;
import com.chenhj.mytool.config.Constant;
import com.chenhj.mytool.config.RedisConfig;
import com.chenhj.mytool.util.PdfUtil;
@Controller
@RequestMapping("/pdf")
public class PdfController {
	
	@Autowired
	private Constant constant;
	
	private static final int FAIL=1;
	private static final int SUCCESS=0;
	
	@Autowired
	private RedisConfig stringRedisTemplate;
	
	@RequestMapping("/pdf2img") 
	public String  pdf2img() {
		return "/word/pdf2img";
	}
	/**
     * 实现多文件上传
     * */
    @RequestMapping(value="/multifileUpload",method=RequestMethod.POST) 
    public @ResponseBody String multifileUpload(HttpServletRequest request){        
    	stringRedisTemplate.redisUtil(null).set("","");
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
            System.out.println(fileName + "-->" + size);
            if(file.isEmpty()){
            	upload.setCode(FAIL);
            }else{        
                File dest = new File(path + "/temp/" + fileName);
                if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                file.transferTo(dest);
                String src = PdfUtil.pdf2Image(dest,path, 296);
                upload.setSrc(src);
                upload.setCode(SUCCESS);
                //删除临时包
                dest.delete();
            }
        }
		} catch (Exception e) {
			upload.setCode(FAIL);
			upload.setMsg(e.getMessage());
		}
    	System.out.println(upload.toString());
        return upload.toString();
    }
    
}
