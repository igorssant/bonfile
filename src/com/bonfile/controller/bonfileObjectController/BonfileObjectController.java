package com.bonfile.controller.bonfileObjectController;

import com.bonfile.model.bonfileObject.BonfileObject;

import java.util.HashMap;
import java.util.LinkedList;

public class BonfileObjectController {
    private BonfileObject bonfileObject;

    public BonfileObjectController() {
        this.bonfileObject = new BonfileObject();
    }

    public BonfileObjectController(String objectName) {
        this.bonfileObject = new BonfileObject(objectName);
    }

    public BonfileObjectController(String objectName, String className) {
        this.bonfileObject = new BonfileObject(objectName, className);
    }

    public BonfileObjectController(BonfileObject bonfileObject) {
        this.bonfileObject = bonfileObject;
    }

    public void resetBonfileObject(BonfileObject bonfileObject) {
        this.bonfileObject = bonfileObject;
    }

    public BonfileObject getBonfileObject() {
        return bonfileObject;
    }

    public String getObjectName() {
        return this.bonfileObject.getObjectName();
    }

    public void setObjectName(String objectName) {
        this.bonfileObject.setObjectName(objectName);
    }

    public void renameObject(String newName) {
        this.bonfileObject.renameObject(newName);
    }

    public String getObjectClass() {
        return this.bonfileObject.getObjectClass();
    }

    public void setObjectClass(String objectClass) {
        this.bonfileObject.setObjectClass(objectClass);
    }

    public void resetClass(String newClass) {
        this.bonfileObject.resetClass(newClass);
    }

    public HashMap<String, Object> getBonfileObjectBody() {
        return this.bonfileObject.getBonfileObject();
    }

    public void setBonfileObject(HashMap<String, Object> bonfileObject) {
        this.bonfileObject.setBonfileObject(bonfileObject);
    }

    public void put(String varName, Integer value) {
        this.bonfileObject.put(varName, value);
    }

    public void put(String varName, Integer[] valueArray) {
        this.bonfileObject.put(varName, valueArray);
    }

    public void put(String varName, Float value) {
        this.bonfileObject.put(varName, value);
    }

    public void put(String varName, Float[] valueArray) {
        this.bonfileObject.put(varName, valueArray);
    }

    public void put(String varName, Double value) {
        this.bonfileObject.put(varName, value);
    }

    public void put(String varName, Double[] valueArray) {
        this.bonfileObject.put(varName, valueArray);
    }

    public void put(String varName, Boolean value) {
        this.bonfileObject.put(varName, value);
    }

    public void put(String varName, Boolean[] valueArray) {
        this.bonfileObject.put(varName, valueArray);
    }

    public void put(String varName, Character value) {
        this.bonfileObject.put(varName, value);
    }

    public void put(String varName, Character[] valueArray) {
        this.bonfileObject.put(varName, valueArray);
    }

    public void put(String varName, String value) {
        this.bonfileObject.put(varName, value);
    }

    public void put(String varName, String[] valueArray) {
        this.bonfileObject.put(varName, valueArray);
    }

    public void put(String varName, HashMap<String, String> dict) {
        this.bonfileObject.put(varName, dict);
    }

    public void put(String varName, HashMap<String, String>[] dictArray) {
        this.bonfileObject.put(varName, dictArray);
    }

    public void put(String varName, BonfileObject bonfileObject) {
        this.bonfileObject.put(varName, bonfileObject);
    }

    public void put(String varName, BonfileObject[] bonfileObjectArray) {
        this.bonfileObject.put(varName, bonfileObjectArray);
    }

    public void put(String varName, LinkedList<BonfileObject> bonfileObjectLinkedList) {
        this.bonfileObject.put(varName, bonfileObjectLinkedList);
    }

    public Integer getInt(String varName) {
        return (Integer) this.bonfileObject.getInt(varName);
    }

    public Integer[] getIntArray(String varName) {
        return (Integer[]) this.bonfileObject.getIntArray(varName);
    }

    public Float getFloat(String varName) {
        return (Float) this.bonfileObject.getFloat(varName);
    }

    public Float[] getFloatArray(String varName) {
        return (Float[]) this.bonfileObject.getFloatArray(varName);
    }

    public Double getDouble(String varName) {
        return (Double) this.bonfileObject.getDouble(varName);
    }

    public Double[] getDoubleArray(String varName) {
        return (Double[]) this.bonfileObject.getDoubleArray(varName);
    }

    public Boolean getBoolean(String varName) {
        return (Boolean) this.bonfileObject.getBoolean(varName);
    }

    public Boolean[] getBooleanArray(String varName) {
        return (Boolean[]) this.bonfileObject.getBooleanArray(varName);
    }

    public Character getCharacter(String varName) {
        return (Character) this.bonfileObject.getCharacter(varName);
    }

    public Character[] getCharacterArray(String varName) {
        return (Character[]) this.bonfileObject.getCharacterArray(varName);
    }

    public String getString(String varName) {
        return (String) this.bonfileObject.getString(varName);
    }

    public String[] getStringArray(String varName) {
        return (String[]) this.bonfileObject.getStringArray(varName);
    }

    public HashMap<String, String> getDict(String varName) {
        return (HashMap<String, String>) this.bonfileObject.getDict(varName);
    }

    public HashMap<String, String>[] getDictArray(String varName) {
        return (HashMap<String, String>[]) this.bonfileObject.getDictArray(varName);
    }

    public BonfileObject getObject(String varName) {
        return (BonfileObject) this.bonfileObject.getObject(varName);
    }

    public BonfileObject[] getClassArray(String varName) {
        return (BonfileObject[]) this.bonfileObject.getClassArray(varName);
    }

    public LinkedList<BonfileObject> getClassList(String varName) {
        return (LinkedList<BonfileObject>) this.bonfileObject.getClassList(varName);
    }

    public void clear() {
        this.bonfileObject.clear();
    }

    public HashMap<String, Object> copy() {
        return (HashMap<String, Object>) this.bonfileObject.copy();
    }
}
