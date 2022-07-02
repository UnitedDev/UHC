package fr.kohei.uhc.utils.world;

import com.google.common.collect.Lists;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Optional;

public class PreGenerationHandler {

    private List<PreGeneration> preGenerations;
    private BukkitTask percTask;

    public PreGenerationHandler() {
        this.preGenerations = Lists.newArrayList();
    }

    public void addPregeneration(PreGeneration preGeneration) {
        this.preGenerations.add(preGeneration);
        if(percTask == null) {
            this.percTask = Bukkit.getScheduler().runTaskTimer(UHC.getPlugin(), () -> {
                if(isRunning()) {
                    percentage();
                } else {
                    percTask.cancel();
                }
            }, 1L, 1L);
        }
    }

    public boolean isRunning() {
        Optional<PreGeneration> preGen = this.preGenerations.stream().filter(PreGeneration::isRunning).findAny();
        return preGen.isPresent();
    }

    public void percentage() {
        int todo = this.preGenerations.stream().mapToInt(PreGeneration::getTodo).sum();
        int pseudoCurrent = this.preGenerations.stream().mapToInt(PreGeneration::getCurrent).sum();
        int current = Math.min(pseudoCurrent, todo);

        Bukkit.getOnlinePlayers().forEach(p ->
                Title.sendActionBar(p,
                        ChatColor.GOLD + "Pré-Génération: " + ChatColor.LIGHT_PURPLE + "[" +
                        getProgressBar(current, todo, 60, '|', ChatColor.GREEN, ChatColor.RED) +
                        ChatColor.YELLOW + "%" + ChatColor.LIGHT_PURPLE + "]")
        );

        if(current >= todo) {
            Bukkit.broadcastMessage(ChatUtil.prefix("&aLa génération des zones de jeu est terminée"));
        }
    }

    public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
                                        ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return com.google.common.base.Strings.repeat("" + completedColor + symbol, progressBars)
                + com.google.common.base.Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
}
