package com.digitalsanctuary.springaiclient;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.digitalsanctuary.springaiclient.adapters.openai.config.OpenAIConfig;
import com.digitalsanctuary.springaiclient.adapters.openai.config.OpenAIConfigProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * A configuration class for the Spring AI Client.
 * <p>
 * This class is responsible for configuring the necessary components and dependencies required by the client. It imports the
 * {@link com.digitalsanctuary.springaiclient.adapters.openai.config.OpenAIConfig} class.
 * </p>
 * <p>
 * The {@link #onStartup()} method is annotated with {@link jakarta.annotation.PostConstruct} and is executed after the bean initialization. It logs a
 * message indicating that the DigitalSanctuary Spring AI Client has been loaded.
 * </p>
 */
@Slf4j
@Configuration
@AutoConfiguration
@Import({OpenAIConfigProperties.class, OpenAIConfig.class})
public class AIClientConfiguration {

    /**
     * Method executed after the bean initialization.
     * <p>
     * This method logs a message indicating that the DigitalSanctuary Spring AI Client has been loaded.
     * </p>
     */
    @PostConstruct
    public void onStartup() {
        log.info("DigitalSanctuary Spring AI Client loaded");
    }
}
