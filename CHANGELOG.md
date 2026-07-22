# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.1.0] - 2026-07-22

### Added

- Support for building custom icon color schemes

### Changed

- Change default for current theme away from Zero Two theme

### Fixed

- Fixed Icon settings page stuck in loading state
- Fixed if icon settings page isn't fully initialize, actions in the settings page can still occur
- Add missing icon for `Named Folders` in doki settings page

## [2.0.0] - 2026-07-20

### Added

- Build Support for 2026.2
- Add `What's New` page in plugin

### Changed

- Reconstruct doki icons settings page

### Fixed

- Fix tool window action buttons reverting to default doki theme colors when hovered
- Fix main tool window using default doki theme colors instead of current doki theme

### Removed

- Remove Update Notification Panel
- Remove post startup activity
- Remove 3rd party plugin features for:
  - [AniiMemes](https://github.com/ani-memes/AMII)
  - [Theme Randomizer](https://github.com/Unthrottled/theme-randomizer)
  - [Waifu Motivator](https://github.com/waifu-motivator/waifu-motivator-plugin)
- Remove update notification action
- Remove `SetPatcher` action

## [1.9.0] - 2026-03-27

### Changed

- Update Gradle dependencies
- Update project dependecies
- Update Gradle: `9.2.1` -> `9.4.1`

## [1.8.0] - 2025-12-13

### Added

- Update Gradle: `9.1` -> `9.2.1`
- Project support for configuration caching
- Update dependencies
- Update project dev source dependencies
- Add build caching support
- Support for IDE 2025.3
- New custom task, `MultiExecTask`

### Changed

- Turn some services into light services
- Change multi-project structure: `buildSrc` -> composite builds (build-logic)

### Removed

- `org.javassist` dependency
- `PlatformHacker.kt`, which uses hacky methods to get UI working correctly

### Fixed

- Increase max memory and metaspace when using Gradle

## [1.7.0] - 2025-10-17

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
- All GitHub workflows

### Fixed

- Apply button no longer enabled after selecting a theme once
- Broken/outdated imports
- Update dependencies

[Unreleased]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v2.1.0...HEAD
[2.1.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v2.0.0...v2.1.0
[2.0.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v1.9.0...v2.0.0
[1.9.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v1.8.0...v1.9.0
[1.8.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v1.7.0...v1.8.0
[1.7.0]: https://github.com/ZimCodes/doki-theme-jetbrains/commits/v1.7.0
