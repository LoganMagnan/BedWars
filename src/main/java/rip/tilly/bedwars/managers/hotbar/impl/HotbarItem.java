package rip.tilly.bedwars.managers.hotbar.impl;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class HotbarItem {

    private static List<HotbarItem> items = Lists.newArrayList();
    private ItemStack itemStack;
    private int slot;
    private boolean enabled;
    private ActionType actionType;

    public HotbarItem(ItemStack itemStack, int slot, boolean enabled, String action) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.enabled = enabled;
        this.actionType = ActionType.valueOf(action);

        items.add(this);
    }

    public static HotbarItem getItemByItemStack(ItemStack itemStack) {
        return items.stream().filter(item -> item.getItemStack().isSimilar(itemStack)).findFirst().orElse(null);
    }
}
