package com.igb.web.online_library.util;

import java.util.HashMap;
import java.util.Map;


import org.springframework.validation.Errors;



public class FieldsCustomValidator {


    public static Map<String, String> getValidationErrorsFrom(Errors errors){
         Map<String, String> validationErrors = new HashMap<>();        
        errors.getFieldErrors().forEach(error -> 
            validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        return validationErrors;
    }



}
