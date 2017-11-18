package de.mccityville.worldborder;

import de.mccityville.worldborder.border.CuboidBorder;
import de.mccityville.worldborder.commands.BorderReloadCommand;
import de.mccityville.worldborder.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldBorderPlugin extends JavaPlugin {

    private final BorderManager borderManager = new BorderManager(this);

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(CuboidBorder.class);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        initializeBorderManager();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(borderManager, this), this);
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