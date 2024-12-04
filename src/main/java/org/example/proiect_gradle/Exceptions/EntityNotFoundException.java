package org.example.proiect_gradle.Exceptions;

public class EntityNotFoundException extends CustomException{
    public EntityNotFoundException(String message){
        super(message,"ERROR_ENTITY_NOT_FOUND");
    }
}
