package com.digitalsanctuary.springaiclient.adapters.openai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * Configuration properties for OpenAI integration.
 * <p>
 * This class holds the properties required for integrating with the OpenAI API. The properties are prefixed with "ds.ai.openai" in the application
 * configuration.
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "ds.ai.openai")
public class OpenAIConfigProperties {

    /**
     * The API key for authenticating with the OpenAI API.
     */
    private String apiKey;

    /**
     * The endpoint URL for the OpenAI API.
     */
    private String apiEndpoint;

    /**
     * The model to be used for generating responses.
     */
    private String model;

    /**
     * The maximum number of tokens to be generated in the response.
     */
    private int outputTokens;

    /**
     * The system prompt to be used for generating responses.
     */
    private String systemPrompt;
}
