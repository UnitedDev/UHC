package fr.uniteduhc.uhc.menu;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.GlassMenu;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.BackButton;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.RulesManager;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RulesMenu extends GlassMenu {

    public final Menu oldMenu;


    @Override
    public String getTitle(Player player) {
        return "Règles";
    }

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new CustomButton(Material.DIAMOND_HELMET, "Casque en Diamant", "diamondHelmet"));
        buttons.put(12, new CustomButton(Material.DIAMOND_CHESTPLATE, "Plastron en Diamant", "diamondChestplate"));
        buttons.put(13, new CustomButton(Material.DIAMOND_LEGGINGS, "Pantalon en Diamant", "diamondLeggings"));
        buttons.put(14, new CustomButton(Material.DIAMOND_BOOTS, "Bottes en Diamant", "diamondBoots"));

        buttons.put(15, new CustomButton(Material.DIAMOND_SWORD, "Knockback", "knockback"));
        buttons.put(20, new CustomButton(Material.BOW, "Punch", "punch"));
        buttons.put(21, new CustomButton(Material.FISHING_ROD, "Canne à pêche", "finishRod"));
        buttons.put(22, new CustomButton(Material.LAVA_BUCKET, "Seau de Lave", "lavaBucket"));

        buttons.put(23, new CustomButton(Material.GRASS, "Dig Down", "digDown"));
        buttons.put(24, new CustomButton(Material.LADDER, "Tower", "towers"));
        buttons.put(29, new CustomButton(Material.ENDER_PEARL, "Ender Pearl", "enderPearl"));

        buttons.put(30, new CustomButton(Material.SADDLE, "Cheveaux", "horse"));
        buttons.put(31, new CustomButton(Material.FLINT_AND_STEEL, "Briquets", "flintAndSteel"));

        if(oldMenu != null) {
            buttons.put(49, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    public static class CustomButton extends Button {
        private final Material material;
        private final String name;
        private final String method;

        @Override
        @SneakyThrows
        public ItemStack getButtonItem(Player player) {
            Field field = RulesManager.class.getDeclaredField(this.method);
            field.setAccessible(true);
            boolean b = field.getBoolean(UHC.getInstance().getGameManager().getGameConfiguration().getRulesManager());

            return new ItemBuilder(material).setName("&6&l" + name + " &8(" + (b ? "&aActivé" : "&cDésactivé") + "&8)").toItemStack();
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }

        @Override
        @SneakyThrows
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Field field = RulesManager.class.getDeclaredField(this.method);
            field.setAccessible(true);
            Object object = UHC.getInstance().getGameManager().getGameConfiguration().getRulesManager();
            boolean b = field.getBoolean(object);

            field.setBoolean(object, !b);
        }
    }

}
