package fr.uniteduhc.uhc.game.scenario.impl;

import fr.uniteduhc.uhc.game.scenario.AbstractScenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class CatEyes extends AbstractScenario {
    @Override
    public String getName() {
        return "CatEyes";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&fAu démarrage de la partie, &ctous les joueurs",
                "&fauront l'effet &9NightVision&f."
        );
    }

    @Override
    public Material getIcon() {
        return Material.SEA_LANTERN;
    }

    private final PotionEffect potionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);

    @Override
    public void onStart() {
        Bukkit.getOnlinePlayers().forEach(player -> player.addPotionEffect(potionEffect));
    }



}
