package fr.kohei.uhc.task;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.plate.SoloPlayerPlate;
import fr.kohei.uhc.game.plate.SquarePlate;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScatterTask extends BukkitRunnable {

    private final GameManager gameManager;
    private final List<Player> players;
    int teleported;
    Random random = new Random();

    public ScatterTask(GameManager gameManager) {
        this.gameManager = gameManager;
        players = new ArrayList<>();
        getGameManager().getPlayers().forEach(uuid -> players.add(Bukkit.getPlayer(uuid)));
        this.runTaskTimer(UHC.getInstance(), 0, 10);
        teleported = 0;
    }

    public GameManager getGameManager() {
        return gameManager;
    }


    @Override
    public void run() {

        if (players.size() == 0) {
            cancel();
            return;
        }

        Player player = players.get(0);
        if (player == null) return;

        World world = Bukkit.getWorld("uhc_world");
        int b = (int) world.getWorldBorder().getSize() - 50;
        int x = (random.nextInt(2) == 0 ? +1 : -1) * random.nextInt(b / 2);
        int z = (random.nextInt(2) == 0 ? +1 : -1) * random.nextInt(b / 2);

        Location location = new Location(world, x, 200, z);

        SquarePlate plate = new SquarePlate(location, 3, Material.STAINED_GLASS, 14);
        UPlayer uPlayer = UPlayer.get(player);
        uPlayer.setPlate(plate);
        new SoloPlayerPlate(player, uPlayer.getPlate()).create();

        player.teleport(location);

        player.setGameMode(GameMode.SURVIVAL);

        teleported++;

        Title.sendActionBar("&fTéléportation de &a" + player.getName() + " &8(&a" + teleported + "&8/&a" + getGameManager().getPlayers().size() + "&8)");

        players.remove(0);

    }
}
