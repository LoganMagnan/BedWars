package rip.tilly.bedwars.utils.menusystem.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.currentgame.TeamUpgrades;
import rip.tilly.bedwars.upgrades.Upgrade;
import rip.tilly.bedwars.upgrades.UpgradeItem;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.menusystem.Menu;
import rip.tilly.bedwars.utils.menusystem.PlayerMenuUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UpgradesMenu extends Menu {

    private BedWars main = BedWars.getInstance();

    List<UpgradeItem> upgradeItems = new ArrayList<UpgradeItem>();

    public UpgradesMenu(PlayerMenuUtil playerMenuUtil) {
        super(playerMenuUtil);

        this.upgradeItems.add(new UpgradeItem("Sharpened Swords", new String[]{"Your team permanently gains", "sharpness one on all swords"}, Material.IRON_SWORD, Upgrade.SHARPENED_SWORDS));
        this.upgradeItems.add(new UpgradeItem("Reinforced Armor", new String[]{"Your team permanently gets", "protection one all all armor"}, Material.IRON_CHESTPLATE, Upgrade.PROTECTION));
        this.upgradeItems.add(new UpgradeItem("Maniac Miner", new String[]{"Your team will permanently get haste"}, Material.GOLD_PICKAXE, Upgrade.MANIAC_MINER));
        this.upgradeItems.add(new UpgradeItem("Faster Forge", new String[]{"Increase your team's generator speed"}, Material.FURNACE, Upgrade.FASTER_FORGE));
        this.upgradeItems.add(new UpgradeItem("Heal Pool", new String[]{"Your team permanently gains", "Sharpness 1 on all swords"}, Material.BEACON, Upgrade.HEAL_POOL));
        this.upgradeItems.add(new UpgradeItem("Dragon Buff", new String[]{"Your team permanently gains", "Sharpness 1 on all swords"}, Material.DRAGON_EGG, Upgrade.DRAGON_BUFF));
        this.upgradeItems.add(new UpgradeItem("Trap", new String[]{"Get an alert when players", "come to your base"}, Material.TRIPWIRE_HOOK, Upgrade.TRAP));
    }

    @Override
    public String getMenuName() {
        return CC.translate("&eUpgrades Menu");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

        TeamUpgrades teamUpgrades = playerData.getPlayerTeam().getTeamUpgrades();

        Game game = this.main.getGameManager().getGame(player.getUniqueId());

        if (game == null) {
            return;
        }

        GameTeam gameTeam = game.getTeams().get(playerData.getTeamId());

        if (event.getView().getTitle().equalsIgnoreCase(CC.translate("&eUpgrades Menu"))) {
            for (UpgradeItem upgradeItem : this.upgradeItems) {
                if (upgradeItem.getItemStack(player, teamUpgrades).getItemMeta().getDisplayName().equalsIgnoreCase(event.getCurrentItem().getItemMeta().getDisplayName())) {
                    if (teamUpgrades.getCostToUpgrade(upgradeItem.getUpgrade()) == -1) {
                        player.sendMessage(CC.translate("&cYour team already has the highest upgrade for &c&l" + upgradeItem.getUpgrade().getFormattedName()));

                        return;
                    }

                    upgradeItem.buy(player, teamUpgrades.getLevelForUpgrade(upgradeItem.getUpgrade()) + 1, game, gameTeam, teamUpgrades);

                    new UpgradesMenu(this.main.getPlayerMenuUtil(player)).open(player);

                    break;
                }
            }
        }
    }

    @Override
    public void setMenuItems(Player player) {
        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

        TeamUpgrades teamUpgrades = playerData.getPlayerTeam().getTeamUpgrades();

        for (int i = 0; i < this.inventory.getSize(); i++) {
            this.inventory.setItem(i, this.FILLER_GLASS);
        }

        AtomicInteger atomicInteger = new AtomicInteger(10);

        this.upgradeItems.forEach(itemStack -> {
            this.inventory.setItem(atomicInteger.get(), itemStack.getItemStack(player, teamUpgrades));

            atomicInteger.addAndGet(1);
        });
    }
}
