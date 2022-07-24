package fr.kohei.uhc.game.scenario.impl;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.scenario.AbstractScenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BetaZombie extends AbstractScenario implements Listener {
    @Override
    public String getName() {
        return "BetaZombies";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&fLes zombies relâchent des plumes à leur",
                "&fmort avec un taux de drop d'une chance",
                "&csur trois&f."
        );
    }

    @Override
    public Material getIcon() {
        return Material.ROTTEN_FLESH;
    }

    @Override
    public void onStart() {
        Bukkit.getServer().getPluginManager().registerEvents(this, UHC.getInstance());
    }

    private final ItemStack featherItem = new ItemStack(Material.FEATHER);

    @EventHandler
    public void onEntityDeath(EntityDeathEvent entityDeathEvent){
        if(entityDeathEvent.getEntity() instanceof Zombie){
            int p = new Random().nextInt(3);
            if(p == 1)
                entityDeathEvent.getEntity().getWorld().dropItemNaturally(entityDeathEvent.getEntity().getLocation(), featherItem);
        }
    }

}
