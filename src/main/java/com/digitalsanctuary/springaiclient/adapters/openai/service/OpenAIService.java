package com.digitalsanctuary.springaiclient.adapters.openai.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.digitalsanctuary.springaiclient.adapters.AbstractAIService;
import com.digitalsanctuary.springaiclient.adapters.openai.config.OpenAIConfigProperties;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIRequest;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for interacting with the OpenAI API.
 * <p>
 * This service provides methods for creating and sending requests to OpenAI's
 * chat completion API. It handles the communication details, error handling,
 * and provides convenient builder patterns for request creation.
 * </p>
 * <p>
 * The service uses Spring's RestClient to communicate with OpenAI and is configured
 * via properties defined in the application configuration. Default properties
 * can be overridden during request building.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * // Simple request with default properties
 * OpenAIResponse response = openAIService.sendRequest("Tell me a joke");
 * 
 * // Custom request with builder
 * OpenAIRequest request = openAIService.createRequestBuilder()
 *     .userMessage("Tell me a joke about birds")
 *     .model("gpt-4o")
 *     .outputTokens(2000)
 *     .systemPrompt("You are a helpful assistant specializing in humor")
 *     .build();
 * OpenAIResponse response = openAIService.sendRequest(request);
 * }
 * </pre>
 * </p>
 */
@Slf4j
@Service
public class OpenAIService extends AbstractAIService {

    private final RestClient openAiRestClient;
    private final OpenAIConfigProperties properties;

    /**
     * Constructor for OpenAIService.
     * <p>
     * The service is typically instantiated by Spring's dependency injection system.
     * It requires a properly configured RestClient for communicating with the OpenAI API
     * and a configuration properties object.
     * </p>
     *
     * @param openAiRestClient the RestClient for making HTTP requests to the OpenAI API
     * @param properties the configuration properties for OpenAI containing API keys, endpoints, etc.
     */
    public OpenAIService(@Qualifier("openAIRestClient") RestClient openAiRestClient, OpenAIConfigProperties properties) {
        this.openAiRestClient = openAiRestClient;
        this.properties = properties;
    }

    /**
     * Creates a new RequestBuilder object with default values from the configuration properties.
     * <p>
     * This is the recommended way to create OpenAI requests. The RequestBuilder provides
     * a fluent interface for building requests with appropriate defaults and validation.
     * The builder is pre-configured with the default model, output tokens, and system
     * prompt from the configuration properties, but these can be overridden.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * {@code
     * OpenAIRequest request = openAIService.createRequestBuilder()
     *     .userMessage("Explain quantum physics")
     *     .model("gpt-4o-mini")  // Override default model
     *     .build();
     * }
     * </pre>
     * </p>
     *
     * @return a new RequestBuilder object with default configuration values
     * @see RequestBuilder
     * @see OpenAIRequest
     */
    public RequestBuilder createRequestBuilder() {
        return new RequestBuilder().model(properties.getModel()).outputTokens(properties.getOutputTokens())
                .systemPrompt(properties.getSystemPrompt());
    }

    /**
     * Sends a request to OpenAI with the given text message, using the default configuration.
     * <p>
     * This is a convenience method that creates a simple request with the provided text
     * as the user message. It uses the default model, output tokens, and system prompt
     * from the configuration properties.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * {@code
     * OpenAIResponse response = openAIService.sendRequest("What is the capital of France?");
     * String answer = response.getMessage();
     * }
     * </pre>
     * </p>
     *
     * @param text the user message to send to the AI
     * @return the response from OpenAI containing the generated message and usage information
     * @throws IllegalArgumentException if the text is null or empty
     * @throws OpenAICommunicationException if there is an error communicating with the OpenAI API
     * @see #sendRequest(OpenAIRequest)
     * @see #createRequestBuilder()
     */
    public OpenAIResponse sendRequest(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("User message cannot be null or empty.");
        }
        // Use default model and output tokens from the properties
        return sendRequest(createRequestBuilder().userMessage(text).build());
    }

    /**
     * Sends a request to OpenAI with the given request object.
     * <p>
     * This method handles the communication with OpenAI API, including error handling
     * and logging. It sends the provided request object to the API endpoint specified
     * in the configuration and processes the response.
     * </p>
     * 
     * @param request the OpenAI request object containing messages, model, and other parameters
     * @return the response from OpenAI containing the generated message and usage information
     * @throws OpenAICommunicationException if there is an error communicating with the OpenAI API
     * @throws IllegalArgumentException if the request is null or invalid
     * @see OpenAIRequest
     * @see OpenAIResponse
     */
    public OpenAIResponse sendRequest(OpenAIRequest request) {
        log.debug("Sending request to OpenAI with model: {}", request.getModel());
        try {
            // Send the request and retrieve the response as OpenAIResponse
            OpenAIResponse response = openAiRestClient.post().body(request).retrieve().body(OpenAIResponse.class);

            if (response != null && !response.getChoices().isEmpty()) {
                log.debug("Received response from OpenAI: {}", response.getChoices().get(0).getMessage().getContent());
            } else {
                log.error("Received an empty or null response from OpenAI.");
            }
            return response;
        } catch (Exception e) {
            log.error("Error occurred while communicating with OpenAI: {}", e.getMessage(), e);
            throw new OpenAICommunicationException("Failed to communicate with OpenAI", e);
        }
    }

}
