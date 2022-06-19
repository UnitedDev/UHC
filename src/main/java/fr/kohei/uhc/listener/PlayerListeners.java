package fr.kohei.uhc.listener;

import fr.kohei.mumble.api.LinkAPI;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.frame.ScoreboardTeam;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.game.config.timers.Timers;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.Module;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.TimeUtil;
import fr.kohei.utils.Title;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListeners implements Listener {

    private final Plugin plugin;

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        UPlayer uPlayer = UPlayer.get(player);
        GameManager gameManager = UHC.getGameManager();
        GameConfiguration gameConfiguration = gameManager.getGameConfiguration();

        if (LinkAPI.getApi().getMumbleManager().getUserFromName(player.getName()) == null) {
            LinkAPI.getApi().getMumbleManager().createUser(player.getName(), Bukkit.getPort() + "." + Bukkit.getPort());
        }

        if (uPlayer.hasHostAccess()) return;

        if (gameManager.getGameState() == GameState.LOBBY) {
            if (gameManager.getHost() == null) {
                gameManager.setHost(player.getName());
                return;
            }

            if (gameManager.getSize() >= gameConfiguration.getSlots()) {
                event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                event.setKickMessage("serveur plein");
                return;
            }
            if (gameManager.isWhitelist()) {
                boolean b = true;

                for (String s : gameManager.getWhitelisted()) {
                    if (s.equalsIgnoreCase(player.getName())) b = false;
                }

                if (!b) return;
                event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                event.setKickMessage("pas whitelist");
                return;
            }
            boolean b = true;

            for (String s : gameManager.getBan()) {
                if (s.equalsIgnoreCase(player.getName())) b = false;
            }

            if (b) return;
            event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
            event.setKickMessage("banni");

        } else {
            if (!gameConfiguration.isSpectators() && !gameManager.getPlayers().contains(player.getUniqueId())) {
                event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                event.setKickMessage("spectateurs désactivés");
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        GameManager gameManager = UHC.getGameManager();
        GameConfiguration gameConfiguration = gameManager.getGameConfiguration();
        UPlayer uPlayer = UPlayer.get(player);

        for (ScoreboardTeam team : UHC.getTeams()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(team.createTeam());
        }

        UHC.getScoreboardUtils().onLogin(player);

        if (gameManager.getGameState() == GameState.LOBBY) {

            gameManager.getPlayers().add(player.getUniqueId());
            Title.sendActionBar("&8❘ &a" + player.getName() + " &fa rejoint la partie &8(&7" + gameManager.getSize() + "&8/&7" + gameConfiguration.getSlots() + "&8)");
            if (gameManager.isRules()) {
                player.teleport(gameManager.getRulesLocation());
            } else {
                player.teleport(gameManager.getLobby());
            }

            uPlayer.updateLobbyHotbar();
        } else {
            if (uPlayer.isAlive()) {
                Bukkit.broadcastMessage(ChatUtil.translate(ChatUtil.prefix("&a" + player.getName() + " &fs'est reconnecté")));
            } else {
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(new Location(gameManager.getUhcWorld(), 0, 100, 0));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        GameManager gameManager = UHC.getGameManager();
        GameConfiguration gameConfiguration = gameManager.getGameConfiguration();
        UPlayer uPlayer = UPlayer.get(player);

        UHC.getScoreboardUtils().onLogout(player);
        UHC.getJoinUser().remove(player.getName());
        UHC.getUnlinkedUsers().remove(player.getName());

        if (gameManager.getGameState() == GameState.LOBBY) {
            gameManager.getPlayers().remove(player.getUniqueId());
            Title.sendActionBar("&8❘ &c" + player.getName() + " &fa quitté la partie &8(&7" + gameManager.getSize() + "&8/&7" + gameConfiguration.getSlots() + "&8)");
        }

        if (gameManager.getGameState() == GameState.PLAYING) {
            UUID uuid = player.getUniqueId();
            if (!uPlayer.isAlive()) return;

            Bukkit.broadcastMessage(ChatUtil.prefix("&c" + player.getName() + " &fs'est déconnecté. Il a &c" + TimeUtil.niceTime(uPlayer.getDisconnect() * 1000L) + " secondes &fpour se reconnecter ou il sera &céliminé&f."));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getPlayer(uuid) != null) {
                        cancel();
                        return;
                    }

                    uPlayer.setDisconnect(uPlayer.getDisconnect() - 1);

                    if (uPlayer.getDisconnect() <= 0) {
                        UHC.getGameManager().getPlayers().remove(uuid);
                        Bukkit.broadcastMessage(ChatUtil.translate("&7&m--------------------"));
                        Bukkit.broadcastMessage(ChatUtil.translate("&c" + uPlayer.getName() + " &fest mort de &cdéconnexion"));
                        Bukkit.broadcastMessage(ChatUtil.translate("&7&m--------------------"));
                        UHC.getModuleManager().getModule().onDisconnectDeath(uuid);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        World world = event.getTo().getWorld();
        int size = (int) world.getWorldBorder().getSize() / 2;

        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        if ((playerLocation.getBlockX() > size || playerLocation.getBlockZ() > size || playerLocation.getBlockX() < -size
                || playerLocation.getBlockZ() < -size) && player.getGameMode() == GameMode.SPECTATOR) {
            player.teleport(world.getSpawnLocation());
        }

        if (UHC.getGameManager().getGameState() == GameState.TELEPORTATION) {
            UPlayer uPlayer = UPlayer.get(player);
            if (uPlayer.getPlate() != null && playerLocation.getBlockY() <= 198) {
                Location loc = uPlayer.getPlate().getTeleportLocation().clone().add(0, 2, 0);
                loc.setPitch(player.getLocation().getPitch());
                loc.setYaw(player.getLocation().getYaw());
                player.teleport(loc);
            }
        }

        if (UHC.getGameManager().getGameState() == GameState.LOBBY) {
            if (playerLocation.getBlockY() <= 1) {
                player.teleport(UHC.getGameManager().getLobby());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (Timers.INVINCIBILITY.isLoading()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();

        event.setDeathMessage(null);

        Module module = UHC.getModuleManager().getModule();
        UPlayer uPlayer = UPlayer.get(player);

        Bukkit.broadcastMessage(ChatUtil.translate("&7&m--------------------"));
        if (module.hasRoles()) {
            Bukkit.broadcastMessage(ChatUtil.translate(" &8┃ &c" + player.getName() + " &fest mort"));
            if (uPlayer.getRole() == null) {
                Bukkit.broadcastMessage(ChatUtil.translate(" &8┃ &fIl n'avait &cpas &fde rôle"));
            } else {
                ChatColor chatColor = uPlayer.getRole().getStartCamp().getColor();
                Bukkit.broadcastMessage(ChatUtil.translate(" &8┃ &fIl était " + chatColor + uPlayer.getRole().getName()));
            }
        } else {
            if (killer == null) {
                Bukkit.broadcastMessage(ChatUtil.translate(" &8┃ &c" + player.getName() + " &fest mort tout seul"));
            } else {
                Bukkit.broadcastMessage(ChatUtil.translate(" &8┃ &c" + player.getName() + " &fa été tué par &c" + killer.getName()));
            }
        }
        Bukkit.broadcastMessage(ChatUtil.translate("&7&m--------------------"));
        Bukkit.getOnlinePlayers().forEach(player1 -> player1.playSound(player1.getLocation(), Sound.WITHER_DEATH, 1f, 1f));

        if (killer != null) {
            UPlayer uKiller = UPlayer.get(killer);
            uKiller.setKills(uKiller.getKills() + 1);

            if(uKiller.getRole() != null) uKiller.getRole().onKill(player, killer);
        }

        UHC.getGameManager().getPlayers().remove(player.getUniqueId());
        module.onDeath(player, killer);

        if(uPlayer.getRole() != null) uPlayer.getRole().onDeath(player, killer);

        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null || content.getType() == Material.AIR) continue;
            player.getWorld().dropItemNaturally(player.getLocation(), content);
        }
        for (ItemStack content : player.getInventory().getArmorContents()) {
            if (content == null || content.getType() == Material.AIR) continue;
            player.getWorld().dropItemNaturally(player.getLocation(), content);
        }
        for (ItemStack content : UHC.getGameManager().getGameConfiguration().getDeathInventory()) {
            if (content == null || content.getType() == Material.AIR) continue;
            player.getWorld().dropItemNaturally(player.getLocation(), content);
        }
        event.getDrops().clear();

        uPlayer.setLastLocation(player.getLocation());
        uPlayer.setLastInventory(player.getInventory().getContents());
        uPlayer.setLastArmor(player.getInventory().getArmorContents());

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.spigot().respawn();
            player.teleport(uPlayer.getLastLocation());
            player.setGameMode(GameMode.SPECTATOR);
        }, 19);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().clear();
    }

}
