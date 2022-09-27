package rip.tilly.bedwars.menus.queue;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.GameType;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayAGameMenu extends Menu {

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eSelect a game to play...");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (GameType type : GameType.values()) {
            buttons.put(type.getSlot(), new QueueButton("&d" + type.getName(), type.getLore(), type, type.getMaterial(), type.getData()));
        }

        fillEmptySlots(buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("").build());

        return buttons;
    }

    @Override
    public int getSize() {
        return 27;
    }

    @AllArgsConstructor
    private class QueueButton extends Button {

        private final String name;
        private final List<String> lore;
        private final GameType gameType;
        private final Material material;
        private final int data;

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> loreList = new ArrayList<>(lore);
            loreList.add(" ");
            loreList.add("&fPlaying: &b" + BedWars.getInstance().getGameManager().getPlayingByType(gameType));

            return new ItemBuilder(material).name(name).durability(data).amount(BedWars.getInstance().getGameManager().getPlayingByType(gameType) > 0 ? BedWars.getInstance().getGameManager().getPlayingByType(gameType) : 1).lore(loreList).build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            BedWars.getInstance().getQueueManager().addPlayerToQueue(player, BedWars.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId()), gameType);
            playNeutral(player);
        }
    }
}
