# loom-quiltflower

![Build status](https://img.shields.io/github/workflow/status/Juuxel/LoomQuiltflower/Build?style=flat-square)
![Maven metadata](https://img.shields.io/maven-metadata/v/https/server.bbkr.space/artifactory/libs-release/io/github/juuxel/loom-quiltflower/maven-metadata.xml.svg?label=latest%20plugin%20version&style=flat-square&color=49bfe0)
![Quiltflower version](https://img.shields.io/maven-metadata/v/https/maven.quiltmc.org/repository/release/org/quiltmc/quiltflower/maven-metadata.xml.svg?label=latest%20quiltflower&style=flat-square&color=fc9505)

A Loom addon that adds [Quiltflower](https://github.com/QuiltMC/Quiltflower) as a Loom decompiler
for non-Quilt distributions of Loom (upstream, Architectury etc.).

- [Version compatibility](#version-compatibility)
- [Getting started](#getting-started)
  - [Fabric projects](#fabric-projects)
  - [Architectury projects](#architectury-projects)
- [Configuration](#configuration)
  - [Quiltflower version](#quiltflower-version)
  - [Quiltflower sources](#quiltflower-sources)
  - [Decompilation preferences](#decompilation-preferences)

## Version compatibility

For the latest Loom 0.10 versions (Fabric 0.10.28+, Architectury 0.10.0.183+),
use [loom-quiltflower-mini](https://github.com/Juuxel/loom-quiltflower-mini) instead.

Loom variant      | Supported versions
------------------|-------------------
Fabric Loom       | 0.8 - 0.10¹
Architectury Loom | 0.7.2, 0.7.4, 0.10.0²

¹ Last supported build: 0.10.27  
² Last supported build: 0.10.0.182

Older versions might be compatible, but using them is unsupported. Bugs caused by outdated Loom versions will not be fixed.

## Getting started

### Fabric projects

1. Add the Cotton maven repository to settings.gradle:
```diff
  pluginManagement {
      repositories {
          maven {
              name = 'Fabric'
              url = 'https://maven.fabricmc.net/'
          }
+         maven {
+             name = 'Cotton'
+             url = 'https://server.bbkr.space/artifactory/libs-release/'
+         }
          gradlePluginPortal()
      }
  }
```

2. Add loom-quiltflower to your plugins:
```diff
  plugins {
      id 'fabric-loom' version '0.8-SNAPSHOT'
+     id 'io.github.juuxel.loom-quiltflower' version '1.4.0'
      id 'maven-publish'
  }
```

3. Instead of `genSources`, you can now use `genSourcesWithQuiltflower`.

### Architectury projects

1. Add the Cotton maven repository to settings.gradle:
```diff
  pluginManagement {
      repositories {
          maven { url "https://maven.fabricmc.net/" }
          maven { url "https://maven.architectury.dev/" }
          maven { url "https://maven.minecraftforge.net/" }
+         maven {
+             name = 'Cotton'
+             url = 'https://server.bbkr.space/artifactory/libs-release/'
+         }
          gradlePluginPortal()
      }
  }
```

2. Add loom-quiltflower to your `plugins` block:
```diff
  plugins {
      id "architectury-plugin" version "3.1-SNAPSHOT"
      id "dev.architectury.loom" version "0.7.2-SNAPSHOT" apply false
+     id 'io.github.juuxel.loom-quiltflower' version '1.4.0' apply false
  }
```

3. Apply loom-quiltflower to subprojects:

```diff
  subprojects {
      apply plugin: "dev.architectury.loom"
+     apply plugin: "io.github.juuxel.loom-quiltflower"
```

> Note: this can also be done in the subprojects' `plugins` blocks.

4. Instead of `genSources`, you can now use `genSourcesWithQuiltflower`.

## Configuration

### Quiltflower version

You can configure the used version of Quiltflower with the `quiltflower` extension (called `loomQuiltflower` before 1.2.0):

```groovy
quiltflower {
    // This is the default; 1.4.0 and above should work
    quiltflowerVersion.set("1.6.0")
  
    // If you're using Groovy DSL, you can also specify the version like this:
    quiltflowerVersion = '1.6.0'
}
```

### Quiltflower sources

> Added in LQF 1.2.0.

In addition to specifying a version, you can also use a completely different *Quiltflower source*.
They are configured with the `quiltflower.source` property.

```kotlin
quiltflower {
    // Downloads the wanted QF version from the QuiltMC Maven repo.
    // This is the default behaviour.
    fromQuiltMaven()

    // Downloads the wanted QF version from the project repositories.
    fromProjectRepositories()

    // Resolves QF using a Gradle dependency.
    // The parameter can be any dependency notation supported by Gradle.
    fromDependency("a:b:1.2.3")

    // Downloads or copies the wanted QF version from a URL.
    fromUrl("https://address.to/the/quiltflower.jar")

    // Uses a local QF file
    fromFile("my-quiltflower.jar")
}
```

### Decompilation preferences

> Added in LQF 1.2.0.

You can also change the preferences used to decompile the game.
For changing the properties, you can either use their 3-letter names or, with some properties, a preset method.

Preset methods as of 1.4.0:

- `inlineSimpleLambdas` for `isl`: collapse single-line lambdas (default: 1)
- `useJadVarnaming` for `jvn`: use JAD-style local variable naming from ForgeFlower (default: 0)
- `patternMatching` for `pam`: experimental pattern matching support (default: 1)
- `experimentalTryLoopFix` for `tlf`: experimental fix for interactions between `try` and loops

#### Groovy DSL
```groovy
quiltflower {
    preferences {
        // fake options: don't try at home
        abc = 1
        ghi = 'thing'

        inlineSimpleLambdas 0
    }
}
```

#### Kotlin DSL
```kotlin
quiltflower {
    preferences(
        // fake options: don't try at home
        "abc" to 1,
        "ghi" to "thing",
    )

    preferences.inlineSimpleLambdas(0)
}
```

#### gradle.properties

> Added in LQF 1.3.0.

Preferences can also be declared in gradle.properties files using their 3-letter names, prefixed
with `loom-quiltflower.preference.`.

```properties
# Fake option here too :^)
loom-quiltflower.preference.abc = 1
```

You can use the global gradle.properties file in the Gradle user home directory to set your
wanted properties, like the indentation string, for each project that uses LQF.
