package fr.kohei.uhc.menu;

import fr.kohei.menu.GlassMenu;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.game.world.WorldGeneration;
import fr.kohei.uhc.menu.options.*;
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
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.kohei.uhc.menu.ManageOptionsMenu.getCycleItem;

public class ConfigurationMenu extends GlassMenu {

    @Override
    public int getGlassColor() {
        return 11;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(21, new RulesButton());
        if (!(WorldGeneration.isFinished() || WorldGeneration.getPercentage() > 0))
            buttons.put(4, new LoadMapButton());
        buttons.put(6, new WhitelistButton());

        buttons.put(49, new StartButton());

        buttons.put(37, new TeleportationButton());
        buttons.put(10, getRenameButton(player));
        buttons.put(22, new TimersButton());
        buttons.put(23, new ScenariosButton());
        buttons.put(30, new EnchantsLimitsButton());
        buttons.put(2, new HiddenCompositionButton());
        buttons.put(31, new SettingsButton());
        buttons.put(16, new OresLimitButton());
        buttons.put(43, new CycleButton());
        buttons.put(32, new ModeButton());
        buttons.put(51, new StopServerButton());

        buttons.put(47, new SlotsButton());

        return buttons;
    }

    public static class SlotsButton extends Button{
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal())
                    .setName("&6&lGestion des Slots").setLore(
                            "&fPermet de modifier les slots pour la",
                            "&fpartie.",
                            "",
                            "&8┃ &7Configuration: &c" + UHC.getGameManager().getGameConfiguration().getSlots(),
                            "",
                            "&f&l» &eCliquez-ici pour y accéder"
                    ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManagerSlotsMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    private class CycleButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getCycleItem();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new CycleTimerMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    private static class HiddenCompositionButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean compo = UHC.getGameManager().getGameConfiguration().isHideComposition();
            return new ItemBuilder(Material.WEB).setName("&6&lComposition cachée").setLore(
                    "&fPermet de définir si la composition sera",
                    "&fcachée ou non.",
                    "",
                    "&fComposition: " + (compo ? "&cCachée" : "&aNon Cachée"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
            gameConfiguration.setHideComposition(!gameConfiguration.isHideComposition());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class StopServerButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.INK_SACK).setDurability(1).setName("&6&lStopper le serveur").setLore(
                    "&fPermet de supprimer complètement le",
                    "&fserveur",
                    "",
                    "&f&l» &eCliquez-ici pour supprimer"
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
            return new ItemBuilder(Material.DIAMOND).setName("&6&lLimite de minerais").setLore(
                    "&fPermet de limite le nombre de minerais",
                    "&fde diamants et d'ors pendant la partie",
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageOresLimitMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    private static class WhitelistButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            GameManager manager = UHC.getGameManager();
            return new ItemBuilder(Material.NAME_TAG).setName("&6&lAccessibilité de la partie").setLore(
                    "&fPermet de modifier l'accessibilité à la",
                    "&fpartie pour les joueurs.",
                    " ",
                    "&8┃ &7Accessibilité: " + (manager.isWhitelist() ? "&cFermé" : "&aOuvert"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
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
                return new ItemBuilder(Material.EMERALD_BLOCK).setName("&a&lLancer la partie").setLore(
                        "&fPermet de lancer la partie si tout est",
                        "&fprêt.",
                        "",
                        "&f&l» &eCliquez-ici pour lancer"
                ).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags().toItemStack();
            } else {
                return new ItemBuilder(Material.REDSTONE_BLOCK).setName("&c&lAnnuler le lancement").setLore(
                        "&fPermet d'annuler le lancement de la",
                        "&fpartie.",
                        "",
                        "&f&l» &eCliquez-ici pour annuler"
                ).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags().toItemStack();
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameManager gameManager = UHC.getGameManager();

            if (!WorldGeneration.isFinished()) {
                player.sendMessage(ChatUtil.prefix("&cLa prégénération doit être terminée pour lancer la partie."));
                return;
            }

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
            return new ItemBuilder(Material.SAPLING).setName("&6&lPrégénération").setLore(
                    "&fPermet de prégénérer toute la map",
                    "",
                    "&8┃ &7Bordure: &c± " + UHC.getGameManager().getGameConfiguration().getBorderStartSize(),
                    "",
                    "&f&l» &eCliquez-ici pour charger"
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
            lore.add("&f&l» &eCliquez-ici pour téléporter");
            return new ItemBuilder(Material.ENDER_PEARL).setName("&6&lTéléportation").setLore(lore).toItemStack();
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
            return new ItemBuilder(Material.ENCHANTED_BOOK).setName("&6&lLimite d'enchantements").setLore(
                    "&fPermet de définir la limite des tous les",
                    "&fles enchantements",
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
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
            return new ItemBuilder(Material.ANVIL).setName("&6&lGestion des scénarios").setLore(
                    "&fPermet d'enlever ou de rajouter des",
                    "&fscénarios",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageScenariosMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    public static Button getRenameButton(Player player) {
        return new ConversationButton<>(
                new ItemBuilder(Material.SIGN).setName("&6&lRenommer").setLore(
                        "&fPermet de modifier le nom du serveur",
                        " ",
                        "&8┃ &7Nom: &c" + UHC.getGameManager().getGameConfiguration().getCustomName(),
                        "",
                        "&f&l» &eCliquez-ici pour y accéder"
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
            return new ItemBuilder(Material.WATCH).setName("&6&lTimers").setLore(
                    "&fPermet de modifier tous les timers de la",
                    "&fpartie comme le PvP, la Bordure, etc.",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
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
            return new ItemBuilder(Material.PRISMARINE_SHARD).setName("&6&lMode de Jeu").setLore(
                    "&fPermet de configurer toutes les options",
                    "&fdu mode de jeu de la partie",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
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
            return new ItemBuilder(Material.ITEM_FRAME).setName("&6&lOptions").setLore(
                    "&fPermet de modifier toutes les options",
                    "&fcomme la bordure, etc.",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
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
            return new ItemBuilder(Material.BOOK_AND_QUILL).setName("&6&lPré-Configurations").setLore(
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new PreConfigurationMenu(true, new ConfigurationMenu()).openMenu(player);
        }
    }

    private static class RulesButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAINTING).setName("&6&lRègles").setLore(
                    "&fPermet de modifier les règles de la",
                    "&fpartie",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new RulesMenu(new ConfigurationMenu()).openMenu(player);
        }
    }
}
