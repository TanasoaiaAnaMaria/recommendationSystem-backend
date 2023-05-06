package com.usv.recommendationSystem.exceptii;

public class CrudOperationException extends RuntimeException{
    public CrudOperationException(String mesaj){
        super(mesaj);
    }
}
