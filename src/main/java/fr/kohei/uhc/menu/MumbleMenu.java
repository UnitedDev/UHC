//package fr.kohei.uhc.menu;
//
//import fr.kohei.link.api.ArashiLinkAPI;
//import fr.kohei.link.api.mumble.IUser;
//import fr.kohei.link.api.mumble.MumbleState;
//import fr.kohei.link.core.ArashiLink;
//import fr.kohei.menu.Button;
//import fr.kohei.menu.Menu;
//import fr.kohei.menu.pagination.PaginatedMenu;
//import fr.kohei.uhc.UHC;
//import fr.kohei.uhc.game.player.UPlayer;
//import fr.kohei.uhc.utils.UHCItems;
//import fr.kohei.utils.ItemBuilder;
//import lombok.RequiredArgsConstructor;
//import org.bukkit.Bukkit;
//import org.bukkit.Material;
//import org.bukkit.SkullType;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.ClickType;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RequiredArgsConstructor
//public class MumbleMenu extends PaginatedMenu {
//    private final Menu oldMenu;
//
//    @Override
//    public String getPrePaginatedTitle(Player player) {
//        return "Mumble";
//    }
//
//    @Override
//    public Map<Integer, Button> getGlobalButtons(Player player) {
//        Map<Integer, Button> buttons = new HashMap<>();
//
//        if(UPlayer.get(player).hasHostAccess())
//        buttons.put(3, new Button() {
//            @Override
//            public ItemStack getButtonItem(Player player) {
//                return new ItemBuilder(Material.INK_SACK).setDurability(10).setName("&cMute tout le monde").setLore(
//                        "",
//                        "&f&l» &cRendre muet tout le monde"
//                ).toItemStack();
//            }
//
//            @Override
//            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
//                for (IUser user : ArashiLink.getApi().getMumbleManager().getServer().getUsers()) {
//                    if(user.getName().equalsIgnoreCase(player.getName())) continue;
//                    user.muteUser();
//                }
//            }
//        });
//        if(UPlayer.get(player).hasHostAccess())
//        buttons.put(5, new Button() {
//            @Override
//            public ItemStack getButtonItem(Player player) {
//                return new ItemBuilder(Material.INK_SACK).setDurability(1).setName("&cUnmute tout le monde").setLore(
//                        "",
//                        "&f&l» &cRendre la parole à la partie"
//                ).toItemStack();
//            }
//
//            @Override
//            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
//                for (IUser user : ArashiLink.getApi().getMumbleManager().getServer().getUsers()) {
//                    user.unmuteUser();
//                }
//            }
//        });
//        buttons.put(4, new GlobalButton());
//
//        return buttons;
//    }
//
//    @Override
//    public Map<Integer, Button> getAllPagesButtons(Player player) {
//        Map<Integer, Button> buttons = new HashMap<>();
//
//        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//            buttons.put(buttons.size(), new MumbleButton(onlinePlayer));
//        }
//
//        return buttons;
//    }
//
//    @Override
//    public Menu backButton() {
//        return oldMenu;
//    }
//
//    @RequiredArgsConstructor
//    public static class MumbleButton extends Button {
//        private final Player player;
//
//        @Override
//        public ItemStack getButtonItem(Player param) {
//            List<String> lore = new ArrayList<>();
//            int amount = 1;
//
//            IUser user = ArashiLinkAPI.getApi().getMumbleManager().getUserFromName(player.getName());
//
//            lore.add(" ");
//            if (user == null) {
//                amount = 0;
//                lore.add("&8┃ &7Connecté: &c" + MumbleState.DISCONNECT.getName());
//                lore.add("&8┃ &7Statut: &c" + MumbleState.DISCONNECT.getName());
//            } else if (ArashiLinkAPI.getApi().getMumbleManager().getStateOf(player.getName()) == MumbleState.DISCONNECT) {
//                lore.add("&8┃ &7Connecté: &c" + MumbleState.DISCONNECT.getName());
//                lore.add("&8┃ &7Statut: &c" + MumbleState.DISCONNECT.getName());
//            } else if (ArashiLinkAPI.getApi().getMumbleManager().getStateOf(player.getName()) == MumbleState.UNLINK) {
//                lore.add("&8┃ &7Connecté: &c" + MumbleState.LINK.getName());
//                lore.add("&8┃ &7Statut: &c" + MumbleState.UNLINK.getName());
//            } else if (ArashiLinkAPI.getApi().getMumbleManager().getStateOf(player.getName()) == MumbleState.LINK) {
//                lore.add("&8┃ &7Connecté: &c" + MumbleState.LINK.getName());
//                lore.add("&8┃ &7Statut: &c" + MumbleState.LINK.getName());
//            }
//
//            lore.add(" ");
//            lore.add("&f&l» &cCliquez-ici pour gérer");
//
//            return new ItemBuilder(Material.SKULL_ITEM, amount).setSkullOwner(player.getName()).setDurability(SkullType.PLAYER.ordinal()).setName("&c" + player.getName()).setLore(lore).toItemStack();
//        }
//
//        @Override
//        public void clicked(Player param, int slot, ClickType clickType, int hotbarButton) {
//            IUser user = ArashiLinkAPI.getApi().getMumbleManager().getUserFromName(player.getName());
//
//            if(user == null) return;
//
//            if(UPlayer.get(param).hasHostAccess())
//                new MumbleManageUser(player, user, new MumbleMenu(null)).openMenu(param);
//        }
//    }
//
//    private static class GlobalButton extends Button {
//        @Override
//        public ItemStack getButtonItem(Player player) {
//            int connected = 0;
//            int linked = 0;
//
//            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//                IUser user = ArashiLinkAPI.getApi().getMumbleManager().getUserFromName(onlinePlayer.getName());
//
//                if(user != null) {
//                    connected++;
//                    if(ArashiLinkAPI.getApi().getMumbleManager().getStateOf(onlinePlayer.getName()) == MumbleState.LINK) {
//                        linked++;
//                    }
//                }
//            }
//
//            return new ItemBuilder(UHCItems.getMumbleItem()).setAmount(connected).setName("&cJoueurs").setLore(
//                    "",
//                    "&8┃ &7Connecté(s): &a" + connected + "&8/" + "&a" + UHC.getGameManager().getSize(),
//                    "&8┃ &7Link(s): &a" + linked + "&8/" + "&a" + connected,
//                    "",
//                    "&f&l» &cCliquez sur quelqu'un pour gérer"
//            ).toItemStack();
//        }
//    }
//}
