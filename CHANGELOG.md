# Changelog

## [0.4.7](https://github.com/sloud/hytale-portals/compare/v0.4.6...v0.4.7) (2026-02-04)


### Code Refactoring

* **settings:** rename project to `portals` ([f76be07](https://github.com/sloud/hytale-portals/commit/f76be07ad86d42117f11f5526b8b262a9bb96edc))

## [0.4.6](https://github.com/sloud/hytale-portals/compare/v0.4.5...v0.4.6) (2026-02-04)


### Continuous Integration

* **build:** add workflow for testing Maven local publishing ([b418ff3](https://github.com/sloud/hytale-portals/commit/b418ff3f3ec0c3a501dd7aecc5c582a3fa3ed4a6))
* **build:** remove redundant artifact declaration ([a85e22d](https://github.com/sloud/hytale-portals/commit/a85e22d1f38679af9b8ab6dafab8524af4347597))
* **build:** update POM metadata for publishing ([d7b695e](https://github.com/sloud/hytale-portals/commit/d7b695e7c15faf301faa543cc707ef26db52ff3e))

## [0.4.5](https://github.com/sloud/hytale-portals/compare/v0.4.4...v0.4.5) (2026-02-04)


### Continuous Integration

* **build:** set Maven group in Gradle configuration ([05c19ec](https://github.com/sloud/hytale-portals/commit/05c19ec0fcd0d3faf2b443609f24250c7e508fed))

## [0.4.4](https://github.com/sloud/hytale-portals/compare/v0.4.3...v0.4.4) (2026-02-04)


### Continuous Integration

* **build:** enhance Maven publishing setup and workflow ([3ed48c7](https://github.com/sloud/hytale-portals/commit/3ed48c7aeef22388c7d04b810d0541a849ce16eb))

## [0.4.3](https://github.com/sloud/hytale-portals/compare/v0.4.2...v0.4.3) (2026-02-04)


### Continuous Integration

* **release:** update Gradle task for GitHub Packages publishing ([e9336d4](https://github.com/sloud/hytale-portals/commit/e9336d4ec0757e652a784d06e8b14cbeef34fde7))
* **release:** update token references in workflow ([3aa53c2](https://github.com/sloud/hytale-portals/commit/3aa53c2f4c2a0c9c373150d0d2fc47dbc3d6a273))

## [0.4.2](https://github.com/joeyaurel/hytale-portals/compare/v0.4.1...v0.4.2) (2026-02-01)


### Code Refactoring

* **settings:** rename project to `SloudPortals` ([7570f73](https://github.com/joeyaurel/hytale-portals/commit/7570f73fd38903b72cf193f456a264049a22c395))

## [0.4.1](https://github.com/joeyaurel/hytale-portals/compare/v0.4.0...v0.4.1) (2026-02-01)


### Code Refactoring

* rename package structure to `network.sloud` ([e7f8d08](https://github.com/joeyaurel/hytale-portals/commit/e7f8d08ab21e42e7144551445af88687d4c46e3c))
* rename plugin and update references ([e220d33](https://github.com/joeyaurel/hytale-portals/commit/e220d336d9c2d8a92ccffbd91793740326a37339))


### Continuous Integration

* **release:** add custom changelog sections to release config ([6e4df08](https://github.com/joeyaurel/hytale-portals/commit/6e4df088c7194b54f5f474bb3e2b703f4b39fa62))

## [0.4.0](https://github.com/joeyaurel/hytale-portals/compare/v0.3.0...v0.4.0) (2026-02-01)


### Features

* **build:** add GitHub Packages support for Maven publishing ([be823bf](https://github.com/joeyaurel/hytale-portals/commit/be823bf4fe7f13fda44a548afd784b6b330b5b66))

## [0.3.0](https://github.com/joeyaurel/hytale-portals/compare/v0.2.0...v0.3.0) (2026-02-01)


### Features

* **commands:** add portal configuration management ([6ebf8a0](https://github.com/joeyaurel/hytale-portals/commit/6ebf8a04f9157fe935952e171ff700330e227eee))
* **portals:** add delayed teleport sound playback ([b921237](https://github.com/joeyaurel/hytale-portals/commit/b921237c13f392e22204ca505ead380d18a55c83))

## [0.2.0](https://github.com/joeyaurel/hytale-portals/compare/v0.1.0...v0.2.0) (2026-01-30)


### Features

* **server:** update server version to 2026.01.28-87d03be09 ([8347a06](https://github.com/joeyaurel/hytale-portals/commit/8347a06004df9e75ce8431787f6b937115796790))


### Bug Fixes

* **build:** package runtime dependencies in fat JAR ([31ff53e](https://github.com/joeyaurel/hytale-portals/commit/31ff53e9d3732ccdc9a9b3b5fb80d5bb50dfbaca))

## [0.1.0](https://github.com/joeyaurel/hytale-portals/compare/v0.0.1...v0.1.0) (2026-01-30)


### Features

* add portal network and database structures ([1167323](https://github.com/joeyaurel/hytale-portals/commit/116732369ee332fe401718b946140c1052c8bb09))
* **config:** add configuration management with Gson ([89a0b14](https://github.com/joeyaurel/hytale-portals/commit/89a0b14f54c08b1d496c1e8924fb78709bbb1e4a))
* **database:** enhance database error handling and shutdown logic ([235ff29](https://github.com/joeyaurel/hytale-portals/commit/235ff29aea5abfeaec16efc518ac3daf7112a0d9))
* initial commit ([e32583f](https://github.com/joeyaurel/hytale-portals/commit/e32583f380ab6b60580afee4a59e98e293865198))
* initialize portal system plugin ([2b1615d](https://github.com/joeyaurel/hytale-portals/commit/2b1615dd03725b83e2389fb6458e53c51ed9cbdd))
* **permissions:** simplify constants ([a61efcf](https://github.com/joeyaurel/hytale-portals/commit/a61efcf4da713085c1cbcc3720c39e2c0e9f35cd))
* **portals:** add bounds calculation and position check ([08e763a](https://github.com/joeyaurel/hytale-portals/commit/08e763a3db10ae29411119665dc3b01d94a0c0b6))
* **portals:** add event systems for block interaction ([645b6d4](https://github.com/joeyaurel/hytale-portals/commit/645b6d47e644f343e3a6b3e0ad126348c87c2671))
* **portals:** add GUI and logic for portal teleportation ([e8d09ec](https://github.com/joeyaurel/hytale-portals/commit/e8d09ecd85646ce60bddcf773037a7f9ede576c7))
* **portals:** add location-based portal detection and tests ([5234cae](https://github.com/joeyaurel/hytale-portals/commit/5234cae98f8bb96c95b48ef9c74c2f4f67dbb1d9))
* **portals:** add logging for portal actions and plugin setup ([262b2d9](https://github.com/joeyaurel/hytale-portals/commit/262b2d976cd345c1f37072f8065b1c0eb6b38850))
* **portals:** add portal cancelation command ([b51e353](https://github.com/joeyaurel/hytale-portals/commit/b51e35324529ddc7ab445d068186b443a90cbcc5))
* **portals:** add portal creation UI and command updates ([74ea667](https://github.com/joeyaurel/hytale-portals/commit/74ea667d9862dd721b0ad0992ce6d16b5e67375e))
* **portals:** add portal destination support ([bd68e15](https://github.com/joeyaurel/hytale-portals/commit/bd68e15f43d5788158b0bdc7d6b1e6515006e5ae))
* **portals:** add portal network editing functionality ([a96eed8](https://github.com/joeyaurel/hytale-portals/commit/a96eed8bf1f0082202a664f47ff2d12f1b21853c))
* **portals:** add portal network management commands ([f2ec9f6](https://github.com/joeyaurel/hytale-portals/commit/f2ec9f60afb380d7b4ba7a948a93e621c80afa86))
* **portals:** add PortalDoneCommand and implement portal DB actions ([2761b9d](https://github.com/joeyaurel/hytale-portals/commit/2761b9d26265f2deb09ccafc5046e43d2b98c800))
* **portals:** add Singleton annotation and inject dependencies ([05f0868](https://github.com/joeyaurel/hytale-portals/commit/05f08683e643e87de2c8a1436596b86974207208))
* **portals:** enhance portal bounds messaging with coordinates ([9afe38a](https://github.com/joeyaurel/hytale-portals/commit/9afe38a1720f6c46b028b377646e7e22e709afc3))
* **portals:** ensure main directory creation on startup ([74a3526](https://github.com/joeyaurel/hytale-portals/commit/74a35262d06f6fc97a642bd23b8a7637ab7f4ad7))
* **portals:** handle empty portal network list gracefully ([238dc8a](https://github.com/joeyaurel/hytale-portals/commit/238dc8ae6c3326f9a173e96696b072ad960eb1cf))
* **portals:** implement EntryTickingSystem for portal logic ([d4ce3f7](https://github.com/joeyaurel/hytale-portals/commit/d4ce3f742af49976b42ec10095c18cc1378e3eaf))
* **portals:** prevent duplicate portal network creation ([5560697](https://github.com/joeyaurel/hytale-portals/commit/5560697b139659f2a566aa31cb037837daa1be23))
* **portals:** refactor rotation handling for teleportation ([3621d3a](https://github.com/joeyaurel/hytale-portals/commit/3621d3ae85d657ca552b8c88a28bc9cebc1ed487))
* **portals:** remove portals when deleting their network ([6d5c6cd](https://github.com/joeyaurel/hytale-portals/commit/6d5c6cd8fcac0e9f2b0f2e69cde2c71c41b1db8c))
* **server:** update server version to 2026.01.24-6e2d4fc36 ([f4a9afc](https://github.com/joeyaurel/hytale-portals/commit/f4a9afc35e0c888e8d590b01a461d01e1ee6ae29))
* **server:** update server version to 2026.01.27-734d39026 ([08baaec](https://github.com/joeyaurel/hytale-portals/commit/08baaec18f42c2cfbc7f49bfae75a87ff7c610a0))
* **utils:** add rotation clipping utility with tests ([ce8a23d](https://github.com/joeyaurel/hytale-portals/commit/ce8a23dc9b23bc8619462fd55dd22370e9a6464a))


### Bug Fixes

* **portals:** adjust confirmation check order in removal command ([91409c7](https://github.com/joeyaurel/hytale-portals/commit/91409c7674007d25acf5273b8254b25fbc5b5952))
* **portals:** correct boundary conditions in portal validation ([3bcb548](https://github.com/joeyaurel/hytale-portals/commit/3bcb548c2e90a9c26020e017d9043d192cbcea15))
* **portals:** ensure configuration is saved on startup ([734f112](https://github.com/joeyaurel/hytale-portals/commit/734f112ecf1ea97c1882fbc509c15a03e263fc20))
* **portals:** fix incorrect variable usage in network creation ([ea3ee9d](https://github.com/joeyaurel/hytale-portals/commit/ea3ee9d86d4f352adfff979267b5d42c028a13bd))
* **portals:** sanitize network names by trimming quotes ([5d6bdc9](https://github.com/joeyaurel/hytale-portals/commit/5d6bdc95026c89d823304f00c4957a6724dfaa9a))
* **portals:** use eager singletons for bindings ([f6f3870](https://github.com/joeyaurel/hytale-portals/commit/f6f3870448f0a929338ec48292ac753154054c8c))
* **portals:** use start instead of setup method ([9e24b1b](https://github.com/joeyaurel/hytale-portals/commit/9e24b1b0284488c7cc65bcf8ccd0a5bb628cb7f9))
