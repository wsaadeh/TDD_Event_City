package com.saadeh.TDD_Event_City.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;


public class ValidationError extends StandardException {

    private List<FieldMessage> errors = new ArrayList<>();

    public void addError(String fieldName, String message){
        errors.add(new FieldMessage(fieldName, message));
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }
}
