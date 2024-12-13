#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.spec.translator;


public interface IBaseTranslator<Source, Target> {

    /**
     * 映射同名属性，可以通过覆盖来实现更复杂的映射逻辑.
     * <p>
     * <p>包含常用的Java类型自动转换</p>
     *
     * @param source 源类型
     * @return 目标类型
     */
    Target translate(Source source);
}
