package fr.kohei.uhc.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.GlassMenu;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.game.config.RulesManager;
import fr.kohei.uhc.game.scenario.Scenario;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRulesMenu extends GlassMenu {
    @Override
    public String getTitle(Player player) {
        return "Configuration de la partie";
    }

    @Override
    public int getGlassColor() {
        return 5;
    }

    private String format(boolean b) {
        return (b ? "§aActivé" : "§cDésactivé");
    }

    private String format(Enchantment type) {
        GameManager manager = UHC.getInstance().getGameManager();
        GameConfiguration gameConfiguration = manager.getGameConfiguration();

        if (!gameConfiguration.getEnchantmentsLimit().containsKey(type)) {
            return "&cDésactivé";
        } else if (gameConfiguration.getEnchantmentsLimit().get(type) <= 0) {
            return "&cDésactivé";
        } else {
            return "&aActivé";
        }
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        GameManager manager = UHC.getInstance().getGameManager();
        GameConfiguration gameConfiguration = manager.getGameConfiguration();
        RulesManager rulesManager = gameConfiguration.getRulesManager();

        buttons.put(10, new DisplayButton(new ItemBuilder(Material.ITEM_FRAME).setName("§e§lRègles").setLore(
                "§7▎ §fEnd: §a" + format(manager.getGameConfiguration().isEnd()),
                "§7▎ §fNether: §a" + format(manager.getGameConfiguration().isNether()),
                " ",
                "§7▎ §fDiamond Limit: §a" + manager.getGameConfiguration().getDiamondLimit(),
                "§7▎ §fAbsorption: §a" + (manager.getGameConfiguration().isAbsorption() ? "§aActivé" : "§cDésactivé"),
                "§7▎ §fCycle Jour/Nuit: §a" + TimeUtil.getReallyNiceTime2(manager.getGameConfiguration().getCycle() * 1000L),
                " ",
                "§7▎ §fBordure Initiale: §a± " + manager.getGameConfiguration().getBorderStartSize(),
                "§7▎ §fBordure Finale: §a± " + manager.getGameConfiguration().getBorderEndSize()
        ).toItemStack()));

        buttons.put(11, new DisplayButton(new ItemBuilder(Material.WATCH).setName("§e§lTimers").setLore(
                "§7▎ §fPvP: §a" + TimeUtil.getReallyNiceTime2(manager.getGameConfiguration().getPvpTimer() * 1000L),
                "§7▎ §fBordure: §a" + TimeUtil.getReallyNiceTime2(manager.getGameConfiguration().getMeetupTimer() * 1000L),
                "§7▎ §fRapiditié: §a" + manager.getGameConfiguration().getBorderSpeed() + " b/s",
                "§7▎ §fInvulnérabilité: §a00:30"
        ).toItemStack()));

        buttons.put(22, new DisplayButton(new ItemBuilder(Material.INK_SACK).setDurability(6).setName("§6§lGame").setLore(
                "§7▎ §fSlots: §a" + manager.getGameConfiguration().getSlots(),
                "§7▎ §fWhitelist: §a" + format(manager.isWhitelist()),
                "§7▎ §fSpectateurs: §a" + format(manager.getGameConfiguration().isSpectators()),
                "§7▎ §fNom du Serveur: §a" + (manager.getGameConfiguration().getCustomName()),
                "§7▎ §fHost: §a" + manager.getHost(),
                "§7▎ §fMode de Jeu: §a" + UHC.getInstance().getModuleManager().getModule().getName()
        ).toItemStack()));

        buttons.put(15, new DisplayButton(new ItemBuilder(Material.DIAMOND_SWORD).setName("§e§lRègles").setLore(
                "§7▎ §fCasque en Diamant: §a" + format(rulesManager.isDiamondHelmet()),
                "§7▎ §fPlastron en Diamant: §a" + format(rulesManager.isDiamondChestplate()),
                "§7▎ §fPantalon en Diamant: §a" + format(rulesManager.isDiamondLeggings()),
                "§7▎ §fBottes en Diamant: §a" + format(rulesManager.isDiamondBoots()),
                "§7▎ §fKnockback: §a" + format(rulesManager.isKnockback()),
                " ",
                "§7▎ §fPunch: §a" + format(rulesManager.isPunch()),
                "§7▎ §fRod: §a" + format(rulesManager.isFinishRod()),
                "§7▎ §fLave: §a" + format(rulesManager.isLavaBucket()),
                "§7▎ §fDig Down: §a" + format(rulesManager.isDigDown()),
                "§7▎ §fTower: §a" + format(rulesManager.isTowers()),
                " ",
                "§7▎ §fEnder Pearl: §a" + format(rulesManager.isEnderPearl()),
                "§7▎ §fHorse: §a" + format(rulesManager.isHorse()),
                "§7▎ §fBriquets: §a" + format(rulesManager.isFlintAndSteel())
        ).toItemStack()));

        List<String> scenarios = new ArrayList<>();
        for (Scenario scenario : Scenario.values()) {
            if(scenario.isEnabled()) {
                scenarios.add("§7▎ §f" + scenario.getScenario().getName());
            }
        }

        buttons.put(16, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.NETHER_STAR).setName("§e§lScénarios").setLore(scenarios).toItemStack();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                new ManageScenariosMenu(new PlayerRulesMenu()).openMenu(player);
            }
        });

        buttons.put(28, new DisplayButton(new ItemBuilder(Material.APPLE).setName("§e§lDrop Rates").setLore(
                "§7▎ §fApple Rate: §a" + manager.getGameConfiguration().getAppleRate() + "%",
                "§7▎ §fFlint Rate: §a" + manager.getGameConfiguration().getFlintRate() + "%",
                "§7▎ §fPearl Rate: §a" + manager.getGameConfiguration().getPearlRate() + "%"
        ).toItemStack()));

        buttons.put(29, new DisplayButton(new ItemBuilder(Material.POTION).setName("§e§lPotions").setLore(
                "§7▎ §fPotions: §a" + format(gameConfiguration.isPotions())
        ).toItemStack()));

        buttons.put(33, new DisplayButton(new ItemBuilder(Material.ENCHANTED_BOOK).setName("§e§lEnchantements").setLore(
                "§7▎ §fInfinity: §a" + format(Enchantment.ARROW_INFINITE),
                "§7▎ §fPower: §a" + format(Enchantment.ARROW_DAMAGE),
                "§7▎ §fPunch: §a" + format(Enchantment.ARROW_KNOCKBACK),
                "§7▎ §fKB: §a" + format(Enchantment.KNOCKBACK),
                "§7▎ §fFire Aspect: §a" + format(Enchantment.FIRE_ASPECT),
                "§7▎ §fFlame: §a" + format(Enchantment.ARROW_FIRE),
                "§7▎ §fThorns: §a" + format(Enchantment.THORNS)
        ).toItemStack()));

        buttons.put(34, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.CHEST).setName("§e§lInventaires").setLore(
                        " ",
                        "§f&l» §eCliquez-ici pour y accéder"
                ).toItemStack();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                new GameInventoryMenu(new PlayerRulesMenu()).openMenu(player);
            }
        });

        buttons.put(42, new DisplayButton(new ItemStack(Material.AIR)));

        return buttons;
    }
}