package fr.kohei.uhc;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.rank.Rank;
import fr.kohei.messaging.packet.ServerDeletePacket;
import fr.kohei.uhc.game.BukkitManager;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.generator.BiomesManager;
import fr.kohei.uhc.game.generator.OrePopulator;
import fr.kohei.uhc.game.generator.WorldGenCavesPatched;
import fr.kohei.uhc.module.ModuleManager;
import fr.kohei.uhc.task.TabListTask;
import fr.kohei.uhc.task.UpdateTask;
import fr.kohei.uhc.utils.ScoreboardTeam;
import fr.kohei.uhc.utils.frame.Frame;
import fr.kohei.uhc.utils.frame.ScoreboardAdapter;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class UHC extends JavaPlugin {

    @Getter
    private static UHC instance;

    private GameManager gameManager;
    private BukkitManager bukkitManager;
    private ModuleManager moduleManager;
    private List<ScoreboardTeam> teams;

    @Override
    public void onEnable() {
        instance = this;

        this.gameManager = new GameManager(this);
        this.bukkitManager = new BukkitManager(this);
        this.moduleManager = new ModuleManager();

        new TabListTask(this);
        new UpdateTask(this);
        new Frame(this, new ScoreboardAdapter());
        this.initRanks();

        try {
            BiomesManager.patchBiomes();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        final OrePopulator orePopulator = new OrePopulator();
        orePopulator.addRule(new OrePopulator.Rule(Material.COAL_ORE, 2, 0, 128, 17));
        orePopulator.addRule(new OrePopulator.Rule(Material.IRON_ORE, 5, 0, 64, 9));
        orePopulator.addRule(new OrePopulator.Rule(Material.GOLD_ORE, 3, 0, 32, 9));
        orePopulator.addRule(new OrePopulator.Rule(Material.REDSTONE_ORE, 3, 0, 16, 8));
        orePopulator.addRule(new OrePopulator.Rule(Material.DIAMOND_ORE, 1, 0, 16, 8));
        orePopulator.addRule(new OrePopulator.Rule(Material.LAPIS_ORE, 3, 0, 32, 7));

        World world = new WorldCreator("uhc_world").createWorld();
        world.getPopulators().add(orePopulator);
        getGameManager().setUhcWorld(world);

        try {
            WorldGenCavesPatched.load(world, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new ServerDeletePacket(Bukkit.getPort()));
    }

    public void initRanks() {
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
            ScoreboardTeam disabled = new ScoreboardTeam(character + "4", prefix, "");
            i++;

            teams.add(connected);
            teams.add(unlinked);
            teams.add(disconnected);
            teams.add(disabled);
        }
        teams.add(new ScoreboardTeam("aa", ChatUtil.translate("&r")));
    }

    public static final HashMap<String, Character> RANKS_ALPHABET = new HashMap<>();

    public ScoreboardTeam getScoreboardTeam(String name) {
        return teams.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    public static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();

}
