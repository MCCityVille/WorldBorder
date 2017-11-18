package de.mccityville.worldborder.border;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public interface Border {

    default void onAttach(Plugin plugin, World world) {
    }

    default void onJoin(Plugin plugin, World world, Player player) {
    }

    boolean isInBorder(Vector point);

    Vector getIntersection(Vector point);
}
