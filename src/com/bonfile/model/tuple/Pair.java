package com.bonfile.model.tuple;

import com.bonfile.model.tuple.prototype.Tuple;

import java.util.LinkedList;

public class Pair<T1, T2> extends Tuple {
    private T1 item1;
    private T2 item2;

    public Pair() {}

    public Pair(T1 item1, T2 item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public T1 getItem1() {
        return item1;
    }

    public T2 getItem2() {
        return item2;
    }

    public void setItem1(T1 item1) {
        this.item1 = item1;
    }

    public void setItem2(T2 item2) {
        this.item2 = item2;
    }

    @Override
    public LinkedList<Object> getTuple() {
        LinkedList<Object> linkedList = new LinkedList<>();
        linkedList.add(this.item1);
        linkedList.add(this.item2);
        return linkedList;
    }
}
