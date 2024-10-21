# Developer Guide

## Building the Project

### Set your OpenAI API Key for unit tests to run

```sh
export OPENAI_API_KEY=YOUR_OPENAI_API_KEY
```

### Build the Project

To build the project, run:

```sh
./gradlew build
```


## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.



# Build and Publish Command Reference

## Publish to Local Maven

```shell
gradle publishToMavenLocal --refresh-dependencies
```

## Publish to Private Maven repository

```shell
gradle publishALlPublicationsToReposiliteRepositoryRepository
```


## Publish to Maven Central

```shell
gradle publishAndReleaseToMavenCentral --no-configuration-cache
```



