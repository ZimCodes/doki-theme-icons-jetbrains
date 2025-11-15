<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog for Doki Theme Icons: JetBrains

## [Unreleased]

## Added
- Update Gradle: `9.1` -> `9.2`
- Project support for configuration caching
- Update dependencies
- Update project dev source dependencies
- Add build caching support

## Changed
- Turn some services into light services
- Change multi-project structure: `buildSrc` -> composite builds (build-logic)

## Removed
- `org.javassist` dependency
- `PlatformHacker.kt`, which uses hacky methods to get UI working correctly

## Fixed
- Increase max memory and metaspace when using Gradle 

## [2025.2 Build Support] - 1.7.0

### Added
- Support for IDE build 2025.2
- Update Gradle: `8.13` -> `9.1`

### Changed
- Replace deprecated items
- Update README

### Removed
- Error reporting telemetry
- All test suites
- Test plugins
- Icon request issue template
- All Github workflows

### Fixed
- Apply button no longer enabled after selecting a theme once
- Broken/outdated imports
- Update dependencies