package rip.tilly.bedwars.menus.shop.tools;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.playerdata.currentgame.PlayerCurrentGameData;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.PlayerUtil;
import rip.tilly.bedwars.utils.menu.Button;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ShearsButton extends Button {

    private final String name;
    private final Material material;
    private final Material costType;
    private final String costTypeName;
    private final ChatColor costTypeColor;
    private final int cost;
    private final PlayerCurrentGameData currentGameData;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> loreList = new ArrayList<>();

        loreList.add(" ");
        if (currentGameData.isShears()) {
            loreList.add("&cYou already have shears!");
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

        player.getInventory().addItem(new ItemBuilder(material)
                .addUnbreakable()
                .hideFlags()
                .build());
    }
}
