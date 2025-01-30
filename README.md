# Hlæja Test

In the forge of software development, where annotations ignite, A crucible of testing, common classes to excite. Each annotation examined, with attention to detail and might, Their effects on code behavior, tested through day and night. From mockk objects to test doubles, a toolkit to refine, Developers and testers, their skills to redefine. The Annotation Validator, a sentinel of code integrity true, A library of verification, where testing wisdom shines anew.

## Releasing library

Run `release.sh` script from `master` branch.

## Publishing library

### Publish library locally

```shell
./gradlew clean build publishToMavenLocal
```

### Publish library to repository

```shell
./gradlew clean build publish
```

### Global Settings

This services rely on a set of global settings to configure development environments. These settings, managed through Gradle properties or environment variables.

*Note: For more information on global properties, please refer to our [global settings](https://github.com/swordsteel/hlaeja-development/blob/master/doc/global_settings.md) documentation.*

#### Gradle Properties

```properties
repository.user=your_user
repository.token=your_token_value
```

#### Environment Variables

```properties
REPOSITORY_USER=your_user
REPOSITORY_TOKEN=your_token_value
```
