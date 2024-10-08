package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a request to the OpenAI API.
 * <p>
 * This class captures the details of a request including the list of messages, the maximum number of tokens, and the model to be used for generating
 * responses.
 * </p>
 */
@Data
public class OpenAIRequest {

    /**
     * The list of messages to be sent to the OpenAI API.
     */
    @JsonProperty("messages")
    private List<Message> messages;

    /**
     * The maximum number of tokens to be generated in the response.
     */
    @JsonProperty("max_tokens")
    private int maxTokens;

    /**
     * The model to be used for generating responses.
     */
    @JsonProperty("model")
    private String model;
}
