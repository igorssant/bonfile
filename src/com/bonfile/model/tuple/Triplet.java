package com.bonfile.model.tuple;

import com.bonfile.model.tuple.prototype.Tuple;
import java.util.LinkedList;

public class Triplet<T1, T2, T3> extends Tuple {
    private T1 item1;
    private T2 item2;
    private T3 item3;

    public Triplet() {}

    public Triplet(T1 item1, T2 item2, T3 item3) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
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

    @Override
    public LinkedList<Object> getTuple() {
        LinkedList<Object> linkedList = new LinkedList<>();
        linkedList.add(this.item1);
        linkedList.add(this.item2);
        linkedList.add(this.item3);
        return linkedList;
    }
}
