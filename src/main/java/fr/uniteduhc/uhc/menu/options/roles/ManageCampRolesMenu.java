package fr.uniteduhc.uhc.menu.options.roles;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.module.Module;
import fr.uniteduhc.uhc.module.ModuleManager;
import fr.uniteduhc.uhc.module.manager.Camp;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ManageCampRolesMenu extends PaginatedMenu {
    private final Camp camp;
    private final Menu oldMenu;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Modification " + camp.getName();
    }

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Module module = UHC.getInstance().getModuleManager().getModule();

        List<Module.RoleType> roles = module.getRoles().keySet().stream().filter(role -> role.getCamp() == camp).sorted(Comparator.comparing(Module.RoleType::getName).reversed()).collect(Collectors.toList());

        for (Module.RoleType role : roles) {
            buttons.put(buttons.size(), new RoleButton(role));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class RoleButton extends Button {
        private final Module.RoleType role;

        @Override
        public ItemStack getButtonItem(Player player) {

            int enabled = 0;

            for (String name : UHC.getInstance().getGameManager().getGameConfiguration().getEnabledRoles()) {
                if (name.equalsIgnoreCase(role.getName())) {
                    enabled++;
                }
            }

            List<String> lore = new ArrayList<>();
            lore.add("&fPermet de &cdésactiver&f ou d'&aactiver&f la");
            lore.add("&fprésence du rôle pendant la partie");
            lore.add(" ");
            lore.add("&8┃ &7Nombre: &c" + (enabled));
            lore.add(" ");
            lore.add("&f&l» &eCliquez-ici pour modifier");
            return new ItemBuilder(role.getDisplay()).setAmount(enabled).setLore(lore).setName("&6&l" + role.getName()).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            ModuleManager manager = UHC.getInstance().getModuleManager();

            if (clickType.isRightClick()) {
                UHC.getInstance().getGameManager().getGameConfiguration().getEnabledRoles().remove(role.getName());
            } else {
                UHC.getInstance().getGameManager().getGameConfiguration().getEnabledRoles().add(role.getName());
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
