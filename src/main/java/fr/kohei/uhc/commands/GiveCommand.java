package fr.kohei.uhc.commands;

import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand {

    @Command(names = "h give")
    public static void onCommand(Player player, @Param(name = "item") String item, @Param(name = "amount", defaultValue = "1") int amount) {
        Material mat = Bukkit.getUnsafe().getMaterialFromInternalName(item);

        if (amount > 64) {
            amount = 64;
        }

        if (mat == null || mat == Material.AIR) {
            player.sendMessage(ChatUtil.prefix("&cCet item n'existe pas."));
            return;
        }

        final int newAmount = amount;
        Bukkit.broadcastMessage(ChatUtil.translate("&7❘ &e&lGIVE &e" + mat.name() + " &8(&a" + amount + "&8)"));
        UHC.getGameManager().getPlayers().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).map(Bukkit::getPlayer).forEach(t ->
                t.getInventory().addItem(new ItemStack(mat, newAmount))
        );
    }

}
