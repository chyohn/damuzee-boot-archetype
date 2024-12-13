package com.damuzee.boot.spec.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import java.io.IOException;

/**
 * long超长自动转String
 */
public class LongToStringSerializer extends NumberSerializers.Base<Object> {

    public LongToStringSerializer() {
        super(Long.class, JsonParser.NumberType.LONG, "number");
    }

    @Override
    public void serialize(Object value, JsonGenerator gen,
        SerializerProvider provider) throws IOException {
        long number = ((Long) value).longValue();
        if (number >= 10000000000000000L) {
            gen.writeString(number + "");
        } else {
            gen.writeNumber(number);
        }
    }

}