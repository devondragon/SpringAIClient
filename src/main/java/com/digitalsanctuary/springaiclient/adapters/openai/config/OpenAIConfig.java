/**
 * Configuration class for OpenAI integration.
 */
package com.digitalsanctuary.springaiclient.adapters.openai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import com.digitalsanctuary.springaiclient.adapters.openai.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OpenAIConfig {

    /**
     * The OpenAI configuration properties.
     */
    private final OpenAIConfigProperties properties;

    /**
     * Creates an instance of the OpenAI service.
     *
     * @return
     */
    @Bean
    public OpenAIService openAIService() {
        return new OpenAIService(openAIRestClient(), properties);
    }

    /**
     * Creates an instance of the OpenAI REST client. The client is configured with the API endpoint, content type, and authorization header.
     *
     * @return
     */
    @Bean(name = "openAIRestClient")
    public RestClient openAIRestClient() {
        log.info("Creating OpenAI REST client with endpoint: {}", properties.getApiEndpoint());
        return RestClient.builder().baseUrl(properties.getApiEndpoint()).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey()).build();
    }
}
