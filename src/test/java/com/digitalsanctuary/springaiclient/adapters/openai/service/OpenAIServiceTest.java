package com.digitalsanctuary.springaiclient.adapters.openai.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.digitalsanctuary.springaiclient.TestApplication;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIRequest;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test") // Use the test profile to load test-specific configurations
class OpenAIServiceTest {

    @Autowired
    private OpenAIService openAIService;

    @Test
    void testSendSimpleRequest() {
        log.info(null != openAIService ? "OpenAI service is not null" : "OpenAI service is null");
        // Create a test request
        OpenAIRequest request = openAIService.createRequestBuilder().userMessage("Tell me a joke.").build();

        // Send the request and get the response
        OpenAIResponse response = openAIService.sendRequest(request);

        log.debug(null != response ? response.toString() : "Response is null");
        // Assert that the response is not null
        assertNotNull(response);
        assertNotNull(response.getMessage());
    }

    @Test
    void testSendCustomRequest() {
        log.info(null != openAIService ? "OpenAI service is not null" : "OpenAI service is null");
        // Create a test request
        OpenAIRequest request = openAIService.createRequestBuilder().userMessage("Tell me a joke.").model("chatgpt-4o-latest").outputTokens(8000)
                .systemPrompt("You only tell jokes about birds").build();

        // Send the request and get the response
        OpenAIResponse response = openAIService.sendRequest(request);

        log.debug(null != response ? response.toString() : "Response is null");
        // Assert that the response is not null
        assertNotNull(response);
        assertNotNull(response.getChoices());
        assertNotNull(response.getChoices().get(0).getMessage().getContent());
    }

    @Test
    void testSendTextRequest() {
        log.info(null != openAIService ? "OpenAI service is not null" : "OpenAI service is null");

        // Send the request and get the response
        OpenAIResponse response = openAIService.sendRequest("Tell me a joke about mountains");

        log.debug(null != response ? response.toString() : "Response is null");
        // Assert that the response is not null
        assertNotNull(response);
        assertNotNull(response.getChoices());
        assertNotNull(response.getChoices().get(0).getMessage().getContent());
    }
}
