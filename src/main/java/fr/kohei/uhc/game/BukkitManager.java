package fr.kohei.uhc.game;

import fr.kohei.BukkitAPI;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.commands.host.*;
import fr.kohei.uhc.commands.players.PlayerCommands;
import fr.kohei.uhc.listener.BasicCancelListeners;
import fr.kohei.uhc.listener.GameOptionsListener;
import fr.kohei.uhc.listener.MumbleEventManager;
import fr.kohei.uhc.listener.PlayerListeners;
import fr.kohei.uhc.module.Module;
import fr.kohei.uhc.module.commands.ModuleCommands;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public class BukkitManager {

    private final Plugin plugin;

    public BukkitManager(JavaPlugin plugin) {
        this.plugin = plugin;

        loadCommands();
        loadListeners();
    }

    public void loadListeners() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerListeners(plugin), plugin);
        pluginManager.registerEvents(new BasicCancelListeners(plugin), plugin);
        pluginManager.registerEvents(new GameOptionsListener(plugin), plugin);
        pluginManager.registerEvents(new MumbleEventManager(plugin), plugin);

    }

    public void loadCommands() {
        BukkitAPI.getCommandHandler().registerClass(HostCommands.class);
        BukkitAPI.getCommandHandler().registerClass(PlayerCommands.class);

    }

    public void loadModuleCommands() {
        BukkitAPI.getCommandHandler().registerClass(ModuleCommands.class);
    }
}
