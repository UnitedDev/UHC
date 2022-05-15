package fr.kohei.uhc.utils;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import fr.kohei.uhc.menu.ConfigurationMenu;
import fr.kohei.uhc.menu.ManageScenariosMenu;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

public class UHCItems {

    public static final CustomItem HOST_CONFIGURATION = new CustomItem(Material.REDSTONE_COMPARATOR, "Configuration", click -> new ConfigurationMenu().openMenu(click.getPlayer()));
    public static final CustomItem FALLBACK = new CustomItem(Material.BED, "Retourner au Lobby", onClick -> {
        IPlayerManager manager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
        manager.getPlayerExecutor(onClick.getPlayer().getUniqueId()).connectToFallback();
    });
    public static final CustomItem SHOW_SCENARIOS = new CustomItem(Material.BOOK, "Scénarios", onClick -> new ManageScenariosMenu(null).openMenu(onClick.getPlayer()));
    public static final CustomItem MUMBLE = new CustomItem(getMumbleItem(), "Mumble", onClick -> {
//        IUser user = ArashiLinkAPI.getApi().getMumbleManager().getUserFromName(onClick.getPlayer().getName());
//        if(user == null || ArashiLinkAPI.getApi().getMumbleManager().getStateOf(user.getName()) == MumbleState.DISCONNECT) {
//            onClick.getPlayer().performCommand("mumble");
//            return;
//        }
//        new MumbleMenu(null).openMenu(onClick.getPlayer());
    });

    public static ItemStack getMumbleItem() {
        String base = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwYjFjZDBjYjEwZGNjM2U5OWJmNDEwNGIxMDM2MGM5Mjc5ZmEwYTJhYTdiZGVkMTQ4MzM1OWIwNDc0ZTExZSJ9fX0=";
        return new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(base).toItemStack();
    }

}
