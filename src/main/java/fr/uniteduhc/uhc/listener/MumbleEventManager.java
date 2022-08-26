package fr.uniteduhc.uhc.listener;

import fr.uniteduhc.command.impl.PlayerCommands;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.mumble.api.LinkAPI;
import fr.uniteduhc.mumble.api.mumble.IUser;
import fr.uniteduhc.mumble.api.mumble.MumbleState;
import fr.uniteduhc.mumble.core.event.impl.MumbleDisconnectEvent;
import fr.uniteduhc.mumble.core.event.impl.MumbleJoinEvent;
import fr.uniteduhc.mumble.core.event.impl.MumbleLinkEvent;
import fr.uniteduhc.mumble.core.event.impl.MumbleUnlinkEvent;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.GameState;
import fr.uniteduhc.uhc.game.player.UPlayer;
import lombok.RequiredArgsConstructor;
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
