#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.web;

import ${package}.foundation.spec.http.interceptor.RequestLoggerInterceptor;
import com.baomidou.mybatisplus.annotation.IEnum;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
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

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new ConverterFactory<String, IEnum>() {
            @Override
            public <T extends IEnum> Converter<String, T> getConverter(Class<T> targetType) {
                return source -> {
                    Assert.isTrue(targetType.isEnum(), () -> "the class what implements IEnum must be the enum type");
                    return Arrays.stream(targetType.getEnumConstants())
                        .filter(t -> String.valueOf(t.getValue()).equals(source))
                        .findFirst().orElse(null);
                };
            }
        });
    }
}