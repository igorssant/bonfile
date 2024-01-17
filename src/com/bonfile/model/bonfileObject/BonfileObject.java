package com.bonfile.model.bonfileObject;

import java.util.HashMap;
import java.util.LinkedList;
import static com.bonfile.util.Capitalize.capitalize;

public class BonfileObject {
    private String objectName;
    private String objectClass;
    private HashMap<String, Object> hashMap;
    public BonfileObject(){}

    public BonfileObject(String objectName) {
        this.objectName = objectName;
        this.hashMap = new HashMap<>();
    }

    public BonfileObject(String objectName, String objectClass) {
        this.objectName = objectName;
        this.objectClass = objectClass;
        this.hashMap = new HashMap<>();
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        if(!objectName.isEmpty()) {
            throw new RuntimeException(
                "The object " + this.objectName + " already has a name.\n"
                + "If you really want to rename this class please use the ***renameObject(String)*** method."
            );
        } else {
            this.objectName = objectName;
        }
    }

    public void renameObject(String newName) {
        this.objectName = newName;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        if(!this.objectClass.isEmpty()) {
            throw new RuntimeException(
                "This object " + this.objectName + " already has a Class.\n"
                + "Class: " + this.objectClass + " .\n"
                + "If you really want to change its Class please use the ***resetClass(String)*** method."
            );
        } else {
            this.objectClass = capitalize(objectClass);
        }
    }

    public void resetClasse(String newClass) {
        this.objectClass = newClass;
    }

    public HashMap<String, Object> getBonfileObject() {
        return hashMap;
    }

    public void setBonfileObject(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
    }

    public void put(String varName, Integer value) {
        this.hashMap.put(varName, value);
    }

    public void put(String varName, Integer[] valueArray) {
        this.hashMap.put(varName, valueArray);
    }

    public void put(String varName, Float value) {
        this.hashMap.put(varName, value);
    }

    public void put(String varName, Float[] valueArray) {
        this.hashMap.put(varName, valueArray);
    }

    public void put(String varName, Double value) {
        this.hashMap.put(varName, value);
    }

    public void put(String varName, Double[] valueArray) {
        this.hashMap.put(varName, valueArray);
    }

    public void put(String varName, Boolean value) {
        this.hashMap.put(varName, value);
    }

    public void put(String varName, Boolean[] valueArray) {
        this.hashMap.put(varName, valueArray);
    }

    public void put(String varName, Character value) {
        this.hashMap.put(varName, value);
    }

    public void put(String varName, Character[] valueArray) {
        this.hashMap.put(varName, valueArray);
    }

    public void put(String varName, String value) {
        this.hashMap.put(varName, value);
    }

    public void put(String varName, String[] valueArray) {
        this.hashMap.put(varName, valueArray);
    }

    public void put(String varName, HashMap<String, String> dict) {
        this.hashMap.put(varName, dict);
    }

    public void put(String varName, HashMap<String, String>[] dictArray) {
        this.hashMap.put(varName, dictArray);
    }

    public void put(String varName, BonfileObject bonfileObject) {
        this.hashMap.put(varName, bonfileObject);
    }

    public void put(String varName, BonfileObject[] bonfileObjectArray) {
        this.hashMap.put(varName, bonfileObjectArray);
    }

    public void put(String varName, LinkedList<BonfileObject> bonfileObjectLinkedList) {
        this.hashMap.put(varName, bonfileObjectLinkedList);
    }

    public Integer getInt(String varName) {
        return (Integer) this.hashMap.get(varName);
    }

    public Integer[] getIntArray(String varName) {
        return (Integer[]) this.hashMap.get(varName);
    }

    public Float getFloat(String varName) {
        return (Float) this.hashMap.get(varName);
    }

    public Float[] getFloatArray(String varName) {
        return (Float[]) this.hashMap.get(varName);
    }

    public Double getDouble(String varName) {
        return (Double) this.hashMap.get(varName);
    }

    public Double[] getDoubleArray(String varName) {
        return (Double[]) this.hashMap.get(varName);
    }

    public Boolean getBoolean(String varName) {
        return (Boolean) this.hashMap.get(varName);
    }

    public Boolean[] getBooleanArray(String varName) {
        return (Boolean[]) this.hashMap.get(varName);
    }

    public Character getCharacter(String varName) {
        return (Character) this.hashMap.get(varName);
    }

    public Character[] getCharacterArray(String varName) {
        return (Character[]) this.hashMap.get(varName);
    }

    public String getString(String varName) {
        return (String) this.hashMap.get(varName);
    }

    public String[] getStringArray(String varName) {
        return (String[]) this.hashMap.get(varName);
    }

    public HashMap<String, String> getDict(String varName) {
        return (HashMap<String, String>) this.hashMap.get(varName);
    }

    public HashMap<String, String>[] getDictArray(String varName) {
        return (HashMap<String, String>[]) this.hashMap.get(varName);
    }

    public BonfileObject getObject(String varName) {
        return (BonfileObject) this.hashMap.get(varName);
    }

    public BonfileObject[] getClassArray(String varName) {
        return (BonfileObject[]) this.hashMap.get(varName);
    }

    public LinkedList<BonfileObject> getClassList(String varName) {
        return (LinkedList<BonfileObject>) this.hashMap.get(varName);
    }

    public void clear() {
        this.hashMap.clear();
    }

    public HashMap<String, Object> copy() {
        return (HashMap<String, Object>) this.hashMap.clone();
    }
}
