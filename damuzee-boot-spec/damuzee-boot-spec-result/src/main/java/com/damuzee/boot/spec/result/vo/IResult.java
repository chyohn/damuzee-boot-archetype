package com.damuzee.boot.spec.result.vo;

import com.damuzee.boot.spec.result.exception.KindlyException;
import java.io.Serializable;

/**
 * 结果接口
 *
 * 
 * 
 */
public interface IResult<T> extends Serializable {

    /**
     * 结果code，成功：0000；系统异常[1000,2000)；业务异常 [3000,4000)；
     */
    String getCode();

    /**
     * 内部结果详细描述，真实错误信息
     */
    String getMsg();

    /**
     * 根源系统错误code，用于前端展示
     * <p>
     * 格式："{产品域(英文/缩写)}"-"{RDMS系统(英文/缩写)}"-"{code}"
     */
    String getDisplayCode();

    /**
     * 友好的错误描述信息
     */
    String getDisplayMsg();

    /**
     * 调用链路UUID，可以用调用链中的tid，方便跟踪查问题
     */
    String getTraceId();

    /**
     * 业务数据
     */
    T getData();

    /**
     * 是否成功
     *
     * @return true:成功;false:失败
     */
    boolean isSuccess();

    /**
     * 检查结果，如果失败则抛出{@link KindlyException}异常
     */
    default void assertSuccess() {
        if (isSuccess()) {
            return;
        }
        KindlyException e = new KindlyException(getCode(), getMsg(), getDisplayMsg());
        e.setDisplayCode(getDisplayCode());
        e.setTraceId(getTraceId());
        throw e;
    }
}
