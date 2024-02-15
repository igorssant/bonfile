package com.bonfile.controller.tupleController;

import com.bonfile.model.tuple.Sextet;
import java.util.LinkedList;

public class SextetController<T1, T2, T3, T4, T5, T6> {
    private Sextet<T1, T2, T3, T4, T5, T6> sextet;

    public SextetController() {
        this.sextet = new Sextet<>();
    }

    public SextetController(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6) {
        this.sextet = new Sextet<>(item1, item2, item3, item4, item5, item6);
    }

    public SextetController(Sextet<T1, T2, T3, T4, T5, T6> sextet) {
        this.sextet = sextet;
    }

    public Sextet<T1, T2, T3, T4, T5, T6> getSextet() {
        return sextet;
    }

    public void setSextet(Sextet<T1, T2, T3, T4, T5, T6> sextet) {
        this.sextet = sextet;
    }

    public T1 getItem1() {
        return this.sextet.getItem1();
    }

    public void setItem1(T1 item1) {
        this.sextet.setItem1(item1);
    }

    public T2 getItem2() {
        return this.sextet.getItem2();
    }

    public void setItem2(T2 item2) {
        this.sextet.setItem2(item2);
    }

    public T3 getItem3() {
        return this.sextet.getItem3();
    }

    public void setItem3(T3 item3) {
        this.sextet.setItem3(item3);
    }

    public T4 getItem4() {
        return this.sextet.getItem4();
    }

    public void setItem4(T4 item4) {
        this.sextet.setItem4(item4);
    }

    public T5 getItem5() {
        return this.sextet.getItem5();
    }

    public void setItem5(T5 item5) {
        this.sextet.setItem5(item5);
    }

    public T6 getItem6() {
        return this.sextet.getItem6();
    }

    public void setItem6(T6 item6) {
        this.sextet.setItem6(item6);
    }

    public LinkedList<Object> getTuple() {
        return this.sextet.getTuple();
    }
}
