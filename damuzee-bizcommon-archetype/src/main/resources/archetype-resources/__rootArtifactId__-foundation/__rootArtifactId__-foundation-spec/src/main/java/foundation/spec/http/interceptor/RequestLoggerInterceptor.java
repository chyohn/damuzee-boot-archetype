#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.foundation.spec.http.interceptor;

import com.damuzee.boot.spec.jackson.utils.JSONUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 请求参数记录
 *
 * 
 * @apiNote http前端后请求参数记录
 */
@Slf4j
@Component
public class RequestLoggerInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        Map<String, String[]> paramsMap = request.getParameterMap();
        String uri = request.getRequestURI();
        if ("/ready".equals(uri) || "/health".equals(uri)) {
            if (log.isDebugEnabled()) {
                log.debug("请求参数记录:uri:{},paramsMap:{}", uri, JSONUtil.toJSONString(paramsMap));
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("请求参数记录:uri:{},paramsMap:{}", uri, JSONUtil.toJSONString(paramsMap));
            }
        }
        return super.preHandle(request, response, handler);
    }

}
