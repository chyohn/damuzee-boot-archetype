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
 * RPC结果数据对象结构
 *
 * @param <T> 业务数据对象
 * 
 * @apiNote rpc接口交互的数据结构
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RpcResult<T> implements IResult<T>, Serializable {

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
     * 调用链路UUID, 可以用调用链中的tid, 方便跟踪查问题
     */
    private String traceId;

    /**
     * 业务数据
     */
    private T data = null;

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
    public static <T> RpcResult<T> success() {
        return success(null);
    }


    /**
     * 创建成功结果对象
     *
     * @param data 业务数据对象
     * @param <T>  业务数据对象类型
     * @return
     */
    public static <T> RpcResult<T> success(T data) {
        return create(ResultCode.SUCCESS, null, data);
    }


    /**
     * 创建系统异常结果对象
     *
     * @param displayMsg 友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> RpcResult<T> failureBySys(String displayMsg) {
        return failure(ResultCode.SYS_EXCEPTION, displayMsg);
    }

    /**
     * 创建参数异常结果对象
     *
     * @param displayMsg 友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> RpcResult<T> failureByParam(String displayMsg) {
        return failure(ResultCode.PARAM_EXCEPTION, displayMsg);
    }

    /**
     * 创建失败结果对象，displayMsg从{@link IResultCode#getDisplayMsg()}获取
     *
     * @param resultCode 失败编码
     * @param <T>
     * @return
     */
    public static <T> RpcResult<T> failure(IResultCode resultCode) {
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
    public static <T> RpcResult<T> failure(IResultCode resultCode, String displayMsg) {
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
    public static <T> RpcResult<T> failure(IResultCode resultCode, String displayCode, String displayMsg) {
        return create(resultCode, displayCode, displayMsg, null);
    }

    /**
     * 创建失败结果对象
     *
     * @param code       内部详细结果编码，系统异常[1000,2000)；业务异常 [3000,4000)
     * @param msg        内部结果详细描述，真实错误信息
     * @param displayMsg 友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> RpcResult<T> failure(String code, String msg, String displayMsg) {
        return create(code, msg, null, displayMsg, null);
    }


    /**
     * 创建失败结果对象
     *
     * @param code        内部详细结果编码，系统异常[1000,2000)；业务异常 [3000,4000)
     * @param msg         内部结果详细描述，真实错误信息
     * @param displayCode 根源系统错误code，用于前端展示
     * @param displayMsg  友好的错误描述信息
     * @param <T>
     * @return
     */
    public static <T> RpcResult<T> failure(String code, String msg, String displayCode, String displayMsg) {
        return create(code, msg, displayCode, displayMsg, null);
    }

    /**
     * 根据异常构建结果
     *
     * @param ex   异常
     * @param <T>
     * @return
     */
    public static <T> RpcResult<T> failure(Throwable ex) {
        if (ex instanceof KindlyException) {
            KindlyException e = (KindlyException)ex;
            return create(e.getCode(), e.getMessage(), e.getDisplayCode(), e.getDisplayMsg(), null);
        }
        RpcResult<T> result = failureBySys(null);
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
    public static <T> RpcResult<T> create(IResultCode resultCode, String displayMsg, T data) {
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
    public static <T> RpcResult<T> create(IResultCode resultCode, String displayCode, String displayMsg, T data) {
        return create(resultCode.getCode(), resultCode.getMsg(), displayCode, displayMsg, data);
    }

    /**
     * 创建结果对象
     *
     * @param code        内部详细结果编码，系统异常[1000,2000)；业务异常 [3000,4000)
     * @param msg         内部结果详细描述，真实错误信息
     * @param displayCode 根源系统错误code，用于前端展示
     * @param displayMsg  友好的错误描述信息
     * @param data        业务数据对象
     * @param <T>         业务数据对象类型
     * @return
     */
    public static <T> RpcResult<T> create(String code, String msg, String displayCode, String displayMsg, T data) {

        if (ObjectUtils.isEmpty(displayCode)) {
            displayCode = DisplayCodeUtils.buildCode(code);
        }

        String traceId = RequestUtils.getTraceId();

        return RpcResult.<T>builder()
            .code(code)
            .msg(msg)
            .displayCode(displayCode)
            .displayMsg(displayMsg)
            .traceId(traceId)
            .data(data)
            .build();
    }

}
