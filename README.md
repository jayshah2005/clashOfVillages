# Clash of Villages

A Java-based networked strategy game featuring base-building, resource management, and PvP combat mechanics inspired by Clash of Clans.

## Overview

Built with clean software architecture in mind, this project demonstrates advanced object-oriented programming concepts, design patterns, and networked multiplayer capabilities. It serves as a comprehensive example of structuring a scalable Java application.

## Key Features & Architecture

- **Client/Server Multiplayer Model:** Fully implemented networked multiplayer using Java Sockets and a custom packet protocol. The server handles multiple concurrent client connections seamlessly.
- **MVC Architecture:** Strict separation of concerns between the Game State (Model), User Interface (View), and Game Engine / Network Handlers (Controller), ensuring a highly maintainable codebase extensible to new forms of GUI.
- **Factory Design Pattern:** Utilized Factory and polymorphic patterns for dynamic, scalable instantiation of diverse game entities, including `Buildings` (Cannons, Archer Towers, Mines) and `Units` (Archers, Knights, Catapults).
- **Strategic Combat Arbitration:** Deep combat mechanics utilizing custom Arbiters to calculate battle outcomes, loot generation, and defensive success.
- **Game State Persistence:** Reliable save/load mechanics using Java Serialization to preserve player progression, resources, and village layouts securely on the server.

## Tech Stack

- **Language:** Java
- **Architecture & Patterns:** Model-View-Controller (MVC), Factory Pattern, Client-Server Model
- **Networking:** Java Sockets, multi-threading (`ClientHandler`), Custom Protocol
- **Persistence:** Java Object Serialization

## Project Structure

```text
src/
├── GameEngine.java          # Core controller and game loop
├── Network/                 # Client/Server implementation and custom Packet protocol
├── GUI/                     # View layer (Terminal GUI, extensible for graphics)
├── PlayerAccount/           # Model layer (Player stats, Villages, Buildings, Units)
├── ChallengeDecision/       # Game logic, combat arbitration, and match resolution
├── Utility/                 # Core game mechanics, input validation, and adapters
├── enums/                   # Game constants (Units, Buildings, Views)
└── exceptions/              # Custom application-level exception handling
```

## Accomplishments

- Designed a flexible **Client/Server Protocol** to handle continuous bidirectional communication.
- Implemented **15+ entity classes** with complex inheritance hierarchies for buildings and troops.
- Refactored legacy code into a robust **MVC architecture**, drastically improving testability and code navigation.
- Built a **dynamic combat arbitration engine** that precisely calculates fairness and combat interactions.