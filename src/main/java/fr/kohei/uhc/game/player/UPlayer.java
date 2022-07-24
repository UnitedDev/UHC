package fr.kohei.uhc.game.player;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.plate.SquarePlate;
import fr.kohei.uhc.module.manager.Camp;
import fr.kohei.uhc.module.manager.Role;
import fr.kohei.uhc.utils.UHCItems;
import fr.kohei.uhc.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class UPlayer {

    public static final HashMap<UUID, UPlayer> players = new HashMap<>();

    private final UUID uniqueId;
    private final String name;
    private ItemStack[] lastInventory, lastArmor;
    private Location lastLocation;
    private int kills, iron, gold, diamond, disconnect;
    private Camp camp;
    private Role role;
    private SquarePlate plate;

    public UPlayer(Player player) {
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();
        this.disconnect = UHC.getInstance().getGameManager().getGameConfiguration().getDisconnectTime();

        players.put(player.getUniqueId(), this);
    }

    public static UPlayer get(Player player) {
        if (players.containsKey(player.getUniqueId()))
            return players.get(player.getUniqueId());

        return new UPlayer(player);
    }

    public static UPlayer get(UUID uuid) {
        if (players.containsKey(uuid))
            return players.get(uuid);

        return null;
    }

    public boolean hasHostAccess() {
        GameManager gameManager = UHC.getInstance().getGameManager();
        if (gameManager.getHost() == null) return false;
        return gameManager.getCoHosts().contains(uniqueId) || gameManager.getHost().equalsIgnoreCase(name);
    }

    public boolean isMainHost() {
        GameManager gameManager = UHC.getInstance().getGameManager();
        return gameManager.getHost().equalsIgnoreCase(name);
    }

    public void updateLobbyHotbar() {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) return;

        Utils.resetPlayer(player);

        if (hasHostAccess()) {
            player.getInventory().setItem(4, UHCItems.HOST_CONFIGURATION.toItemStack());
        } else {
            player.getInventory().setItem(4, null);
        }
        player.getInventory().setItem(1, UHCItems.MUMBLE.toItemStack());
        player.getInventory().setItem(6, UHCItems.SHOW_SCENARIOS.toItemStack());
        player.getInventory().setItem(8, UHCItems.FALLBACK.toItemStack());
    }

    public boolean isEditingStartInventory() {
        if (UHC.getInstance().getGameManager().getEditingStartInventory() == null) return false;
        return UHC.getInstance().getGameManager().getEditingStartInventory().equals(this.uniqueId);
    }

    public boolean isEditingDeathInventory() {
        if (UHC.getInstance().getGameManager().getEditingDeathInventory() == null) return false;
        return UHC.getInstance().getGameManager().getEditingDeathInventory().equals(this.uniqueId);
    }

    public boolean isAlive() {
        return UHC.getInstance().getGameManager().getPlayers().contains(uniqueId);
    }
}
