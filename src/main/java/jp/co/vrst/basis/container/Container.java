package jp.co.vrst.basis.container;

/**
 * コンポーネントホルダー.<br>
 * ビーンを保持するクラスのインターフェース
 * 
 * @version 1.0.0
 * @author 鈴木@VRST
 */
public interface Container {

    /** 指定したクラスを含む配下のコンポーネントを取得する. */
    void setAllObjectsUnder(final Object baseClass);
    /** idに紐づくオブジェクトを呼び出す. */
    Object returnObjectBy(final String id);
}
