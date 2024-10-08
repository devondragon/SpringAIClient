package com.digitalsanctuary.springaiclient.adapters.openai.service;

/**
 * Exception thrown when there is a communication error with the OpenAI API.
 * <p>
 * This exception is used to indicate issues such as network errors, API timeouts, or other communication-related problems.
 * </p>
 */
public class OpenAICommunicationException extends RuntimeException {

    /**
     * Constructs a new OpenAICommunicationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public OpenAICommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
