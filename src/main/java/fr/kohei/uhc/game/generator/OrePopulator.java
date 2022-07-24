package fr.kohei.uhc.game.generator;

import net.minecraft.server.v1_8_R3.ChunkSnapshot;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.WorldGenCanyon;
import net.minecraft.server.v1_8_R3.WorldGenCaves;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.Random;

public class OrePopulator extends BlockPopulator
{
    private final ArrayList<Rule> rules;
    
    public OrePopulator() {
        this.rules = new ArrayList<Rule>();
    }
    
    private int randInt(final Random rand, final int min, final int max) {
        return rand.nextInt(max - min + 1) + min;
    }
    
    public void addRule(final Rule rule) {
        if (!this.rules.contains(rule)) {
            this.rules.add(rule);
        }
    }
    
    public void populate(final World world, final Random random, final Chunk chunk) {
        if (chunk == null) {
            return;
        }
        final CraftWorld handle = (CraftWorld)world;
        final int xr = this.randInt(random, -200, 200);
        if (xr >= 50) {
            new WorldGenCaves().a(handle.getHandle().chunkProviderServer, handle.getHandle(), chunk.getX(), chunk.getZ(), new ChunkSnapshot());
        }
        else if (xr <= -50) {
            new WorldGenCanyon().a(handle.getHandle().chunkProviderServer, handle.getHandle(), chunk.getX(), chunk.getZ(), new ChunkSnapshot());
        }
        for (final Rule bloc : this.rules) {
            for (int i = 0; i < bloc.round; ++i) {
                final int x = chunk.getX() * 16 + random.nextInt(16);
                final int y = bloc.minY + random.nextInt(bloc.maxY - bloc.minY);
                final int z = chunk.getZ() * 16 + random.nextInt(16);
                this.generate(world, random, x, y, z, bloc.size, bloc);
            }
        }
    }
    
    private void generate(final World world, final Random rand, final int x, final int y, final int z, final int size, final Rule material) {
        final double rpi = rand.nextDouble() * 3.141592653589793;
        final double x2 = x + 8 + Math.sin(rpi) * size / 8.0;
        final double x3 = x + 8 - Math.sin(rpi) * size / 8.0;
        final double z2 = z + 8 + Math.cos(rpi) * size / 8.0;
        final double z3 = z + 8 - Math.cos(rpi) * size / 8.0;
        final double y2 = y + rand.nextInt(3) + 2;
        final double y3 = y + rand.nextInt(3) + 2;
        for (int i = 0; i <= size; ++i) {
            final double xPos = x2 + (x3 - x2) * i / size;
            final double yPos = y2 + (y3 - y2) * i / size;
            final double zPos = z2 + (z3 - z2) * i / size;
            final double fuzz = rand.nextDouble() * size / 16.0;
            final double fuzzXZ = (Math.sin((float)(i * 3.141592653589793 / size)) + 1.0) * fuzz + 1.0;
            final double fuzzY = (Math.sin((float)(i * 3.141592653589793 / size)) + 1.0) * fuzz + 1.0;
            final int xStart = (int)Math.floor(xPos - fuzzXZ / 2.0);
            final int yStart = (int)Math.floor(yPos - fuzzY / 2.0);
            final int zStart = (int)Math.floor(zPos - fuzzXZ / 2.0);
            final int xEnd = (int)Math.floor(xPos + fuzzXZ / 2.0);
            final int yEnd = (int)Math.floor(yPos + fuzzY / 2.0);
            final int zEnd = (int)Math.floor(zPos + fuzzXZ / 2.0);
            for (int ix = xStart; ix <= xEnd; ++ix) {
                final double xThresh = (ix + 0.5 - xPos) / (fuzzXZ / 2.0);
                if (xThresh * xThresh < 1.0) {
                    for (int iy = yStart; iy <= yEnd; ++iy) {
                        final double yThresh = (iy + 0.5 - yPos) / (fuzzY / 2.0);
                        if (xThresh * xThresh + yThresh * yThresh < 1.0) {
                            for (int iz = zStart; iz <= zEnd; ++iz) {
                                final double zThresh = (iz + 0.5 - zPos) / (fuzzXZ / 2.0);
                                if (xThresh * xThresh + yThresh * yThresh + zThresh * zThresh < 1.0) {
                                    final Block block = this.getBlock(world, ix, iy, iz);
                                    if (block != null && block.getType() == Material.STONE) {
                                        block.setType(material.id);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private Block getBlock(final World world, final int x, final int y, final int z) {
        final int cx = x >> 4;
        final int cz = z >> 4;
        if (!world.isChunkLoaded(cx, cz) && !world.loadChunk(cx, cz, false)) {
            return null;
        }
        final Chunk chunk = world.getChunkAt(cx, cz);
        if (chunk == null) {
            return null;
        }
        return chunk.getBlock(x & 0xF, y, z & 0xF);
    }
    
    public static class Rule
    {
        public Material id;
        public int round;
        public int minY;
        public int maxY;
        public int size;
        
        public Rule(final Material type, final int round, final int minY, final int maxY, final int size) {
            this.id = type;
            this.round = round;
            this.minY = minY;
            this.maxY = maxY;
            this.size = size;
        }
    }
}
