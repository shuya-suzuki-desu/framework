package jp.co.vrst.exception;

import static jp.co.vrst.basis.common.ErrorMessageConstants.ILLEGAL_PREFIX_OR_SUFFIX;;

public class IllegalCommandException extends BaseException {
    public IllegalCommandException() {
        super(ILLEGAL_PREFIX_OR_SUFFIX);
    }

    public IllegalCommandException(String message) {
        super(ILLEGAL_PREFIX_OR_SUFFIX + " " + message);
    }
}
