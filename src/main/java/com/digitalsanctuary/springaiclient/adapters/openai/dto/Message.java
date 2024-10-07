package com.digitalsanctuary.springaiclient.adapters.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Message {

    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;
}
