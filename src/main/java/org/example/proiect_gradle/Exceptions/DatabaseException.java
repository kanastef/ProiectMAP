package org.example.proiect_gradle.Exceptions;

public class DatabaseException extends CustomException{
    public DatabaseException(String message){
        super(message,"DATABASE_ERROR");
    }

}