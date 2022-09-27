package rip.tilly.bedwars.menus.shop;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.menus.shop.armor.ArmorMenu;
import rip.tilly.bedwars.menus.shop.blocks.BlocksMenu;
import rip.tilly.bedwars.menus.shop.utilities.UtilityMenu;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ShopTypeButton extends Button {

    private final ShopType shopType;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> loreList = new ArrayList<>(shopType.getLore());
        loreList.add("&eClick to open the " + shopType.getName() + " section");

        return new ItemBuilder(shopType.getMaterial())
                .durability(shopType.getData())
                .name("&d&l" + shopType.getName())
                .lore(loreList)
                .hideFlags()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        playNeutral(player);

        switch (shopType) {
            case QUICK:
                break;
            case BLOCKS:
                new BlocksMenu().openMenu(player);

                break;
            case ARMOR:
                new ArmorMenu().openMenu(player);

                break;
            case TOOLS:
                break;
            case WEAPONS:
                break;
            case RANGED:
                break;
            case POTIONS:
                break;
            case UTILITY:
                new UtilityMenu().openMenu(player);

                break;
        }
    }
}
