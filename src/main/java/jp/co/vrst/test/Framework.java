package jp.co.vrst.test;

import java.util.Map.Entry;

import jp.co.vrst.basis.container.ComponentContainer;

public class Framework {

    /*
     * 現在動作確認用で動かしているメソッド
     */
    public static void main(String[] args) throws Exception {
        ComponentContainer container = new ComponentContainer();
        container.testRun();
        for (Entry<String, Object> e : container.componentMap.entrySet()) {
            String controller = (container.controllerMap.containsKey(e.getKey())) ? " (controller)" : "";
            String beanType = (e.getValue() instanceof Class<?>) ? ((Class<?>)e.getValue()).getName() : e.getValue().getClass().getName();
            System.out.println(e.getKey() + ": " + beanType + controller);
        }
    }
}
