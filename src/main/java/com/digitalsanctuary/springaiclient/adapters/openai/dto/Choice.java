package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Choice {

    @JsonProperty("index")
    private int index;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("logprobs")
    private Object logprobs;

    @JsonProperty("finish_reason")
    private String finishReason;
}
