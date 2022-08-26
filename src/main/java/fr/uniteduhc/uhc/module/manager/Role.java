package fr.uniteduhc.uhc.module.manager;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.uhc.module.Module;
import fr.uniteduhc.utils.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class Role {

    public abstract ItemStack getItem();

    public abstract String getName();

    public abstract Camp getStartCamp();

    public List<CustomItem> getItems() {
        return new ArrayList<>();
    }

    public List<PotionEffect> getPotionEffects() {
        return new ArrayList<>();
    }

    public abstract String[] getDescription();

    public void onDistribute(Player player) {
    }

    public void onDeath(Player player, Player killer) {
    }

    public void onSecond(Player player) {
    }

    public void onKill(Player death, Player killer) {
    }

    public void onDay(Player player) {
    }

    public void onNight(Player player) {
    }

    public void onEpisode(Player player) {
    }

    public List<String> getStartMessage() {
        Module module = UHC.getInstance().getModuleManager().getModule();
        return new ArrayList<>(Arrays.asList(
                " ",
                "» Utilisez &c/" + module.getCommandPrefix() + " me &fpour consulter votre rôle.",
                "» Utilisez &c/" + module.getCommandPrefix() + " role &fpour voir la liste des rôles en vie.",
                "» Utilisez &c/" + module.getCommandPrefix() + " claim &fafin de récupérer vos items.",
                " "
        ));
    }

    public Player getPlayer() {
        Player toReturn = null;

        for (UUID uuid : UHC.getInstance().getGameManager().getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            UPlayer uPlayer = UPlayer.get(player);
            if (uPlayer.getRole() == null || !uPlayer.isAlive()) continue;

            if (uPlayer.getRole() == this) toReturn = player;
        }

        return toReturn;
    }

}
