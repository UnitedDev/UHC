package fr.kohei.uhc.game;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.config.PreConfiguration;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.game.config.timers.Timers;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.game.scenario.AbstractScenario;
import fr.kohei.uhc.game.scenario.Scenario;
import fr.kohei.uhc.game.world.WorldGeneration;
import fr.kohei.uhc.task.*;
import fr.kohei.uhc.utils.world.Cuboid;
import fr.kohei.uhc.utils.world.PreGeneration;
import fr.kohei.uhc.utils.world.PreGenerationHandler;
import fr.kohei.utils.ChatUtil;
import fr.kohei.uhc.task.*;
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
        new WorldGeneration(uhcWorld, (int) (uhcWorld.getWorldBorder().getSize() / 2) + 25).load();
//        int radius = (int) (uhcWorld.getWorldBorder().getSize() / 2) + 50;
//        Cuboid cuboid = new Cuboid(UUID.randomUUID().toString(), "uhc_world", this.uhcWorld,
//               0, 50, 0,radius, radius, 0, 0, false, false);
//
//        PreGenerationHandler handler = new PreGenerationHandler();
//        cuboid.getSubCuboids().forEach(subCuboid -> handler.addPregeneration(new PreGeneration(subCuboid)));
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
        UHC.getBukkitManager().loadModuleCommands();
        new GameTask(this);
        this.episodeManager.start();
        UHC.getModuleManager().getModule().onStart();
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
    }

    public Location getCenter() {
        return new Location(getUhcWorld(), 0, 100, 0);
    }

    public void applyConfiguration(PreConfiguration config) {
        setGameConfiguration(config.getGameConfiguration());
    }

}
