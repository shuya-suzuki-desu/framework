package jp.co.vrst.basis.creation;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jp.co.vrst.Framework;
import jp.co.vrst.basis.annotations.Bean;
import jp.co.vrst.basis.annotations.Component;
import jp.co.vrst.basis.annotations.Config;
import jp.co.vrst.basis.annotations.Controller;
import jp.co.vrst.test.TestRun;

import static jp.co.vrst.common.Constants.*;

public class ComponentHolderImpl implements ComponentHolder, TestRun {

    /** 
     * DIコンテナに登録されているのすべてのBean
     * 
     * @param key beanName 登録Bean名
     * @param value bean 登録Bean
     */
    public Map<String, Class<?>> compoentMap = new HashMap<>();
    /** DIコンテナに登録されているのすべてのController
     * 
     * @param key beanName 登録Controller名
     * @param value bean 登録Controller
     */
    public Map<String, Class<?>> controllerMap = new HashMap<>();

    static ClassLoader loader = ClassLoader.getSystemClassLoader();

    /**
     * 指定したファイルを含むディレクトリ配下すべてのクラスを取得する
     */
    public void setComponents(final File file) throws Exception {
        if (!file.isDirectory()) {
            // ビーンの登録処理
            String pathSeparatedPilliod = file.getPath().replace(FILE_SEP, PACKAGE_SEP); // スラッシュ→.
            String className = pathSeparatedPilliod
                .substring(pathSeparatedPilliod.indexOf(BASE_PACKAGE_NAME, 0)) // ベースパッケージ以降の文字列を切り出し
                .replaceAll(SUF_CLASS, EMPTY_STRING); // ".class"を削除
            String[] tree = className.split(".");
            if (tree.length <= 1) {
                return;
            }

            String beanName = tree[tree.length - 1].substring(0, 1).toLowerCase()
                        + tree[tree.length - 1].substring(1); // クラス名を取得し、LCCに変換

            System.out.println(beanName);
            Class<?> clazz;
            try {
                clazz = loader.loadClass(className);
                if (this.isComponent(clazz)) this.compoentMap.put(beanName, clazz);
                if (this.isController(clazz)) this.controllerMap.put(beanName, clazz);
                if (this.isConfig(clazz)) this.setConfig(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
            return;
        }

        for (File f : file.listFiles()) {
            this.setComponents(f);
        }
    }

    /**
     * テストメソッド
     */
    public void testRun() throws Exception {
        Framework fw = new Framework();

        Package pkg = fw.getClass().getPackage();
        String packageName = pkg.getName();
        String packageResource = packageName.replace(PACKAGE_SEP, FILE_SEP);
        URL url = loader.getResource(packageResource);
        Path p = Path.of(url.getPath());
        this.setComponents(p.toFile());
    }

    private void setConfig(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods)
            .filter(method -> this.isBean(method))
            .forEach(method -> {
                try {
                    this.compoentMap
                                    .put(
                                        method.getAnnotation(Bean.class).value(),
                                        (method.invoke(clazz) instanceof Class<?>) ? (Class<?>) method.invoke(clazz) : null);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
    }

    private boolean isComponent(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }

    private boolean isController(Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class);
    }

    private boolean isBean(Method method) {
        return method.isAnnotationPresent(Bean.class);
    }

    private boolean isConfig(Class<?> clazz) {
        return clazz.isAnnotationPresent(Config.class);
    }

    @Override
    public Class<?> returnClassBy(String request) {
        return null;
    }

    @Override
    public Method returnMethodBy(String request) {
        return null;
    }

}
