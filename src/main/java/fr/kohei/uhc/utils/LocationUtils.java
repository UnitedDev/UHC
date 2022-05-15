package fr.kohei.uhc.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LocationUtils {

    public static String getArrow(Location from, Location to) {
        if (from == null || to == null)
            return "?";
        if (!from.getWorld().getName().equals(to.getWorld().getName()))
            return "?";
        from.setY(0.0D);
        to.setY(0.0D);
        String[] arrows = {"⬆", "⬈", "➡", "⬊", "⬇", "⬋", "⬅", "⬉", "⬆"};
        Vector d = from.getDirection();
        Vector v = to.subtract(from).toVector().normalize();
        double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
        a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));
        a = ((int) (a + 22.5D) % 360);
        if (a < 0.0D)
            a += 360.0D;
        return arrows[(int) a / 45];
    }

}
