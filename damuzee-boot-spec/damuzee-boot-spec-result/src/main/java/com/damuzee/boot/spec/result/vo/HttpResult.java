package com.damuzee.boot.spec.result.vo;


import com.damuzee.boot.spec.result.enums.IResultCode;
import com.damuzee.boot.spec.result.enums.ResultCode;
import com.damuzee.boot.spec.result.exception.KindlyException;
import com.damuzee.boot.spec.result.utils.DisplayCodeUtils;
import com.damuzee.boot.spec.result.utils.RequestUtils;
import com.damuzee.boot.spec.util.ObjectUtils;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * http 交互对象
 *
 * @param <T> 业务数据对象
 * 
 * @apiNote http前后端交互, 业务数据承载数据结构
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HttpResult<T> implements IResult<T>, Serializable {

    /**
     * 结果code，成功：0000；系统异常[1000,2000)；业务异常 [3000,4000)；
     */
    private String code;

    /**
     * 内部结果详细描述，真实错误信息
     */
    private String msg;

    /**
     * 根源系统错误code，用于前端展示
     * <p>
     * 格式："{产品域(英文/缩写)}"-"{RDMS系统(英文/缩写)}"-"{code}"
     */
    private String displayCode;


    /**
     * 友好的错误描述信息
     */
    private String displayMsg;


    /**
     * 业务数据
     */
    private T data = null;

    /**
     * 调用链路UUID，可以用调用链中的tid，方便跟踪查问题
     */
    String traceId;

    /**
     * 行为code，用于指示前端执行一些特殊的操作，（比如token即将过期时，可以告知前端需要刷新token）
     */
    private String actionCode;

    /**
     * 是否成功
     *
     * @return true:成功;false:失败
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.getCode());
    }

    /**
     * 创建成功结果对象, data字段为null
     *
     * @param <T> 业务数据对象类型
     * @return
     */
    public static <T> HttpResult<T> success() {
        return success(null);
    }

    /**
     * 创建成功结果对象
     *
     * @param data 业务数据对象
     * @param <T>  业务数据对象类型
     * @return
     */
    public static <T> HttpResult<T> success(T data) {
        return create(ResultCode.SUCCESS, null, data);
    }

    /**
     * 创建系统异常结果对象
     *
     * @param displayMsg 友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failureBySys(String displayMsg) {
        return failureBySys(null, displayMsg);
    }

    /**
     * 创建系统异常结果对象
     *
     * @param displayCode 根源系统错误code，用于前端展示
     * @param displayMsg  友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failureBySys(String displayCode, String displayMsg) {
        return failure(ResultCode.SYS_EXCEPTION, displayCode, displayMsg);
    }

    /**
     * 创建参数异常结果对象
     *
     * @param displayMsg 友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failureByParam(String displayMsg) {
        return failureByParam(null, displayMsg);
    }

    /**
     * 创建参数异常结果对象
     *
     * @param displayCode 根源系统错误code，用于前端展示
     * @param displayMsg  友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failureByParam(String displayCode, String displayMsg) {
        return failure(ResultCode.PARAM_EXCEPTION, displayCode, displayMsg);

    }

    /**
     * 创建失败结果对象，displayMsg从{@link IResultCode#getDisplayMsg()}获取
     *
     * @param resultCode 失败编码
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failure(IResultCode resultCode) {
        return create(resultCode, resultCode.getDisplayMsg(), null);
    }

    /**
     * 创建失败结果对象
     *
     * @param resultCode 失败编码
     * @param displayMsg 友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failure(IResultCode resultCode, String displayMsg) {
        return create(resultCode, displayMsg, null);
    }

    /**
     * 创建失败结果对象
     *
     * @param resultCode  失败编码
     * @param displayCode 根源系统错误code，用于前端展示
     * @param displayMsg  友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failure(IResultCode resultCode, String displayCode, String displayMsg) {
        return create(resultCode, displayCode, displayMsg, null);
    }

    /**
     * @param code       失败编码
     * @param msg        内部结果详细描述，真实错误信息
     * @param displayMsg 友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failure(String code, String msg, String displayMsg) {
        return create(code, msg, null, displayMsg, null);
    }

    /**
     * @param code        失败编码
     * @param msg         内部结果详细描述，真实错误信息
     * @param displayCode 根源系统错误code，用于前端展示
     * @param displayMsg  友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failure(String code, String msg, String displayCode, String displayMsg) {
        return create(code, msg, displayCode, displayMsg, null);
    }

    /**
     * 根据异常构建结果
     *
     * @param ex   异常
     * @param <T>
     * @return
     */
    public static <T> HttpResult<T> failure(Throwable ex) {
        if (ex instanceof KindlyException) {
            KindlyException e = (KindlyException)ex;
            return create(e.getCode(), e.getMessage(), e.getDisplayCode(), e.getDisplayMsg(), null);
        }
        HttpResult<T> result = failureBySys(null);
        result.setMsg(ex.getMessage());
        return result;
    }

    /**
     * 创建结果对象
     *
     * @param resultCode 内部详细结果编码
     * @param displayMsg 友好的错误描述信息
     * @param data       业务数据对象
     * @param <T>        业务数据对象类型
     * @return
     */
    public static <T> HttpResult<T> create(IResultCode resultCode, String displayMsg, T data) {
        return create(resultCode.getCode(), resultCode.getMsg(), null, displayMsg, data);
    }

    /**
     * 创建结果对象
     *
     * @param resultCode  内部详细结果编码
     * @param displayCode 根源系统错误code，用于前端展示
     * @param displayMsg  友好的错误描述信息
     * @param data        业务数据对象
     * @param <T>         业务数据对象类型
     * @return
     */
    public static <T> HttpResult<T> create(IResultCode resultCode, String displayCode, String displayMsg, T data) {
        return create(resultCode.getCode(), resultCode.getMsg(), displayCode, displayMsg, data);
    }

    /**
     * 创建结果对象
     *
     * @param code        内部详细结果编码，系统异常[1000,2000)；业务异常 [3000,4000)
     * @param msg         内部结果详细描述，真实错误信息
     * @param displayCode 根源系统错误code，用于前端展示，如果为null，自动创建格式："{产品域(英文/缩写)}"-"{RDMS系统(英文/缩写)}"-"{code}"
     * @param displayMsg  友好的错误描述信息
     * @param data        业务数据对象
     * @param <T>         业务数据对象类型
     * @return
     */
    public static <T> HttpResult<T> create(String code, String msg, String displayCode, String displayMsg, T data) {

        if (ObjectUtils.isEmpty(displayCode)) {
            displayCode = DisplayCodeUtils.buildCode(code);
        }

        String traceId = RequestUtils.getTraceId();

        return HttpResult.<T>builder()
            .code(code)
            .msg(msg)
            .displayCode(displayCode)
            .displayMsg(displayMsg)
            .data(data)
            .traceId(traceId)
            .build();
    }


}
