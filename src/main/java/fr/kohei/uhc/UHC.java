package fr.kohei.uhc;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.rank.Rank;
import fr.kohei.messaging.packet.ServerDeletePacket;
import fr.kohei.uhc.frame.ScoreboardTeam;
import fr.kohei.uhc.frame.ScoreboardUtils;
import fr.kohei.uhc.game.BukkitManager;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.module.ModuleManager;
import fr.kohei.uhc.task.TabListTask;
import fr.kohei.uhc.task.UpdateTask;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    private static ScoreboardUtils scoreboardUtils;
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

        scoreboardUtils = new ScoreboardUtils();

        new TabListTask(plugin);
        new UpdateTask(plugin);

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

        for (Rank value : BukkitAPI.getCommonAPI().getRanks()) {
            String prefix = ChatUtil.translate(value.getTabPrefix());
            int position = value.getPermissionPower();

            ScoreboardTeam connected = new ScoreboardTeam(number((position)) + 1, prefix, " §a§l✔");
            ScoreboardTeam unlinked = new ScoreboardTeam(number((position)) + 2, prefix, " §6§l✈");
            ScoreboardTeam disconnected = new ScoreboardTeam(number((position)) + 3, prefix, " §c§l✖");

            teams.add(connected);
            teams.add(unlinked);
            teams.add(disconnected);

            System.out.println(connected);
            System.out.println(unlinked);
            System.out.println(disconnected);
        }
        teams.add(new ScoreboardTeam("aa", ChatUtil.translate("&r")));
    }

    public static ScoreboardTeam getScoreboardTeam(String name) {
        return teams.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    public static int number(float power) {
        if(power == 0) power = 1;
        return (int) ((1F / power) * 100000F);
    }

}
