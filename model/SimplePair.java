package model;

public class SimplePair<T, M> {
    private T obj1;
    private M obj2;

    public SimplePair(T obj1, M obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public T getObj1() {
        return obj1;
    }

    public M getObj2() {
        return obj2;
    }
}
