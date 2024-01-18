package com.bonfile.controller.bonfileObjectController;

import com.bonfile.model.bonfileObject.BonfileObject;

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
}
