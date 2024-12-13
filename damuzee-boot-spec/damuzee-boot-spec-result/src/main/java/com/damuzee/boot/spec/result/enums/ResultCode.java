package com.damuzee.boot.spec.result.enums;

import java.io.Serializable;
import lombok.Getter;

/**
 * [1000,2000)错误码定义
 *
 * @apiNote http/RPC交互的错误码定义
 */
@Getter
public enum ResultCode implements IResultCode, Serializable {


    SUCCESS("0000", "success"),
    SYS_EXCEPTION("1000", "系统未预料统一捕获的异常，例如空指针、数组越界等"),
    PARAM_EXCEPTION("1010", "参数异常，必填参数未传递"),
    DEPENDENCY_EXCEPTION("1020", "依赖服务（内部系统RPC）调用异常，超时、服务不可用等"),
    THIRD_PARTY_EXCEPTION("1030", "三方服务（外部系统http）调用异常，超时、对方系统异常等"),
    DATABASE_EXCEPTION("1040", "数据层（mysql、es等）异常，主键冲突、超时等"),
    CACHE_EXCEPTION("1050", "缓存异常"),
    RATE_LIMITED_EXCEPTION("1060", "请求被限流");

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String msg;


    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getDisplayMsg() {
        return null;
    }
}
