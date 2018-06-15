package com.chenhj.mytool.interceptor;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: MyWebInterceptor.java
* @Description: 该类的功能描述
*自定义拦截器 
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月14日 下午7:42:06 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月14日     chenhj          v1.0.0               修改原因
 */
@Configuration
public class MyWebInterceptor extends WebMvcConfigurerAdapter {
    @Bean   
    public HandlerInterceptor getMyInterceptor(){
        return new MyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/api/phone/query");
        super.addInterceptors(registry);
    }
    
    
     class MyInterceptor implements HandlerInterceptor {
    	
    	 /**
         * 在请求处理之前进行调用（Controller方法调用之前）调用,
         *  返回true 则放行， false 则将直接跳出方法
         */
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
                 return true;
        }


        //请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
        @Override
        public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        }
        //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
        @Override
        public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        }
    }
    
    
}
