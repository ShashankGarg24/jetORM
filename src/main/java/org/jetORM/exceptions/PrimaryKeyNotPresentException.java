package org.jetORM.exceptions;

public class PrimaryKeyNotPresentException extends Exception{
    public PrimaryKeyNotPresentException(String message) {
        super(message);
    }
}
