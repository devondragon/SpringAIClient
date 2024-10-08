package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a message in the OpenAI API response.
 * <p>
 * This class captures the role and content of a message returned by the OpenAI API.
 * </p>
 */
@Data
public class Message {

    /**
     * The role of the message sender (e.g., "system", "user", "assistant").
     */
    @JsonProperty("role")
    private String role;

    /**
     * The content of the message.
     */
    @JsonProperty("content")
    private String content;
}
