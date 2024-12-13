package com.damuzee.boot.spec.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 处理key为number,value不为number的情况
 */
@Slf4j
public class FixedNumberKeySerializer extends StdKeySerializers.Default {

    private Class<?> cls;

    public FixedNumberKeySerializer(int typeId, Class<?> type) {
        super(typeId, type);
        cls = type;
    }

    @Override
    public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
        if (!isNumber() || value instanceof Number) {
            //type为非number的正常处理、value为真实number的情况下正常处理
            super.serialize(value, g, provider);
            return;
        }

        //type为number value不为number的情况，当做toString处理
        log.warn("javaType:{},valueType:{} 不匹配,请检查!", cls.getName(), value.getClass().getName());
        g.writeFieldName(value.toString());
    }

    private boolean isNumber() {
        return _typeId == 5 || _typeId == 6;
    }

}
