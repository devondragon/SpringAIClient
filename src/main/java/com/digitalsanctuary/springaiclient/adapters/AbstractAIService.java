package com.digitalsanctuary.springaiclient.adapters;

/**
 * Abstract base class for AI service implementations.
 * <p>
 * This class serves as the foundation for all AI service implementations in the library.
 * It defines the common contract and shared functionality across different AI providers
 * such as OpenAI, Anthropic, Google, etc.
 * </p>
 * <p>
 * Implementing classes should extend this abstract class and provide the specific
 * logic for interacting with their respective AI providers. This ensures a consistent
 * interface for consumers of the library while allowing provider-specific implementation
 * details.
 * </p>
 * <p>
 * Currently, the library includes the following implementations:
 * <ul>
 *   <li>OpenAIService - For interacting with OpenAI's models</li>
 * </ul>
 * <p>
 * Future implementations may include:
 * <ul>
 *   <li>AnthropicService - For Claude and other Anthropic models</li>
 *   <li>GeminiService - For Google's Gemini models</li>
 *   <li>MistralService - For Mistral AI models</li>
 * </ul>
 *
 * @see com.digitalsanctuary.springaiclient.adapters.openai.service.OpenAIService
 */
public abstract class AbstractAIService {
    // Common functionality for AI services can be added here
}
