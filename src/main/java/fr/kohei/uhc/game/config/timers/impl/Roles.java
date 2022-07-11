package fr.kohei.uhc.game.config.timers.impl;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.timers.CustomTimer;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.Module;
import fr.kohei.uhc.module.manager.Role;
import fr.kohei.utils.ChatUtil;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Roles extends CustomTimer {
    public Roles() {
        super(UHC.getGameManager().getGameConfiguration().getRolesTime());
    }

    @Override
    @SneakyThrows
    public void onEnable() {
        Module module = UHC.getModuleManager().getModule();
        module.onRoles();
        if (!module.hasRoles()) return;

        if (module.isRolesPerTeam()) {
            //TODO
            return;
        }

        List<Class<? extends Role>> roles = new ArrayList<>();

        for (String enabledRole : UHC.getGameManager().getGameConfiguration().getEnabledRoles()) {
            UHC.getModuleManager().getModule().getRoles().forEach((roleType, aClass) -> {
                if (roleType.getName().equals(enabledRole)) {
                    roles.add(aClass);
                }
            });
        }

        Collections.shuffle(roles);
        Bukkit.broadcastMessage(ChatUtil.translate("&cAttribution des rôles &fen cours..."));
        for (UUID uuid : UHC.getGameManager().getPlayers()) {
            if (roles.size() == 0) break;

            Role role = roles.get(0).getDeclaredConstructor().newInstance();
            Player player = Bukkit.getPlayer(uuid);
            UPlayer uPlayer = UPlayer.get(player);

            if(role instanceof Listener) Bukkit.getServer().getPluginManager().registerEvents((Listener) role, UHC.getPlugin());

            uPlayer.setRole(role);
            uPlayer.setCamp(role.getStartCamp());
            onDistribute(player, role);
            for (String s : role.getDescription()) {
                player.sendMessage(ChatUtil.translate(s));
            }
            role.getPotionEffects().forEach(player::addPotionEffect);
            role.getItems().forEach(customItem -> player.getInventory().addItem(customItem.toItemStack()));

            roles.remove(0);
        }
        Bukkit.broadcastMessage(ChatUtil.translate("&fLes rôles ont été &cattribués &f!"));
    }

    @SneakyThrows
    private void onDistribute(Player player, Role role) {
        Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> role.onDistribute(player), 100);
    }
}
