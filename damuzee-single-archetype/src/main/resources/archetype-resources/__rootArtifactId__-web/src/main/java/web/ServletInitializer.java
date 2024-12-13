#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
    package ${package}.web;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationStartup.class);
    }

}