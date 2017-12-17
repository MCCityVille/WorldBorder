package de.mccityville.worldborder;

import de.mccityville.worldborder.border.CuboidBorder;
import de.mccityville.worldborder.border.VerticalLimitedCuboidWorldBorder;
import de.mccityville.worldborder.commands.BorderReloadCommand;
import de.mccityville.worldborder.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldBorderPlugin extends JavaPlugin {

    private final BorderManager borderManager = new BorderManager(this);

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(CuboidBorder.class);
        ConfigurationSerialization.registerClass(VerticalLimitedCuboidWorldBorder.class);

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(borderManager, this, this::initializeBorderManager), this);
        getCommand("borderreload").setExecutor(new BorderReloadCommand(this));
    }

    public void reloadBorderManager() {
        reloadConfig();
        initializeBorderManager();
    }

    private void initializeBorderManager() {
        borderManager.load(getConfig().getConfigurationSection("borders"));
    }
}
