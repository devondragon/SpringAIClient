package com.digitalsanctuary.springaiclient.adapters.openai.service;

public class OpenAICommunicationException extends RuntimeException {
    public OpenAICommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
