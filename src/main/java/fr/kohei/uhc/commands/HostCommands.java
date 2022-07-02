package fr.kohei.uhc.commands;

import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.BukkitAPI;
import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.config.timers.Timers;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.menu.ConfigurationMenu;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HostCommands {

    @Command(names = {"h", "h 1", "h help"})
    public static void hostHelp(Player player) {
        player.sendMessage(" ");
        player.sendMessage(ChatUtil.translate("&8» &c&lAide &l/h"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cop add &f<pseudo> &8» &7Ajouter en co-host quelqu'un"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cop remove &f<pseudo> &8» &7Retirer le co-host de quelqu'un"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cop list &8» &7Voir la liste de co-host"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cban &f<pseudo> &8» &7Bannir quelqu'un de l'host"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cunban &f<pseudo> &8» &7Débannir quelqu'un de l'host"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cbanlist &8» &7Voir la liste des bannis"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &ckick &f<pseudo> &8» &7Expulser quelqu'un de la partie"));
        player.sendMessage(ChatUtil.translate("&fPage &c1 &fsur &c2"));
    }

    @Command(names = {"h 2", "h help 2"})
    public static void hostHelp2(Player player) {
        player.sendMessage(" ");
        player.sendMessage(ChatUtil.translate("&8» &c&lAide &l/h"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cforce &f<timer> &8» &7Forcer un timer"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cconfig &8» &7Ouvrir le menu de configuration"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cwl add &f<pseudo> &8» &7Ajouter quelqu'un à la whitelist"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cwl remove &f<pseudo> &8» &7Retirer quelqu'un à la whitelist"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cwl list &8» &7Voir la liste des joueurs whitelistés"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cwl spec add &f<pseudo> &8» &7Ajouter un spectateur"));
        player.sendMessage(ChatUtil.translate(" &7■ &4/&7h &cwl spec remove &f<pseudo> &8» &7Retirer un spectateur"));
        player.sendMessage(ChatUtil.translate("&fPage &c2 &fsur &c2"));
    }

    @Command(names = {"h op add"})
    public static void opPlayer(Player player, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getGameManager();
        UPlayer uTarget = UPlayer.get(target);
        if (uTarget.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà co-host"));
        }

        gameManager.getCoHosts().add(target.getUniqueId());
        player.sendMessage(ChatUtil.prefix("&a" + target.getName() + " &fest désormais &aco-host&f."));

        if (gameManager.getGameState() == GameState.LOBBY) {
            UPlayer.get(target).updateLobbyHotbar();
        }
    }

    @Command(names = {"h ban"})
    public static void ban(Player player, @Param(name = "player") Player target, @Param(name = "raison", wildcard = true) String raison) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getGameManager();

        gameManager.getBan().add(target.getName());
        player.sendMessage(ChatUtil.prefix("&fVous avez &cbanni &c" + target.getName() + " &fde cet host."));
        target.sendMessage(ChatUtil.prefix("&fVous avez été &cbanni &fpar &c" + player.getName() + " &fpour &c" + raison));

        target.kickPlayer("banned");
    }

    @Command(names = {"h unban"})
    public static void unban(Player player, @Param(name = "player") String target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getGameManager();

        if (!gameManager.getBan().contains(target)) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas banni de vos hosts"));
            return;
        }

        gameManager.getBan().remove(target);
        player.sendMessage(ChatUtil.prefix("&fVous avez &adébanni &c" + target + " &fde cet host."));
    }


    @Command(names = {"h banlist"})
    public static void banList(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getGameManager();
        if (gameManager.getBan().isEmpty()) {
            player.sendMessage(ChatUtil.prefix("&cPersonne n'est banni"));
            return;
        }

        player.sendMessage(" ");
        player.sendMessage(ChatUtil.translate("&8» &c&lBan-List"));
        gameManager.getBan().forEach(s -> player.sendMessage(ChatUtil.translate(" &7■ &f" + s)));
        player.sendMessage(" ");
    }

    @Command(names = {"h op remove"})
    public static void deOp(Player player, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getGameManager();
        UPlayer uTarget = UPlayer.get(target);
        if (!uTarget.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas co-host"));
        }

        gameManager.getCoHosts().remove(target.getUniqueId());
        player.sendMessage(ChatUtil.prefix("&c" + target.getName() + " &fn'est plus &cco-host&f."));


        if (gameManager.getGameState() == GameState.LOBBY) {
            UPlayer.get(target).updateLobbyHotbar();
        }
    }

    @Command(names = {"h op list"})
    public static void opList(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getGameManager();
        if (gameManager.getCoHosts().isEmpty()) {
            player.sendMessage(ChatUtil.prefix("&cPersonne n'est op"));
            return;
        }

        player.sendMessage(" ");
        player.sendMessage(ChatUtil.translate("&8» &c&lOP-List"));
        gameManager.getCoHosts().stream()
                .map(uuid -> UPlayer.get(uuid).getName())
                .forEach(s -> player.sendMessage(ChatUtil.translate(" &7■ &f" + s)));
        player.sendMessage(" ");

    }

    @Command(names = {"h kick"})
    public static void kick(Player player, @Param(name = "player") Player target, @Param(name = "raison", wildcard = true) String raison) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        target.sendMessage(ChatUtil.prefix("&fVous avez été &ckick &fpar &c" + player.getName() + " &fpour &c" + raison));
        player.sendMessage(ChatUtil.prefix("&fVous avez &ckick &c" + target.getName() + " &fde la partie"));
        target.kickPlayer("kicked");
    }

    @Command(names = {"h force pvp"})
    public static void forcePvP(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getGameManager().getGameState() != GameState.PLAYING) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande si la game n'a pas commencé"));
            return;
        }

        if (!Timers.PVP.isLoading() || Timers.PVP.getCustomTimer().getTimer() <= 5) return;

        Timers.PVP.getCustomTimer().setTimer(5);
        Bukkit.broadcastMessage(ChatUtil.prefix("&fActivation du &cPvP &fdans &c5 secondes&f."));
    }

    @Command(names = {"h heal"})
    public static void heal(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getGameManager().getGameState() != GameState.PLAYING) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande si la game n'a pas commencé"));
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            player1.setFoodLevel(20);
            player1.setHealth(player1.getMaxHealth());
            player1.sendMessage(ChatUtil.prefix("&fVous avez été &dsoigné&f."));
        });
    }

    @Command(names = {"h setgroup"})
    public static void setgroups(Player player, @Param(name = "nombre") int number) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getGameManager().getGameState() != GameState.PLAYING) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande si la game n'a pas commencé"));
            return;
        }

        UHC.getGameManager().getGameConfiguration().setGroups(number);
        Bukkit.getOnlinePlayers().forEach(player1 -> {
            Title.sendTitle(player1, 10, 100, 10, "&c⚠ &cAttention &c⚠", "&cVeuillez respecter les groupes");
        });
    }

    @Command(names = {"h group"})
    public static void groups(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getGameManager().getGameState() != GameState.PLAYING) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande si la game n'a pas commencé"));
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            Title.sendTitle(player1, 10, 100, 10, "&c⚠ &cAttention &c⚠", "&cVeuillez respecter les groupes");
        });
    }

    @Command(names = {"h force mur"})
    public static void forceMur(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getGameManager().getGameState() != GameState.PLAYING) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande si la game n'a pas commencé"));
            return;
        }

        if (!Timers.BORDER.isLoading() || Timers.BORDER.getCustomTimer().getTimer() <= 5) return;

        Timers.BORDER.getCustomTimer().setTimer(5);
        Bukkit.broadcastMessage(ChatUtil.prefix("&fActivation de la &cBordure &fdans &c5 secondes&f."));
    }

    @Command(names = {"h force roles"})
    public static void forceRoles(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (!UHC.getModuleManager().getModule().hasRoles()) return;

        if (UHC.getGameManager().getGameState() != GameState.PLAYING) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande si la game n'a pas commencé"));
            return;
        }

        if (!Timers.ROLES.isLoading() || Timers.ROLES.getCustomTimer().getTimer() <= 5) return;

        Timers.ROLES.getCustomTimer().setTimer(5);
        Bukkit.broadcastMessage(ChatUtil.prefix("&fAnnonce des &cRôles &fdans &c5 secondes&f."));
    }

    @Command(names = {"h config"})
    public static void config(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        new ConfigurationMenu().openMenu(player);
    }

    @Command(names = {"h me"}, power = 40)
    public static void hostMe(Player player) {
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        UHC.getGameManager().getCoHosts().add(player.getUniqueId());
        GameManager gameManager = UHC.getGameManager();

        if (gameManager.getGameState() == GameState.LOBBY) {
            UPlayer.get(player).updateLobbyHotbar();
        }
    }

    @Command(names = {"h whitelist add", "h wl add"})
    public static void whitelistAdd(Player player, @Param(name = "player") String target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        boolean b = true;
        for (String s : UHC.getGameManager().getWhitelisted()) {
            if (s.equalsIgnoreCase(target)) {
                b = false;
                break;
            }
        }

        if (!b) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà whitelist"));
            return;
        }

        UHC.getGameManager().getWhitelisted().add(target);
        player.sendMessage(ChatUtil.prefix("&fVous avez &cwhitelist &fle joueur &c" + target));
    }

    @Command(names = {"h whitelist remove", "h wl remove"})
    public static void whitelistRemove(Player player, @Param(name = "player") String target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        boolean b = true;
        for (String s : UHC.getGameManager().getWhitelisted()) {
            if (s.equalsIgnoreCase(target)) {
                b = false;
                break;
            }
        }

        if (b) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas whitelist"));
            return;
        }

        UHC.getGameManager().getWhitelisted().remove(target);
        player.sendMessage(ChatUtil.prefix("&fVous avez retiré &c" + target + " &fde la &cwhitelist"));
    }

    @Command(names = {"h spec add"})
    public static void specAdd(Player player, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getGameManager().getGameState() != GameState.LOBBY) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cet item pendant la partie"));
            return;
        }

        if (!UHC.getGameManager().getPlayers().contains(target.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà spectateur"));
            return;
        }

        player.sendMessage(ChatUtil.prefix("&fVous avez mis en mode &cspectateur &fle joueur &c" + target));
        target.setGameMode(GameMode.SPECTATOR);
        UHC.getGameManager().getPlayers().remove(target.getUniqueId());
    }


    @Command(names = {"h spec remove"})
    public static void specRemove(Player player, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        player.sendMessage(ChatUtil.prefix("&cPour que ce joueur ne soit plus spectateurs, il faut qu'il se qu'il se déconnecte de la partie"));
    }

    @Command(names = {"h whitelist list", "h wl list"})
    public static void whitelistList(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getGameManager();
        if (gameManager.getWhitelisted().isEmpty()) {
            player.sendMessage(ChatUtil.prefix("&cPersonne n'est whitelist"));
            return;
        }

        player.sendMessage(" ");
        player.sendMessage(ChatUtil.translate("&8» &c&lWhitelist-List"));
        gameManager.getWhitelisted().forEach(s -> player.sendMessage(ChatUtil.translate(" &7■ &f" + s)));
        player.sendMessage(" ");
    }

    @Command(names = {"h stop"})
    public static void stop(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        Bukkit.broadcastMessage(ChatUtil.prefix("&c" + player.getName() + " &fa stoppé le serveur"));
        Bukkit.getServer().shutdown();
    }

    @Command(names = {"revive", "h revive"})
    public static void revive(Player player, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        UPlayer uTarget = UPlayer.get(target);
        if (uTarget.getLastLocation() == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est jamais mort."));
            return;
        }

        if (UHC.getGameManager().getPlayers().contains(target.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà en vie."));
            return;
        }

        Bukkit.broadcastMessage(ChatUtil.prefix("&c" + target.getName() + " &fa été revive"));
        target.getInventory().setContents(uTarget.getLastInventory());
        target.getInventory().setArmorContents(uTarget.getLastArmor());
        uTarget.getLastLocation().getWorld().getEntities().stream().filter(entity -> entity instanceof Item)
                .filter(entity -> entity.getLocation().distance(uTarget.getLastLocation()) <= 7).forEach(Entity::remove);
        target.setGameMode(GameMode.SURVIVAL);
        target.teleport(uTarget.getLastLocation());
        UHC.getGameManager().getPlayers().add(target.getUniqueId());
    }

}
