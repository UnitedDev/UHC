package fr.kohei.uhc.task;

import fr.kohei.BukkitAPI;
import fr.kohei.common.CommonProvider;
import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.common.cache.rank.Grant;
import fr.kohei.common.cache.server.impl.UHCServer;
import fr.kohei.common.utils.messaging.list.packets.UHCUpdatePacket;
import fr.kohei.mumble.api.LinkAPI;
import fr.kohei.mumble.api.mumble.IUser;
import fr.kohei.mumble.api.mumble.MumbleState;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.game.config.timers.Timers;
import fr.kohei.uhc.game.scenario.Scenario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
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
                pvp, meetup, scenarios, UHC.getGameManager().getPlayers(), toUUIDs(UHC.getGameManager().getWhitelisted()));

        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new UHCUpdatePacket(uhcServer));

        List<String> users = UHC.getJoinUser();
        List<IUser> realsUsers = new ArrayList<>(LinkAPI.getApi().getMumbleManager().getServer().getUsers());

        for (IUser realsUser : realsUsers) {
            MumbleState state = LinkAPI.getApi().getMumbleManager().getStateOf(realsUser.getName());
            if (state == MumbleState.UNLINK) realsUser.muteUser();
            if (!users.contains(realsUser.getName())) {
                manageJoin(realsUser);
                users.add(realsUser.getName());
            }
        }

        for (String str : UHC.getUnlinkedUsers()) {
            IUser user = LinkAPI.getApi().getMumbleManager().getUserFromName(str);
            if(user == null) return;

            MumbleState state = LinkAPI.getApi().getMumbleManager().getStateOf(str);
            if(state == MumbleState.UNLINK) return;

            UHC.getUnlinkedUsers().remove(str);
            manageJoin(user);
        }
    }

    private void manageJoin(IUser user) {
        String name = user.getName();
        if (UHC.getGameManager().getHost().equalsIgnoreCase(name)) return;

        Player player = Bukkit.getPlayer(name);
        if (player == null) return;

        if (!UHC.getGameManager().getPlayers().contains(player.getUniqueId())) {
            user.muteUser();
            return;
        }
        if (UHC.getGameManager().getGameState() != GameState.PLAYING) user.muteUser();
    }

    public List<UUID> toUUIDs(List<String> string) {
        List<UUID> toReturn = new ArrayList<>();

        for (String s : string) {
            Map<UUID, ProfileData> map = CommonProvider.getInstance().getPlayers();
            toReturn.add(
                    map.keySet().stream().filter(uuid -> map.get(uuid).getDisplayName().equalsIgnoreCase(s)).findFirst().orElse(null)
            );
        }

        return toReturn;
    }

    public UHCServer.ServerType getType() {
        if (UHC.getModuleManager().getModule().getName().contains("Mugiwara")) return UHCServer.ServerType.MUGIWARA;
        else if (UHC.getModuleManager().getModule().getName().contains("MHA")) return UHCServer.ServerType.MHA;
        else return UHCServer.ServerType.MHA;
    }

}
