package com.damuzee.boot.spec.util;

import lombok.Getter;
import lombok.Setter;

/**
 * 5元组类
 */
@Getter
@Setter
public class Tuple5<FIRST, SECOND, THIRD, FOURTH, FIFTH> extends Tuple4<FIRST, SECOND, THIRD, FOURTH>{

    private FIFTH fifth;

    public static <FIRST, SECOND, THIRD, FOURTH, FIFTH>
    Tuple5<FIRST, SECOND, THIRD, FOURTH, FIFTH> of(FIRST first, SECOND second, THIRD third, FOURTH fourth, FIFTH fifth) {
        return new Tuple5<>(first, second, third, fourth, fifth);
    }

    public Tuple5(FIRST first, SECOND second, THIRD third, FOURTH fourth, FIFTH fifth) {
        super(first, second, third, fourth);
        this.fifth = fifth;
    }

}
