package fr.kohei.uhc.commands.players;

import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.common.CommonProvider;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.menu.GameInventoryMenu;
import fr.kohei.uhc.menu.PlayerRulesMenu;
import fr.kohei.utils.ChatUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerCommands {

    @Command(names = {"h", "h 1", "h help"})
    public static void hostHelp(Player player) {
        player.sendMessage(ChatUtil.translate("&7&m--------------------------------"));
        player.sendMessage(ChatUtil.translate("&8┃ &6&lCommandes de Host"));
        player.sendMessage(ChatUtil.translate("  &e/h op add &f<pseudo> &7(&fAjouter en co-host quelqu'un&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h op remove &f<pseudo> &7(&fRetirer le co-host de quelqu'un&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h op list &7(&fVoir la liste de co-host&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h ban &f<pseudo> &7(&fBannir quelqu'un de l'host&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h unban &f<pseudo> &7(&fDébannir quelqu'un de l'host&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h banlist &7(&fVoir la liste des bannis&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h kick &f<pseudo> &7(&fExpulser quelqu'un de la partie&7)"));
        TextComponent next = new TextComponent(ChatUtil.translate("&e&l[» PAGE 2]"));   
        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/h 2"));
        player.spigot().sendMessage(
                new TextComponent(ChatUtil.translate("&8┃ &fPage &6&l1 &fsur &6&l2 ")),
                next
        );
        player.sendMessage(ChatUtil.translate("&7&m--------------------------------"));
    }

    @Command(names = {"h 2", "h help 2"})
    public static void hostHelp2(Player player) {
        player.sendMessage(ChatUtil.translate("&7&m--------------------------------"));
        player.sendMessage(ChatUtil.translate("&8┃ &6&lCommandes de Host "));
        player.sendMessage(ChatUtil.translate("  &e/h force &f<timer> &7(&fForcer un timer&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h config &7(&fOuvrir le menu de configuration&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h wl add &f<pseudo> &7(&fAjouter quelqu'un à la whitelist&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h wl remove &f<pseudo> &7(&fRetirer quelqu'un à la whitelist&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h wl list &7(&fVoir la liste des joueurs whitelistés&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h spec add &f<pseudo> &7(&fAjouter un spectateur&7)"));
        player.sendMessage(ChatUtil.translate("  &e/h spec remove &f<pseudo> &7(&fRetirer un spectateur&7)"));
        TextComponent previous = new TextComponent(ChatUtil.translate("&e&l[« PAGE 1]"));
        previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/h 1"));
        player.spigot().sendMessage(
                new TextComponent(ChatUtil.translate("&8┃ &fPage &6&l2 &fsur &6&l2 ")),
                previous
        );
        player.sendMessage(ChatUtil.translate("&7&m--------------------------------"));
    }

    @Command(names = "helpop")
    public static void helpop(Player player, @Param(name = "message", wildcard = true) String message) {
        if (message.isEmpty()) {
            player.sendMessage(ChatUtil.translate("&cVous devez entrer un message"));
            return;
        }

        player.sendMessage(ChatUtil.prefix("&fVous avez envoyé un &ahelpop &favec succès"));
        Bukkit.getOnlinePlayers().forEach(player1 -> {
            if(UHC.getInstance().getGameManager().getPlayers().contains(player1.getUniqueId())) return;

            if (CommonProvider.getInstance().getProfile(player1.getUniqueId()).getRank().getPermissionPower() >= 39) {
                player1.sendMessage(ChatUtil.translate("&8» &c&lHELPOP &7(&f" + player.getName() + ": " + message));
            }
        });
    }

    @Command(names = {"uhc", "rules"})
    public static void rules(Player player) {
        new PlayerRulesMenu().openMenu(player);
    }

    @Command(names = {"inv", "inventaire"})
    public static void inv(Player player) {
        new GameInventoryMenu(null).openMenu(player);
    }
}
