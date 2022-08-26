package fr.uniteduhc.uhc.game.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RulesManager {

    private final GameConfiguration gameConfiguration;

    private boolean diamondHelmet, diamondChestplate, diamondLeggings, diamondBoots;
    private boolean knockback, punch, finishRod, lavaBucket;
    private boolean digDown, towers, potions, enderPearl;
    private boolean horse, flintAndSteel, flame;

    public RulesManager(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;

        this.diamondHelmet = true;
        this.diamondChestplate = true;
        this.diamondLeggings = true;
        this.diamondBoots = true;

        this.knockback = true;
        this.punch = true;
        this.finishRod = true;
        this.lavaBucket = true;

        this.enderPearl = true;
        this.potions = true;
        this.towers = true;
        this.digDown = true;

        this.horse = true;
        this.flintAndSteel = true;
    }
}
