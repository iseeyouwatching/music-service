package ru.hits.musicservice.exception;

public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException(String message) {
        super(message);
    }

}
