package com.bonfile.model.tuple;

import com.bonfile.model.tuple.prototype.Tuple;

import java.util.LinkedList;

public class Quartet<T1, T2, T3, T4> extends Tuple {
    private T1 item1;
    private T2 item2;
    private T3 item3;
    private T4 item4;

    public Quartet() {}

    public Quartet(T1 item1, T2 item2, T3 item3, T4 item4) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
    }

    public T1 getItem1() {
        return item1;
    }

    public void setItem1(T1 item1) {
        this.item1 = item1;
    }

    public T2 getItem2() {
        return item2;
    }

    public void setItem2(T2 item2) {
        this.item2 = item2;
    }

    public T3 getItem3() {
        return item3;
    }

    public void setItem3(T3 item3) {
        this.item3 = item3;
    }

    public T4 getItem4() {
        return item4;
    }

    public void setItem4(T4 item4) {
        this.item4 = item4;
    }

    @Override
    public LinkedList<Object> getTuple() {
        LinkedList<Object> linkedList = new LinkedList<>();
        linkedList.add(this.item1);
        linkedList.add(this.item2);
        linkedList.add(this.item3);
        linkedList.add(this.item4);
        return linkedList;
    }
}
