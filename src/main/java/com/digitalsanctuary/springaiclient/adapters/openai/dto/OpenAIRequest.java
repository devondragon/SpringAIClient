package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a request to the OpenAI chat completions API.
 * <p>
 * This class encapsulates all the parameters needed for making a request to the OpenAI 
 * chat completions API. It includes the conversation context (as a list of messages), 
 * model specifications, and generation parameters.
 * </p>
 * <p>
 * Instances of this class should be created using the {@link com.digitalsanctuary.springaiclient.adapters.openai.service.RequestBuilder}
 * provided by the OpenAIService. This ensures proper configuration and validation.
 * </p>
 * <p>
 * Example creation via RequestBuilder:
 * <pre>
 * {@code
 * OpenAIRequest request = openAIService.createRequestBuilder()
 *     .userMessage("What is the capital of France?")
 *     .model("gpt-4o")
 *     .outputTokens(1000)
 *     .systemPrompt("You are a helpful geography assistant.")
 *     .build();
 * }
 * </pre>
 * <p>
 * The request is serialized to JSON when sent to the OpenAI API. The JSON structure
 * follows OpenAI's API specification for chat completions.
 * </p>
 * 
 * @see com.digitalsanctuary.springaiclient.adapters.openai.service.RequestBuilder
 * @see com.digitalsanctuary.springaiclient.adapters.openai.service.OpenAIService
 * @see com.digitalsanctuary.springaiclient.adapters.openai.dto.Message
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
