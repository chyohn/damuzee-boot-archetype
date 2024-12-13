package com.damuzee.boot.spec.util;

import lombok.Getter;
import lombok.Setter;

/**
 * 4元组类
 *
 */
@Getter
@Setter
public class Tuple4<FIRST, SECOND, THIRD, FOURTH> extends Tuple3<FIRST, SECOND, THIRD> {

    private FOURTH fourth;

    public static <FIRST, SECOND, THIRD, FOURTH>
    Tuple4<FIRST, SECOND, THIRD, FOURTH> of(FIRST first, SECOND second, THIRD third, FOURTH fourth) {
        return new Tuple4<>(first, second, third, fourth);
    }

    public Tuple4(FIRST first, SECOND second, THIRD third, FOURTH fourth) {
        super(first, second, third);
        this.fourth = fourth;
    }

}
