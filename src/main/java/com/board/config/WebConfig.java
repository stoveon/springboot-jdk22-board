package com.board.config;

import com.board.interceptor.MyInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@PropertySources({
        @PropertySource(value = "classpath:/application.properties"),
})
public class WebConfig implements WebMvcConfigurer {

    /**
     * resources 패턴
     */
    public static final String[] resourcePatterns = {
            "static/**",
            "resources/**",
            "WEB-INF/**"
    };

    /**
     * 제외시킬 url 패턴
     */
    public static final String[] excludeUrlPatterns = {
            "/error",
    };

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(resourcePatterns)
                .excludePathPatterns(excludeUrlPatterns)
        ;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        } catch (Exception e) {
            log.error("[ERROR] addResourceHandlers << {} | {}", e.getMessage(), e);
        }
    }
}
