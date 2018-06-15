package com.chenhj.mytool;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class App 
{
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	/**  
     * 文件上传配置  
     * @return  
     */  
//    @Bean  
//    public MultipartConfigElement multipartConfigElement() {  
//        MultipartConfigFactory factory = new MultipartConfigFactory();  
//        //单个文件最大  
//        factory.setMaxFileSize("10240KB"); //KB,MB  
//        /// 设置总上传数据总大小  
//        factory.setMaxRequestSize("102400KB");  
//        return factory.createMultipartConfig();  
//    }  
}
