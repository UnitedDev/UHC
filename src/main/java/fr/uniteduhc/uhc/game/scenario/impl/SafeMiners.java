package fr.uniteduhc.uhc.game.scenario.impl;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.BackButton;
import fr.uniteduhc.menu.buttons.DisplayButton;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.scenario.AbstractScenario;
import fr.uniteduhc.uhc.menu.ManageScenariosMenu;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SafeMiners extends AbstractScenario implements Listener {
    @Override
    public String getName() {
        return "SafeMiners";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&fEn dessous d'une certaine couche, les joueurs",
                "&fne prendront &caucun dégât de lave&f."
        );
    }

    private int yAxe = 50;

    @Override
    public Material getIcon() {
        return Material.LAVA_BUCKET;
    }

    @Override
    public void onStart() {
        Bukkit.getServer().getPluginManager().registerEvents(this, UHC.getInstance());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getEntity() instanceof Player &&
                (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.LAVA
                        || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE
                        || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
                && entityDamageEvent.getEntity().getLocation().getY() < yAxe)
            entityDamageEvent.setCancelled(true);

    }

    @Override
    public Menu getMenu() {
        return new SafeMinersMenu();
    }

    public class SafeMinersMenu extends Menu {

        @Override
        public String getTitle(Player player) {
            return "Safe Miners";
        }

        @Override
        public Map<Integer, Button> getButtons(Player player) {
            Map<Integer, Button> buttons = new HashMap<>();

            buttons.put(0, new ChangeYButton(-10, 1));
            buttons.put(1, new ChangeYButton(-5, 14));
            buttons.put(2, new ChangeYButton(-1, 11));

            buttons.put(4, new DisplayButton(new ItemBuilder(Material.LAVA_BUCKET).setName("&6&lSafeMiners").setLore(
                    "",
                    "&fConfiguration: &cy" + yAxe,
                    "",
                    "&f&l» &eCliquez-ici pour changer"
            ).toItemStack()));

            buttons.put(6, new ChangeYButton(1, 12));
            buttons.put(7, new ChangeYButton(5, 10));
            buttons.put(8, new ChangeYButton(10, 2));

            buttons.put(13, new BackButton(new ManageScenariosMenu(null)));

            return buttons;
        }

        @RequiredArgsConstructor
        private class ChangeYButton extends Button {

            private final int add;
            private final int durability;

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.BANNER).setDurability(durability).setName(add > 0 ? "&a+" + add : "&c" + add).toItemStack();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                if (yAxe + add <= 10) {
                    yAxe = (10);
                    return;
                }
                yAxe = (Math.min(yAxe + add, 50));
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        }
    }
}