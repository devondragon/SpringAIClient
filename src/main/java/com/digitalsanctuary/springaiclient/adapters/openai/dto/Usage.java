package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents the usage details of an OpenAI API request. This class captures the number of tokens used for the prompt, completion, and the total
 * tokens.
 */
@Data
public class Usage {

    /**
     * The number of tokens used in the prompt.
     */
    @JsonProperty("prompt_tokens")
    private int promptTokens;

    /**
     * The number of tokens used in the completion.
     */
    @JsonProperty("completion_tokens")
    private int completionTokens;

    /**
     * The total number of tokens used (prompt + completion).
     */
    @JsonProperty("total_tokens")
    private int totalTokens;
}
