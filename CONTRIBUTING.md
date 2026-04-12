# Contributing
## Requirements
- Java 21+
- [Intellij Idea](https://www.jetbrains.com/idea/download)
- [Yarn 4+](https://yarnpkg.com/getting-started/install)
- IDE Plugins:
  - [Plugin Devkit](https://plugins.jetbrains.com/plugin/22851-plugin-devkit)
  - [Gradle](https://plugins.jetbrains.com/plugin/13112-gradle)
  - [Kotlin](https://plugins.jetbrains.com/plugin/6954-kotlin)
  - _(optional)_ [Java](https://plugins.jetbrains.com/plugin/27368-java)
___
## Setup
### Repo Dependencies
`doki-theme-icons-jetbrains` depends on **4** other repositories *(a.k.a subprojects)*:
- [masterThemes](https://github.com/ZimCodes/doki-master-theme.git)
- [iconSource](https://github.com/ZimCodes/doki-icon-source.git)
- [doki-build-source-jvm](https://github.com/ZimCodes/doki-build-source-jvm)
- [doki-build-source](https://github.com/ZimCodes/doki-build-source.git)

`getRepoDependencies.sh` retrieves these repositories. This script is found at the root of this project.


### Sub project Dependencies
The next step is to install the dependencies for each of the sub projects. Luckily, there’s a gradle task called `buildThemeDeps` that takes care of this for you.

```bash
# Windows
gradlew.bat buildThemeDeps

# Others
./gradlew buildThemeDeps
```

### Build Themes
It’s time to build the themes! This is handled by another gradle task called `buildThemes`.

```bash
# Windows
gradlew.bat buildThemes

# Others
./gradlew buildThemes
```

___
## Testing Plugin
There are 2 ways to test out the plugin.

### Local Sandbox Method
For this method, you will need to setup an *Development Environment*. See Jetbrain’s [Setting Up a Development Environment﻿](https://plugins.jetbrains.com/docs/intellij/setting-up-theme-environment.html#add-intellij-platform-plugin-sdk).

### Build and Use Method
This method involves building the plugin and installing it on your IDE.
**Steps:**
1. Build plugin.
```bash
# windows
gradlew.bat buildPlugin

# Others
./gradlew buildPlugin
```
2. Navigate to `Settings > Plugins ⚙️ > Install plugin from disk.`
3. Select `doki-theme-icons-jetbrains.zip` found in the `build/distributions` folder to install plugin.
## 