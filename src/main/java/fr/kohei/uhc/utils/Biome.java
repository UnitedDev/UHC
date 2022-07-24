package fr.kohei.uhc.utils;

public enum Biome {
  OCEAN(Integer.valueOf(0)),
  PLAINS(Integer.valueOf(1)),
  DESERT(Integer.valueOf(2)),
  EXTREME_HILLS(Integer.valueOf(3)),
  FOREST(Integer.valueOf(4)),
  TAIGA(Integer.valueOf(5)),
  SWAMPLAND(Integer.valueOf(6)),
  RIVER(Integer.valueOf(7)),
  FROZEN_OCEAN(Integer.valueOf(10)),
  FROZEN_RIVER(Integer.valueOf(11)),
  ICE_PLAINS(Integer.valueOf(12)),
  ICE_MOUNTAINS(Integer.valueOf(13)),
  MUSHROOM_ISLAND(Integer.valueOf(14)),
  MUSHROOM_SHORE(Integer.valueOf(15)),
  BEACH(Integer.valueOf(16)),
  DESERT_HILLS(Integer.valueOf(17)),
  FOREST_HILLS(Integer.valueOf(18)),
  TAIGA_HILLS(Integer.valueOf(19)),
  SMALL_MOUNTAINS(Integer.valueOf(20)),
  JUNGLE(Integer.valueOf(21)),
  JUNGLE_HILLS(Integer.valueOf(22)),
  JUNGLE_EDGE(Integer.valueOf(23)),
  DEEP_OCEAN(Integer.valueOf(24)),
  STONE_BEACH(Integer.valueOf(25)),
  COLD_BEACH(Integer.valueOf(26)),
  BIRCH_FOREST(Integer.valueOf(27)),
  BIRCH_FOREST_HILLS(Integer.valueOf(28)),
  ROOFED_FOREST(Integer.valueOf(29)),
  COLD_TAIGA(Integer.valueOf(30)),
  COLD_TAIGA_HILLS(Integer.valueOf(31)),
  MEGA_TAIGA(Integer.valueOf(32)),
  MEGA_TAIGA_HILLS(Integer.valueOf(33)),
  EXTREME_HILLS_PLUS(Integer.valueOf(34)),
  SAVANNA(Integer.valueOf(35)),
  SAVANNA_PLATEAU(Integer.valueOf(36)),
  MESA(Integer.valueOf(37)),
  MESA_PLATEAU_F(Integer.valueOf(38)),
  MESA_PLATEAU(Integer.valueOf(39)),
  SUN_FLOWER_PLAINS(Integer.valueOf(129)),
  DESERT_M(Integer.valueOf(130)),
  EXTREME_HILLS_M(Integer.valueOf(131)),
  FLOWER_FOREST(Integer.valueOf(132)),
  TAIGA_M(Integer.valueOf(133)),
  SWAMPLAND_M(Integer.valueOf(134)),
  ICE_PLAINS_SPIKES(Integer.valueOf(140)),
  JUNGLE_M(Integer.valueOf(149)),
  JUNGLE_EDGE_M(Integer.valueOf(151)),
  BIRCH_FOREST_M(Integer.valueOf(155)),
  BIRCH_FOREST_HILLS_M(Integer.valueOf(156)),
  ROOFED_FOREST_M(Integer.valueOf(157)),
  COLD_TAIGA_M(Integer.valueOf(158)),
  MEGA_SPRUCE_TAIGA(Integer.valueOf(160)),
  MEGA_SPRUCE_TAIGA_2(Integer.valueOf(161)),
  EXTREME_HILLS_PLUS_M(Integer.valueOf(162)),
  SAVANNA_M(Integer.valueOf(163)),
  SAVANNA_PLATEAU_M(Integer.valueOf(164)),
  MESA_BRYCE(Integer.valueOf(165)),
  MESA_PLATEAU_F_M(Integer.valueOf(166)),
  MESA_PLATEAU_M(Integer.valueOf(167));
  
  private Integer id;
  
  Biome(Integer id) {
    this.id = id;
  }
  
  public Integer getId() {
    return this.id;
  }
  
  public static Boolean isAcceptableBiomes(String b) {
    Biome[] arrayOfBiome;
    int j = (arrayOfBiome = values()).length;
    for (int i = 0; i < j; i++) {
      Biome bio = arrayOfBiome[i];
      if (bio.toString().equals(b))
        return Boolean.valueOf(true); 
    } 
    return Boolean.valueOf(false);
  }
  
  public static Boolean isAcceptableBiomes(Integer id) {
    Biome[] arrayOfBiome;
    int j = (arrayOfBiome = values()).length;
    for (int i = 0; i < j; i++) {
      Biome bio = arrayOfBiome[i];
      if (bio.getId() == id)
        return Boolean.valueOf(true); 
    } 
    return Boolean.valueOf(false);
  }
}
