package fr.kohei.uhc.game.generator;

import fr.kohei.uhc.utils.Biome;
import fr.kohei.utils.Reflection;
import net.minecraft.server.v1_8_R3.*;

import java.lang.reflect.Field;
import java.util.Map;

public class BiomesManager {
    public static void patchBiomes() throws ReflectiveOperationException {
        Field worldGenForestFirstField = BiomeForest.class.getDeclaredField("aD");
        worldGenForestFirstField.setAccessible(true);
        Field worldGenForestSecondField = BiomeForest.class.getDeclaredField("aE");
        worldGenForestSecondField.setAccessible(true);
        Field worldGenForestTreeField = BiomeForest.class.getDeclaredField("aF");
        worldGenForestTreeField.setAccessible(true);
        Reflection.setFinalStatic(worldGenForestFirstField, new WorldGenForest(false, true));
        Reflection.setFinalStatic(worldGenForestSecondField, new WorldGenForest(false, false));
        Field lo = BiomeBase.class.getDeclaredField("as");
        lo.setAccessible(true);
        Field mp = BiomeBase.class.getDeclaredField("aA");
        mp.setAccessible(true);
        Field mps = BiomeBase.class.getDeclaredField("aB");
        mps.setAccessible(true);
        Field mpsg = BiomeBase.class.getDeclaredField("aC");
        mpsg.setAccessible(true);
        BiomeBase[] biomes = BiomeBase.getBiomes();
        Map<String, BiomeBase> biomesMap = BiomeBase.o;
        Field biomeF = BiomeBase.class.getDeclaredField("biomes");
        biomeF.setAccessible(true);
        BiomeBase defaultBiome = getDefaultBiome();
        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("ad"), defaultBiome);
        disableBiomes(biomes);
        for (int i = 0; i < biomes.length; i++) {
            if (biomes[i] != null) {
                if (!biomesMap.containsKey((biomes[i]).ah))
                    biomes[i] = defaultBiome;
                mp.set(biomes[i], new WorldGenTrees(true));
                mps.set(biomes[i], new WorldGenBigTree(true));
            }
        }
        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("biomes"), biomes);
    }

    private static BiomeBase getDefaultBiome() {
        return BiomeBase.ROOFED_FOREST;
    }

    public static int getIdDefaultBiome() {
        int id = (getDefaultBiome()).id;
        System.out.println("(SupUHC) ID Default Biome: " + id);
        return id;
    }

    private static void disableBiomes(BiomeBase[] biomesMap) {
        for (Biome value : Biome.values()) {
            if (value.name().contains("ROOFED_FOREST")) continue;
            swap(biomesMap, value);
        }
    }

    public static void swap(BiomeBase[] biomes, Biome from) {
        biomes[from.getId()] = getDefaultBiome();
    }
}