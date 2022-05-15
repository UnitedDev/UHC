package fr.kohei.uhc.commands;

import fr.kohei.command.Command;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import org.bukkit.entity.Player;

public class FinishCommand {

    @Command(names = "finish")
    public static void finish(Player sender) {
        UPlayer uPlayer = UPlayer.get(sender);
        if(!uPlayer.hasHostAccess()) {
            sender.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if(uPlayer.isEditingStartInventory()) {
            UHC.getGameManager().getGameConfiguration().setStartInventory(sender.getInventory().getContents());
            UHC.getGameManager().getGameConfiguration().setStartArmor(sender.getInventory().getArmorContents());
            uPlayer.updateLobbyHotbar();
            sender.teleport(UHC.getGameManager().getLobby());
            sender.sendMessage(ChatUtil.prefix("&fVous avez &amodifié &fl'inventaire de &adépart"));
            UHC.getGameManager().setEditingStartInventory(null);
        } else if(uPlayer.isEditingDeathInventory()) {
            UHC.getGameManager().getGameConfiguration().setDeathInventory(sender.getInventory().getContents());
            uPlayer.updateLobbyHotbar();
            sender.teleport(UHC.getGameManager().getLobby());
            sender.sendMessage(ChatUtil.prefix("&fVous avez &amodifié &fl'inventaire de &cmort"));
            UHC.getGameManager().setEditingDeathInventory(null);
        } else {
            sender.sendMessage(ChatUtil.prefix("&cVous n'êtes pas en train de modifier un inventaire"));
        }
    }

}
