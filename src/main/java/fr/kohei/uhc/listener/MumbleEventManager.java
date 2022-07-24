package fr.kohei.uhc.listener;

import fr.kohei.command.impl.PlayerCommands;
import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.mumble.api.LinkAPI;
import fr.kohei.mumble.api.mumble.IUser;
import fr.kohei.mumble.api.mumble.MumbleState;
import fr.kohei.mumble.core.event.impl.MumbleDisconnectEvent;
import fr.kohei.mumble.core.event.impl.MumbleJoinEvent;
import fr.kohei.mumble.core.event.impl.MumbleLinkEvent;
import fr.kohei.mumble.core.event.impl.MumbleUnlinkEvent;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.player.UPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class MumbleEventManager implements Listener {
    private final Plugin plugin;

    @EventHandler
    public void onJoin(MumbleJoinEvent event) {
        IUser user = event.getUser();

        refresh(user);
    }

    @EventHandler
    public void onQuit(MumbleDisconnectEvent event) {
        String user = event.getUser();
    }

    @EventHandler
    public void onLink(MumbleLinkEvent event) {
        IUser user = event.getUser();

        refresh(user);
    }

    @EventHandler
    public void onLink(MumbleUnlinkEvent event) {
        IUser user = event.getUser();

        refresh(user);
    }

    public static void refresh(IUser user) {
        ProfileData profileData = PlayerCommands.getProfile(user.getName());

        if (profileData == null) {
            user.muteUser();
            return;
        }

        UPlayer uPlayer = UPlayer.get(profileData.getUniqueId());
        if (uPlayer == null) {
            user.muteUser();
            return;
        }

        GameState gameState = UHC.getInstance().getGameManager().getGameState();
        if (gameState == GameState.LOBBY) {
            if (uPlayer.hasHostAccess()) {
                user.unmuteUser();
            } else {
                user.muteUser();
            }
        } else {
            boolean link = (LinkAPI.getApi().getMumbleManager().getStateOf(user.getName()) != MumbleState.UNLINK);

            if(link) {
                if (uPlayer.isAlive()) {
                    user.unmuteUser();
                } else {
                    user.muteUser();
                }
            } else {
                user.muteUser();
            }
        }
    }
}
