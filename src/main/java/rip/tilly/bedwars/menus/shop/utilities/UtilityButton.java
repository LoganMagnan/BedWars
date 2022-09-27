package rip.tilly.bedwars.menus.shop.utilities;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.PlayerUtil;
import rip.tilly.bedwars.utils.menu.Button;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class UtilityButton extends Button {

    private String name;
    private String[] lore;
    private Material material;
    private int data;
    private int amount;
    private Material costType;
    private String costTypeName;
    private ChatColor costTypeColor;
    private int cost;
    private boolean color;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add("");

        for (String string : this.lore) {
            lore.add("&7" + string);
        }

        lore.add("");
        lore.add("&9Cost: " + costTypeColor + cost + " " + costTypeName);
        lore.add("&9Amount: &e" + amount + "x");

        int costItems = 0;
        for (ItemStack contents : player.getInventory().getContents()) {
            if (contents != null) {
                if (contents.getType() == costType) {
                    costItems += contents.getAmount();
                }
            }
        }

        lore.add(" ");
        lore.add(costItems >= cost ? "&aClick to purchase" : "&cYou don't have enough " + costTypeName);

        PlayerData playerData = BedWars.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (color) {
            return new ItemBuilder(material)
                    .name((costItems >= cost ? "&a" : "&c") + name)
                    .lore(lore)
                    .amount(amount)
                    .durability(playerData.getPlayerTeam().getColorData())
                    .hideFlags()
                    .build();
        } else {
            return new ItemBuilder(material)
                    .name((costItems >= cost ? "&a" : "&c") + name)
                    .lore(lore)
                    .amount(amount)
                    .durability(data)
                    .hideFlags()
                    .build();
        }
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
            player.sendMessage(CC.translate("&cYou don't have enough " + costTypeName));

            playFail(player);

            return;
        }

        PlayerData playerData = BedWars.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());

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

        if (color) {
            player.getInventory().addItem(new ItemBuilder(material)
                    .amount(amount)
                    .durability(playerData.getPlayerTeam().getColorData())
                    .hideFlags()
                    .build());
        } else {
            player.getInventory().addItem(new ItemBuilder(material)
                    .amount(amount)
                    .durability(data)
                    .hideFlags()
                    .build());
        }

        playNeutral(player);
    }
}
