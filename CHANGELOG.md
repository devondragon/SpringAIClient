## [1.1.5] - 2025-07-21
### Features
- Updated project guidance in CLAUDE.md for comprehensive project understanding.
- Additional permissions added to Claude Code settings.
- JUnit 5 dependencies added for improved testing.
- Comprehensive JavaDocs added to core classes for easy code understanding.
- Added build commands and code style guidelines to CLAUDE.md.
- Python interpreter path updated in VSCode settings for JavaScript and Python.

### Fixes
- Successful build with the fix in javadoc HTML errors.
- Compatibility issue with vanniktech maven publish plugin 0.34.0 fixed. 
- Fixed type in code. 

### Updates
- Spring Boot version updated from 3.4.5 to 3.5.3.
- Gradle wrapper updated from version 8.13 to 8.14.2.
- Bump in version from org.projectlombok:lombok from 1.18.36 to 1.18.38.
- Bump in version com.vanniktech.maven.publish from 0.30.0 to 0.34.0 
- Incremental updates of org.junit.jupiter:junit-jupiter from 5.13.0 to 5.13.3
- Version bump in ds-spring-ai-client to 1.1.4 and log changes.
- Adjustments in VSCode settings to specify default runtime.
- Bump in com.github.ben-manes.versions from 0.51.0 to 0.52.0.

### Breaking Changes
- Updated the Spring Boot version from 3.4.1 to 3.4.2.
- Merged main branch of https://github.com/devondragon/SpringAIClient. In case of conflicting changes, check this merge.
- Changed Java version specification to mise.toml along with Spring Boot version updated to 3.4.3. Make sure to check system configuration.
- The new version commit: '1.1.5-SNAPSHOT' with Gradle Release Plugin.

## [1.1.4] - 2024-12-31
### Features
- Enhanced changelog generation script.

### Fixes
- Updated dependencies and related building process in pull request #15.
- Bumped up Spring Boot version from 3.3.5 to 3.4.0 in pull request #9 and to 3.4.1 later for better stability.
- Upgraded Spring Boot version in build.gradle file from the base version to 3.3.5 for improved application compatibility.
- Updated Gradle wrapper to version 8.11.1 to address any associated vulnerabilities and to boost performance.

### Breaking Changes
- Bumped up org.projectlombok:lombok version from 1.18.34 to 1.18.36 - This may cause potential conflicts with some existing APIs or methods.
- Updated Spring Boot version could lead to potential issues in the application if deprecated features were used previously.

