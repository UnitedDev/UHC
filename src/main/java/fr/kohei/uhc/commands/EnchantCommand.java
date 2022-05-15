package fr.kohei.uhc.commands;

import fr.kohei.command.Command;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.menu.EnchantmentMenu;
import fr.kohei.utils.ChatUtil;
import org.bukkit.entity.Player;

public class EnchantCommand {

    @Command(names = "enchant")
    public static void enchant(Player sender) {
        UPlayer uPlayer = UPlayer.get(sender);
        if(!(uPlayer.isEditingStartInventory() || uPlayer.isEditingDeathInventory())) {
            sender.sendMessage(ChatUtil.prefix("&cVous devez être en train de modifier un inventaire pour exécuter cette commande."));
            return;
        }

        new EnchantmentMenu().openMenu(sender);
    }

}
