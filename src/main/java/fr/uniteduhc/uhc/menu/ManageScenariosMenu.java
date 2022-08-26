package fr.uniteduhc.uhc.menu;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.uhc.game.scenario.AbstractScenario;
import fr.uniteduhc.uhc.game.scenario.Scenario;
import fr.uniteduhc.uhc.game.scenario.impl.SafeMiners;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ManageScenariosMenu extends PaginatedMenu {

    private final Menu oldMenu;

    @Override
    public int getGlassColor() {
        return 14;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Scénarios";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        UPlayer uPlayer = UPlayer.get(player);
        if(!uPlayer.hasHostAccess()) {
            for (Scenario value : Scenario.values()) {
                if(value.isEnabled()) {
                    buttons.put(buttons.size(), new ScenarioButton(value));
                }
            }
            return buttons;
        }
        for (Scenario value : Scenario.values()) {
            buttons.put(buttons.size(), new ScenarioButton(value));
        }

        return buttons;
    }

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @RequiredArgsConstructor
    private class ScenarioButton extends Button {

        private final Scenario scenario;

        @Override
        public ItemStack getButtonItem(Player player) {
            AbstractScenario abstractScenario = scenario.getScenario();
            List<String> lore = new ArrayList<>(abstractScenario.getLore());
            lore.add(" ");
            if(scenario.isEnabled()) {
                if(abstractScenario.getMenu() != null) {
                    lore.add("&f&l» &eClic-gauche pour désactiver");
                    lore.add("&f&l» &eClic-droit pour configurer");
                } else {
                    lore.add("&f&l» &eCliquez-ici pour désactiver");
                }
            } else {
                lore.add("&f&l» &eCliquez-ici pour activer");
            }

            ItemBuilder itemBuilder = new ItemBuilder(abstractScenario.getIcon()).setLore(lore);
            if(scenario.isEnabled()) {
                if(scenario.getScenario() instanceof SafeMiners)
                    itemBuilder.setAmount(((SafeMiners) scenario.getScenario()).getYAxe());
                itemBuilder.setName("&a" + abstractScenario.getName());
                itemBuilder.addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags();
            } else {
                itemBuilder.setName("&6&l" + abstractScenario.getName());
                itemBuilder.setAmount(0);
            }

            return itemBuilder.toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            UPlayer uPlayer = UPlayer.get(player);
            if(!uPlayer.hasHostAccess()) return;
            if(clickType.isLeftClick()) {
                scenario.setEnabled(!scenario.isEnabled());
                new ManageScenariosMenu(oldMenu).openMenu(player);
            } else if(clickType.isRightClick()) {
                if(scenario.getScenario().getMenu() != null) {
                    scenario.getScenario().getMenu().openMenu(player);
                }
            }
        }
    }
}
