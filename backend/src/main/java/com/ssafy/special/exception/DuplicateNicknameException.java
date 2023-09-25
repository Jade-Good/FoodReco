package com.ssafy.special.exception;

public class DuplicateNicknameException extends RuntimeException{

    public DuplicateNicknameException(String message) {
        super(message);
    }
}
