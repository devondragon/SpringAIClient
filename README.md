# Spring AI Client Library

## Overview

The Spring AI Client Library is a simple and efficient library for interacting with the OpenAI API from your Spring Boot application. It provides a streamlined way to send requests and receive responses from OpenAI's models, making it easy to integrate AI capabilities into your Spring Boot applications. I plan on adding support for other AI models and services in the future.


> **NOTE**: You should check out the official Spring AI project at [https://spring.io/projects/spring-ai](https://spring.io/projects/spring-ai) to see if it meets your needs before using this library. I created this library because I wanted a simpler way to interact with the OpenAI API in my Spring Boot projects, and I wanted to be able to support new OpenAI models and features as they are released.


## Features

- Easy configuration using Spring Boot's `application.yml`.
- Supports multiple OpenAI models.
- Handles API requests and responses seamlessly.
- Provides a clean and maintainable code structure.
- Supports proxy configuration for corporate environments.

## Getting Started

### Prerequisites

- Java 17 or later
- Gradle 8.10.1 or Maven 3.8.1+
- OpenAI API Key

### Quick Start

The library is available through the Maven Central Repository. You can include it in your Spring Boot project using either Maven or Gradle.

#### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.digitalsanctuary</groupId>
    <artifactId>ds-spring-ai-client</artifactId>
    <version>1.1.4</version>
</dependency>
```

#### Gradle

Add the following dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.digitalsanctuary:ds-spring-ai-client:1.1.4'
}
```

#### Configuration

Configure the library using the `application.yml` file located in

`src/main/resources`



```yaml
ds:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY} # OpenAI API key

```
This is the only required configuration. You can also configure optional properties described in the Full Example section below, including proxy settings for corporate environments.

#### Simple Example Service

Create a service that uses the `OpenAIService` to send requests to OpenAI.

```java
package com.digitalsanctuary.springaiclient.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIRequest;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIResponse;
import com.digitalsanctuary.springaiclient.adapters.openai.service.OpenAIService;

@Service
public class ExampleService {

    @Autowired
    private OpenAIService openAIService;

    public String getAIResponse(String userInput) {
          // Create a simple request
        OpenAIRequest request = openAIService.createRequestBuilder().userMessage("Tell me a joke.").build();

        // Send the request and get the response
        OpenAIResponse response = openAIService.sendRequest(request);

        // Return the response message (this is a helper method that extracts the message from the response)
        return response.getMessage();
    }
}
```



## Full Example

### Full Configuration

Configure the library using the `application.yml` file located in `src/main/resources`. The following example shows all available configuration options, including proxy settings.


```yaml
ds:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY} # OpenAI API key
      model: gpt-4o # OpenAI model
      output-tokens: 4096 # OpenAI max output tokens
      api-endpoint: https://api.openai.com/v1/chat/completions
      system-prompt: "You are a helpful assistant."
      
      # Optional proxy configuration
      proxy:
        enabled: false # Set to true to enable proxy
        host: proxy.example.com # Proxy server hostname or IP
        port: 8080 # Proxy server port
        username: proxyuser # Optional proxy authentication username
        password: proxypass # Optional proxy authentication password
```



### Usage

#### More Complex Example Service

Create a service that uses the `OpenAIService` to send requests to OpenAI.

```java
package com.digitalsanctuary.springaiclient.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIRequest;
import com.digitalsanctuary.springaiclient.adapters.openai.dto.OpenAIResponse;
import com.digitalsanctuary.springaiclient.adapters.openai.service.OpenAIService;

@Service
public class ExampleService {

    @Autowired
    private OpenAIService openAIService;

    public String getAIResponse(String userInput) {
          // Create a simple request
         OpenAIRequest request = openAIService.createRequestBuilder()
                .userMessage("Tell me a joke.")
                .model("chatgpt-4o-latest")
                .outputTokens(8000)
                .systemPrompt("You only tell jokes about birds")
                .build();

        // Send the request and get the response
        OpenAIResponse response = openAIService.sendRequest(request);

        // Return the response message (skipping null checks, etc. for brevity)
        return response.getChoices().get(0).getMessage().getContent();
    }
}
```



## Proxy Configuration

The library supports running behind corporate proxies. To configure a proxy:

1. Set `ds.ai.openai.proxy.enabled` to `true`
2. Configure the proxy host and port
3. Optionally, provide proxy authentication credentials

Example configuration:

```yaml
ds:
  ai:
    openai:
      # ... other settings ...
      proxy:
        enabled: true
        host: proxy.example.com
        port: 8080
        username: proxyuser  # Optional
        password: proxypass  # Optional
```

This is especially useful in corporate environments where direct internet access is restricted.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.
See the [Developer Guide](DEVELOP.md) file for more information on how to contribute.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contact

For any questions or support, please open an issue on the GitHub repository.
