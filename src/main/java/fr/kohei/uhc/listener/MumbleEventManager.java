package fr.kohei.uhc.listener;

import fr.kohei.mumble.api.mumble.IUser;
import fr.kohei.mumble.core.event.impl.MumbleDisconnectEvent;
import fr.kohei.mumble.core.event.impl.MumbleJoinEvent;
import fr.kohei.mumble.core.event.impl.MumbleLinkEvent;
import fr.kohei.mumble.core.event.impl.MumbleUnlinkEvent;
import fr.kohei.uhc.game.BukkitManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class MumbleEventManager implements Listener {
    private final Plugin plugin;

    @EventHandler
    public void onJoin(MumbleJoinEvent event) {
        IUser user = event.getUser();

        Bukkit.broadcastMessage("debug join : " + user);
    }

    @EventHandler
    public void onQuit(MumbleDisconnectEvent event) {
        String user = event.getUser();

        Bukkit.broadcastMessage("debug quit : " + user);
    }

    @EventHandler
    public void onLink(MumbleLinkEvent event) {
        IUser user = event.getUser();

        Bukkit.broadcastMessage("debug link : " + user);
    }

    @EventHandler
    public void onLink(MumbleUnlinkEvent event) {
        IUser user = event.getUser();

        Bukkit.broadcastMessage("debug unlink : " + user);
    }

}
