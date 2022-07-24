package fr.kohei.uhc.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.menu.teams.TeamSizeMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ManageTeamsMenu extends Menu {

    public static ItemStack getTeamsSizeDisplay() {
        return new ItemBuilder(Material.WATCH).setName("&6&lTaille des équipes").setLore(
                "&fPermet le nombre de personnes maximum",
                "&fdans une équipe",
                "",
                "&8┃ &7Taille: &c" + UHC.getInstance().getGameManager().getGameConfiguration().getTeams(),
                "",
                "&f&l» &eCliquez-ici pour modifier"
        ).toItemStack();
    }

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Equipes";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 18, 19, 25, 26}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDurability(8).toItemStack()));
        }

        buttons.put(12, new TeamSizeButton());
        buttons.put(13, new FriendlyFireButton());
        buttons.put(14, new RandomTeamButton());

        if (oldMenu != null) {
            buttons.put(22, new BackButton(oldMenu));
        }

        return buttons;
    }

    public class TeamSizeButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getTeamsSizeDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new TeamSizeMenu(new ManageTeamsMenu(oldMenu)).openMenu(player);
        }
    }

    public static class FriendlyFireButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean friendlyFire = UHC.getInstance().getGameManager().getGameConfiguration().isFriendlyFire();
            return new ItemBuilder(Material.LAVA_BUCKET).setName("&6&lFriendly Fire").setLore(
                    "&fPermet d'activer ou de désactiver le",
                    "&ffriendly fire pour les équipes.",
                    "",
                    "&8┃ &7Friendly Fire: " + (friendlyFire ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            boolean friendlyFire = UHC.getInstance().getGameManager().getGameConfiguration().isFriendlyFire();
            UHC.getInstance().getGameManager().getGameConfiguration().setFriendlyFire(!friendlyFire);
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    public static class RandomTeamButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean randomTeam = UHC.getInstance().getGameManager().getGameConfiguration().isRandomTeam();
            return new ItemBuilder(Material.COMPASS).setName("&6&lRandom Team").setLore(
                    "&fPermet d'activer ou de désactiver la",
                    "&fsélection des équipes.",
                    "",
                    "&8┃ &7Random Team: " + (randomTeam? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            boolean randomTeam = UHC.getInstance().getGameManager().getGameConfiguration().isRandomTeam();
            UHC.getInstance().getGameManager().getGameConfiguration().setRandomTeam(!randomTeam);
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
