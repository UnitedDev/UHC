package fr.kohei.uhc.utils.world;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

public class Cuboid {

    private String identifier, name;
    private World world;
    private int x, y, z, radiusX, radiusZ;
    private float yaw, pitch;
    private boolean rounded, wrapping;
    private double maxX, minX, maxZ, minZ, radiusXSquared, radiusZSquared, definiteRectangleX, definiteRectangleZ, radiusSquaredQuotient;
    private List<SubCuboid> subCuboids;

    public Cuboid(String identifier, String name, World world, int x, int y, int z, int radiusX, int radiusZ, float yaw, float pitch, boolean rounded, boolean wrapping) {
        this.identifier = identifier;
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radiusX = radiusX;
        this.radiusZ = radiusZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.rounded = rounded;
        this.wrapping = wrapping;
        this.sequenceSubCuboids();
    }

    private void sequenceSubCuboids() {
        this.subCuboids = Lists.newArrayList();
        this.subCuboids.add(new SubCuboid(this.world, (this.x - this.radiusX), this.z, this.radiusX, this.radiusZ));
        this.subCuboids.add(new SubCuboid(this.world, this.z, this.z, this.radiusX, this.radiusZ));
        this.subCuboids.add(new SubCuboid(this.world, (this.x - this.radiusX), (this.z - this.radiusZ), this.radiusX, this.radiusZ));
        this.subCuboids.add(new SubCuboid(this.world, this.x, (this.z - this.radiusZ), this.radiusX, this.radiusZ));
    }

    public List<SubCuboid> getSubCuboids() {
        return this.subCuboids;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getName() {
        return this.name;
    }

    public World getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
        this.maxZ = z + this.radiusZ;
        this.minZ = z - this.radiusZ;
    }

    public int getRadius() {
        return (this.radiusX + this.radiusZ) / 2;
    }

    public int getRadiusX() {
        return this.radiusX;
    }

    public int getRadiusZ() {
        return this.radiusZ;
    }

    public boolean isRounded() {
        return this.rounded;
    }

    public boolean isWrapped() {
        return this.wrapping;
    }

    public boolean contains(Location loc, boolean round) {
        if (!loc.getWorld().equals(this.world)) return false;
        return this.contains(loc.getX(), loc.getZ(), round);
    }

    public boolean contains(double x, double z, boolean round) {
        if (!round)
            return (x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ);
        double dX = Math.abs(this.x - x);
        double dZ = Math.abs(this.z - z);
        if (dX < this.definiteRectangleX && z < this.definiteRectangleZ)
            return true;
        if (dX >= this.radiusX || z >= this.radiusZ)
            return false;
        return dX * dX + dZ * dZ * this.radiusSquaredQuotient < this.radiusXSquared;
    }

    ///////////////////////////////////////////////////

    public static final LinkedHashSet<Integer> safeOpenBlocks = new LinkedHashSet<>(
            Arrays.asList(0, 6, 8, 9, 27, 28, 30, 31, 32, 37,
                    38, 39, 40, 50, 55, 59, 63, 64, 65, 66,
                    68, 69, 70, 71, 72, 75, 76, 77, 78, 83,
                    90, 93, 94, 96, 104, 105, 106, 115, 131,
                    132, 141, 142, 149, 150, 157, 171));

    public static final LinkedHashSet<Integer> painfulBlocks = new LinkedHashSet<>(
            Arrays.asList(10, 11, 51, 81, 119));

    public boolean isSafeSpot(World w, int x, int y, int z, boolean flying) {
        boolean safe = (safeOpenBlocks.contains(w.getBlockTypeIdAt(x, y, z))) && safeOpenBlocks.contains(w.getBlockTypeIdAt(x, y, z));
        if (!safe || flying)
            return safe;
        int below = w.getBlockTypeIdAt(x, y - 1, z);
        return (!safeOpenBlocks.contains(below) || below == 8 || below == 9) && !painfulBlocks.contains(below);
    }

    public double getSafeY(World w, int x, int y, int z, boolean flying) {
        boolean nether = (w.getEnvironment().equals(World.Environment.NETHER));
        int limTop = nether ? 125 : (w.getMaxHeight() - 2);
        int highestBlockBoundary = Math.min(w.getHighestBlockYAt(x, z) + 1, limTop);
        if (flying && y > limTop && !nether)
            return y;
        if (y > limTop)
            if (nether || flying) {
                y = limTop;
            } else {
                y = highestBlockBoundary;
            }
        if (y < 0)
            y = 0;
        if (!nether && !flying)
            limTop = highestBlockBoundary;
        for (int y1 = y, y2 = y; y1 > 0 || y2 < limTop; y1--, y2++) {
            if (y1 > 0)
                if (this.isSafeSpot(w, x, y1, z, flying))
                    return y1;
            if (y2 < limTop && y2 != y1)
                if (this.isSafeSpot(w, x, y2, z, flying))
                    return y2;
        }
        return -1.0D;
    }


    public static class SubCuboid {

        private UUID uuid;
        private World w;
        private int x, z, radiusX, radiusZ;

        public SubCuboid(World w, int x, int z, int radiusX, int radiusZ) {
            this.w = w;
            this.x = x;
            this.z = z;
            this.radiusX = radiusX;
            this.radiusZ = radiusZ;
            this.uuid = UUID.randomUUID();
        }

        public UUID getId() {
            return this.uuid;
        }

        public World getW() {
            return w;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public int getRadiusX() {
            return radiusX;
        }

        public int getRadiusZ() {
            return radiusZ;
        }
    }

}
