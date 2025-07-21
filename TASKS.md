# SpringAIClient Improvement Tasks

This document contains detailed tasks for improving the SpringAIClient library based on the comprehensive code review. Each task includes goals, implementation details, and success criteria.

## Critical Priority Tasks

### 1. Implement Dynamic API Key Management

**Goal**: Enable API key rotation without application restart and improve security.

**Current Issue**: API key is embedded in RestClient default headers at bean creation time, making it impossible to change without restart.

**Changes Needed**:
1. Create a `ClientHttpRequestInterceptor` implementation:
   ```java
   @Component
   public class OpenAIAuthInterceptor implements ClientHttpRequestInterceptor {
       private final OpenAIConfigProperties properties;
       
       @Override
       public ClientHttpResponse intercept(HttpRequest request, byte[] body, 
           ClientHttpRequestExecution execution) throws IOException {
           request.getHeaders().add(HttpHeaders.AUTHORIZATION, 
               "Bearer " + properties.getApiKey());
           return execution.execute(request, body);
       }
   }
   ```

2. Modify `OpenAIConfig.java`:
   - Remove the authorization header from `defaultHeader()`
   - Add the interceptor to RestClient builder
   - Line 60: Replace `.defaultHeader(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_PREFIX + properties.getApiKey())`
   - With: `.requestInterceptor(openAIAuthInterceptor)`

3. Add API key validation to `OpenAIConfigProperties.java`:
   ```java
   @NotBlank(message = "OpenAI API key must not be blank")
   @Pattern(regexp = "^sk-[a-zA-Z0-9]{48}$", message = "Invalid OpenAI API key format")
   private String apiKey;
   ```

**Success Criteria**:
- API key can be changed via configuration without restart
- Invalid API key format triggers validation error at startup
- All existing functionality continues to work
- Unit tests pass with mocked interceptor

### 2. Add Request Timeout Configuration

**Goal**: Prevent hanging requests by implementing configurable timeouts.

**Current Issue**: RestClient has no timeout configuration, potentially causing indefinite hangs.

**Changes Needed**:
1. Add timeout properties to `OpenAIConfigProperties.java`:
   ```java
   @Min(1000)
   private int connectTimeoutMs = 10000; // 10 seconds default
   
   @Min(1000)
   private int readTimeoutMs = 30000; // 30 seconds default
   ```

2. Modify `OpenAIConfig.java` to configure timeouts:
   ```java
   @Bean
   public RestClient openAIRestClient(OpenAIConfigProperties properties, 
                                     ClientHttpRequestFactory factory) {
       return RestClient.builder()
           .baseUrl(properties.getApiEndpoint())
           .requestFactory(factory)
           .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
           .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
           .requestInterceptor(openAIAuthInterceptor)
           .build();
   }
   
   @Bean
   public ClientHttpRequestFactory clientHttpRequestFactory(OpenAIConfigProperties properties) {
       SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
       factory.setConnectTimeout(Duration.ofMillis(properties.getConnectTimeoutMs()));
       factory.setReadTimeout(Duration.ofMillis(properties.getReadTimeoutMs()));
       return factory;
   }
   ```

**Success Criteria**:
- Requests timeout after configured duration
- Timeout values are configurable via properties
- Timeout exceptions are properly handled
- Default timeouts are reasonable (10s connect, 30s read)

## High Priority Tasks

### 3. Add RequestBuilder Validation

**Goal**: Prevent null pointer exceptions and invalid requests by validating required fields.

**Current Issue**: RequestBuilder.build() doesn't validate required fields, risking NPEs.

**Changes Needed**:
1. Add validation to `RequestBuilder.build()` method (lines 70-88):
   ```java
   public OpenAIRequest build() {
       // Validate required fields
       if (userMessage == null || userMessage.trim().isEmpty()) {
           throw new IllegalStateException("User message is required");
       }
       if (model == null || model.trim().isEmpty()) {
           throw new IllegalStateException("Model is required");
       }
       
       // Existing build logic...
   }
   ```

2. Add output token validation to `RequestBuilder.outputTokens()`:
   ```java
   public RequestBuilder outputTokens(int outputTokens) {
       if (outputTokens <= 0 || outputTokens > 4096) {
           throw new IllegalArgumentException(
               "Output tokens must be between 1 and 4096, was: " + outputTokens);
       }
       this.outputTokens = outputTokens;
       return this;
   }
   ```

3. Add model validation against known models:
   ```java
   private static final Set<String> VALID_MODELS = Set.of(
       "gpt-4", "gpt-4-turbo", "gpt-3.5-turbo", "gpt-4o", "gpt-4o-mini"
   );
   
   public RequestBuilder model(String model) {
       if (!VALID_MODELS.contains(model)) {
           log.warn("Unknown model: {}. Proceeding anyway.", model);
       }
       this.model = model;
       return this;
   }
   ```

**Success Criteria**:
- Building without required fields throws clear exceptions
- Invalid output token values are rejected
- Unknown models trigger warnings
- All validation errors have descriptive messages

### 4. Implement Specific Exception Types

**Goal**: Provide clear error handling for different failure scenarios.

**Current Issue**: Generic exception handling masks specific error types (401, 429, etc).

**Changes Needed**:
1. Create new exception classes in `service` package:
   ```java
   public class OpenAIAuthenticationException extends OpenAICommunicationException {
       public OpenAIAuthenticationException(String message, Throwable cause) {
           super(message, cause);
       }
   }
   
   public class OpenAIRateLimitException extends OpenAICommunicationException {
       private final int retryAfterSeconds;
       // constructor with retry-after header value
   }
   
   public class OpenAIRequestException extends OpenAICommunicationException {
       private final int statusCode;
       private final String errorMessage;
       // constructor with error details
   }
   ```

2. Update `OpenAIService.sendRequest()` exception handling (lines 151-154):
   ```java
   } catch (HttpClientErrorException.Unauthorized e) {
       log.error("Authentication failed: {}", e.getMessage());
       throw new OpenAIAuthenticationException("Invalid API key", e);
   } catch (HttpClientErrorException.TooManyRequests e) {
       String retryAfter = e.getResponseHeaders().getFirst("Retry-After");
       log.warn("Rate limit exceeded, retry after: {}", retryAfter);
       throw new OpenAIRateLimitException("Rate limit exceeded", e, 
           parseRetryAfter(retryAfter));
   } catch (HttpClientErrorException.BadRequest e) {
       log.error("Invalid request: {}", e.getResponseBodyAsString());
       throw new OpenAIRequestException("Invalid request", e, 
           e.getStatusCode().value(), e.getResponseBodyAsString());
   } catch (HttpServerErrorException e) {
       log.error("OpenAI server error: {}", e.getMessage());
       throw new OpenAICommunicationException("OpenAI service error", e);
   } catch (ResourceAccessException e) {
       log.error("Network error: {}", e.getMessage());
       throw new OpenAICommunicationException("Network communication failed", e);
   }
   ```

**Success Criteria**:
- Different HTTP status codes throw appropriate exceptions
- Rate limit exceptions include retry-after information
- Authentication errors are clearly identified
- All exceptions maintain backward compatibility

### 5. Document Thread Safety

**Goal**: Prevent concurrency issues by clearly documenting thread safety guarantees.

**Current Issue**: RequestBuilder maintains mutable state and isn't thread-safe.

**Changes Needed**:
1. Add class-level documentation to `RequestBuilder.java`:
   ```java
   /**
    * Fluent builder for creating OpenAI requests.
    * 
    * <p><b>Thread Safety:</b> This class is NOT thread-safe. 
    * Each thread should create its own RequestBuilder instance.
    * Do not share RequestBuilder instances between threads.</p>
    * 
    * <p>Example usage:</p>
    * <pre>{@code
    * OpenAIRequest request = openAIService.requestBuilder()
    *     .userMessage("Hello")
    *     .model("gpt-4")
    *     .build();
    * }</pre>
    */
   @NotThreadSafe  // Add JSR-305 annotation
   public class RequestBuilder {
   ```

2. Update `OpenAIService` to always return new builder instances:
   ```java
   /**
    * Creates a new RequestBuilder instance.
    * Each call returns a new builder to ensure thread safety.
    */
   public RequestBuilder requestBuilder() {
       // Ensure we always return a new instance
       return new RequestBuilder(properties);
   }
   ```

**Success Criteria**:
- Clear documentation about thread safety
- @NotThreadSafe annotation present
- Service always returns new builder instances
- No shared mutable state between builders

## Medium Priority Tasks

### 6. Remove or Enhance AbstractAIService

**Goal**: Eliminate unnecessary abstraction or add meaningful shared functionality.

**Current Issue**: AbstractAIService is empty, violating YAGNI principle.

**Option A - Remove AbstractAIService**:
1. Delete `AbstractAIService.java`
2. Remove `extends AbstractAIService` from `OpenAIService`
3. Create an interface if planning future providers:
   ```java
   public interface AIService {
       String sendMessage(String message);
       AIResponse sendRequest(AIRequest request);
   }
   ```

**Option B - Add Shared Functionality**:
1. Add common functionality to `AbstractAIService`:
   ```java
   public abstract class AbstractAIService {
       protected final Logger log = LoggerFactory.getLogger(getClass());
       
       protected void validateRequest(String message) {
           if (message == null || message.trim().isEmpty()) {
               throw new IllegalArgumentException("Message cannot be empty");
           }
       }
       
       protected void logRequest(String provider, String model) {
           log.debug("Sending request to {} using model: {}", provider, model);
       }
       
       public abstract String sendMessage(String message);
   }
   ```

**Success Criteria**:
- Either no empty abstractions exist, OR
- Abstract class provides meaningful shared functionality
- Code remains extensible for future providers

### 7. Implement Test Mocking Strategy

**Goal**: Eliminate real API calls in tests to avoid costs and improve reliability.

**Current Issue**: Tests make real API calls, incurring costs and failing without valid keys.

**Changes Needed**:
1. Create test configuration class:
   ```java
   @TestConfiguration
   public class OpenAITestConfig {
       @Bean
       @Primary
       public RestClient mockRestClient() {
           RestClient mockClient = Mockito.mock(RestClient.class);
           // Configure mock responses
           return mockClient;
       }
   }
   ```

2. Create separate integration test class:
   ```java
   @SpringBootTest
   @ActiveProfiles("integration")
   @EnabledIf("#{environment['OPENAI_API_KEY'] != null}")
   public class OpenAIServiceIntegrationTest {
       // Real API tests here
   }
   ```

3. Update existing tests to use mocks:
   ```java
   @SpringBootTest
   @Import(OpenAITestConfig.class)
   public class OpenAIServiceTest {
       @MockBean
       private RestClient restClient;
       
       @Test
       void testSendSimpleRequest() {
           // Configure mock
           when(restClient.post()).thenReturn(...);
           
           // Test without real API call
       }
   }
   ```

**Success Criteria**:
- Unit tests run without API key
- No real API calls in unit tests
- Integration tests only run when explicitly enabled
- Test coverage remains high

### 8. Add Production-Ready Features

**Goal**: Implement retry mechanism and basic monitoring capabilities.

**Current Issue**: No retry logic for transient failures or monitoring hooks.

**Changes Needed**:
1. Add retry configuration to `OpenAIConfigProperties`:
   ```java
   private int maxRetries = 3;
   private long retryDelayMs = 1000;
   private double retryMultiplier = 2.0;
   ```

2. Implement retry logic in `OpenAIService`:
   ```java
   @Retryable(
       value = {OpenAICommunicationException.class},
       maxAttempts = 3,
       backoff = @Backoff(delay = 1000, multiplier = 2)
   )
   public OpenAIResponse sendRequest(OpenAIRequest request) {
       // Existing implementation
   }
   
   @Recover
   public OpenAIResponse recoverFromCommunicationError(
           OpenAICommunicationException e, OpenAIRequest request) {
       log.error("All retries exhausted for request: {}", request.getModel());
       throw e;
   }
   ```

3. Add request/response interceptor for monitoring:
   ```java
   @Component
   public class OpenAIMetricsInterceptor implements ClientHttpRequestInterceptor {
       private final MeterRegistry meterRegistry;
       
       @Override
       public ClientHttpResponse intercept(...) {
           Timer.Sample sample = Timer.start(meterRegistry);
           try {
               ClientHttpResponse response = execution.execute(request, body);
               meterRegistry.counter("openai.requests", "status", "success").increment();
               return response;
           } catch (Exception e) {
               meterRegistry.counter("openai.requests", "status", "error").increment();
               throw e;
           } finally {
               sample.stop(meterRegistry.timer("openai.request.duration"));
           }
       }
   }
   ```

**Success Criteria**:
- Transient failures are automatically retried
- Retry configuration is customizable
- Basic metrics are collected (request count, duration, errors)
- No performance impact on normal operations

## Low Priority Tasks

### 9. Fix Type Safety Issues

**Goal**: Replace generic Object types with proper type definitions.

**Current Issue**: Choice.logprobs uses Object type, reducing type safety.

**Changes Needed**:
1. Create proper type for logprobs in `dto` package:
   ```java
   @Data
   public class LogProbs {
       private List<String> tokens;
       private List<Double> tokenLogprobs;
       private List<Map<String, Double>> topLogprobs;
       private List<Integer> textOffset;
   }
   ```

2. Update `Choice.java`:
   ```java
   private LogProbs logprobs;  // Replace Object with LogProbs
   ```

3. Add consistent null handling to all DTOs:
   ```java
   public String getContent() {
       return content != null ? content : "";
   }
   ```

**Success Criteria**:
- No Object types in DTOs
- All DTOs handle null values consistently
- Deserialization works with new types

### 10. Implement Secure Logging Practices

**Goal**: Prevent sensitive data exposure in logs.

**Current Issue**: Response content and potentially sensitive data are logged.

**Changes Needed**:
1. Create a log sanitizer utility:
   ```java
   public class LogSanitizer {
       private static final int MAX_LENGTH = 100;
       
       public static String sanitizeContent(String content) {
           if (content == null) return "null";
           if (content.length() <= MAX_LENGTH) return content;
           return content.substring(0, MAX_LENGTH) + "... [truncated]";
       }
       
       public static String maskApiKey(String key) {
           if (key == null || key.length() < 8) return "***";
           return key.substring(0, 4) + "****" + key.substring(key.length() - 4);
       }
   }
   ```

2. Update logging statements in `OpenAIService`:
   ```java
   // Line 146 - replace with:
   log.debug("Received response with {} choices, {} tokens used", 
       response.getChoices().size(), 
       response.getUsage() != null ? response.getUsage().getTotalTokens() : 0);
   ```

3. Add correlation ID support:
   ```java
   private static final String CORRELATION_ID = "X-Correlation-ID";
   
   // In request method:
   String correlationId = UUID.randomUUID().toString();
   MDC.put("correlationId", correlationId);
   try {
       log.debug("Starting request with correlation ID: {}", correlationId);
       // ... rest of method
   } finally {
       MDC.remove("correlationId");
   }
   ```

**Success Criteria**:
- No sensitive data in logs
- API keys are masked if logged
- Response content is truncated
- Correlation IDs enable request tracking

## Testing Instructions

For each task:
1. Write unit tests for new functionality
2. Ensure existing tests continue to pass
3. Add integration tests where appropriate
4. Verify no performance regression
5. Update documentation and examples

## Notes

- Maintain backward compatibility where possible
- Follow existing code style and conventions
- Update CLAUDE.md with any new build/test commands
- Consider creating a migration guide for breaking changes