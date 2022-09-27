package rip.tilly.bedwars.managers.hotbar;

import lombok.Getter;
import org.bukkit.Material;
import rip.tilly.bedwars.managers.hotbar.impl.HotbarItem;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HotbarManager {

    private final List<HotbarItem> spawnItems = new ArrayList<>();
    private final List<HotbarItem> queueItems = new ArrayList<>();
    private final List<HotbarItem> partyItems = new ArrayList<>();
    private final List<HotbarItem> spectatorItems = new ArrayList<>();

    public HotbarManager() {
        this.loadSpawnItems();
        this.loadQueueItems();
        this.loadPartyItems();
        this.loadSpectatorItems();
    }

    private void loadSpawnItems() {
        spawnItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.IRON_SWORD, CC.translate("&e&l» &dPlay A Game &e&l«"), 1, (short) 0
                ), 0, true, "QUEUE_MENU")
        );
        spawnItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.NAME_TAG, CC.translate("&e&l» &dCreate Party &e&l«"), 1, (short) 0
                ), 1, true, "CREATE_PARTY")
        );
        spawnItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.EMERALD, CC.translate("&e&l» &dCosmetics &e&l«"), 1, (short) 0
                ), 4, true, "COSMETICS_MENU")
        );
        spawnItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.BOOK, CC.translate("&e&l» &dHotbar Preference &e&l«"), 1, (short) 0
                ), 7, true, "PREFERENCES_MENU")
        );
        spawnItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.WATCH, CC.translate("&e&l» &dSettings &e&l«"), 1, (short) 0
                ), 8, true, "SETTINGS_MENU")
        );
    }

    private void loadQueueItems() {
        queueItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.INK_SACK, CC.translate("&e&l» &cLeave Queue &e&l«"), 1, (short) 1
                ), 8, true, "LEAVE_QUEUE")
        );
    }

    private void loadPartyItems() {
        partyItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.GOLD_SWORD, CC.translate("&e&l» &dParty Games &e&l«"), 1, (short) 0
                ), 0, true, "PARTY_GAMES")
        );
        partyItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.NETHER_STAR, CC.translate("&e&l» &dParty Info &e&l«"), 1, (short) 0
                ), 4, true, "PARTY_INFO")
        );
        partyItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.INK_SACK, CC.translate("&e&l» &cLeave Party &e&l«"), 1, (short) 1
                ), 8, true, "PARTY_LEAVE")
        );
    }

    private void loadSpectatorItems() {
        spectatorItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.COMPASS, CC.translate("&e&l» &dPlayer Tracker &e&l«"), 1, (short) 0
                ), 0, true, "SPECTATOR_MENU")
        );
        spectatorItems.add(new HotbarItem(
                ItemUtil.createUnbreakableItem(
                        Material.INK_SACK, CC.translate("&e&l» &cLeave Spectator Mode &e&l«"), 1, (short) 1
                ), 8, true, "SPECTATOR_LEAVE")
        );
    }
}
