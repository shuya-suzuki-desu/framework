package jp.co.vrst.basis.util;

import java.lang.reflect.Method;

import jp.co.vrst.basis.annotations.component.Bean;
import jp.co.vrst.basis.annotations.component.Component;
import jp.co.vrst.basis.annotations.component.Config;
import jp.co.vrst.basis.annotations.component.Controller;

import static jp.co.vrst.basis.common.Constants.BASE_PACKAGE_NAME;
import static jp.co.vrst.basis.common.Constants.EMPTY_STRING;;

public class ComponentUtils {

    /**
     * コンポーネントの種類を全て定義.<br>
     */
    public enum ComponentType {
        Component, Controller, Bean, NOT_BEAN;
    }

    /**
     * ビーンIDの生成.<br>
     */
    public static String getKeyName(Class<?> clazz) throws Exception {

        // ベースパッケージ配下のクラス以外は相手にしない
        String[] tree = clazz.getName().split("\\.");
        boolean isIncludedInBasePackage = tree.length > 1 && clazz.getPackage().getName().contains(BASE_PACKAGE_NAME);
        if (!isIncludedInBasePackage) return null;

        String className = tree[tree.length - 1];
        String beanId = "";

        // Component or Controller の場合のみ
        switch (getComponentType(clazz)) {
        case Component:
            beanId = clazz.getAnnotation(Component.class).value();
            break;
        case Controller:
            beanId = clazz.getAnnotation(Controller.class).value();
            break;
        default:
            break;
        }

        // 値が設定されていなければLCCをBeanIDに登録する
        if (StringUtils.isEmpty(beanId)) {
            beanId = className.substring(0, 1).toLowerCase()
                    + className.substring(1);
        }

        return beanId;
    }

    /**
     * ビーンIDの生成.<br>
     */
    public static String getKeyName(Method method) throws Exception {

        String key = method.getAnnotation(Bean.class).value();
        if (EMPTY_STRING.equals(key)) {
            String className = method.getReturnType().getSimpleName();
            key = className.substring(0, 1).toLowerCase() + className.substring(1);
        }

        return key;
    }

    /**
     * <code>ComponentType</code>を取得.<br>
     * 
     * @param clazz
     * @return
     */
    public static ComponentType getComponentType(Object object) {
        if (isComponent(object)) return ComponentType.Component;
        if (isController(object)) return ComponentType.Controller;
        if (isBean(object)) return ComponentType.Bean;
        return ComponentType.NOT_BEAN;
    }

    public static boolean isComponent(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }

    public static boolean isController(Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class);
    }

    public static boolean isConfig(Class<?> clazz) {
        return clazz.isAnnotationPresent(Config.class);
    }

    public static boolean isBean(Method method) {
        return method.isAnnotationPresent(Bean.class);
    }

    public static boolean isComponent(Object object) {
        return object.getClass().isAnnotationPresent(Component.class);
    }

    public static boolean isController(Object object) {
        return object.getClass().isAnnotationPresent(Controller.class);
    }

    public static boolean isBean(Object object) {
        if (!(object instanceof Method)) return false;
        Method method = (Method) object;
        return method.isAnnotationPresent(Bean.class);
    }

    public static boolean isConfig(Object object) {
        return object.getClass().isAnnotationPresent(Config.class);
    }
}
