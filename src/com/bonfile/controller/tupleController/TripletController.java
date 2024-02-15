package com.bonfile.controller.tupleController;

import com.bonfile.model.tuple.Triplet;
import java.util.LinkedList;

public class TripletController<T1, T2, T3> {
    private Triplet<T1, T2, T3> triplet;

    public TripletController() {
        this.triplet = new Triplet<>();
    }

    public TripletController(T1 item1, T2 item2, T3 item3) {
        this.triplet = new Triplet<>(item1, item2, item3);
    }

    public TripletController(Triplet<T1, T2, T3> triplet) {
        this.triplet = triplet;
    }

    public Triplet<T1, T2, T3> getTriplet() {
        return triplet;
    }

    public void setTriplet(Triplet<T1, T2, T3> triplet) {
        this.triplet = triplet;
    }

    public T1 getItem1() {
        return this.triplet.getItem1();
    }

    public void setItem1(T1 item1) {
        this.triplet.setItem1(item1);
    }

    public T2 getItem2() {
        return this.triplet.getItem2();
    }

    public void setItem2(T2 item2) {
        this.triplet.setItem2(item2);
    }

    public T3 getItem3() {
        return this.triplet.getItem3();
    }

    public void setItem3(T3 item3) {
        this.triplet.setItem3(item3);
    }

    public LinkedList<Object> getTuple() {
        return this.triplet.getTuple();
    }
}
