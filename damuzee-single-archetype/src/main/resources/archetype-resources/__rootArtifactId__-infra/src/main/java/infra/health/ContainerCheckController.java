#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.infra.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 容器健康检查
 */
@RestController
@Slf4j
@ResponseBody
public class ContainerCheckController {

    @GetMapping(value = "/ready")
    public String ready() throws Exception {
        return "success";
    }

    @GetMapping(value = "/health")
    public String health() throws Exception {
        return "success";
    }

}
