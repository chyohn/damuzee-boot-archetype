package com.damuzee.boot.spec;

import com.damuzee.boot.spec.jackson.utils.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@SpringBootApplication(scanBasePackages = {"com.damuzee.boot.spec.jackson"})
@ActiveProfiles(value = "include_null")
public class SpringTest {

    @Test
    public void testParseObject_typeReference() {
        Hello hello = new Hello();
        hello.setHelloContent("hello leo");
        Assertions.assertTrue(JSON.toJSONString(hello).contains("null"));
    }

    @Getter
    @Setter
    public static class Hello {

        private String helloContent;
        private Integer testNoneField;
    }

}
