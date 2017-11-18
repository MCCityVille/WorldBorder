package de.mccityville.worldborder.utils;

import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayerConversionUtils {

    private PlayerConversionUtils() {
    }

    public static void playSound(Player player, ConfigurationSection config) {
        if (config == null)
            return;
        String name = config.getString("name");
        float pitch = (float) config.getDouble("pitch");
        float volume = (float) config.getDouble("volume");
        SoundCategory category = SoundCategory.valueOf(config.getString("category", SoundCategory.MASTER.name()));
        player.playSound(player.getLocation(), name, category, volume, pitch);
    }

    public static void sendMessage(Player player, String message) {
        if (message != null && !message.isEmpty())
            player.sendMessage(message);
    }
}
