#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.foundation.spec.translator;

import ${package}.foundation.spec.translator.IBaseTranslator;
import java.util.function.BiConsumer;

/**
 * 指定字段的翻译方式
 *
 * @param <Source>
 * @param <Target>
 */
public interface IAssignTranslator<Source, Target> extends IBaseTranslator<Source, Target> {

    Target translate(Source source, BiConsumer<Source, Target> consumer);

}
