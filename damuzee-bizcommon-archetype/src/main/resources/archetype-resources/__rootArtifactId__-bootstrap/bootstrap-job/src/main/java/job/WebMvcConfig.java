#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.job;

import ${package}.foundation.spec.http.interceptor.RequestLoggerInterceptor;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLoggerInterceptor requestLoggerInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        //请求参数记录日志拦截器
        interceptorRegistry.addInterceptor(requestLoggerInterceptor).addPathPatterns("/**");

    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        Charset defaultCharset = StandardCharsets.UTF_8;
        return new StringHttpMessageConverter(defaultCharset);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }

}