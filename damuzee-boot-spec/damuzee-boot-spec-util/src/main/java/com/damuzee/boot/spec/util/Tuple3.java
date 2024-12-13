package com.damuzee.boot.spec.util;

import lombok.Getter;
import lombok.Setter;

/**
 * 3元组类
 */
@Getter
@Setter
public class Tuple3<FIRST, SECOND, THIRD> extends Tuple2<FIRST, SECOND> {

    private THIRD third;

    public static <FIRST, SECOND, THIRD> Tuple3<FIRST, SECOND, THIRD> of(FIRST first, SECOND second, THIRD third) {
        return new Tuple3<>(first, second, third);
    }

    public Tuple3(FIRST first, SECOND second, THIRD third) {
        super(first, second);
        this.third = third;
    }
}
