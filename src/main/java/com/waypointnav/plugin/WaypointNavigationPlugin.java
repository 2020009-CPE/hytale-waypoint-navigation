package com.waypointnav.plugin;

import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.logging.HytaleLogger;
import com.hypixel.hytale.plugin.JavaPlugin;
import com.hypixel.hytale.plugin.JavaPluginInit;
import com.waypointnav.plugin.api.WaypointAPI;
import com.waypointnav.plugin.commands.WaypointCommand;
import com.waypointnav.plugin.listeners.PlayerJoinListener;
import com.waypointnav.plugin.listeners.PlayerMoveListener;
import com.waypointnav.plugin.listeners.PlayerQuitListener;
import com.waypointnav.plugin.player.PlayerDataManager;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.rendering.HUDRenderer;
import com.waypointnav.plugin.rendering.WorldRenderer;
import com.waypointnav.plugin.storage.ConfigManager;
import com.waypointnav.plugin.storage.WaypointStorage;
import com.waypointnav.plugin.waypoint.Waypoint;
import com.waypointnav.plugin.waypoint.WaypointManager;
import com.waypointnav.plugin.waypoint.WaypointType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * Main plugin class for the Waypoint Navigation system.
 * Manages all plugin components and provides the public API.
 */
public class WaypointNavigationPlugin extends JavaPlugin implements WaypointAPI {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    private WaypointManager waypointManager;
    private PlayerDataManager playerDataManager;
    private WaypointStorage storage;
    private ConfigManager configManager;
    private HUDRenderer hudRenderer;
    private WorldRenderer worldRenderer;
    
    private WaypointCommand waypointCommand;
    private PlayerJoinListener joinListener;
    private PlayerQuitListener quitListener;
    private PlayerMoveListener moveListener;
    
    private static WaypointNavigationPlugin instance;
    
    /**
     * Plugin constructor.
     *
     * @param init Plugin initialization data
     */
    public WaypointNavigationPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }
    
    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance
     */
    @Nonnull
    public static WaypointNavigationPlugin getInstance() {
        return instance;
    }
    
    /**
     * Plugin setup method called during initialization.
     */
    @Override
    protected void setup() {
        LOGGER.atInfo().log("Initializing Waypoint Navigation Plugin...");
        
        // Initialize data folder
        Path dataFolder = getDataFolder();
        
        // Initialize managers
        this.waypointManager = new WaypointManager();
        this.playerDataManager = new PlayerDataManager();
        this.storage = new WaypointStorage(dataFolder);
        this.configManager = new ConfigManager(dataFolder);
        
        // Initialize renderers
        this.hudRenderer = new HUDRenderer();
        this.worldRenderer = new WorldRenderer();
        
        // Load configuration
        configManager.load();
        
        // Initialize storage
        storage.initialize();
        
        // Register commands
        registerCommands();
        
        // Register event listeners
        registerListeners();
        
        // Start update tasks
        startUpdateTasks();
        
        LOGGER.atInfo().log("Waypoint Navigation Plugin enabled!");
    }
    
    /**
     * Plugin shutdown method called during disable.
     */
    public void onDisable() {
        LOGGER.atInfo().log("Disabling Waypoint Navigation Plugin...");
        
        // Save all player data
        saveAllPlayerData();
        
        // Save configuration
        configManager.save();
        
        LOGGER.atInfo().log("Waypoint Navigation Plugin disabled!");
    }
    
    /**
     * Registers all commands.
     */
    private void registerCommands() {
        this.waypointCommand = new WaypointCommand();
        this.getCommandRegistry().registerCommand(waypointCommand);
    }
    
    /**
     * Registers all event listeners.
     */
    private void registerListeners() {
        this.joinListener = new PlayerJoinListener(this);
        this.quitListener = new PlayerQuitListener(this);
        this.moveListener = new PlayerMoveListener(this);
    }
    
    /**
     * Starts periodic update tasks for rendering.
     */
    private void startUpdateTasks() {
        // Note: Specific scheduler API not yet documented
        // This would use Hytale's World.execute() or scheduler system for periodic tasks
        // to update HUD rendering, world markers, and perform auto-saves
    }
    
    /**
     * Saves all player data asynchronously.
     */
    private void saveAllPlayerData() {
        // Note: Would iterate through online players using Hytale's player manager
        // and save their waypoint data
    }
    
    /**
     * Gets the plugin's data folder.
     *
     * @return The data folder path
     */
    @Nonnull
    private Path getDataFolder() {
        return super.getDataFolder().toPath();
    }
    
    // Getters for managers
    @Nonnull
    public WaypointManager getWaypointManager() { return waypointManager; }
    
    @Nonnull
    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    
    @Nonnull
    public WaypointStorage getStorage() { return storage; }
    
    @Nonnull
    public ConfigManager getConfigManager() { return configManager; }
    
    @Nonnull
    public HUDRenderer getHudRenderer() { return hudRenderer; }
    
    @Nonnull
    public WorldRenderer getWorldRenderer() { return worldRenderer; }
    
    // API Implementation
    
    @Override
    @Nonnull
    public Waypoint createWaypoint(@Nonnull UUID playerUuid, @Nonnull String name,
                                   double x, double y, double z, @Nonnull WaypointType type) {
        return createWaypoint(playerUuid, name, x, y, z, type, 
            configManager.getDouble("waypoint.defaultRadius", 5.0));
    }
    
    @Override
    @Nonnull
    public Waypoint createWaypoint(@Nonnull UUID playerUuid, @Nonnull String name,
                                   double x, double y, double z, 
                                   @Nonnull WaypointType type, double radius) {
        Waypoint waypoint = new Waypoint(name, x, y, z, type);
        waypoint.setCollectionRadius(radius);
        
        PlayerWaypointData playerData = playerDataManager.getOrCreatePlayerData(playerUuid);
        playerData.addWaypoint(waypoint);
        
        return waypoint;
    }
    
    @Override
    public boolean removeWaypoint(@Nonnull UUID playerUuid, @Nonnull UUID waypointId) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        if (playerData == null) return false;
        
        return playerData.getWaypoints().stream()
            .filter(wp -> wp.getId().equals(waypointId))
            .findFirst()
            .map(playerData::removeWaypoint)
            .orElse(false);
    }
    
    @Override
    @Nonnull
    public List<Waypoint> getPlayerWaypoints(@Nonnull UUID playerUuid) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        return playerData != null ? playerData.getWaypoints() : List.of();
    }
    
    @Override
    @Nullable
    public Waypoint getActiveWaypoint(@Nonnull UUID playerUuid) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        return playerData != null ? playerData.getActiveWaypoint() : null;
    }
    
    @Override
    public boolean setActiveWaypoint(@Nonnull UUID playerUuid, int index) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        if (playerData == null) return false;
        
        if (index >= 0 && index < playerData.getWaypoints().size()) {
            playerData.setActiveWaypointIndex(index);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean nextWaypoint(@Nonnull UUID playerUuid) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        return playerData != null && playerData.nextWaypoint();
    }
    
    @Override
    public void completeWaypoint(@Nonnull UUID playerUuid, @Nonnull UUID waypointId) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        if (playerData != null) {
            playerData.completeWaypoint(waypointId);
        }
    }
    
    @Override
    public boolean isWaypointCompleted(@Nonnull UUID playerUuid, @Nonnull UUID waypointId) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        return playerData != null && playerData.isWaypointCompleted(waypointId);
    }
    
    @Override
    public void clearWaypoints(@Nonnull UUID playerUuid) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        if (playerData != null) {
            playerData.clearWaypoints();
        }
    }
    
    @Override
    @Nullable
    public PlayerWaypointData getPlayerData(@Nonnull UUID playerUuid) {
        return playerDataManager.getPlayerData(playerUuid);
    }
    
    @Override
    public void setNavigationEnabled(@Nonnull UUID playerUuid, boolean enabled) {
        PlayerWaypointData playerData = playerDataManager.getOrCreatePlayerData(playerUuid);
        playerData.setNavigationEnabled(enabled);
    }
    
    @Override
    public void setHudEnabled(@Nonnull UUID playerUuid, boolean enabled) {
        PlayerWaypointData playerData = playerDataManager.getOrCreatePlayerData(playerUuid);
        playerData.setHudEnabled(enabled);
    }
    
    @Override
    public void setWorldMarkersEnabled(@Nonnull UUID playerUuid, boolean enabled) {
        PlayerWaypointData playerData = playerDataManager.getOrCreatePlayerData(playerUuid);
        playerData.setWorldMarkersEnabled(enabled);
    }
}
