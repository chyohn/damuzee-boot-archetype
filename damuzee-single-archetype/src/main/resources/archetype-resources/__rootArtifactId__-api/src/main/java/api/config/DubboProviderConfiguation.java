#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboProviderConfiguation {

//    @Bean
//    public ServiceBean<IAdjustSdkService> transformService(IAdjustSdkService adjustSdkService) {
//        ServiceBean<IAdjustSdkService> serviceBean = new ServiceBean<>();
//        serviceBean.setInterface(IAdjustSdkService.class.getName());
//        serviceBean.setRef(adjustSdkService);
//        serviceBean.setTimeout(5000);
//        return serviceBean;
//    }

}
