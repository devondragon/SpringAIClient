package com.digitalsanctuary.springaiclient.adapters.openai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * Configuration properties for OpenAI integration.
 * <p>
 * This class encapsulates all configuration properties required for interacting with 
 * the OpenAI API. The properties are automatically loaded from application configuration
 * files with the prefix "ds.ai.openai".
 * </p>
 * <p>
 * Configuration can be provided in application.yml, application.properties, or environment
 * variables. For example, in YAML:
 * </p>
 * <pre>
 * ds:
 *   ai:
 *     openai:
 *       api-key: ${OPENAI_API_KEY}
 *       api-endpoint: https://api.openai.com/v1/chat/completions
 *       model: gpt-4o
 *       output-tokens: 4096
 *       system-prompt: "You are a helpful assistant."
 *       proxy:
 *         enabled: true
 *         host: proxy.example.com
 *         port: 8080
 *         username: proxyuser
 *         password: proxypass
 * </pre>
 * <p>
 * The following properties are supported:
 * <ul>
 *   <li>api-key: Your OpenAI API key (required)</li>
 *   <li>api-endpoint: OpenAI API endpoint URL (defaults to chat completions endpoint)</li>
 *   <li>model: Default model to use (defaults to gpt-4o or as specified)</li>
 *   <li>output-tokens: Maximum tokens in responses (defaults to 4096)</li>
 *   <li>system-prompt: Default system prompt (defaults to "You are a helpful assistant.")</li>
 *   <li>proxy.enabled: Whether to use a proxy for OpenAI API requests (defaults to false)</li>
 *   <li>proxy.host: Proxy server hostname or IP address</li>
 *   <li>proxy.port: Proxy server port</li>
 *   <li>proxy.username: Username for proxy authentication (optional)</li>
 *   <li>proxy.password: Password for proxy authentication (optional)</li>
 * </ul>
 * </p>
 * <p>
 * For security, it's recommended to use environment variables for sensitive properties
 * like api-key and proxy.password rather than hardcoding them in configuration files.
 * </p>
 */
@Data
@Component
@PropertySource("classpath:config/dsspringaiconfig.properties")
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
    
    /**
     * Proxy configuration for OpenAI API requests.
     */
    private ProxyConfig proxy = new ProxyConfig();
    
    /**
     * Inner class for proxy configuration properties.
     */
    @Data
    public static class ProxyConfig {
        /**
         * Whether to use a proxy for OpenAI API requests.
         */
        private boolean enabled = false;
        
        /**
         * Proxy server hostname or IP address.
         */
        private String host;
        
        /**
         * Proxy server port.
         */
        private int port;
        
        /**
         * Username for proxy authentication (optional).
         */
        private String username;
        
        /**
         * Password for proxy authentication (optional).
         */
        private String password;
    }
}
