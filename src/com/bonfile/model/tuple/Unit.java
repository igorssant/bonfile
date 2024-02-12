package com.bonfile.model.tuple;

import com.bonfile.model.tuple.prototype.Tuple;

import java.util.LinkedList;

public class Unit<T> extends Tuple {
    private T item;

    public Unit() {}

    public Unit(T item) {
        this.item = item;
    }

    public T getItem() {
        return this.item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public LinkedList<T> getTuple() {
        LinkedList<T> linkedList = new LinkedList<>();
        linkedList.add(this.item);
        return linkedList;
    }
}
