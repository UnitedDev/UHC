package fr.kohei.uhc.menu;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.utils.Heads;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class TimersMenu extends Menu {
    private final Menu oldMenu;
    private Timer selectedTimer;

    @Override
    public String getTitle(Player player) {
        return "Timers";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i : new int[]{0, 1, 7, 8, 9, 18, 17, 26, 47, 48, 50, 51}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(1).toItemStack()));
        }

        for (int i : new int[]{2, 3, 4, 5, 6, 27, 36, 45, 46, 35, 44, 53, 52}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).toItemStack()));
        }

        buttons.put(49, new BackButton(oldMenu));

        buttons.put(22, new TimerButton(Timer.PVP));
        buttons.put(21, new TimerButton(Timer.BORDER));
        buttons.put(23, new TimerButton(Timer.ROLES));

        if (selectedTimer != null) {
            buttons.put(29, new ChangeValueButton(-120, 14));
            buttons.put(30, new ChangeValueButton(-60, 1));
            buttons.put(31, new ChangeValueButton(-10, 4));
            buttons.put(32, new ChangeValueButton(60, 5));
            buttons.put(33, new ChangeValueButton(120, 13));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    public class ChangeValueButton extends Button {
        private final int amount;
        private final int data;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.STAINED_CLAY).setDurability(data).setName(amount < 0 ? "&c" + amount : "&a+" + amount).toItemStack();
        }

        @Override
        @SneakyThrows
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Field field = GameConfiguration.class.getDeclaredField(selectedTimer.getField());
            field.setAccessible(true);
            Object object = UHC.getGameManager().getGameConfiguration();
            int value = field.getInt(object);
            value += amount;
            if (value < 10) {
                value = 10;
            }

            field.setInt(object, value);
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    @RequiredArgsConstructor
    public class TimerButton extends Button {
        private final Timer timer;

        @Override
        @SneakyThrows
        public ItemStack getButtonItem(Player player) {
            Field field = GameConfiguration.class.getDeclaredField(timer.getField());
            field.setAccessible(true);
            Object object = UHC.getGameManager().getGameConfiguration();
            int value = field.getInt(object);

            if (selectedTimer != null && timer == selectedTimer) {
                return new ItemBuilder(timer.getItemDisplay()).addEnchant(Enchantment.DEPTH_STRIDER, 1).hideItemFlags().setName("&c" + timer.getDisplay()).setLore(
                        "",
                        "&fConfiguration: &c" + TimeUtil.niceTime(value),
                        "",
                        "&f&l» &cCliquez-ici pour séléctionner"
                ).setAmount(1).toItemStack();
            }
            return new ItemBuilder(timer.getItemDisplay()).setName("&c" + timer.getDisplay()).setLore(
                    "",
                    "&fConfiguration: &c" + TimeUtil.niceTime(value),
                    "",
                    "&f&l» &cCliquez-ici pour séléctionner"
            ).setAmount(0).removeEnchantment(Enchantment.DEPTH_STRIDER).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            selectedTimer = timer;
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum Timer {
        PVP(new ItemStack(Material.DIAMOND_SWORD), "PvP", "pvpTimer"),
        BORDER(new ItemStack(Material.IRON_FENCE), "Bordure", "meetupTimer"),
        ROLES(Heads.DICE.toItemStack(), "Rôles", "rolesTime");

        private final ItemStack itemDisplay;
        private final String display;
        private final String field;
    }

}