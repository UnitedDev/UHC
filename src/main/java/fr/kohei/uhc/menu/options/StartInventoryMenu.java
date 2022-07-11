package fr.kohei.uhc.menu.options;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.menu.buttons.Glass;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class StartInventoryMenu extends Menu {

    private final Menu oldMenu;


    @Override
    public String getTitle(Player player) {
        return "Inventaire de départ";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;

        for (ItemStack item : UHC.getGameManager().getGameConfiguration().getStartInventory()) {
            if (item == null || item.getType() == Material.AIR) {
                i++;
                continue;
            }
            buttons.put(i++, new DisplayButton(item));
        }

        for (int j = 36; j < 45; j++) {
            buttons.put(j, new Glass(7));
        }

        int k = 36;
        for (ItemStack armor : UHC.getGameManager().getGameConfiguration().getStartArmor()) {
            if (armor == null || armor.getType() == Material.AIR) {
                k++;
                continue;
            }
            buttons.put(k++, new DisplayButton(armor));
        }

        if(oldMenu != null) {
            buttons.put(40, new BackButton(oldMenu));
        }

        UPlayer uPlayer = UPlayer.get(player);
        if(uPlayer.hasHostAccess()) {
            buttons.put(44, new ChangeInventoryButton());
        }

        return buttons;
    }

    public static class ChangeInventoryButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setDurability(10).setName("&6&lModifier l'inventaire").setLore(
                    "&fPermet de modifier l'inventaire de départ",
                    "&fdonné en début de game",
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if(UHC.getGameManager().getGameState() != GameState.LOBBY) {
                player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cet item pendant la partie"));
                return;
            }

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setGameMode(GameMode.CREATIVE);
            player.closeInventory();
            player.getInventory().setContents(UHC.getGameManager().getGameConfiguration().getStartInventory());
            player.getInventory().setArmorContents(UHC.getGameManager().getGameConfiguration().getStartArmor());
            player.sendMessage(ChatUtil.prefix("&fUtilisez &a/finish &fpour sauvegarder l'inventaire"));
            UHC.getGameManager().setEditingStartInventory(player.getUniqueId());
        }
    }

}
