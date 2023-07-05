package jp.co.vrst.exception;

import static jp.co.vrst.basis.common.ErrorMessageConstants.NOT_REGISTERED_BEAN_ID;

public class NotRegisterdBeanIdException extends BaseException {

    public NotRegisterdBeanIdException() {
        super(NOT_REGISTERED_BEAN_ID);
    }

    public NotRegisterdBeanIdException(String message) {
        super(NOT_REGISTERED_BEAN_ID + " " + message);
    }
}
