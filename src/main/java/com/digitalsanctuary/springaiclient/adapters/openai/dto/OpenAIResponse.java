package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenAIResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created")
    private long created;

    @JsonProperty("model")
    private String model;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("usage")
    private Usage usage;

    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * Gets the content of the first choice's message. This is a helper method to make it easier to access the content of the first choice's message.
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
