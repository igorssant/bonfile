package com.bonfile.controller.tupleController;

import com.bonfile.model.tuple.Quartet;
import java.util.ArrayList;
import java.util.LinkedList;

public class QuartetController<T1, T2, T3, T4> {
    private Quartet<T1, T2, T3, T4> quartet;

    public QuartetController() {
        this.quartet = new Quartet<>();
    }

    public QuartetController(T1 item1, T2 item2, T3 item3, T4 item4) {
        this.quartet = new Quartet<>(item1, item2, item3, item4);
    }

    public QuartetController(ArrayList<Object> arrayList) {
        this.quartet = new Quartet<>();
        this.quartet.setItem1((T1) arrayList.get(0));
        this.quartet.setItem2((T2) arrayList.get(1));
        this.quartet.setItem3((T3) arrayList.get(2));
        this.quartet.setItem4((T4) arrayList.get(3));
    }

    public QuartetController(Quartet<T1, T2, T3, T4> quartet) {
        this.quartet = quartet;
    }

    public Quartet<T1, T2, T3, T4> getQuartet() {
        return quartet;
    }

    public void setQuartet(Quartet<T1, T2, T3, T4> quartet) {
        this.quartet = quartet;
    }

    public T1 getItem1() {
        return this.quartet.getItem1();
    }

    public void setItem1(T1 item1) {
        this.quartet.setItem1(item1);
    }

    public T2 getItem2() {
        return this.quartet.getItem2();
    }

    public void setItem2(T2 item2) {
        this.quartet.setItem2(item2);
    }

    public T3 getItem3() {
        return this.quartet.getItem3();
    }

    public void setItem3(T3 item3) {
        this.quartet.setItem3(item3);
    }

    public T4 getItem4() {
        return this.quartet.getItem4();
    }

    public void setItem4(T4 item4) {
        this.quartet.setItem4(item4);
    }

    public LinkedList<Object> getTuple() {
        return this.quartet.getTuple();
    }

    public void elementsToObject() {
        this.quartet = new Quartet<>(this.quartet.getItem1(), this.quartet.getItem2(), this.quartet.getItem3(), this.quartet.getItem4());
    }

    public Quartet<Object, Object, Object, Object> getElementsAsObject() {
        return new Quartet<>(this.quartet.getItem1(), this.quartet.getItem2(), this.quartet.getItem3(), this.quartet.getItem4());
    }
}
