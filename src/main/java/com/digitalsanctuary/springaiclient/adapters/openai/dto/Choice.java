package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a choice returned by the OpenAI API.
 * <p>
 * This class captures various details of a choice including the index, message, log probabilities, and finish reason.
 * </p>
 */
@Data
public class Choice {

    /**
     * The index of the choice in the list of choices.
     */
    @JsonProperty("index")
    private int index;

    /**
     * The message associated with the choice.
     */
    @JsonProperty("message")
    private Message message;

    /**
     * The log probabilities of the tokens in the choice.
     */
    @JsonProperty("logprobs")
    private Object logprobs;

    /**
     * The reason why the choice was finished.
     */
    @JsonProperty("finish_reason")
    private String finishReason;
}
