package ru.vels.taskplanner.exception;

public class DuplicateUsernameException extends  Exception{
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
