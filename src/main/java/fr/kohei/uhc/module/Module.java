package fr.kohei.uhc.module;

import fr.kohei.menu.Menu;
import fr.kohei.uhc.module.manager.Camp;
import fr.kohei.uhc.module.manager.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class Module {

    public abstract String getCommandPrefix();

    public abstract String getName();

    public abstract HashMap<RoleType, Class<? extends Role>> getRoles();

    public abstract List<Camp> getCamps();

    public abstract void onStart();

    public abstract void onEpisode();

    public abstract void onDay();

    public abstract void onNight();

    public abstract void onRoles();

    public abstract void onDeath(Player player, Player killer);

    public abstract void onDisconnectDeath(UUID uuid);

    public abstract boolean hasRoles();

    public abstract boolean hasCycle();

    public abstract boolean isRolesPerTeam();

    public HashMap<ItemStack, Menu> getMenus() {
        return new HashMap<>();
    }

    @Getter
    @RequiredArgsConstructor
    public static class RoleType {
        private final String name;
        private final Camp camp;
        private final ItemStack display;
    }
}
