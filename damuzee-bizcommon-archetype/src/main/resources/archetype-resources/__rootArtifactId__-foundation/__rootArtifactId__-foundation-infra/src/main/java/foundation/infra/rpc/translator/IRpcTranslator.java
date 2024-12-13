#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.foundation.infra.rpc.translator;

import ${package}.foundation.spec.translator.IBaseTranslator;

/**
 * rpc模型转换器
 */
public interface IRpcTranslator<Source, Target> extends IBaseTranslator<Source, Target> {

}
