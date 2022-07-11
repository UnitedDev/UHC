package fr.kohei.uhc;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.rank.Rank;
import fr.kohei.messaging.packet.ServerDeletePacket;
import fr.kohei.uhc.game.BukkitManager;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.module.ModuleManager;
import fr.kohei.uhc.task.TabListTask;
import fr.kohei.uhc.task.UpdateTask;
import fr.kohei.uhc.utils.ScoreboardTeam;
import fr.kohei.uhc.utils.frame.Frame;
import fr.kohei.uhc.utils.frame.ScoreboardAdapter;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@Getter
public class UHC extends JavaPlugin {

    @Getter
    private static JavaPlugin plugin;
    @Getter
    private static GameManager gameManager;
    @Getter
    private static BukkitManager bukkitManager;
    @Getter
    private static ModuleManager moduleManager;
    @Getter
    private static List<ScoreboardTeam> teams;
    @Getter
    private static ScheduledExecutorService executorMonoThread;
    @Getter
    private static ScheduledExecutorService scheduledExecutorService;
    @Getter
    private static List<String> joinUser;
    @Getter
    private static List<String> unlinkedUsers;

    @Override
    public void onEnable() {

        UHC.plugin = this;
        gameManager = new GameManager(plugin);
        bukkitManager = new BukkitManager(plugin);
        moduleManager = new ModuleManager();
        joinUser = new ArrayList<>();
        unlinkedUsers = new ArrayList<>();

        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        executorMonoThread = Executors.newScheduledThreadPool(1);

        new TabListTask(plugin);
        new UpdateTask(plugin);
        new Frame(this, new ScoreboardAdapter());
        initRanks();

        gameManager.getLobby().getChunk().load();
        World world = new WorldCreator("uhc_world").createWorld();
        getGameManager().setUhcWorld(world);
    }

    @Override
    public void onDisable() {
        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new ServerDeletePacket(Bukkit.getPort()));
    }

    public static void initRanks() {
        teams = new ArrayList<>();

        Queue<Rank> ranksNotSorted = BukkitAPI.getCommonAPI().getRanks();
        List<Rank> ranks = ranksNotSorted.stream().sorted(Comparator.comparingInt(Rank::getPermissionPower).reversed()).collect(Collectors.toList());

        int i = 0;
        for (Rank value : ranks) {
            String prefix = ChatUtil.translate(value.getTabPrefix() + (value.getToken().equals("default") ? "" : " "));

            char character = alphabet[i];
            RANKS_ALPHABET.put(value.token(), character);
            ScoreboardTeam connected = new ScoreboardTeam(character + "1", prefix, " §a§l✔");
            ScoreboardTeam unlinked = new ScoreboardTeam(character + "2", prefix, " §6§l✈");
            ScoreboardTeam disconnected = new ScoreboardTeam(character + "3", prefix, " §c§l✖");
            i++;

            teams.add(connected);
            teams.add(unlinked);
            teams.add(disconnected);

            System.out.println(connected);
            System.out.println(unlinked);
            System.out.println(disconnected);
        }
        teams.add(new ScoreboardTeam("aa", ChatUtil.translate("&r")));
    }

    public static final HashMap<String, Character> RANKS_ALPHABET = new HashMap<>();

    public static ScoreboardTeam getScoreboardTeam(String name) {
        return teams.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    public static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();

}
