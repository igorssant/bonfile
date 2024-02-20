package com.bonfile.controller.tupleController;

import com.bonfile.model.tuple.Unit;
import java.util.LinkedList;

public class UnitController<T> {
    private Unit<T> unit;

    public UnitController() {
        this.unit = new Unit<>();
    }
    public UnitController(T item) {
        this.unit = new Unit<>(item);
    }

    public UnitController(Unit<T> unit) {
        this.unit = unit;
    }

    public Unit<T> getUnit() {
        return unit;
    }

    public void setUnit(Unit<T> unit) {
        this.unit = unit;
    }

    public T getItem() {
        return this.unit.getItem();
    }

    public void setItem(T item) {
        this.unit.setItem(item);
    }

    public LinkedList<T> getTuple() {
        return this.unit.getTuple();
    }

    public void elementsToObject() {
        this.unit = new Unit<>(this.unit.getItem());
    }

    public Unit<Object> getElementsAsObject() {
        return new Unit<>(this.unit.getItem());
    }
}
