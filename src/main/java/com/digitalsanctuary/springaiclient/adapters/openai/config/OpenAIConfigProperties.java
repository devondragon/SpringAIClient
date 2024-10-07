/**
 * Configuration properties for OpenAI integration. This class holds the properties required for integrating with OpenAI API.
 */
package com.digitalsanctuary.springaiclient.adapters.openai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "ds.openai")
public class OpenAIConfigProperties {
    private String apiKey;
    private String apiEndpoint;
    private String model;
    private int outputTokens;
    private String systemPrompt;
}
