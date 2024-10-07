package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenAIRequest {

    @JsonProperty("messages")
    private List<Message> messages;

    @JsonProperty("max_tokens")
    private int maxTokens;

    @JsonProperty("model")
    private String model;
}
