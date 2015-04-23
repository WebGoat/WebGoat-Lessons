package org.owasp.webgoat.converter;

public class ConverterException extends RuntimeException {

    public ConverterException(String msg, Exception e) {
        super(msg, e);
    }
}
