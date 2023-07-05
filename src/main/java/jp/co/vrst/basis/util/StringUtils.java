package jp.co.vrst.basis.util;

import static jp.co.vrst.basis.common.Constants.*;

import java.util.Objects;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return (
            Objects.isNull(str)
            || EMPTY_STRING.equals(str)
            || str.replace(" ", EMPTY_STRING).replace("　", EMPTY_STRING).equals(EMPTY_STRING)
        );
    }
}
