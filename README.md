# 🏃 RoomRunner

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![JSON](https://img.shields.io/badge/JSON-000000?style=for-the-badge&logo=json&logoColor=white)](https://www.json.org/)
[![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/)

A sophisticated **text-based adventure game engine** built with Java, featuring dynamic content loading, comprehensive game state management, and extensible architecture. This project demonstrates advanced software engineering principles including **design patterns**, **JSON serialization**, and **modular architecture**.

## 🌟 Key Features

- **🎮 Interactive Gameplay Engine**: Command-driven text adventure with room navigation, item management, and puzzle solving
- **📁 Dynamic Content Loading**: JSON-based game world configuration enabling easy content creation and modification
- **💾 Save/Load System**: Complete game state persistence with separate player and world data management
- **🏗️ Modular Architecture**: Clean separation of concerns with well-defined interfaces and abstract classes
- **🎨 Multiple View Support**: Both graphical and text-based user interfaces
- **⚡ Extensible Design**: Interface-driven architecture supporting easy feature additions

## 🛠️ Technical Architecture

### Core Components

| Component         | Description                          | Key Classes                                       |
| ----------------- | ------------------------------------ | ------------------------------------------------- |
| **Engine Driver** | Core game logic and state management | `GameEngineApp`, `GameController`, `GameWorld`    |
| **JSON I/O**      | Serialization and data persistence   | `GameDataLoader`, Custom deserializers            |
| **Entity System** | Game object hierarchy and interfaces | `IIdentifiableEntity`, `IValuable`, `IWeightable` |
| **View Layer**    | User interface abstraction           | `IView`, `GraphicView`, `TextView`                |

### Design Patterns Implemented

- **🏭 Factory Pattern**: Dynamic entity creation from JSON data
- **🔄 Command Pattern**: User input processing and game actions
- **📦 Container Pattern**: Generic entity management and retrieval
- **🎯 Strategy Pattern**: Multiple serialization and view strategies
- **🏛️ MVC Architecture**: Clear separation of model, view, and controller

## 🚀 Getting Started

### Prerequisites

- **Java 11** or higher
- **JUnit 5** (included in `lib/` directory)
- **Jackson JSON Library** (included in `lib/` directory)

### Installation & Setup

```bash
# Clone the repository
git clone https://github.com/thomaszhang2661/RoomRunner.git
cd RoomRunner

# Compile the project
javac -cp "lib/*:src" src/enginedriver/GameEngineApp.java

# Run the game
java -cp "lib/*:src" enginedriver.GameEngineApp
```

### Project Structure

```
RoomRunner/
├── src/
│   ├── enginedriver/     # Core game engine
│   ├── jsonio/          # JSON serialization
│   └── viewer/          # UI components
├── resources/           # Game data and assets
│   ├── players/         # Player save files
│   └── worlds/          # Game world definitions
├── data/               # Sample game scenarios
├── lib/                # External dependencies
└── test/               # Unit tests
```

## 🎯 Game Features

### Core Gameplay Elements

- **🏠 Room Navigation**: Move between interconnected rooms with directional commands
- **🎒 Inventory Management**: Collect, use, and manage items with weight constraints
- **🧩 Puzzle Solving**: Solve puzzles to unlock new areas and progress
- **👹 Monster Encounters**: Combat system with health and damage mechanics
- **🏆 Scoring System**: Point-based progression with valuable items and achievements

### Supported Commands

| Command            | Description                | Example        |
| ------------------ | -------------------------- | -------------- |
| `move <direction>` | Navigate between rooms     | `move north`   |
| `take <item>`      | Pick up items              | `take sword`   |
| `use <item>`       | Use items to solve puzzles | `use key`      |
| `solve <puzzle>`   | Attempt to solve puzzles   | `solve riddle` |
| `look`             | Examine current room       | `look`         |
| `inventory`        | View carried items         | `inventory`    |

## 🔧 Technical Highlights

### Advanced Features

- **🔄 Custom JSON Serialization**: Efficient game state persistence using Jackson annotations
- **🎭 Interface-Driven Design**: Flexible entity system supporting easy extension
- **🧪 Comprehensive Testing**: JUnit test suite covering core functionality
- **📊 UML Documentation**: Complete system design with class and sequence diagrams
- **🎨 Multi-View Architecture**: Support for both graphical and text interfaces

### Code Quality

- **📋 Design Patterns**: Implementation of multiple software engineering patterns
- **🧹 Clean Architecture**: Well-organized codebase with clear separation of concerns
- **📝 Documentation**: Comprehensive JavaDoc and system documentation
- **✅ Testing**: Unit tests for critical components and functionality

## 📈 Development Journey

This project evolved through multiple iterations, incorporating advanced software engineering concepts:

1. **Initial Design**: Basic room navigation and entity management
2. **Interface Integration**: Added `IValuable`, `IWeightable`, and `IProblem` interfaces
3. **Serialization Enhancement**: Implemented custom Jackson serializers/deserializers
4. **Architecture Refinement**: Introduced `EntityContainer` for generic type management
5. **View Layer Addition**: Multi-interface support for different user experiences

## 🎮 Sample Game Scenario

Experience the "Align Quest" adventure:

- Start in the **Dungeon Entrance** with multiple path choices
- Navigate through **layered dungeon** with interconnected rooms
- Solve the **Nobleman's Commission** puzzle to access the Guild
- Battle the **Goblin Monster** to reach the treasure room
- Use strategic item management to overcome obstacles
- Complete objectives to achieve victory!

## 🔮 Future Enhancements

- **🌐 Network Multiplayer**: Support for multiple concurrent players
- **🎨 Enhanced Graphics**: Advanced GUI with animations and effects
- **🔊 Audio System**: Sound effects and background music
- **📱 Mobile Support**: Cross-platform compatibility
- **🛠️ Level Editor**: Visual tool for creating custom game worlds

---

**🎓 Educational Project**: This repository showcases advanced Java programming concepts, software design patterns, and system architecture principles suitable for computer science portfolios and academic demonstration.
