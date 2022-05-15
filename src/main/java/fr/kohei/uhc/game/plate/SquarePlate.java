package fr.kohei.uhc.game.plate;

import fr.kohei.utils.Cuboid;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

@Getter
public class SquarePlate {

    private final Location teleportLocation;

    private final int size;

    private final Material material;

    private final byte data;

    private final Location loc1;

    private final Location loc2;

    public SquarePlate(Location center, int size, Material material, int data) {
        this.teleportLocation = center;
        this.size = size + (size + 1) % 2;
        this.material = material;
        this.data = (byte) data;
        double a = size / 2.0D;
        Location loc1 = center.clone().add(a, 0.0D, a);
        Location loc2 = center.clone().add(-a, 0.0D, -a);
        World world = center.getWorld();
        int x1 = loc1.getBlockX();
        int z1 = loc1.getBlockZ();
        int x2 = loc2.getBlockX();
        int z2 = loc2.getBlockZ();
        int y = center.getBlockY();
        this.loc1 = new Location(world, ((Math.min(x1, x2)) - size), y, ((Math.min(z1, z2)) - size));
        this.loc2 = new Location(world, ((Math.max(x1, x2)) + size), y, ((Math.max(z1, z2)) + size));
    }

    public void build() {
        fill(this.loc1, this.loc2, this.material, this.data);
    }

    public void destroy() {
        fill(this.loc1, this.loc2, Material.AIR, (byte) 0);
    }

    public static void fill(Location loc1, Location loc2, Material material, byte data) {
        Cuboid floor = new Cuboid(loc1, loc2);
        floor.getBlockList().forEach(b -> b.setTypeIdAndData(material.getId(), data, true));
    }
}
