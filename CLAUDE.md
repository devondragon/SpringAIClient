# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# SpringAIClient Repository Guidelines

## Project Overview
SpringAIClient is a Spring Boot library that simplifies OpenAI API integration. It follows an adapter pattern architecture to support future AI service providers.

## Architecture
```
com.digitalsanctuary.springaiclient/
├── AIClientConfiguration.java          # Spring auto-configuration
├── adapters/
│   ├── AbstractAIService.java         # Base abstraction for AI services
│   └── openai/
│       ├── config/                    # Configuration properties
│       ├── dto/                       # Request/Response DTOs
│       └── service/                   # OpenAIService implementation
```

Key design patterns:
- **Adapter Pattern**: AbstractAIService allows easy extension for new AI providers
- **Builder Pattern**: RequestBuilder provides fluent API for request creation
- **Spring Auto-Configuration**: Zero-config setup with sensible defaults

## Build and Test Commands
```bash
# Build project
./gradlew build

# Run all tests (with both JDK 17 and 21)
./gradlew test

# Run with specific JDK
./gradlew testJdk17
./gradlew testJdk21

# Run a single test
./gradlew test --tests "com.digitalsanctuary.springaiclient.adapters.openai.service.OpenAIServiceTest.testSendSimpleRequest"

# Publishing
./gradlew publishToMavenLocal                              # Local testing
./gradlew publishAndReleaseToMavenCentral --no-configuration-cache  # Maven Central
./gradlew publishAllPublicationsToReposiliteRepository     # Private repo

# Version management
./gradlew release                                          # Interactive release
```

## Development Setup
1. Set environment variable: `export OPENAI_API_KEY=your_key_here`
2. Build: `./gradlew build`
3. Test changes locally: `./gradlew publishToMavenLocal`

## Code Style Guidelines
- **Java Version**: Java 17+ (toolchain set to 17)
- **Imports**: Organized by package hierarchy; standard libraries first
- **Indentation**: 4 spaces
- **Naming**: CamelCase classes; camelCase methods/variables; ALL_CAPS constants
- **Types**: Use interfaces over implementations; strong type checking
- **Error Handling**: Custom exceptions with descriptive names; proper logging with SLF4J
- **Null Safety**: Explicit null checks; use of Lombok annotations
- **Testing**: Use JUnit 5; descriptive test names; set OPENAI_API_KEY env var for tests

## Key Components
1. **OpenAIService**: Main service extending AbstractAIService
   - Uses Spring's RestClient for HTTP communication
   - Provides both simple string and builder-based request methods

2. **OpenAIConfigProperties**: Configuration bound to `ds.ai.openai.*`
   - Configurable: apiKey, model, maxOutputTokens, endpoint, systemPrompt

3. **RequestBuilder**: Fluent API pre-configured with defaults
   - Chain methods to override any request parameter
   - Builds OpenAIRequest objects

4. **Error Handling**: OpenAICommunicationException for API errors

Remember to follow existing code patterns when making changes.