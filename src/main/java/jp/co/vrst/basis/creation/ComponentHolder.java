package jp.co.vrst.basis.creation;

import java.io.File;
import java.lang.reflect.Method;

public interface ComponentHolder {

    void setComponents(final File file) throws Exception;
    Class returnClassBy(String request);
    Method returnMethodBy(String request);
}
