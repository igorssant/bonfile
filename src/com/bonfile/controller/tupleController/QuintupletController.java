package com.bonfile.controller.tupleController;

import com.bonfile.model.tuple.Quintuplet;
import java.util.LinkedList;

public class QuintupletController<T1, T2, T3, T4, T5> {
    private Quintuplet<T1, T2, T3, T4, T5> quintuplet;

    public QuintupletController() {
        this.quintuplet = new Quintuplet<>();
    }

    public QuintupletController(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5) {
        this.quintuplet = new Quintuplet<>(item1, item2, item3, item4, item5);
    }

    public QuintupletController(Quintuplet<T1, T2, T3, T4, T5> quintuplet) {
        this.quintuplet = quintuplet;
    }

    public Quintuplet<T1, T2, T3, T4, T5> getQuintuplet() {
        return quintuplet;
    }

    public void setQuintuplet(Quintuplet<T1, T2, T3, T4, T5> quintuplet) {
        this.quintuplet = quintuplet;
    }

    public T1 getItem1() {
        return this.quintuplet.getItem1();
    }

    public void setItem1(T1 item1) {
        this.quintuplet.setItem1(item1);
    }

    public T2 getItem2() {
        return this.quintuplet.getItem2();
    }

    public void setItem2(T2 item2) {
        this.quintuplet.setItem2(item2);
    }

    public T3 getItem3() {
        return this.quintuplet.getItem3();
    }

    public void setItem3(T3 item3) {
        this.quintuplet.setItem3(item3);
    }

    public T4 getItem4() {
        return this.quintuplet.getItem4();
    }

    public void setItem4(T4 item4) {
        this.quintuplet.setItem4(item4);
    }

    public T5 getItem5() {
        return this.quintuplet.getItem5();
    }

    public void setItem5(T5 item5) {
        this.quintuplet.setItem5(item5);
    }

    public LinkedList<Object> getTuple() {
       return this.quintuplet.getTuple();
    }
}
