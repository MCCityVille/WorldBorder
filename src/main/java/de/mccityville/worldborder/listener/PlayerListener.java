package de.mccityville.worldborder.listener;

import de.mccityville.worldborder.BorderManager;
import de.mccityville.worldborder.border.Border;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Objects;

public class PlayerListener implements Listener {

    private final BorderManager borderManager;
    private final Plugin plugin;
    private final Runnable initializer;
    private boolean initialized;

    public PlayerListener(BorderManager borderManager, Plugin plugin, Runnable initializer) {
        this.borderManager = Objects.requireNonNull(borderManager, "borderManager must not be null");
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null");
        this.initializer = Objects.requireNonNull(initializer, "initializer must not be null");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            Border border = borderManager.getBorder(to.getWorld().getName());
            Vector toVector = to.toVector();
            if (border != null && !border.isInBorder(toVector)) {
                Player player = event.getPlayer();
                event.setCancelled(true);
                borderManager.notifyForBorder(player);
                if (!border.isInBorder(from.toVector())) {
                    Vector correction = border.getIntersection(toVector);
                    if (correction != null) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(correction.toLocation(to.getWorld(), to.getYaw(), to.getPitch())), 1);
                    } else {
                        player.teleport(to.getWorld().getSpawnLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        triggerJoinPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        borderManager.removeNotificationCooldown(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        borderManager.removeNotificationCooldown(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        triggerJoinPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location to = event.getTo();
        Border border = borderManager.getBorder(to.getWorld());
        if (border != null && !border.isInBorder(to.toVector())) {
            event.setCancelled(true);
            borderManager.notifyForBorder(event.getPlayer());
        }
    }

    private void triggerJoinPlayer(Player player) {
        if (!initialized) {
            initialized = true;
            initializer.run();
        }

        World world = player.getWorld();
        Border border = borderManager.getBorder(world);
        if (border != null)
            border.onJoin(plugin, world, player);
    }
}
