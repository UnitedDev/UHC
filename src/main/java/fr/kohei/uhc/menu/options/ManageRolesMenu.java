package fr.kohei.uhc.menu.options;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.menu.options.roles.ManageCampRolesMenu;
import fr.kohei.uhc.module.Module;
import fr.kohei.uhc.module.manager.Camp;
import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ManageRolesMenu extends PaginatedMenu {

    private final Menu oldMenu;


    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Choisissez une camp";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Module module = UHC.getModuleManager().getModule();
        for (Camp camp : module.getCamps()) {
            buttons.put(buttons.size(), new CampButton(camp));
        }
        UHC.getModuleManager().getModule().getMenus().forEach((itemStack, menu) -> {
            buttons.put(buttons.size(), new CustomButton(itemStack, menu));
        });

        return buttons;
    }

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @RequiredArgsConstructor
    private class CampButton extends Button {
        private final Camp camp;

        @Override
        public ItemStack getButtonItem(Player player) {
            Module module = UHC.getModuleManager().getModule();
            int size = (int) module.getRoles().keySet().stream().filter(role -> role.getCamp() == camp).count();
            int enabled = (int) module.getRoles().keySet().stream().filter(role -> role.getCamp() == camp)
                    .filter(role -> UHC.getGameManager().getGameConfiguration().getEnabledRoles().contains(role.getName()))
                    .count();

            List<String> lore = new ArrayList<>();
            lore.add("&fPermet d'accéder à la liste de rôles du");
            lore.add("&fcamp '" + camp.getColor() + camp.getName() + "&f'");
            lore.add(" ");
            lore.add("&8┃ &7Activé(s): &a" + enabled + "&8/&c" + size);
            lore.add(" ");
            lore.add("&f&l» &eCliquez-ici pour modifier");

            return new ItemBuilder(Material.INK_SACK).setDurability(14).setName(camp.getColor() + camp.getName()).setLore(lore).setAmount(size).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageCampRolesMenu(camp, new ManageRolesMenu(oldMenu)).openMenu(player);
        }
    }

    @RequiredArgsConstructor
    private class CustomButton extends Button {
        private final ItemStack itemStack;
        private final Menu menu;


        @Override
        public ItemStack getButtonItem(Player player) {
            return itemStack;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            menu.openMenu(player);
        }
    }
}
