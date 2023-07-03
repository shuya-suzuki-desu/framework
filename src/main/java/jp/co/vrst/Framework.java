package jp.co.vrst;

import java.util.Map.Entry;

import jp.co.vrst.basis.creation.ComponentHolderImpl;
import jp.co.vrst.test.TestRun;

public class Framework {

    public static void main(String[] args) throws Exception {
        ComponentHolderImpl ch = new ComponentHolderImpl();
        ch.testRun();
        for (Entry<String, Class<?>> e : ch.compoentMap.entrySet()) {
            String str = (ch.controllerMap.containsKey(e.getKey())) ? "(controller)" : "";
            System.out.print(e.getKey() + ": " + e.getValue() + str);
        }
    }
}
