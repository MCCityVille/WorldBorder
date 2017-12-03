package de.mccityville.worldborder.border;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.Map;

public class VerticalLimitedCuboidWorldBorder extends CuboidBorder implements ConfigurationSerializable {

    private final double maxY;

    public VerticalLimitedCuboidWorldBorder(double centerX, double centerZ, double radius, double maxY) {
        super(centerX, centerZ, radius);
        this.maxY = maxY;
    }

    @Override
    public boolean isInBorder(Vector point) {
        return Double.compare(point.getY(), maxY) <= 0 && super.isInBorder(point);
    }

    @Override
    public Vector getIntersection(Vector point) {
        if (Double.compare(point.getY(), maxY) > 0)
            point = point.setY(maxY);
        return point;
    }

    @Override
    public String toString() {
        return "VerticalLimitedCuboid[center=[" + centerX + ", " + centerZ + ", radius=" + radius + ", maxY=" + maxY + "]";
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = super.serialize();
        data.put("max_y", maxY);
        return data;
    }

    @SuppressWarnings("unused")
    public static VerticalLimitedCuboidWorldBorder deserialize(Map<String, Object> data) {
        return new VerticalLimitedCuboidWorldBorder(
                NumberConversions.toDouble(data.get("center_x")),
                NumberConversions.toDouble(data.get("center_z")),
                NumberConversions.toDouble(data.get("radius")),
                NumberConversions.toDouble(data.get("max_y"))
        );
    }
}
