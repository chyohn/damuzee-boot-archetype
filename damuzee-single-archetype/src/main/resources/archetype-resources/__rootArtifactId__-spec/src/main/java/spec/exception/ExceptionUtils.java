#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
    package ${package}.spec.exception;

import ${package}.spec.enums.BizResultCode;
import com.damuzee.boot.spec.result.enums.IResultCode;
import com.damuzee.boot.spec.result.exception.KindlyException;

/**
 * 业务异常工具类
 * <p>
 * 关于错误码的设计，参考：{@link BizResultCode}
 */
public class ExceptionUtils {

    /**
     * throw KindlyException
     *
     * @param code 错误编码
     */
    public static void throwException(IResultCode code) {
        throw new KindlyException(code);
    }

    /**
     * throw KindlyException
     *
     * @param code       错误编码
     * @param displayMsg 展示给前端的错误信息
     */
    public static void throwException(IResultCode code, String displayMsg) {
        throw new KindlyException(code, displayMsg);
    }

    /**
     * throw KindlyException
     *
     * @param code       错误编码
     * @param displayMsg 展示给前端的错误信息
     * @param causeBy    cause
     */
    public static void throwException(IResultCode code, String displayMsg, Throwable causeBy) {
        throw new KindlyException(code, displayMsg, causeBy);
    }

    /**
     * throw KindlyException
     *
     * @param code
     * @param message
     */
    public static void throwException(String code, String message) {
        throw new KindlyException(code, message);
    }

    /**
     * throw KindlyException
     *
     * @param code
     * @param message
     * @param displayMsg
     */
    public static void throwException(String code, String message, String displayMsg) {
        throw new KindlyException(code, message, displayMsg);
    }

    /**
     * throw KindlyException
     *
     * @param code
     * @param message
     * @param displayMsg
     * @param causeBy
     */
    public static void throwException(String code, String message, String displayMsg, Throwable causeBy) {
        throw new KindlyException(code, message, displayMsg, causeBy);
    }

    /**
     * throw KindlyException
     *
     * @param code
     * @param message
     * @param displayCode
     * @param displayMsg
     * @param causeBy
     */
    public static void throwException(String code, String message, String displayCode, String displayMsg,
        Throwable causeBy) {
        throw new KindlyException(code, message, displayCode, displayMsg, causeBy);

    }

}
