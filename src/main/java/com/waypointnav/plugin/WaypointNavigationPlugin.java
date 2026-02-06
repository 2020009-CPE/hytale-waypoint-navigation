package com.waypointnav.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.DrainPlayerFromWorldEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
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
    public WaypointNavigationPlugin(JavaPluginInit init) {
        super(init);
        instance = this;
    }
    
    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance
     */
    public static WaypointNavigationPlugin getInstance() {
        return instance;
    }
    
    /**
     * Plugin setup method called during initialization.
     */
    @Override
    protected void setup() {
        LOGGER.info("Initializing Waypoint Navigation Plugin...");
        
        // Initialize data folder
        Path dataFolder = getDataDirectory();
        LOGGER.info("Data directory: %s", dataFolder);
        
        // Initialize managers
        this.waypointManager = new WaypointManager();
        this.playerDataManager = new PlayerDataManager();
        this.storage = new WaypointStorage(dataFolder);
        this.configManager = new ConfigManager(dataFolder);
        LOGGER.info("Managers initialized.");
        
        // Initialize renderers
        this.hudRenderer = new HUDRenderer();
        this.worldRenderer = new WorldRenderer();
        LOGGER.info("Renderers initialized (HUD + World markers).");
        
        // Load configuration
        configManager.load();
        LOGGER.info("Configuration loaded.");
        
        // Initialize storage
        storage.initialize();
        LOGGER.info("Storage system initialized.");
        
        // Register commands
        registerCommands();
        LOGGER.info("Commands registered (including /waypoint help).");
        
        // Register event listeners
        registerListeners();
        LOGGER.info("Event listeners registered.");
        
        // Start update tasks
        startUpdateTasks();
        LOGGER.info("Update tasks started.");
        
        LOGGER.info("Waypoint Navigation Plugin enabled!");
    }
    
    /**
     * Plugin shutdown method called during disable.
     */
    public void onDisable() {
        LOGGER.info("Disabling Waypoint Navigation Plugin...");
        
        // Save all player data
        saveAllPlayerData();
        
        // Save configuration
        configManager.save();
        
        LOGGER.info("Waypoint Navigation Plugin disabled!");
    }
    
    /**
     * Registers all commands.
     */
    private void registerCommands() {
        this.waypointCommand = new WaypointCommand();
        this.getCommandRegistry().registerCommand(waypointCommand);
    }
    
    /**
     * Registers all event listeners using the event registry.
     */
    private void registerListeners() {
        this.joinListener = new PlayerJoinListener(this);
        this.quitListener = new PlayerQuitListener(this);
        this.moveListener = new PlayerMoveListener(this);

        this.getEventRegistry().registerGlobal(AddPlayerToWorldEvent.class, event -> {
            PlayerRef playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());
            if (playerRef != null) {
                joinListener.onPlayerJoin(playerRef);
            }
        });

        this.getEventRegistry().registerGlobal(DrainPlayerFromWorldEvent.class, event -> {
            PlayerRef playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());
            if (playerRef != null) {
                quitListener.onPlayerQuit(playerRef);
            }
        });
    }
    
    /**
     * Starts periodic update tasks for rendering.
     */
    private void startUpdateTasks() {
        // Note: Specific scheduler API not yet documented
        // This would use Hytale's World.execute() or scheduler system for periodic tasks
        // to update HUD rendering, world markers, and perform auto-saves
        LOGGER.info("Periodic update tasks not yet started - awaiting Hytale scheduler API.");
    }
    
    /**
     * Saves all player data asynchronously.
     */
    private void saveAllPlayerData() {
        LOGGER.info("Saving all player data on shutdown...");
        // Note: Would iterate through online players using Hytale's player manager
        // and save their waypoint data
    }
    
    // Getters for managers

    public WaypointManager getWaypointManager() { return waypointManager; }
    

    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    

    public WaypointStorage getStorage() { return storage; }
    

    public ConfigManager getConfigManager() { return configManager; }
    

    public HUDRenderer getHudRenderer() { return hudRenderer; }
    

    public WorldRenderer getWorldRenderer() { return worldRenderer; }
    
    // API Implementation
    
    @Override

    public Waypoint createWaypoint(UUID playerUuid, String name,
                                   double x, double y, double z, WaypointType type) {
        return createWaypoint(playerUuid, name, x, y, z, type, 
            configManager.getDouble("waypoint.defaultRadius", 5.0));
    }
    
    @Override

    public Waypoint createWaypoint(UUID playerUuid, String name,
                                   double x, double y, double z, 
                                   WaypointType type, double radius) {
        Waypoint waypoint = new Waypoint(name, x, y, z, type);
        waypoint.setCollectionRadius(radius);
        
        PlayerWaypointData playerData = playerDataManager.getOrCreatePlayerData(playerUuid);
        playerData.addWaypoint(waypoint);
        
        return waypoint;
    }
    
    @Override
    public boolean removeWaypoint(UUID playerUuid, UUID waypointId) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        if (playerData == null) return false;
        
        return playerData.getWaypoints().stream()
            .filter(wp -> wp.getId().equals(waypointId))
            .findFirst()
            .map(playerData::removeWaypoint)
            .orElse(false);
    }
    
    @Override

    public List<Waypoint> getPlayerWaypoints(UUID playerUuid) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        return playerData != null ? playerData.getWaypoints() : List.of();
    }
    
    @Override

    public Waypoint getActiveWaypoint(UUID playerUuid) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        return playerData != null ? playerData.getActiveWaypoint() : null;
    }
    
    @Override
    public boolean setActiveWaypoint(UUID playerUuid, int index) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        if (playerData == null) return false;
        
        if (index >= 0 && index < playerData.getWaypoints().size()) {
            playerData.setActiveWaypointIndex(index);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean nextWaypoint(UUID playerUuid) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        return playerData != null && playerData.nextWaypoint();
    }
    
    @Override
    public void completeWaypoint(UUID playerUuid, UUID waypointId) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        if (playerData != null) {
            playerData.completeWaypoint(waypointId);
        }
    }
    
    @Override
    public boolean isWaypointCompleted(UUID playerUuid, UUID waypointId) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        return playerData != null && playerData.isWaypointCompleted(waypointId);
    }
    
    @Override
    public void clearWaypoints(UUID playerUuid) {
        PlayerWaypointData playerData = playerDataManager.getPlayerData(playerUuid);
        if (playerData != null) {
            playerData.clearWaypoints();
        }
    }
    
    @Override

    public PlayerWaypointData getPlayerData(UUID playerUuid) {
        return playerDataManager.getPlayerData(playerUuid);
    }
    
    @Override
    public void setNavigationEnabled(UUID playerUuid, boolean enabled) {
        PlayerWaypointData playerData = playerDataManager.getOrCreatePlayerData(playerUuid);
        playerData.setNavigationEnabled(enabled);
    }
    
    @Override
    public void setHudEnabled(UUID playerUuid, boolean enabled) {
        PlayerWaypointData playerData = playerDataManager.getOrCreatePlayerData(playerUuid);
        playerData.setHudEnabled(enabled);
    }
    
    @Override
    public void setWorldMarkersEnabled(UUID playerUuid, boolean enabled) {
        PlayerWaypointData playerData = playerDataManager.getOrCreatePlayerData(playerUuid);
        playerData.setWorldMarkersEnabled(enabled);
    }
}
