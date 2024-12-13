package com.damuzee.boot.spec.util;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 2元组类
 */
@Getter
@Setter
public class Tuple2<FIRST, SECOND> implements Serializable {

    private FIRST first;

    private SECOND second;

    public static <FIRST, SECOND> Tuple2<FIRST, SECOND> of(FIRST first, SECOND second) {
        return new Tuple2<>(first, second);
    }

    public Tuple2(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

}
