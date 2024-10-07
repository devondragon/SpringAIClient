package com.digitalsanctuary.springaiclient.adapters.openai.service;

import com.digitalsanctuary.springaiclient.adapters.openai.dto.Message;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIRequest;

import java.util.Arrays;

/**
 * The {@code RequestBuilder} class is a builder pattern implementation for constructing an {@code OpenAIRequest}. This class allows users to set
 * various optional fields, such as the model, output tokens, system prompt, and user message. After setting the desired properties, the {@code build}
 * method can be called to create an {@code OpenAIRequest} object.
 */
public class RequestBuilder {

    private String model;
    private int outputTokens;
    private String systemPrompt;
    private String userMessage;

    /**
     * Sets the model to be used in the OpenAI request.
     *
     * @param model the model to use (e.g., "gpt-4o", "gpt-4o-mini" - https://platform.openai.com/docs/models/continuous-model-upgrades).
     * @return the current instance of {@code RequestBuilder} for method chaining.
     */
    public RequestBuilder model(String model) {
        this.model = model;
        return this;
    }

    /**
     * Sets the maximum number of tokens for the OpenAI response.
     *
     * @param outputTokens the maximum number of tokens to generate (e.g., 1000).
     * @return the current instance of {@code RequestBuilder} for method chaining.
     */
    public RequestBuilder outputTokens(int outputTokens) {
        this.outputTokens = outputTokens;
        return this;
    }

    /**
     * Sets the system prompt for the OpenAI request. This is typically used to configure the behavior of the assistant.
     *
     * @param systemPrompt the prompt that sets the context for the conversation (e.g., "You are a helpful assistant.").
     * @return the current instance of {@code RequestBuilder} for method chaining.
     */
    public RequestBuilder systemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
        return this;
    }

    /**
     * Sets the user message for the OpenAI request. This is the message that the user sends to the model.
     *
     * @param userMessage the message from the user to be sent to OpenAI (e.g., "Tell me a joke.").
     * @return the current instance of {@code RequestBuilder} for method chaining.
     */
    public RequestBuilder userMessage(String userMessage) {
        this.userMessage = userMessage;
        return this;
    }

    /**
     * Builds the {@code OpenAIRequest} object with the provided values. The {@code OpenAIRequest} consists of a system message and a user message,
     * along with the specified model and token limit.
     *
     * @return a fully constructed {@code OpenAIRequest} object.
     */
    public OpenAIRequest build() {
        // Create the system message
        Message systemMessage = new Message();
        systemMessage.setRole("system");
        systemMessage.setContent(systemPrompt);

        // Create the user message
        Message userMessageObj = new Message();
        userMessageObj.setRole("user");
        userMessageObj.setContent(userMessage);

        // Construct the OpenAIRequest object
        OpenAIRequest request = new OpenAIRequest();
        request.setMessages(Arrays.asList(systemMessage, userMessageObj)); // Add the system and user messages
        request.setModel(model); // Set the model to use
        request.setMaxTokens(outputTokens); // Set the max number of tokens

        return request;
    }
}
