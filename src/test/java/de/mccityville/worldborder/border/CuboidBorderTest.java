package de.mccityville.worldborder.border;

import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.Test;

public class CuboidBorderTest {

    @Test
    public void testIsInBorder() {
        Border border = createTestBorder();
        Assert.assertTrue(border.isInBorder(new Vector(10.0, 255.0, 10.0)));
        Assert.assertTrue(border.isInBorder(new Vector(20.0, 255.0, 20.0)));
        Assert.assertTrue(border.isInBorder(new Vector(-10.0, 255.0, -10.0)));
        Assert.assertFalse(border.isInBorder(new Vector(-15.0, 255.0, -15.0)));
    }

    @Test
    public void testGetIntersection() {
        Border border = createTestBorder();
        Assert.assertEquals(new Vector(-10.0, 255.0, -10.0), border.getIntersection(new Vector(-50.0, 255.0, -50.0)));
    }

    private static Border createTestBorder() {
        return new CuboidBorder(10.0, 10.0, 20.0);
    }
}
