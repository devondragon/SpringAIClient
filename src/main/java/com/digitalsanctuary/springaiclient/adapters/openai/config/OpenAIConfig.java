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
 * This class is responsible for creating and configuring the necessary beans for interacting 
 * with the OpenAI API. It includes beans for the OpenAI service and the REST client used
 * to communicate with the OpenAI API.
 * </p>
 * <p>
 * The configuration supports proxy settings for environments that require connecting 
 * through a corporate proxy server. Proxy settings can be enabled via configuration
 * properties.
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
     * The client is configured with the API endpoint, content type, authorization header,
     * and proxy settings if enabled.
     * </p>
     *
     * @return an instance of {@link RestClient}
     */
    @Bean(name = "openAIRestClient")
    public RestClient openAIRestClient() {
        log.info("Creating OpenAI REST client with endpoint: {}", properties.getApiEndpoint());
        
        RestClient.Builder builder = RestClient.builder()
            .baseUrl(properties.getApiEndpoint())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_PREFIX + properties.getApiKey());
        
        // Apply proxy configuration if enabled
        if (properties.getProxy().isEnabled()) {
            // Validate proxy configuration
            validateProxyConfiguration(properties.getProxy());
            
            log.info("Configuring proxy for OpenAI client: {}:{}", properties.getProxy().getHost(), properties.getProxy().getPort());
            
            // Configure proxy settings using system properties
            System.setProperty("http.proxyHost", properties.getProxy().getHost());
            System.setProperty("http.proxyPort", String.valueOf(properties.getProxy().getPort()));
            System.setProperty("https.proxyHost", properties.getProxy().getHost());
            System.setProperty("https.proxyPort", String.valueOf(properties.getProxy().getPort()));
            
            // Add proxy authentication if credentials are provided
            if (properties.getProxy().getUsername() != null && !properties.getProxy().getUsername().isEmpty()) {
                log.debug("Adding proxy authentication for user: {}", properties.getProxy().getUsername());
                
                // Set system properties for proxy authentication
                System.setProperty("http.proxyUser", properties.getProxy().getUsername());
                System.setProperty("http.proxyPassword", properties.getProxy().getPassword());
                System.setProperty("https.proxyUser", properties.getProxy().getUsername());
                System.setProperty("https.proxyPassword", properties.getProxy().getPassword());
                
                // Configure proxy authentication
                java.net.Authenticator authenticator = new java.net.Authenticator() {
                    @Override
                    protected java.net.PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType() == java.net.Authenticator.RequestorType.PROXY) {
                            return new java.net.PasswordAuthentication(
                                properties.getProxy().getUsername(),
                                properties.getProxy().getPassword().toCharArray()
                            );
                        }
                        return null;
                    }
                };
                
                // Set the authenticator
                java.net.Authenticator.setDefault(authenticator);
            }
        }
        
        return builder.build();
    }
    
    /**
     * Validates that the proxy configuration is complete and valid.
     * 
     * @param proxyConfig the proxy configuration to validate
     * @throws IllegalArgumentException if the proxy configuration is invalid
     */
    private void validateProxyConfiguration(OpenAIConfigProperties.ProxyConfig proxyConfig) {
        if (proxyConfig.getHost() == null || proxyConfig.getHost().trim().isEmpty()) {
            throw new IllegalArgumentException("Proxy host cannot be null or empty when proxy is enabled");
        }
        
        if (proxyConfig.getPort() <= 0 || proxyConfig.getPort() > 65535) {
            throw new IllegalArgumentException("Proxy port must be between 1 and 65535, got: " + proxyConfig.getPort());
        }
        
        // If username is provided, password must also be provided
        if (proxyConfig.getUsername() != null && !proxyConfig.getUsername().isEmpty()) {
            if (proxyConfig.getPassword() == null || proxyConfig.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Proxy password cannot be null or empty when username is provided");
            }
        }
        
        log.debug("Proxy configuration validated successfully");
    }
}
