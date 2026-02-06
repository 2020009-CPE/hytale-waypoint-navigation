# Hytale Waypoint Navigation Plugin

A comprehensive waypoint navigation system for Hytale that provides players with visual guidance to their destinations through HUD displays and world markers.

## Features

### Core Navigation System
- **Waypoint Management**: Create, edit, and delete waypoints with custom names and collection radii
- **Auto-Progression**: Automatically advance to the next waypoint when reaching the current one
- **Multiple Waypoint Types**: Support for block coordinates, NPC entities, specific blocks, custom objectives, and user-defined waypoints
- **Per-Player Tracking**: Each player has their own independent waypoint list and progress

### Visual Guidance
- **HUD Arrow**: On-screen directional arrow pointing toward the active waypoint
- **Distance Display**: Real-time distance calculation and display
- **World Markers**: 3D particle effects showing waypoint locations and direction
- **Customizable Colors**: Distance-based color coding (green = close, yellow = medium, red = far)

### Player Controls
- **Toggle Systems**: Enable/disable navigation, HUD, and world markers independently
- **Skip Functionality**: Skip individual waypoints or all remaining waypoints
- **List View**: See all waypoints with distances and completion status
- **Progress Tracking**: Track completed vs remaining waypoints

### Data Persistence
- **JSON Storage**: Player waypoint data saved to individual JSON files
- **Auto-Save**: Configurable automatic saving at regular intervals
- **Async I/O**: Non-blocking file operations for optimal performance

### Public API
- **Developer-Friendly**: Other plugins can integrate with the waypoint system
- **Complete Control**: Create, modify, and query waypoints programmatically

## Commands

All commands use the `/waypoint` (or `/wp`) prefix:

| Command | Description | Usage |
|---------|-------------|-------|
| `/waypoint add <name> [radius]` | Add a waypoint at your current location | `/waypoint add "Village Center" 10` |
| `/waypoint remove <index>` | Remove a waypoint by its list number | `/waypoint remove 2` |
| `/waypoint clear` | Remove all your waypoints | `/waypoint clear` |
| `/waypoint list` | Display all your waypoints | `/waypoint list` |
| `/waypoint next` | Advance to the next waypoint | `/waypoint next` |
| `/waypoint skip` | Skip the current waypoint | `/waypoint skip` |
| `/waypoint skipall` | Skip all remaining waypoints | `/waypoint skipall` |
| `/waypoint toggle` | Toggle navigation on/off | `/waypoint toggle` |
| `/waypoint toggle-hud` | Toggle HUD display | `/waypoint toggle-hud` |
| `/waypoint toggle-world` | Toggle world markers | `/waypoint toggle-world` |

## Installation

1. Download the latest `WaypointNavigation.jar` from the releases page
2. Place the JAR file in your Hytale server's `plugins` folder
3. Start or restart your server
4. The plugin will create a `plugins/WaypointNavigation` folder with default configuration

## Building from Source

### Prerequisites
- Java 25 or higher
- Gradle 8.0 or higher

### Build Steps
```bash
git clone https://github.com/yourusername/hytale-waypoint-navigation.git
cd hytale-waypoint-navigation
./gradlew shadowJar
```

The compiled plugin will be in `build/libs/WaypointNavigation.jar`

## Configuration

The plugin creates a `config.json` file in `plugins/WaypointNavigation/`:

```json
{
  "hud": {
    "enabled": true,
    "showDistance": true,
    "showCoordinates": true,
    "arrowColor": "YELLOW"
  },
  "world": {
    "enabled": true,
    "particleType": "FLAME",
    "particleCount": 10,
    "updateInterval": 20
  },
  "waypoint": {
    "defaultRadius": 5.0,
    "autoProgress": true,
    "playSound": true,
    "maxWaypoints": 50
  },
  "playerScope": true,
  "autoSave": {
    "enabled": true,
    "interval": 300
  }
}
```

### Configuration Options

#### HUD Settings
- `enabled`: Enable/disable HUD system globally
- `showDistance`: Show distance to waypoint
- `showCoordinates`: Show waypoint coordinates
- `arrowColor`: Color of the directional arrow

#### World Marker Settings
- `enabled`: Enable/disable world markers globally
- `particleType`: Type of particle to use for markers
- `particleCount`: Number of particles per update
- `updateInterval`: Ticks between marker updates

#### Waypoint Settings
- `defaultRadius`: Default collection radius in blocks
- `autoProgress`: Automatically advance to next waypoint
- `playSound`: Play sound when reaching a waypoint
- `maxWaypoints`: Maximum waypoints per player

#### General Settings
- `playerScope`: Per-player waypoints (true) vs global waypoints (false)
- `autoSave.enabled`: Enable automatic data saving
- `autoSave.interval`: Auto-save interval in seconds

## API Usage

Other plugins can use the Waypoint Navigation API:

```java
// Get the API instance
WaypointAPI api = WaypointNavigationPlugin.getInstance();

// Create a waypoint for a player
UUID playerUuid = player.getUniqueId();
Waypoint waypoint = api.createWaypoint(
    playerUuid, 
    "Quest Location", 
    100.0, 64.0, 200.0, 
    WaypointType.CUSTOM_OBJECTIVE,
    10.0 // radius
);

// Get player's active waypoint
Waypoint active = api.getActiveWaypoint(playerUuid);

// Check if waypoint is completed
boolean completed = api.isWaypointCompleted(playerUuid, waypoint.getId());

// Toggle player settings
api.setNavigationEnabled(playerUuid, true);
api.setHudEnabled(playerUuid, true);
```

## Project Structure

```
src/main/java/com/waypointnav/plugin/
├── WaypointNavigationPlugin.java    # Main plugin class
├── api/
│   └── WaypointAPI.java             # Public API interface
├── commands/
│   ├── WaypointCommand.java         # Main command handler
│   └── subcommands/                 # Individual subcommand implementations
├── listeners/
│   ├── PlayerJoinListener.java      # Load player data on join
│   ├── PlayerQuitListener.java      # Save player data on quit
│   └── PlayerMoveListener.java      # Track waypoint completion
├── player/
│   ├── PlayerWaypointData.java      # Player-specific waypoint data
│   └── PlayerDataManager.java       # Manages all player data
├── rendering/
│   ├── HUDRenderer.java             # HUD display rendering
│   └── WorldRenderer.java           # World marker rendering
├── storage/
│   ├── ConfigManager.java           # Configuration management
│   └── WaypointStorage.java         # Data persistence
├── utils/
│   ├── MathUtils.java               # Mathematical calculations
│   └── MessageUtils.java            # Formatted messages
└── waypoint/
    ├── Waypoint.java                # Waypoint data model
    ├── WaypointType.java            # Waypoint type enum
    └── WaypointManager.java         # Global waypoint management
```

## Technical Details

### Waypoint Types
1. **BLOCK_COORDINATES**: Simple coordinate-based waypoint
2. **NPC_ENTITY**: Waypoint targeting an NPC
3. **SPECIFIC_BLOCK**: Waypoint for a specific block type
4. **CUSTOM_OBJECTIVE**: Extensible waypoint for custom conditions
5. **USER_DEFINED**: Standard player-created waypoint

### Rendering System
- **HUD Renderer**: Updates every tick (20 times/second) for smooth arrow movement
- **World Renderer**: Updates every 5 ticks (4 times/second) to reduce particle spam
- **Distance Optimization**: World markers only render within 500 blocks

### Data Storage
- Player data stored in `plugins/WaypointNavigation/playerdata/<uuid>.json`
- Asynchronous save/load operations prevent server lag
- Auto-save system configurable via config file

## Development Notes

This plugin compiles against the official Hytale Server API published to `maven.hytale.com`. No stub classes are needed — the API is fetched automatically by Gradle at build time and provided by the Hytale server at runtime. For more information on Hytale modding, see the [community documentation](https://github.com/HytaleModding/site).

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes with appropriate tests
4. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Check existing documentation
- Review the API javadocs

## Roadmap

- [ ] Waypoint sharing between players
- [ ] Waypoint categories and filtering
- [ ] Custom waypoint icons
- [ ] Minimap integration
- [ ] Waypoint import/export
- [ ] Multi-world support
- [ ] Waypoint beacons with custom colors
- [ ] Quest integration hooks

## Acknowledgments

Built using the official Hytale Plugin Template structure for proper project organization and build configuration.
Hytale Pathfinder
