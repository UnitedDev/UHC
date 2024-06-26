package fr.uniteduhc.uhc.game;

import fr.uniteduhc.mumble.api.LinkAPI;
import fr.uniteduhc.mumble.api.mumble.IUser;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.config.PreConfiguration;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import fr.uniteduhc.uhc.game.config.timers.Timers;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.uhc.game.scenario.AbstractScenario;
import fr.uniteduhc.uhc.game.scenario.Scenario;
import fr.uniteduhc.uhc.game.world.WorldGeneration;
import fr.uniteduhc.uhc.listener.MumbleEventManager;
import fr.uniteduhc.uhc.task.*;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.uhc.task.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameManager {

    private final JavaPlugin plugin;
    private GameConfiguration gameConfiguration;
    private final EpisodeManager episodeManager;

    private final List<UUID> players;
    private String host;
    private final List<UUID> coHosts;
    private GameState gameState;
    private World uhcWorld, lobbyWorld;
    private final Location lobby, rulesLocation;
    private boolean rules, whitelist;
    private StartTask startTask;
    private int duration;
    private UUID editingStartInventory;
    private UUID editingDeathInventory;
    private final List<String> ban;
    private final List<String> whitelisted;
    private CycleTask cycleTask;

    public GameManager(JavaPlugin plugin) {
        this.plugin = plugin;

        this.gameConfiguration = new GameConfiguration(this);
        this.episodeManager = new EpisodeManager(this);

        this.players = new ArrayList<>();
        this.host = null;
        this.coHosts = new ArrayList<>();
        this.gameState = GameState.LOBBY;
        this.lobbyWorld = Bukkit.getWorld("world");
        this.lobby = new Location(lobbyWorld, 0, 100, 0, -90, 0);
        this.rulesLocation = new Location(lobbyWorld, -3.5, 85, -9.5, -67, 0);

        this.rules = false;
        this.whitelist = true;
        this.ban = new ArrayList<>();
        this.whitelisted = new ArrayList<>();
    }

    public void generateChunks() {

        this.uhcWorld.setGameRuleValue("naturalRegeneration", "false");
        this.uhcWorld.setGameRuleValue("doDaylightCycle", "false");
        this.uhcWorld.setGameRuleValue("randomTickSpeed", "2");
        this.uhcWorld.setGameRuleValue("doMobSpawning", "false");
        this.uhcWorld.getWorldBorder().setCenter(0, 0);
        this.uhcWorld.setPVP(false);
        this.uhcWorld.getWorldBorder().setSize(2 * getGameConfiguration().getBorderStartSize());

        Bukkit.broadcastMessage(ChatUtil.prefix("&fLa prégénération vient de &acommencer&f."));
        new WorldGeneration(uhcWorld, (int) (uhcWorld.getWorldBorder().getSize() / 2)).load();
    }

    public int getSize() {
        return getPlayers().size();
    }

    public void loadGame() {
        setGameState(GameState.TELEPORTATION);
        clearAllPlayers();
        new ScatterTask(this);
        Bukkit.getScheduler().runTaskLater(plugin, () -> new LoadingTask().runTaskTimer(plugin, 0, 20), getPlayers().size() * 10L + 40);
    }

    private void clearAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setMaxHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.setWalkSpeed(0.2F);
        });
    }


    public void startGame() {
        setGameState(GameState.PLAYING);
        UHC.getInstance().getBukkitManager().loadModuleCommands();
        new GameTask(this);
        this.episodeManager.start();
        UHC.getInstance().getModuleManager().getModule().onStart();
        this.cycleTask = new CycleTask(this);
        Timers.onStart();

        Arrays.stream(Scenario.values())
                .filter(Scenario::isEnabled)
                .map(Scenario::getScenario)
                .forEach(AbstractScenario::onStart);
        clearAllPlayers();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UPlayer uPlayer = UPlayer.get(player);
            if (uPlayer.getPlate() != null) uPlayer.getPlate().destroy();
            player.getInventory().setArmorContents(getGameConfiguration().getStartArmor());
            player.getInventory().setContents(getGameConfiguration().getStartInventory());
        }

        if (LinkAPI.getApi().isEnabled())
            for (IUser user : LinkAPI.getApi().getMumbleManager().getServer().getUsers()) {
                MumbleEventManager.refresh(user);
            }
    }

    public Location getCenter() {
        return new Location(getUhcWorld(), 0, 100, 0);
    }

    public void applyConfiguration(PreConfiguration config) {
        setGameConfiguration(config.getGameConfiguration());
    }

}
