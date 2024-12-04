package org.example.proiect_gradle.Exceptions;

public class ValidationException extends CustomException{
    public ValidationException(String message){
        super(message,"VALIDATION_ERROR");
    }
}