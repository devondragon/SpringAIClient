package com.digitalsanctuary.springaiclient.adapters.openai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import com.digitalsanctuary.springaiclient.adapters.openai.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for setting up OpenAI-related beans.
 * <p>
 * This class is responsible for creating and configuring the necessary beans for interacting with the OpenAI API. It includes beans for the OpenAI
 * service and the REST client used to communicate with the OpenAI API.
 * </p>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class OpenAIConfig {

    /**
     * The Bearer token prefix for the authorization header.
     */
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    /**
     * The OpenAI configuration properties.
     */
    private final OpenAIConfigProperties properties;

    /**
     * Creates an instance of the OpenAI service.
     * <p>
     * This bean provides the main service for interacting with the OpenAI API.
     * </p>
     *
     * @return an instance of {@link OpenAIService}
     */
    @Bean
    public OpenAIService openAIService() {
        return new OpenAIService(openAIRestClient(), properties);
    }

    /**
     * Creates an instance of the OpenAI REST client.
     * <p>
     * The client is configured with the API endpoint, content type, and authorization header.
     * </p>
     *
     * @return an instance of {@link RestClient}
     */
    @Bean(name = "openAIRestClient")
    public RestClient openAIRestClient() {
        log.info("Creating OpenAI REST client with endpoint: {}", properties.getApiEndpoint());
        return RestClient.builder().baseUrl(properties.getApiEndpoint()).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_PREFIX + properties.getApiKey()).build();
    }
}
