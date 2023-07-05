package jp.co.vrst.basis.container;

import static jp.co.vrst.basis.common.Constants.FILE_SEP;
import static jp.co.vrst.basis.common.Constants.PACKAGE_SEP;
import static jp.co.vrst.basis.common.Constants.BASE_PACKAGE_NAME;
import static jp.co.vrst.basis.common.Constants.SUF_CLASS;
import static jp.co.vrst.basis.common.Constants.EMPTY_STRING;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import jp.co.vrst.basis.util.ComponentUtils;
import jp.co.vrst.exception.BeanNotReturnedException;
import jp.co.vrst.exception.NotRegisterdBeanIdException;
import jp.co.vrst.test.Framework;

public class ComponentContainer implements Container {

    /** 
     * DIコンテナに登録されているのすべてのBean.<br>
     * 
     * @param key beanName 登録Bean名
     * @param value bean 登録Bean
     */
    public Map<String, Object> componentMap = new HashMap<>();
    /** 
     * DIコンテナに登録されているのすべてのController.<br>
     * 
     * @param key beanName 登録Controller名
     * @param value bean 登録Controller
     */
    public Map<String, Object> controllerMap = new HashMap<>();
    /** 
     * DIコンテナに登録されているのすべてのController.<br>
     * 
     * @param key beanName 登録Controller名
     * @param value bean 登録Controller
     */
    public Map<String, Method> methodMap = new HashMap<>();

    /** クラスローダー */
    ClassLoader loader = ClassLoader.getSystemClassLoader();

    @Override
    public void setAllObjectsUnder(final Object baseClass) {

        String packageName = baseClass.getClass().getPackage().getName();
        String packagePath = packageName.replace(PACKAGE_SEP, FILE_SEP);
        URL url = loader.getResource(packagePath);
        Path p = Path.of(url.getPath());
        this.setComponents(p.toFile());
    }

    /**
     * コンポーネント登録を行う.<br>
     * @param file
     */
    private void setComponents(final File file) {

        if (!file.isDirectory()) {
            // ビーンの登録処理
            String pathSeparatedPilliod = file.getPath().replace(FILE_SEP, PACKAGE_SEP); // "/" -> "."
            String className = pathSeparatedPilliod
                .substring(pathSeparatedPilliod.indexOf(BASE_PACKAGE_NAME, 0)) // ベースパッケージ以降の文字列を切り出し
                .replaceAll(SUF_CLASS, EMPTY_STRING); // ".class"を削除

            // よくわからないものが紛れてしまった場合の対処
            String[] tree = className.split("\\.");
            if (tree.length < 1)  return;

            // クラスファイルではない場合、除去
            if (!pathSeparatedPilliod.endsWith(SUF_CLASS)) return;

            // Beanの情報を保持しておく
            Class<?> clazz;
            try {
                clazz = loader.loadClass(className);
                String beanName = ComponentUtils.getKeyName(clazz);

                if (ComponentUtils.isComponent(clazz)) {
                    this.componentMap.put(beanName, clazz);
                }
                if (ComponentUtils.isController(clazz)) {
                    this.componentMap.put(beanName, clazz);
                    this.controllerMap.put(beanName, clazz);
                }
                if (ComponentUtils.isConfig(clazz)) {
                    this.setBeanMethod(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }

        // ディレクトリの場合は再起的な呼び出しを行い、配下のディレクトリ全てを検索する
        for (File subDirectory : file.listFiles()) {
            this.setComponents(subDirectory);
        }
    }

    /** 
     * 設定クラスの場合はメソッドを呼び出して、戻り値をMapに格納する.<br>
     * 
     * @param clazz
     * @throws Exception
     */
    private void setBeanMethod(Class<?> clazz) throws Exception {
        Method[] methods = clazz.getMethods();
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Arrays.stream(methods)
            .filter(method -> ComponentUtils.isBean(method))
            .forEach(method -> {
                try {
                    String key = ComponentUtils.getKeyName(method);
                    Object returnBean = method.invoke(instance);
                    if (returnBean == null) throw new BeanNotReturnedException();
                    this.componentMap.put(key, returnBean);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
    }

    @Override
    public Object returnObjectBy(String beanId) {
        if (!this.componentMap.containsKey(beanId)) throw new NotRegisterdBeanIdException();
        return this.componentMap.get(beanId);
    }

    /**
     * テストメソッド
     */
    public void testRun() throws Exception {
        Framework fw = new Framework();
        this.setAllObjectsUnder(fw);
    }
}
