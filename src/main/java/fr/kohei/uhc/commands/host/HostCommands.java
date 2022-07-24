package fr.kohei.uhc.commands.host;

import fr.kohei.BukkitAPI;
import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.common.CommonProvider;
import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.config.timers.Timers;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.menu.ConfigurationMenu;
import fr.kohei.uhc.menu.EnchantmentMenu;
import fr.kohei.uhc.menu.MumbleMenu;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HostCommands {

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
        UHC.getInstance().getGameManager().getPlayers().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).map(Bukkit::getPlayer).forEach(t ->
                t.getInventory().addItem(new ItemStack(mat, newAmount))
        );
    }

    @Command(names = "enchant")
    public static void enchant(Player sender) {
        UPlayer uPlayer = UPlayer.get(sender);
        if(!(uPlayer.isEditingStartInventory() || uPlayer.isEditingDeathInventory())) {
            sender.sendMessage(ChatUtil.prefix("&cVous devez être en train de modifier un inventaire pour exécuter cette commande."));
            return;
        }

        new EnchantmentMenu().openMenu(sender);
    }

    @Command(names = "finish")
    public static void finish(Player sender) {
        UPlayer uPlayer = UPlayer.get(sender);
        if(!uPlayer.hasHostAccess()) {
            sender.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if(uPlayer.isEditingStartInventory()) {
            UHC.getInstance().getGameManager().getGameConfiguration().setStartInventory(sender.getInventory().getContents());
            UHC.getInstance().getGameManager().getGameConfiguration().setStartArmor(sender.getInventory().getArmorContents());
            uPlayer.updateLobbyHotbar();
            sender.teleport(UHC.getInstance().getGameManager().getLobby());
            sender.sendMessage(ChatUtil.prefix("&fVous avez &amodifié &fl'inventaire de &adépart"));
            UHC.getInstance().getGameManager().setEditingStartInventory(null);
        } else if(uPlayer.isEditingDeathInventory()) {
            UHC.getInstance().getGameManager().getGameConfiguration().setDeathInventory(sender.getInventory().getContents());
            uPlayer.updateLobbyHotbar();
            sender.teleport(UHC.getInstance().getGameManager().getLobby());
            sender.sendMessage(ChatUtil.prefix("&fVous avez &amodifié &fl'inventaire de &cmort"));
            UHC.getInstance().getGameManager().setEditingDeathInventory(null);
        } else {
            sender.sendMessage(ChatUtil.prefix("&cVous n'êtes pas en train de modifier un inventaire"));
        }
    }

    @Command(names = {"h op add"})
    public static void opPlayer(Player player, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getInstance().getGameManager();
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

    @Command(names = {"disperse"}, power = 39)
    public static void disperse(Player player, @Param(name = "player") Player target) {
        int x = (int) (Math.random() * (UHC.getInstance().getGameManager().getUhcWorld().getWorldBorder().getSize() / 2));
        int z = (int) (Math.random() * (UHC.getInstance().getGameManager().getUhcWorld().getWorldBorder().getSize() / 2));
        int y = UHC.getInstance().getGameManager().getUhcWorld().getHighestBlockYAt(x, z) + 1;

        Location location = new Location(UHC.getInstance().getGameManager().getUhcWorld(), x, y, z);
        target.teleport(location);
        player.sendMessage(ChatUtil.prefix("&a" + target.getName() + " &fa été dispersé."));
    }

    @Command(names = {"h center"})
    public static void center(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getInstance().getGameManager().getGameState() != GameState.LOBBY) return;

        if (player.getWorld().getName().equalsIgnoreCase("world")) {
            player.teleport(new Location(Bukkit.getWorld("uhc_world"), 0, 100, 0));
        } else {
            player.teleport(UHC.getInstance().getGameManager().getLobby());
        }
    }

    @Command(names = {"h ban"})
    public static void ban(Player player, @Param(name = "player") Player target, @Param(name = "raison", wildcard = true) String raison) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.isMainHost()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getInstance().getGameManager();

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

        GameManager gameManager = UHC.getInstance().getGameManager();

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

        GameManager gameManager = UHC.getInstance().getGameManager();
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

        GameManager gameManager = UHC.getInstance().getGameManager();
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

        GameManager gameManager = UHC.getInstance().getGameManager();
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

        if (UHC.getInstance().getGameManager().getGameState() != GameState.PLAYING) {
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

        if (UHC.getInstance().getGameManager().getGameState() != GameState.PLAYING) {
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

        if (UHC.getInstance().getGameManager().getGameState() != GameState.PLAYING) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande si la game n'a pas commencé"));
            return;
        }

        UHC.getInstance().getGameManager().getGameConfiguration().setGroups(number);
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

        if (UHC.getInstance().getGameManager().getGameState() != GameState.PLAYING) {
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

        if (UHC.getInstance().getGameManager().getGameState() != GameState.PLAYING) {
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

        if (!UHC.getInstance().getModuleManager().getModule().hasRoles()) return;

        if (UHC.getInstance().getGameManager().getGameState() != GameState.PLAYING) {
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

        UHC.getInstance().getGameManager().getCoHosts().add(player.getUniqueId());
        GameManager gameManager = UHC.getInstance().getGameManager();

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
        for (String s : UHC.getInstance().getGameManager().getWhitelisted()) {
            if (s.equalsIgnoreCase(target)) {
                b = false;
                break;
            }
        }

        if (!b) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà whitelist"));
            return;
        }

        UHC.getInstance().getGameManager().getWhitelisted().add(target);
        CommonProvider.getInstance().getPlayers().values().stream()
                .filter(uPlayer1 -> uPlayer1.getDisplayName().equalsIgnoreCase(target))
                .findFirst()
                .ifPresent(uPlayer1 -> BukkitAPI.getCommonAPI().getServerCache().getUhcServers().get(Bukkit.getPort()).getWhitelistedPlayers().add(uPlayer1.getUniqueId()));
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
        for (String s : UHC.getInstance().getGameManager().getWhitelisted()) {
            if (s.equalsIgnoreCase(target)) {
                b = false;
                break;
            }
        }

        if (b) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas whitelist"));
            return;
        }

        UHC.getInstance().getGameManager().getWhitelisted().remove(target);
        CommonProvider.getInstance().getPlayers().values().stream()
                .filter(uPlayer1 -> uPlayer1.getDisplayName().equalsIgnoreCase(target))
                .findFirst()
                .ifPresent(uPlayer1 -> BukkitAPI.getCommonAPI().getServerCache().getUhcServers().get(Bukkit.getPort()).getWhitelistedPlayers().remove(uPlayer1.getUniqueId()));
        player.sendMessage(ChatUtil.prefix("&fVous avez retiré &c" + target + " &fde la &cwhitelist"));
    }

    @Command(names = {"h spec add"})
    public static void specAdd(Player player, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getInstance().getGameManager().getGameState() != GameState.LOBBY) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cet item pendant la partie"));
            return;
        }

        if (!UHC.getInstance().getGameManager().getPlayers().contains(target.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà spectateur"));
            return;
        }

        if (UHC.getInstance().getGameManager().getGameState() != GameState.LOBBY) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas retirer un spec pendant la partie."));
            return;
        }

        player.sendMessage(ChatUtil.prefix("&fVous avez mis en mode &aspectateur &fle joueur &a" + target));
        target.setGameMode(GameMode.SPECTATOR);
        UHC.getInstance().getGameManager().getPlayers().remove(target.getUniqueId());
    }


    @Command(names = {"h spec remove"})
    public static void specRemove(Player player, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        if (UHC.getInstance().getGameManager().getGameState() != GameState.LOBBY) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas retirer un spec pendant la partie."));
            return;
        }

        player.sendMessage(ChatUtil.prefix("&fVous avez &cenlevé le mode &cspectateur &fdu joueur &c" + target));
        target.setGameMode(GameMode.SURVIVAL);
        target.teleport(UHC.getInstance().getGameManager().getLobby());
        UHC.getInstance().getGameManager().getPlayers().add(target.getUniqueId());

    }

    @Command(names = {"h whitelist list", "h wl list"})
    public static void whitelistList(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        GameManager gameManager = UHC.getInstance().getGameManager();
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

    @Command(names = {"mumble panel", "mumble config"})
    public static void mumblePanel(Player player) {
        UPlayer uPlayer = UPlayer.get(player);
        if (!uPlayer.hasHostAccess()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'exécuter cette commande"));
            return;
        }

        new MumbleMenu(null).openMenu(player);
    }

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

        if (UHC.getInstance().getGameManager().getPlayers().contains(target.getUniqueId())) {
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
        UHC.getInstance().getGameManager().getPlayers().add(target.getUniqueId());
    }

}
