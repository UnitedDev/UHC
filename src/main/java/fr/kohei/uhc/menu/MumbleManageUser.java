package fr.kohei.uhc.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.GlassMenu;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.mumble.api.mumble.IUser;
import fr.kohei.mumble.api.mumble.MumbleState;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.Heads;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MumbleManageUser extends GlassMenu {
    private final Player player;
    private final IUser user;
    private final Menu oldMenu;

    @Override
    public int getGlassColor() {
        return 14;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player param) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new MumbleMenu.MumbleButton(player));

        buttons.put(12, new MuteUserButton());
        buttons.put(13, new KickUserButton());
        buttons.put(14, new BanUserButton());

        if (oldMenu != null) buttons.put(22, new BackButton(oldMenu));

        return buttons;
    }

    @Override
    public String getTitle(Player param) {
        return "Manage " + player.getName();
    }

    private class MuteUserButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BOOK_AND_QUILL).setName("&cRendre Muet").setLore(
                    "",
                    "&8┃ &7Muet: " + (user.isMute() ? MumbleState.LINK.getName() : MumbleState.DISCONNECT.getName()),
                    "",
                    "&f&l» &cCliquez-ici pour le rendre muet"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (user.isMute()) user.unmuteUser();
            else user.muteUser();
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private class KickUserButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BARRIER).setName("&cExpulser &8(&cProchainement&8)").setLore(
                    "",
                    "&f&l» &cCliquez-ici pour expulser"
            ).toItemStack();
        }
    }

    private static class BanUserButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Heads.SOON.toItemStack()).setName("&cBannir &8(&cProchainement&8)").setLore(
                    "",
                    "&f&l» &cCliquez-ici pour bannir"
            ).toItemStack();
        }
    }
}
