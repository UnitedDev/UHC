package fr.kohei.uhc.task;

import fr.kohei.BukkitAPI;
import fr.kohei.manager.server.UHCServer;
import fr.kohei.messaging.packet.UHCUpdatePacket;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.game.config.timers.Timers;
import fr.kohei.uhc.game.scenario.Scenario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class UpdateTask extends BukkitRunnable {

    public UpdateTask(JavaPlugin plugin) {
        this.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        GameManager manager = UHC.getGameManager();
        GameConfiguration config = manager.getGameConfiguration();
        UHCServer.ServerStatus type;
        if (manager.getGameState() == GameState.FINISHING || manager.getGameState() == GameState.PLAYING) {
            type = UHCServer.ServerStatus.PLAYING;
        } else if (manager.getSize() >= config.getSlots()) {
            type = UHCServer.ServerStatus.FULL;
        } else if (manager.isWhitelist()) {
            type = UHCServer.ServerStatus.CLOSED;
        } else {
            type = UHCServer.ServerStatus.OPENED;
        }

        List<String> scenarios = Arrays.stream(Scenario.values()).filter(Scenario::isEnabled).map(scenario -> scenario.getScenario().getName()).collect(Collectors.toList());

        int border = config.getBorderStartSize();
        if (manager.getUhcWorld() != null) {
            border = (int) manager.getUhcWorld().getWorldBorder().getSize() / 2;
        }

        int pvp = config.getPvpTimer();
        if (manager.getGameState() == GameState.PLAYING) {
            pvp = Timers.PVP.getCustomTimer().getTimer();
        }

        int meetup = config.getMeetupTimer();
        if (manager.getGameState() == GameState.PLAYING) {
            meetup = Timers.BORDER.getCustomTimer().getTimer();
        }

        UHCServer uhcServer = new UHCServer(Bukkit.getPort(), getType(), config.getCustomName(), manager.getHost(),
                config.getSlots(), type, manager.getSize(), config.getTeams(), border, config.getBorderEndSize(),
                pvp, meetup, scenarios, UHC.getGameManager().getPlayers());

        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new UHCUpdatePacket(uhcServer));
    }

    public UHCServer.ServerType getType() {
        return UHCServer.ServerType.MHA;
    }

}
