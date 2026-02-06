# Hytale API Stub Classes

## Overview

This directory contains stub implementations of the Hytale API classes required for compilation. These stubs are temporary placeholders until the official Hytale API is released by Hypixel.

## Why Stubs Are Needed

The project is designed to use the official Hytale Plugin API (`com.hypixel.hytale:hytale-api`), but this API has not been released yet. Without these stub classes, the project would not compile.

## Stub Classes Included

The following Hytale API packages and classes are stubbed:

### Plugin Framework
- `com.hypixel.hytale.plugin.JavaPlugin` - Base plugin class
- `com.hypixel.hytale.plugin.JavaPluginInit` - Plugin initialization

### Logging
- `com.hypixel.hytale.logging.HytaleLogger` - Logging system

### Command System
- `com.hypixel.hytale.command.AbstractCommandCollection` - Command collection base
- `com.hypixel.hytale.command.AbstractPlayerCommand` - Player command base
- `com.hypixel.hytale.command.CommandContext` - Command execution context
- `com.hypixel.hytale.command.argument.RequiredArg` - Required command argument
- `com.hypixel.hytale.command.argument.OptionalArg` - Optional command argument
- `com.hypixel.hytale.command.argument.type.ArgTypes` - Argument type definitions

### Event System
- `com.hypixel.hytale.event.EventHandler` - Event handler annotation
- `com.hypixel.hytale.event.player.PlayerReadyEvent` - Player ready event
- `com.hypixel.hytale.event.player.PlayerDisconnectEvent` - Player disconnect event

### Entity Component System (ECS)
- `com.hypixel.hytale.entity.Player` - Player component
- `com.hypixel.hytale.entity.PlayerRef` - Player reference
- `com.hypixel.hytale.entity.component.TransformComponent` - Position/transform component
- `com.hypixel.hytale.store.Store` - Component store
- `com.hypixel.hytale.store.Ref` - Entity reference
- `com.hypixel.hytale.store.EntityStore` - Entity store
- `com.hypixel.hytale.store.ComponentType` - Component type definition

### Math & Utilities
- `com.hypixel.hytale.math.Vec3d` - 3D vector
- `com.hypixel.hytale.message.Message` - Message system
- `com.hypixel.hytale.permission.GameMode` - Game mode enum
- `com.hypixel.hytale.world.World` - World representation

## Important Notes

### Limitations
1. **Stub implementations are minimal** - They provide just enough functionality to compile the code
2. **No actual functionality** - Most methods are empty or return null/default values
3. **Threading behavior differs** - The `World.execute()` stub runs synchronously, while the real API would be asynchronous
4. **No validation** - Stubs don't validate inputs or enforce constraints

### Development Implications
- The code will compile but won't run as a working plugin until the real API is available
- Testing is limited to compilation and static analysis
- Some runtime issues may not be discoverable until the real API is available

## Migration to Official API

When Hypixel releases the official Hytale API:

### Step 1: Remove Stub Classes
Delete the entire `src/main/java/com/hypixel/hytale/` directory:
```bash
rm -rf src/main/java/com/hypixel/hytale/
```

### Step 2: Update build.gradle.kts
Uncomment the official dependency in `build.gradle.kts`:
```kotlin
dependencies {
    // Uncomment when official API is released:
    compileOnly("com.hypixel.hytale:hytale-api:+")
    
    // Remove this comment about stubs
    
    // Keep other dependencies
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}
```

### Step 3: Verify Compilation
```bash
./gradlew clean build
```

### Step 4: Update Code if Needed
The official API may have different method signatures or behavior. Review compilation errors and update code accordingly.

### Step 5: Test Thoroughly
Since the stubs were minimal, test all functionality with the real API to ensure everything works as expected.

## Build Configuration

Current `build.gradle.kts` configuration:
- **Hytale API**: Using local stub classes (no external dependency)
- **Gson**: `2.10.1` for JSON processing
- **JSR-305**: `3.0.2` for `@Nonnull`/`@Nullable` annotations (compile-only)

## Support

For questions or issues related to:
- **Stub implementation**: Open an issue in this repository
- **Official Hytale API**: Refer to Hypixel/Hytale official documentation when available

## License

These stub classes are provided as-is for development purposes only. They will be replaced by the official Hytale API when it becomes available.
