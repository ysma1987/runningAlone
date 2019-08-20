package com.ysma.ppt.config;

import com.ysma.ppt.config.filter.JsonMapperArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 附加配置
 */
@Configuration
public class AdditionConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //设置默认访问路径跳转
        registry.addRedirectViewController("/", "/index");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //限制api路径可以跨域
        registry.addMapping("/api/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JsonMapperArgumentResolver());
    }
}
