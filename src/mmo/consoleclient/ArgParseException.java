package mmo.consoleclient;

public class ArgParseException extends RuntimeException {
    public ArgParseException(String errorMessage) {
        super(errorMessage);
    }

    public ArgParseException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
