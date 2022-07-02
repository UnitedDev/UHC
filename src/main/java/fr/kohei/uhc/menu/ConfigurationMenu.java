package fr.kohei.uhc.menu;

import fr.kohei.menu.GlassMenu;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.world.WorldGeneration;
import fr.kohei.uhc.menu.options.EnchantmentManager;
import fr.kohei.uhc.menu.options.ManageDropsMenu;
import fr.kohei.uhc.menu.options.ManageRolesMenu;
import fr.kohei.uhc.menu.options.SettingsMenu;
import fr.kohei.uhc.menu.options.rate.ManageOresLimitMenu;
import fr.kohei.uhc.task.StartTask;
import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.ConversationButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.menu.pagination.ConfirmationMenu;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Heads;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationMenu extends GlassMenu {

    @Override
    public int getGlassColor() {
        return 8;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(2, new LoadMapButton());
        buttons.put(6, new WhitelistButton());

        buttons.put(49, new StartButton());

        buttons.put(37, new TeleportationButton());
        buttons.put(10, getRenameButton(player));
        buttons.put(22, new TimersButton());
        buttons.put(16, new ScenariosButton());
        buttons.put(20, new DropsButton());
        buttons.put(27, new EnchantsLimitsButton());
        buttons.put(31, new SettingsButton());
        buttons.put(26, new OresLimitButton());
        buttons.put(35, new ModeButton());

        buttons.put(43, new StopServerButton());

//        buttons.put(49, new ConfigButton());

        return buttons;
    }

    private static class StopServerButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.INK_SACK).setDurability(1).setName("&cStopper le serveur").setLore(
                    "&fPermet de supprimer complètement le",
                    "&fserveur",
                    "",
                    "&f&l» &cCliquez-ici pour supprimer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (UHC.getGameManager().getGameState() != GameState.LOBBY) {
                player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cet item pendant la partie"));
                return;
            }
            new ConfirmationMenu(() -> UHC.getPlugin().getServer().shutdown(), getButtonItem(player), new ConfigurationMenu()).openMenu(player);

        }
    }

    private class OresLimitButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.DIAMOND).setName("&cLimite de minerais").setLore(
                    "&fPermet de limite le nombre de minerais",
                    "&fde diamants et d'ors pendant la partie",
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageOresLimitMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    private class DropsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.APPLE).setName("&cTaux de drop").setLore(
                    "&fPermet de modifier les taux de drops",
                    "&fpour certains items",
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageDropsMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    private static class WhitelistButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            GameManager manager = UHC.getGameManager();
            return new ItemBuilder(Material.NAME_TAG).setName("&cAccessibilité de la partie").setLore(
                    "&fPermet de modifier l'accessibilité à la",
                    "&fpartie pour les joueurs.",
                    " ",
                    "&8┃ &7Accessibilité: " + (manager.isWhitelist() ? "&cFermé" : "&aOuvert"),
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameManager manager = UHC.getGameManager();
            manager.setWhitelist(!manager.isWhitelist());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class StartButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            GameManager gameManager = UHC.getGameManager();
            if (gameManager.getStartTask() == null) {
                return new ItemBuilder(Material.INK_SACK).setDurability(10).setName("&aLancer la partie").setLore(
                        "&fPermet de lancer la partie si tout est",
                        "&fprêt.",
                        "",
                        "&f&l» &cCliquez-ici pour lancer"
                ).toItemStack();
            } else {
                return new ItemBuilder(Material.INK_SACK).setDurability(1).setName("&cAnnuler le lancement").setLore(
                        "&fPermet d'annuler le lancement de la",
                        "&fpartie.",
                        "",
                        "&f&l» &cCliquez-ici pour annuler"
                ).toItemStack();
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameManager gameManager = UHC.getGameManager();

//            if (!WorldGeneration.isFinished()) {
//                player.sendMessage(ChatUtil.prefix("&cLa prégénération doit être terminée pour lancer la partie."));
//                return;
//            }

            if (gameManager.getStartTask() == null) {
                gameManager.setStartTask(new StartTask(gameManager));
                gameManager.getStartTask().runTaskTimer(UHC.getPlugin(), 0, 20);
            } else {
                gameManager.getStartTask().cancel();
                gameManager.setStartTask(null);
                Bukkit.getOnlinePlayers().forEach(player1 -> {
                    player1.setLevel(0);
                    player1.setExp(0F);
                });
            }
        }
    }

    private static class LoadMapButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.SAPLING).setName("&cPrégénération").setLore(
                    "&fPermet de prégénérer toute la map",
                    "",
                    "&8┃ &7Bordure: &c± " + UHC.getGameManager().getGameConfiguration().getBorderStartSize(),
                    "",
                    "&f&l» &cCliquez-ici pour charger"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {

            if (WorldGeneration.isFinished()) {
                player.sendMessage(ChatUtil.prefix("&cLa prégénération est déjà terminée."));
                return;
            }

            if (WorldGeneration.getPercentage() > 0) {
                player.sendMessage(ChatUtil.prefix("&cLa prégénération est déjà en cours."));
                return;
            }

            new ConfirmationMenu(() -> UHC.getGameManager().generateChunks(), getButtonItem(player), new ConfigurationMenu()).openMenu(player);
        }
    }

    private static class TeleportationButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            if (UHC.getGameManager().isRules()) {
                lore.add("&fCliquez-ici pour téléporter tous les");
                lore.add("&fjoueurs au lobby principal");
            } else {
                lore.add("&fCliquez-ici pour téléporter tous les");
                lore.add("&fjoueurs à la salle des règles");
            }
            lore.add(" ");
            lore.add("&f&l» &cCliquez-ici pour téléporter");
            return new ItemBuilder(Material.ENDER_PEARL).setName("&cTéléportation").setLore(lore).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (UHC.getGameManager().isRules()) {
                Bukkit.getOnlinePlayers().forEach(players -> players.teleport(UHC.getGameManager().getLobby()));
            } else {
                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.teleport(UHC.getGameManager().getRulesLocation());
                    Title.sendTitle(players, 10, 50, 10, "&8❘ &cRègles", "&fLe Host va énoncer les &crègles");
                });
            }
            UHC.getGameManager().setRules(!UHC.getGameManager().isRules());
            player.closeInventory();
        }
    }


    public static class EnchantsLimitsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.ENCHANTED_BOOK).setName("&cLimite d'enchantements").setLore(
                    "&fPermet de définir la limite des tous les",
                    "&fles enchantements",
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new EnchantmentManager(new ConfigurationMenu()).openMenu(player);
        }
    }

    private static class ScenariosButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.EYE_OF_ENDER).setName("&cGestion des scénarios").setLore(
                    "&fPermet d'enlever ou de rajouter des",
                    "&fscénarios",
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageScenariosMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    public static Button getRenameButton(Player player) {
        return new ConversationButton<>(
                new ItemBuilder(Material.ANVIL).setName("&cRenommer").setLore(
                        "&fPermet de modifier le nom du serveur",
                        " ",
                        "&8┃ &7Nom: &c" + UHC.getGameManager().getGameConfiguration().getCustomName(),
                        "",
                        "&f&l» &cCliquez-ici pour y accéder"
                ).toItemStack(),
                player, ChatUtil.prefix("&aMerci de rentrer le nouveau nom du serveur"),
                (name, pair) -> {
                    UHC.getGameManager().getGameConfiguration().setCustomName(pair.getRight());
                    new ConfigurationMenu().openMenu(player);
                }
        );
    }

    @Override
    public String getTitle(Player player) {
        return "Configuration";
    }

    private static class TimersButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.WATCH).setName("&cTimers").setLore(
                    "&fPermet de modifier tous les timers de la",
                    "&fpartie comme le PvP, la Bordure, etc.",
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new TimersMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    private static class ModeButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PRISMARINE_SHARD).setName("&cMode de Jeu").setLore(
                    "&fPermet de configurer toutes les options",
                    "&fdu mode de jeu de la partie",
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageRolesMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    public static class SettingsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.ITEM_FRAME).setName("&cOptions").setLore(
                    "&fPermet de modifier toutes les options",
                    "&fcomme la bordure, etc.",
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new SettingsMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    private static class ConfigButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BOOK_AND_QUILL).setName("&cPré-Configurations").setLore(
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new PreConfigurationMenu(true, new ConfigurationMenu()).openMenu(player);
        }
    }
}
