package jp.co.vrst.exception;

import static jp.co.vrst.basis.common.ErrorMessageConstants.BEAN_NOT_RETURNED;;

public class BeanNotReturnedException extends BaseException {
    
    public BeanNotReturnedException() {
        super(BEAN_NOT_RETURNED);
    }

    public BeanNotReturnedException(String message) {
        super(BEAN_NOT_RETURNED + " " + message);
    }
}
