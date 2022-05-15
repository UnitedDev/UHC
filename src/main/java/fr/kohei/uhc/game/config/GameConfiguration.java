package fr.kohei.uhc.game.config;

import fr.kohei.uhc.game.GameManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameConfiguration {

    private final GameManager gameManager;
    private final RulesManager rulesManager;
    private final EnchantRuleManager enchantManager;
    private List<String> enabledRoles;

    private String customName;
    private ItemStack[] startInventory, startArmor, deathInventory;
    private int finalHealTimer, pvpTimer, meetupTimer, damageTimer, rolesTime;
    private int slots, teams;
    private boolean spectators, nether, end, hideComposition, potions;
    private int borderStartSize, borderEndSize, borderSpeed;
    private int disconnectTime, cycle;
    private int appleRate, flintRate, pearlRate;
    private int goldLimit, diamondLimit, ironLimit;
    private boolean randomTeam, friendlyFire, heads;

    public GameConfiguration(GameManager gameManager) {
        this.enabledRoles = new ArrayList<>();

        this.gameManager = gameManager;
        this.rulesManager = new RulesManager(this);
        this.enchantManager = new EnchantRuleManager(this);

        this.customName = "UHC";

        this.startInventory = new ItemStack[36];
        this.startArmor = new ItemStack[4];
        this.deathInventory = new ItemStack[36];

        this.finalHealTimer = 20 * 60;
        this.pvpTimer = 20 * 60;
        this.rolesTime = 20 * 60;
        this.meetupTimer = 90 * 60;
        this.damageTimer = 30;

        this.slots = 20;
        this.teams = 1;

        this.spectators = true;
        this.nether = false;
        this.end = false;
        this.hideComposition = false;
        this.potions = false;

        this.borderStartSize = 750;
        this.borderEndSize = 200;
        this.borderSpeed = 1;

        this.disconnectTime = 10 * 60;
        this.cycle = 5 * 60;

        this.appleRate = 5;
        this.flintRate = 50;
        this.pearlRate = 33;

        this.diamondLimit = 0;
        this.goldLimit = 0;
        this.ironLimit = 0;

        this.randomTeam = false;
        this.friendlyFire = true;
    }
}
