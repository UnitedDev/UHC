package fr.kohei.uhc.commands;

import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SayCommand {

    @Command(names = {"h say", "say"})
    public static void onCommand(Player player, @Param(name = "message", wildcard = true) String message) {
        UPlayer uPlayer = UPlayer.get(player);
        if(!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatUtil.translate("&7❘ &e&lHOST &e" + player.getName() + " &8» &f" + message));
        Bukkit.broadcastMessage(" ");
    }
}
