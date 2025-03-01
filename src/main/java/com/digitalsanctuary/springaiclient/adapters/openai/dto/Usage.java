package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents the token usage details returned by the OpenAI API.
 * <p>
 * This class captures detailed information about the number of tokens used in an OpenAI API request 
 * and response. Token usage is important for monitoring API consumption, estimating costs, and 
 * ensuring that requests stay within model context limits.
 * </p>
 * <p>
 * Different OpenAI models have different context window sizes (maximum total tokens), pricing
 * structures, and computational requirements. Understanding token usage helps optimize application
 * performance and cost efficiency.
 * </p>
 * <p>
 * Token counts are broken down into:
 * <ul>
 *   <li>Prompt tokens - Tokens used in the input/prompt sent to the API</li>
 *   <li>Completion tokens - Tokens generated in the API response</li>
 *   <li>Total tokens - Sum of prompt and completion tokens</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * OpenAIResponse response = openAIService.sendRequest("What is the capital of France?");
 * Usage usage = response.getUsage();
 * System.out.println("Total tokens used: " + usage.getTotalTokens());
 * }
 * </pre>
 * </p>
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
