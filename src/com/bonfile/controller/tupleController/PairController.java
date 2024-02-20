package com.bonfile.controller.tupleController;

import com.bonfile.model.tuple.Pair;
import java.util.LinkedList;

public class PairController<T1, T2> {
    private Pair<T1, T2> pair;

    public PairController() {
        this.pair = new Pair<>();
    }

    public PairController(T1 item1, T2 item2){
        this.pair = new Pair<>(item1, item2);
    }

    public PairController(Pair<T1, T2> pair) {
        this.pair = pair;
    }

    public Pair<T1, T2> getPair() {
        return pair;
    }

    public void setPair(Pair<T1, T2> pair) {
        this.pair = pair;
    }

    public T1 getItem1() {
        return this.pair.getItem1();
    }

    public T2 getItem2() {
        return this.pair.getItem2();
    }

    public void setItem1(T1 item1) {
        this.pair.setItem1(item1);
    }

    public void setItem2(T2 item2) {
        this.pair.setItem2(item2);
    }

    public LinkedList<Object> getTuple() {
        return this.pair.getTuple();
    }

    public void elementsToObject() {
        this.pair = new Pair<>(this.pair.getItem1(), this.pair.getItem2());
    }
    public Pair<Object, Object> getElementsAsObject() {
        return new Pair<>(this.pair.getItem1(), this.pair.getItem2());
    }
}
