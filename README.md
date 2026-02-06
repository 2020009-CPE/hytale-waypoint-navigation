# Hytale Waypoint Navigation Plugin

A comprehensive waypoint navigation system for Hytale that provides players with automatic visual guidance to their destinations through HUD displays and world markers.

## Features

### Core Navigation System
- **Automatic Navigation**: Waypoint navigation is always enabled by default and automatically applied to players. Navigation can only be disabled via configuration.
- **Waypoint Management**: Create, edit, and delete waypoints with custom names, priorities, and collection radii.
- **Priority-Based Targeting**: Waypoints use priority values to determine navigation order (lower number = higher priority).
- **Auto-Progression**: Automatically advance to the next valid waypoint when reaching the current one.
- **Multiple Waypoint Types**: Support for player position ("HERE" mode), world coordinates, NPC entities, specific blocks, custom objectives, and user-defined waypoints.
- **Per-Player Tracking**: Each player has their own independent waypoint list and progress.

### Visual Guidance
- **HUD Arrow**: On-screen directional arrow pointing toward the active waypoint.
- **Distance Display**: Real-time distance calculation and display.
- **World Markers**: Tall beacon columns with rotating rings and dense particle trails for maximum visibility.
- **Customizable Colors**: Distance-based color coding (green = close, yellow = medium, red = far).

### Player Controls
- **Automatic Activation**: Navigation starts automatically when waypoints exist.
- **Skip Functionality**: Skip individual waypoints or all remaining waypoints.
- **List View**: View all waypoints with priority, distance, and completion status.
- **Progress Tracking**: Track completed vs remaining waypoints.

### Data Persistence
- **JSON Storage**: Player waypoint data saved to individual JSON files.
- **Auto-Save**: Configurable automatic saving at regular intervals.
- **Async I/O**: Non-blocking file operations for optimal performance.

### Public API
- **Developer-Friendly**: Other plugins can integrate with the waypoint system.
- **Complete Control**: Create, modify, and query waypoints programmatically.

## How Navigation Works (Simple Behavior)

1. Player joins the server.
2. Waypoint data is loaded from JSON.
3. Navigation is **automatically enabled**.
4. The arrow targets the **highest-priority** incomplete waypoint.
5. When the player enters the waypoint's radius:
   - Waypoint is automatically completed.
   - Arrow immediately switches to the next priority waypoint.
6. When no waypoints remain, navigation visuals are hidden automatically.

## Commands (Admin Only)

⚠ **All waypoint commands are restricted to admins only.** Players without permission cannot create, modify, or remove waypoints.

**Required Permission:** `waypoint.admin`

All commands use the `/waypoint` (or `/wp`) prefix:

| Command | Description | Usage |
|---------|-------------|-------|
| `/waypoint add here <name> [priority] [radius]` | Add a waypoint at your current position | `/waypoint add here "Town Square" 0 10` |
| `/waypoint add <name> <x> <y> <z> [priority] [radius]` | Add a waypoint at specific coordinates | `/waypoint add "Camp" 100 64 200 1 5` |
| `/waypoint remove <index>` | Remove a waypoint by its list number | `/waypoint remove 2` |
| `/waypoint clear` | Remove all your waypoints | `/waypoint clear` |
| `/waypoint list` | Display all waypoints with priority | `/waypoint list` |
| `/waypoint next` | Advance to the next waypoint | `/waypoint next` |
| `/waypoint skip` | Skip the current waypoint | `/waypoint skip` |
| `/waypoint skipall` | Skip all remaining waypoints | `/waypoint skipall` |
| `/waypoint toggle` | Toggle navigation on/off | `/waypoint toggle` |
| `/waypoint toggle-hud` | Toggle HUD display | `/waypoint toggle-hud` |
| `/waypoint toggle-world` | Toggle world markers | `/waypoint toggle-world` |
| `/waypoint ui` | Open visual management panel | `/waypoint ui` |
| `/waypoint help` | Show step-by-step admin guide | `/waypoint help` |

### Priority System

- Lower number = higher priority (processed first).
- Default priority is `0`.
- When a waypoint is completed, the system automatically targets the next highest-priority incomplete waypoint.
- Example: Priority 0 waypoints are visited before priority 1, which are visited before priority 2, etc.

## Quick Start Tutorial (Admin)

**Step 1:** Add a waypoint at your current position:
```
/waypoint add here "Town Square" 0 10
```
This saves your position with priority 0 and a 10-block collection radius.

**Step 2:** Add waypoints at specific coordinates:
```
/waypoint add "Forest Camp" 100 64 200 1
/waypoint add "Mountain Peak" 300 100 400 2 15
```

**Step 3:** View all your waypoints and see priorities/distances:
```
/waypoint list
```

**Step 4:** Navigate! Follow the tall particle beacon and HUD arrow toward each waypoint. When you walk within the collection radius, the waypoint auto-completes and the system advances to the next priority waypoint.

**Need help in-game?** Run `/waypoint help` for the full admin guide.

## Visual UI Panel

Run `/waypoint ui` to open an interactive management panel (requires `waypoint.admin` permission).

The panel provides:
- **Status display** — Shows the current navigation target and priority
- **Add waypoint form** — Name, priority, and radius fields with an "Add Here" button
- **Waypoint list** — All waypoints with priority, distance, and completion status
- **Click to remove** — Click any waypoint in the list to remove it
- **Action buttons** — Skip Current, Skip All, Clear All, and Close

This is a native Hytale custom UI page (not chat-based). It uses `InteractiveCustomUIPage` with `.ui` layout files located in `Common/UI/Custom/WaypointNavigation/`.

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
    "particleCount": 20,
    "updateInterval": 2
  },
  "waypoint": {
    "defaultRadius": 5.0,
    "autoProgress": true,
    "playSound": true,
    "maxWaypoints": 50
  },
  "navigation": {
    "alwaysEnabled": true
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
- `updateInterval`: Ticks between marker updates (lower = more frequent, default 2)

#### Waypoint Settings
- `defaultRadius`: Default collection radius in blocks
- `autoProgress`: Automatically advance to next waypoint
- `playSound`: Play sound when reaching a waypoint
- `maxWaypoints`: Maximum waypoints per player

#### Navigation Settings
- `alwaysEnabled`: When true, navigation is always forced on for all players (default: true). Set to false to allow per-player toggle.

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

// Set priority (lower = higher priority)
waypoint.setPriority(1);

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
├── ui/
│   └── WaypointPage.java            # Interactive UI page handler
├── storage/
│   ├── ConfigManager.java           # Configuration management
│   └── WaypointStorage.java         # Data persistence
├── utils/
│   ├── MathUtils.java               # Mathematical calculations
│   └── MessageUtils.java            # Formatted messages
└── waypoint/
    ├── Waypoint.java                # Waypoint data model (with priority)
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
- **World Renderer**: Updates every 2 ticks (10 times/second) with dense particle effects
- **Beacon Column**: 20-block tall vertical particle column at waypoint location
- **Particle Trail**: Up to 20 particles from player toward waypoint at 1.5-block spacing
- **Rotating Ring**: 16-point circular particle ring at beacon base
- **Distance Optimization**: World markers only render within 500 blocks

### Data Storage
- Player data stored in `plugins/WaypointNavigation/playerdata/<uuid>.json`
- Asynchronous save/load operations prevent server lag
- Auto-save system configurable via config file

## Development Notes

This plugin compiles against the official Hytale Server API published to `maven.hytale.com`. No stub classes are needed — the API is fetched automatically by Gradle at build time and provided by the Hytale server at runtime. For more information on Hytale modding, see the [community documentation](https://github.com/HytaleModding/site).

### Text Formatting

Hytale does not support Minecraft-style color codes (`§` section-sign codes or `&` ampersand codes). All chat messages use plain text. When Hytale's native text styling API becomes available, the formatting constants in `MessageUtils.java` can be updated.

### Debug Logging

The plugin includes log messages throughout the rendering pipeline, player listeners, and plugin lifecycle. These messages appear in the server console and help diagnose issues such as:

- Particles not appearing (check for `Particle spawn requested` messages at FINE level)
- HUD not rendering (check for `HUD disabled` or `No active waypoint` messages at FINE level)
- Player data not loading/saving (check for `Player ... joined` messages at INFO level)
- Waypoint completion not triggering (check for `Player ... reached waypoint` messages at INFO level)

Info-level messages (plugin setup, player join/quit, waypoint completion) are always visible. Fine-level messages (per-tick rendering and particle details) require the logging level to be set to FINE or lower.

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
