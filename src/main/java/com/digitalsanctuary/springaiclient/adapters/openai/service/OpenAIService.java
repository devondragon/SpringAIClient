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
 * This class represents a service for interacting with the OpenAI API. It extends the AbstractAIService class and provides methods for sending
 * requests to the API.
 */
@Slf4j
@Service
public class OpenAIService extends AbstractAIService {

    private final RestClient openAiRestClient;
    private final OpenAIConfigProperties properties;

    /**
     * Constructor for OpenAIService.
     *
     * @param openAiRestClient the RestClient for OpenAI API
     * @param properties the configuration properties for OpenAI
     */
    public OpenAIService(@Qualifier("openAIRestClient") RestClient openAiRestClient, OpenAIConfigProperties properties) {
        this.openAiRestClient = openAiRestClient;
        this.properties = properties;
    }

    /**
     * Creates a new RequestBuilder object with default values from the properties.
     *
     * @return a new RequestBuilder object
     */
    public RequestBuilder createRequestBuilder() {
        return new RequestBuilder().model(properties.getModel()).outputTokens(properties.getOutputTokens())
                .systemPrompt(properties.getSystemPrompt());
    }

    /**
     * Sends a request to OpenAI with the given text message, using the default model and output tokens.
     *
     * @param text the user message to send
     * @return the response from OpenAI
     * @throws IllegalArgumentException if the text is null or empty
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
     *
     * @param request the OpenAI request object
     * @return the response from OpenAI
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
