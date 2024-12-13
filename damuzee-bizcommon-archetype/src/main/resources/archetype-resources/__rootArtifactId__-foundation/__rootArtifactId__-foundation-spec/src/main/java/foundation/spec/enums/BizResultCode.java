#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
    package ${package}.foundation.spec.enums;

import com.damuzee.boot.spec.result.enums.IResultCode;

/**
 * 业务错误码自定义枚举
 *
 * <p> 系统错误码可见{@link com.damuzee.boot.spec.result.enums.ResultCode}
 * <p> 错误码范围使用说明：
 * <ul>
 *     <li>系统异常[1000,2000)；已定义的错误码可见{@link com.damuzee.boot.spec.result.enums.ResultCode}，
 *     <li>业务异常 [3000,4000)。
 * </ul>
 *
 * @apiNote http/RPC交互的错误码定义
 * @see com.damuzee.boot.spec.result.vo.RpcResult
 * @see com.damuzee.boot.spec.result.vo.HttpResult
 */
public enum BizResultCode implements IResultCode {

//    NOT_ENOUGH_BALANCE("3001", "余额不足", "not enough balance"),
//    NOT_SUPPORT_DELIVERY_ADDRESS("3002", "收获地址超出配送范围", "address is not support")
    ;

    /**
     * 结果code: 系统异常[1000,2000)；业务异常 [3000,4000)
     */
    private final String code;
    /**
     * 真实错误信息，用于内部排查问题时展示
     */
    private final String msg;
    /**
     * 友好的错误描述信息（需做国际化处理）
     */
    private final String displayMsg;

    BizResultCode(String code, String msg, String displayMsg) {
        this.code = code;
        this.msg = msg;
        this.displayMsg = displayMsg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public String getDisplayMsg() {
        return this.displayMsg;
    }
}
