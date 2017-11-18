package de.mccityville.worldborder.border;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class CuboidBorder implements Border, ConfigurationSerializable {

    private final double centerX;
    private final double centerZ;
    private final double radius;

    public CuboidBorder(double centerX, double centerZ, double radius) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.radius = radius;
    }

    @Override
    public void onAttach(Plugin plugin, World world) {
        WorldBorder border = world.getWorldBorder();
        border.setCenter(centerX, centerZ);
        border.setSize((radius * 2) + 1);
    }

    @Override
    public boolean isInBorder(Vector point) {
        return Double.compare(Math.abs(centerX - point.getX()), radius) <= 0 &&
                Double.compare(Math.abs(centerZ - point.getZ()), radius) <= 0;
    }

    @Override
    public Vector getIntersection(Vector point) {
        double x = point.getX() - centerX;
        double z = point.getZ() - centerZ;
        double u = Math.max(Math.abs(x), Math.abs(z));
        return new Vector(centerX + ((x / u) * radius), point.getY(), centerZ + ((z / u) * radius));
    }

    @Override
    public String toString() {
        return "Cuboid[center=[" + centerX + ", " + centerZ + ", radius=" + radius + "]";
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("center_x", centerX);
        data.put("center_z", centerZ);
        data.put("radius", radius);
        return data;
    }

    @SuppressWarnings("unused")
    public static CuboidBorder deserialize(Map<String, Object> data) {
        return new CuboidBorder(
                NumberConversions.toDouble(data.get("center_x")),
                NumberConversions.toDouble(data.get("center_z")),
                NumberConversions.toDouble(data.get("radius"))
        );
    }
}
