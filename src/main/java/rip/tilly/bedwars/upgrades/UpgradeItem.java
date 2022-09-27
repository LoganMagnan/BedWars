package rip.tilly.bedwars.upgrades;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.playerdata.currentgame.TeamUpgrades;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.PlayerUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpgradeItem {

    private String name;

    private String[] lore;

    private Material material;

    private Upgrade upgrade;

    public UpgradeItem(String name, String[] lore, Material material, Upgrade upgrade) {
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.upgrade = upgrade;
    }

    public ItemStack getItemStack(Player player, TeamUpgrades teamUpgrades) {
        int level = teamUpgrades.getLevelForUpgrade(this.upgrade);

        ItemBuilder itemBuilder = new ItemBuilder(this.material, Math.min(level + 1, this.upgrade.getHighestLevel()));
        itemBuilder.name(CC.translate("&e" + this.name + " " + this.upgrade.getNumberToRomanNumeral(Math.min(level + 1, this.upgrade.getHighestLevel()))));

        List<String> lore = Arrays.stream(this.lore).map(line -> "&7" + line).collect(Collectors.toList());
        lore.add(CC.translate(""));

        if (this.upgrade.getHighestLevel() == 1) {
            lore.add(CC.translate("&7Cost: &b" + this.upgrade.getCostForLevel(1) + " Diamond" + (this.upgrade.getCostForLevel(1) == 1 ? "" : "s")));
        } else {
            for (int i = 0; i < this.upgrade.getHighestLevel(); i++) {
                int amount = i + 1;

                String string = this.upgrade.getCostForLevel(amount) + " Diamond" + (this.upgrade.getCostForLevel(amount) == 1 ? "" : "s");

                lore.add(CC.translate((level < amount ? "&7" : "&a") + "Tier " + amount + ": " + this.upgrade.getPerkForLevel(amount) + ", &b" + string));
            }
        }

        lore.add(CC.translate(""));

        int costItems = 0;
        for (ItemStack contents : player.getInventory().getContents()) {
            if (contents != null) {
                if (contents.getType() == Material.DIAMOND) {
                    costItems += contents.getAmount();
                }
            }
        }
        int cost = this.upgrade.getCostForLevel(level);

        if (teamUpgrades.getCostToUpgrade(this.upgrade) != -1) {
            if (costItems >= cost) {
                lore.add(CC.translate("&aClick to purchase"));
            } else {
                lore.add(CC.translate("&cNot enough diamonds"));
            }
        } else {
            lore.add(CC.translate("&aUnlocked"));
        }

        itemBuilder = itemBuilder.lore(lore);

        return itemBuilder.build();
    }

    public void buy(Player player, int level, Game game, GameTeam gameTeam, TeamUpgrades teamUpgrades) {
        int costItems = 0;
        for (ItemStack contents : player.getInventory().getContents()) {
            if (contents != null) {
                if (contents.getType() == Material.DIAMOND) {
                    costItems += contents.getAmount();
                }
            }
        }

        int cost = this.upgrade.getCostForLevel(level);
        if (costItems < cost) {
            player.sendMessage(CC.translate("&cYou don't have enough Diamonds!"));
            return;
        }

        int finalCost = cost;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i == null) {
                continue;
            }
            if (i.getType() == Material.DIAMOND) {
                if (i.getAmount() < finalCost) {
                    finalCost -= i.getAmount();
                    PlayerUtil.minusAmount(player, i, i.getAmount());
                    player.updateInventory();
                } else {
                    PlayerUtil.minusAmount(player, i, finalCost);
                    player.updateInventory();
                    break;
                }
            }
        }

        teamUpgrades.upgrade(player, game, gameTeam, this.upgrade);
    }

    public Upgrade getUpgrade() {
        return this.upgrade;
    }
}
