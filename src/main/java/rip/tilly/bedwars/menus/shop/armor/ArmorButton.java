package rip.tilly.bedwars.menus.shop.armor;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.currentgame.ArmorType;
import rip.tilly.bedwars.playerdata.currentgame.TeamUpgrades;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.PlayerUtil;
import rip.tilly.bedwars.utils.menu.Button;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ArmorButton extends Button {

    private final String name;
    private final Material material;
    private final Material costType;
    private final String costTypeName;
    private final ChatColor costTypeColor;
    private final int cost;
    private final ArmorType armorType;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> loreList = new ArrayList<>();

        PlayerData playerData = BedWars.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());
        TeamUpgrades teamUpgrades = playerData.getPlayerTeam().getTeamUpgrades();

        loreList.add(" ");
        loreList.add("&7&oYou will keep this armor");
        loreList.add("&7&oupon death.");
        loreList.add(" ");
        if (teamUpgrades.getArmorType(player) == armorType) {
            loreList.add("&cYou already own this!");
        } else if (teamUpgrades.getArmorType(player) == ArmorType.DIAMOND) {
            loreList.add("&cYou have the highest tier of armor!");
        } else if (teamUpgrades.getArmorType(player) == ArmorType.IRON && armorType == ArmorType.CHAIN) {
            loreList.add("&cYou already have a higher tier of armor!");
        } else {
            loreList.add("&9Cost: " + costTypeColor + cost + " " + costTypeName);
        }

        int costItems = 0;
        for (ItemStack contents : player.getInventory().getContents()) {
            if (contents != null) {
                if (contents.getType() == costType) {
                    costItems += contents.getAmount();
                }
            }
        }

        loreList.add(" ");
        loreList.add(costItems >= cost ? "&aClick to purchase!" : "&cYou don't have enough " + costTypeName + "!");

        return new ItemBuilder(material)
                .name((costItems >= cost ? "&a" : "&c") + name)
                .lore(loreList)
                .hideFlags()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (clickType.isShiftClick()) {
            return;
        }

        PlayerData playerData = BedWars.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());
        TeamUpgrades teamUpgrades = playerData.getPlayerTeam().getTeamUpgrades();
        if (teamUpgrades.getArmorType(player) == armorType) {
            player.sendMessage(CC.translate("&cYou already own this armor!"));
            playFail(player);
            return;
        }

        if (teamUpgrades.getArmorType(player) == ArmorType.DIAMOND) {
            player.sendMessage(CC.translate("&cYou have the highest tier of armor!"));
            playFail(player);
            return;
        }

        if (teamUpgrades.getArmorType(player) == ArmorType.IRON && armorType == ArmorType.CHAIN) {
            player.sendMessage(CC.translate("&cYou already have a higher tier of armor!"));
            playFail(player);
            return;
        }

        int costItems = 0;
        for (ItemStack contents : player.getInventory().getContents()) {
            if (contents != null) {
                if (contents.getType() == costType) {
                    costItems += contents.getAmount();
                }
            }
        }

        if (costItems < cost) {
            player.sendMessage(CC.translate("&cYou don't have enough " + costTypeName + "!"));
            playFail(player);
            return;
        }

        int finalCost = cost;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i == null) {
                continue;
            }
            if (i.getType() == costType) {
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

        teamUpgrades.setArmorType(player, armorType);

        playNeutral(player);
    }
}
