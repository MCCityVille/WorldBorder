package de.mccityville.worldborder;

import de.mccityville.worldborder.border.Border;
import de.mccityville.worldborder.utils.PlayerConversionUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class BorderManager {

    private static final long NOTIFICATION_COOLDOWN = TimeUnit.SECONDS.toMillis(10);

    private final Map<String, Border> borders = new HashMap<>();
    private final Map<UUID, Long> lastNotifications = new HashMap<>();
    private final Plugin plugin;

    public BorderManager(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null");
    }

    public void setBorder(World world, Border border) {
        Objects.requireNonNull(world, "world must not be null");
        Objects.requireNonNull(border, "border must not be null");
        String worldName = world.getName();
        borders.put(worldName, border);
        border.onAttach(plugin, world);
        world.getPlayers().forEach(p -> border.onJoin(plugin, world, p));
        plugin.getLogger().info("World border for world " + worldName + " set: " + border);
    }

    public void setBorder(String worldName, Border border) {
        Objects.requireNonNull(worldName, "worldName must not be null");
        Objects.requireNonNull(border, "border must not be null");
        World world = Bukkit.getWorld(worldName);
        if (world == null)
            throw new UnsupportedOperationException("Unknown world: " + worldName);
        setBorder(world, border);
    }

    public void removeBorder(String worldName) {
        boolean removed = borders.remove(worldName) != null;
        if (removed)
            plugin.getLogger().info("World border for world " + worldName + " removed");
    }

    public void removeBorder(World world) {
        removeBorder(world.getName());
    }

    public Border getBorder(World world) {
        Objects.requireNonNull(world, "world must not be null");
        return getBorder(world.getName());
    }

    public Border getBorder(String worldName) {
        Objects.requireNonNull(worldName, "worldName must not be null");
        return borders.get(worldName);
    }

    public void load(ConfigurationSection config) {
        Set<String> remaining = new HashSet<>(borders.keySet());
        if (config != null) {
            for (Map.Entry<String, Object> entry : config.getValues(false).entrySet()) {
                String worldName = entry.getKey();
                remaining.remove(worldName);
                try {
                    setBorder(worldName, (Border) entry.getValue());
                } catch (Throwable throwable) {
                    plugin.getLogger().log(Level.SEVERE, "Error while loading world border for world " + worldName, throwable);
                }
            }
        }
        for (String name : remaining)
            removeBorder(name);
    }

    public void notifyForBorder(Player player) {
        Long lastNotification = lastNotifications.get(player.getUniqueId());
        long now = System.currentTimeMillis();
        if (lastNotification == null || now - lastNotification > NOTIFICATION_COOLDOWN) {
            lastNotifications.put(player.getUniqueId(), now);
            PlayerConversionUtils.playSound(player, plugin.getConfig().getConfigurationSection("limits.sound"));
            PlayerConversionUtils.sendMessage(player, plugin.getConfig().getString("limits.message"));
        }
    }

    public void removeNotificationCooldown(Player player) {
        lastNotifications.remove(player.getUniqueId());
    }
}
