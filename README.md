# Sloud Portals

A comprehensive portal system for Hytale, allowing players to teleport between worlds or specific points of interest.

> **⚠️ Warning: Early Access**  
> Both Hytale and this project are in early access. Features may be incomplete, unstable, or subject to frequent changes. We appreciate your patience and understanding as development progresses.

## Features

- **Portal Networks:** Portals are organized into networks. A network requires at least two portals to function.
- **Seamless Teleportation:** Stepping through a portal teleports you to another portal within the same network.
- **Destination Selection:** If a network contains more than two portals, players can choose their specific destination via an interactive menu.

## Installation

<!-- x-release-please-start-version -->

- Download the latest JAR file from the [here](https://github.com/sloud/hytale-portals/releases/latest). (The current version is: **v0.4.7**)
- Put the JAR file in your Hytale `mods` folder.

<!-- x-release-please-end -->

## Maven/Gradle

Under https://github.com/sloud/hytale-portals/packages/ you can find packages for Maven and Gradle.

> [!IMPORTANT]  
> Packages are stored on GitHub Packages. In order to use them, you should read the following:
> - [Working with the Apache Maven registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
> - [Working with the Gradle registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry)

To add it to your Maven project, include the following dependency in your `pom.xml`:

<!-- x-release-please-start-version -->

```xml
<dependency>
  <groupId>network.sloud.hytale</groupId>
  <artifactId>portals</artifactId>
  <version>0.4.7</version>
</dependency>
```

To add it to your Gradle project, include the following dependency in your `build.gradle`:

```groovy
dependencies {
    implementation 'network.sloud.hytale:portals:0.4.7'
}
```

<!-- x-release-please-end -->

## Getting Started

### Creating a Network

To start, you need to create a network that will house your portals:
- **Create:** `/portal network create <name>`
- **List:** `/portal network list` (view all existing networks)
- **Remove:** `/portal network remove <network-name>` (Note: This also removes all portals within that network)

### Creating a Portal

1.  **Initialize:** Run `/portal create <portal-name>` to start the portal creation process.
2.  **Select Network:** Choose the network the portal should belong to.
3.  **Name It:** Provide a unique name for your portal.
4.  **Define Bounds:** Touch two blocks to define the portal's physical area. (Note: These blocks cannot be destroyed until the creation is finished).
5.  **Set Destination:** Stand at the desired exit point, face the direction you want players to arrive at, and run `/portal done`.

## Commands

- `/portal network create <network-name>`: Create a new network.
- `/portal network list`: List all existing networks.
- `/portal network edit <network-name>`: Edit an existing network.
- `/portal network remove <network-name>`: Delete a network and its portals. Has to be confirmed with an optional `--confirm` flag.
- `/portal network <network-name> list`: List all portals within a specific network.
- `/portal create <portal-name>`: Begin the creation of a new portal.
- `/portal cancel`: Cancel the creation of a portal. (Uses the same permission as `/portal create <portal-name>`)
- `/portal done`: Finalize the portal and set its exit point. (Uses the same permission as `/portal create <portal-name>`)

## Contributing

I welcome contributions! Please refer to the [contributing guide](CONTRIBUTING.md) for more details.
