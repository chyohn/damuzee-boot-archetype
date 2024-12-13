package com.damuzee.boot.spec.result.exception;


import com.damuzee.boot.spec.result.enums.IResultCode;
import com.damuzee.boot.spec.result.utils.RequestUtils;
import com.damuzee.boot.spec.result.utils.DisplayCodeUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 应用所抛出的异常类封装
 *
 * 
 * 
 */
@Getter
@Setter
public class KindlyException extends RuntimeException {

    /**
     * 结果code，系统异常[1000,2000)；业务异常 [3000,4000)；
     */
    private String code;

    /**
     * 根源系统错误code，用于前端展示
     * <p>
     * 格式："{产品域(英文/缩写)}"-"{RDMS系统(英文/缩写)}"-"{code}"
     */
    private String displayCode;

    /**
     * 提供友好的错误描述信息给前端UI展示，
     * <p>注意1：需做国际化处理
     * <p>注意2：返回<code>null</code>，则需要通过其它方式提供友好信息提示给上游
     */
    private String displayMsg;


    /**
     * 调用链路UUID，可以用调用链中的tid，方便跟踪查问题
     */
    String traceId;

    public KindlyException(IResultCode code) {
        this(code.getCode(), code.getMsg(), code.getDisplayMsg(), null);
    }

    public KindlyException(IResultCode code, String displayMsg) {
        this(code.getCode(), code.getMsg(), displayMsg, null);
    }

    public KindlyException(IResultCode code, String displayMsg, Throwable e) {
        this(code.getCode(), code.getMsg(), displayMsg, e);
    }

    public KindlyException(String code, String message) {
        this(code, message, null, null);
    }

    public KindlyException(String code, String message, String displayMsg) {
        this(code, message, displayMsg, null);
    }

    public KindlyException(String code, String message, String displayMsg, Throwable e) {
        this(code, message, null, displayMsg, e);
    }

    public KindlyException(String code, String message, String displayCode, String displayMsg, Throwable e) {
        super(message, e);
        this.code = code;
        this.displayMsg = displayMsg;
        this.displayCode = displayCode == null || displayCode.trim().isEmpty() ?
            DisplayCodeUtils.buildCode(code) : displayCode;
        this.traceId = RequestUtils.getTraceId();

    }

}
