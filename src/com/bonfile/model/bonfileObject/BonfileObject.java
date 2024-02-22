package com.bonfile.model.bonfileObject;

import java.util.HashMap;
import java.util.LinkedList;
import static com.bonfile.util.fileHelper.FileHelper.capitalize;

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
        if(this.objectName == null) {
            this.objectName = objectName;
        } else {
            throw new RuntimeException(
                "The object " + this.objectName + " already has a name.\n"
                + "If you really want to rename this class please use the ***renameObject(String)*** method."
            );
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

    public void resetClass(String newClass) {
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

    public void putIntList(String varName, LinkedList<Integer> valueList) {
        this.hashMap.put(varName, valueList);
    }

    public void put(String varName, Float value) {
        this.hashMap.put(varName, value);
    }

    public void putFloatList(String varName, LinkedList<Float> valueList) {
        this.hashMap.put(varName, valueList);
    }

    public void put(String varName, Double value) {
        this.hashMap.put(varName, value);
    }

    public void putDoubleList(String varName, LinkedList<Double> valueList) {
        this.hashMap.put(varName, valueList);
    }

    public void put(String varName, Boolean value) {
        this.hashMap.put(varName, value);
    }

    public void putBooleanList(String varName, LinkedList<Boolean> valueList) {
        this.hashMap.put(varName, valueList);
    }

    public void put(String varName, Character value) {
        this.hashMap.put(varName, value);
    }

    public void putCharList(String varName, LinkedList<Character> valueList) {
        this.hashMap.put(varName, valueList);
    }

    public void put(String varName, String value) {
        this.hashMap.put(varName, value);
    }

    public void putStringList(String varName, LinkedList<String> valueList) {
        this.hashMap.put(varName, valueList);
    }

    public void put(String varName, HashMap<String, String> dict) {
        this.hashMap.put(varName, dict);
    }

    public void putDictList(String varName, LinkedList<HashMap<String, String>> dictArray) {
        this.hashMap.put(varName, dictArray);
    }

    public void put(String varName, BonfileObject bonfileObject) {
        this.hashMap.put(varName, bonfileObject);
    }

    public void putBonfileObjectList(String varName, LinkedList<BonfileObject> bonfileObjectLinkedList) {
        this.hashMap.put(varName, bonfileObjectLinkedList);
    }

    public Integer getInt(String varName) {
        return (Integer) this.hashMap.get(varName);
    }

    public LinkedList<Integer> getIntList(String varName) {
        return (LinkedList<Integer>) this.hashMap.get(varName);
    }

    public Float getFloat(String varName) {
        return (Float) this.hashMap.get(varName);
    }

    public LinkedList<Float> getFloatList(String varName) {
        return (LinkedList<Float>) this.hashMap.get(varName);
    }

    public Double getDouble(String varName) {
        return (Double) this.hashMap.get(varName);
    }

    public LinkedList<Double> getDoubleList(String varName) {
        return (LinkedList<Double>) this.hashMap.get(varName);
    }

    public Boolean getBoolean(String varName) {
        return (Boolean) this.hashMap.get(varName);
    }

    public LinkedList<Boolean> getBooleanList(String varName) {
        return (LinkedList<Boolean>) this.hashMap.get(varName);
    }

    public Character getCharacter(String varName) {
        return (Character) this.hashMap.get(varName);
    }

    public LinkedList<Character> getCharacterList(String varName) {
        return (LinkedList<Character>) this.hashMap.get(varName);
    }

    public String getString(String varName) {
        return (String) this.hashMap.get(varName);
    }

    public LinkedList<String> getStringList(String varName) {
        return (LinkedList<String>) this.hashMap.get(varName);
    }

    public HashMap<String, String> getDict(String varName) {
        return (HashMap<String, String>) this.hashMap.get(varName);
    }

    public LinkedList<HashMap<String, String>> getDictList(String varName) {
        return (LinkedList<HashMap<String, String>>) this.hashMap.get(varName);
    }

    public BonfileObject getObject(String varName) {
        return (BonfileObject) this.hashMap.get(varName);
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
