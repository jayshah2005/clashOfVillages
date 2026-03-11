# Clash of Villages

A Java-based strategy game featuring base-building, resource management, and PvP combat mechanics inspired by Clash of Clans.

## Features

- **Dynamic Village Management** – Construct and upgrade defensive structures (towers, cannons, walls) and production buildings (farms, mines, lumber mills)
- **Strategic Combat System** – Train diverse unit types (archers, knights, catapults, soldiers) with unique stats and costs
- **Resource Economy** – Manage three resource types across gathering, training, and construction with persistent progression
- **Multiplayer Attacks** – Launch coordinated raids against other players with calculated loot systems
- **Game State Persistence** – Serialization-based save/load system for player data and progress
- **Dual Interface** – Terminal-based GUI with extensible architecture for multiple interface implementations

## Tech Stack

- **Language:** Java
- **Architecture:** Object-oriented design with MVC patterns
- **Persistence:** Java Serialization
- **Key Concepts:** Inheritance hierarchies, polymorphism, state management, exception handling

## Project Structure

```
src/
├── GameEngine.java          # Core game loop and rule engine
├── GUI/                     # Interface layer (Terminal & extensible to graphical)
├── PlayerAccount/           # Player management, villages, resources
├── enums/                   # Game entities (units, buildings, views)
├── exceptions/              # Custom exception handling
└── Utility/                 # Game arbitration and input validation
```

## Key Accomplishments

- Implemented **15+ entity classes** with complex inheritance for buildings and units
- Built **game arbitration system** for combat resolution and fairness
- Designed **resource economy** with production rates, costs, and loot mechanics
- Created **save/load functionality** with full game state serialization