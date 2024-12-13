package com.damuzee.boot.spec.result.enums;

import java.io.Serializable;

/**
 * 错误码接口定义，业务方可以实现该接口，扩展自己的业务码
 *
 * @apiNote http/RPC交互的错误码定义接口
 */
public interface IResultCode extends Serializable {

    /**
     * 提供结果（错误）对应编码
     * <p>注：不可为<code>null</code>
     */
    String getCode();


    /**
     * 提供结果描述，用于内部错误信息展示
     * <p>
     * 原名：getDesc()
     */
    String getMsg();

    /**
     * 提供友好的错误描述信息给前端UI展示，
     * <p>注意1：需做国际化处理
     * <p>注意2：返回<code>null</code>，则需要通过其它方式提供友好信息提示给上游
     */
    String getDisplayMsg();
}
