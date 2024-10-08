package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a response from the OpenAI API.
 * <p>
 * This class captures various details of the response including the ID, object type, creation timestamp, model used, choices, usage details, and
 * system fingerprint.
 * </p>
 */
@Data
public class OpenAIResponse {

    /**
     * The unique identifier for the response.
     */
    @JsonProperty("id")
    private String id;

    /**
     * The type of object returned (e.g., "text_completion").
     */
    @JsonProperty("object")
    private String object;

    /**
     * The timestamp when the response was created.
     */
    @JsonProperty("created")
    private long created;

    /**
     * The model used to generate the response.
     */
    @JsonProperty("model")
    private String model;

    /**
     * The list of choices returned by the model.
     */
    @JsonProperty("choices")
    private List<Choice> choices;

    /**
     * The usage details of the API request.
     */
    @JsonProperty("usage")
    private Usage usage;

    /**
     * The system fingerprint associated with the response.
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * Gets the content of the first choice's message.
     * <p>
     * This is a helper method to make it easier to access the content of the first choice's message.
     * </p>
     *
     * @return the content of the first choice's message, or null if not available
     */
    public String getMessage() {
        if (choices != null && !choices.isEmpty() && choices.get(0).getMessage() != null) {
            return choices.get(0).getMessage().getContent();
        }
        return null;
    }
}
