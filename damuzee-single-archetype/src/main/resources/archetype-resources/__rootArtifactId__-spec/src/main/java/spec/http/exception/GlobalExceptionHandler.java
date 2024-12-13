#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.spec.http.exception;

import com.damuzee.boot.spec.result.vo.HttpResult;
import com.damuzee.boot.spec.result.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 *
 * 
 * @apiNote http业务接口, 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class})
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 其他异常或非检查异常处理
     */
    @ExceptionHandler
    @ResponseBody
    public HttpResult exceptionHandler(Exception e) {
        log.error("接口异常", e);
        // todo 业务系统需要添加自己的displayMsg
//        String displayMsg = e.getMessage();
        return HttpResult.failure(e);
    }

}
