package rip.tilly.bedwars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.TrapDoor;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.managers.hotbar.impl.HotbarItem;
import rip.tilly.bedwars.menus.queue.PlayAGameMenu;
import rip.tilly.bedwars.menus.settings.SettingsMenu;
import rip.tilly.bedwars.menus.shop.blocks.BlocksMenu;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.utils.menusystem.menu.UpgradesMenu;

public class InteractListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (event.getAction().name().endsWith("_BLOCK")) {
            if (event.getClickedBlock().getType().name().contains("FENCE") && (event.getClickedBlock().getState() instanceof TrapDoor)
                    || event.getClickedBlock().getType().name().contains("TRAP") || event.getClickedBlock().getType().name().contains("CHEST")
                    || event.getClickedBlock().getType().name().contains("DOOR") || event.getClickedBlock().getType().equals(Material.BEACON)
                    || event.getClickedBlock().getType().equals(Material.FURNACE) || event.getClickedBlock().getType().equals(Material.WORKBENCH)
                    || event.getClickedBlock().getType().equals(Material.NOTE_BLOCK) || event.getClickedBlock().getType().equals(Material.JUKEBOX)
                    || event.getClickedBlock().getType().equals(Material.ANVIL) || event.getClickedBlock().getType().equals(Material.HOPPER)
                    || event.getClickedBlock().getType().equals(Material.BED_BLOCK) || event.getClickedBlock().getType().equals(Material.DROPPER)
                    || event.getClickedBlock().getType().equals(Material.BREWING_STAND)) {
                event.setCancelled(true);
                return;
            }
        }

        ItemStack itemStack = player.getItemInHand();
        if (!event.hasItem() || itemStack == null) {
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (playerData.getPlayerState() == PlayerState.PLAYING) {
            return;
        }

        HotbarItem hotbarItem = HotbarItem.getItemByItemStack(player.getItemInHand());
        if (hotbarItem == null) {
            return;
        }

        if (hotbarItem.getActionType() == null) {
            return;
        }

        event.setCancelled(true);

        switch (playerData.getPlayerState()) {
            case SPAWN:
                switch (hotbarItem.getActionType()) {
                    case QUEUE_MENU:
                        new PlayAGameMenu().openMenu(player);

                        break;
                    case SETTINGS_MENU:
                        new SettingsMenu().openMenu(player);

                        break;
                    case CREATE_PARTY:
                        this.plugin.getPartyManager().createParty(player);

                        break;
                    case COSMETICS_MENU:
                        new BlocksMenu().openMenu(player);
                        // open cosmetics menu
                        break;
                    case PREFERENCES_MENU:
                        // open preferences menu
                        break;

                    case PARTY_GAMES:
                        // open party games menu
                        break;
                    case PARTY_INFO:
                        player.chat("/party info");

                        break;
                    case PARTY_LEAVE:
                        this.plugin.getPartyManager().leaveParty(player);

                        break;
                }
                break;
            case QUEUE:
                switch (hotbarItem.getActionType()) {
                    case LEAVE_QUEUE:
                        this.plugin.getQueueManager().removePlayerFromQueue(player);
                        break;
                }
                break;
            case SPECTATING:
                switch (hotbarItem.getActionType()) {
                    case SPECTATOR_MENU:
                        // open player tracker menu
                        break;
                    case SPECTATOR_LEAVE:
                        this.plugin.getGameManager().removeSpectator(player);
                        break;
                }
                break;
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof ArmorStand) {
            event.setCancelled(true);

            return;
        }

        String name = event.getRightClicked().getName();

        if (name == null) {
            return;
        }

        name = ChatColor.stripColor(name);

        switch (name) {
            case "Item Shop":
                event.setCancelled(true);

                new BlocksMenu().openMenu(player);

                break;
            case "Upgrades Shop":
                event.setCancelled(true);

                new UpgradesMenu(this.plugin.getPlayerMenuUtil(player)).open(player);

                break;
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.VILLAGER) {
            event.setCancelled(true);

            return;
        }
    }
}
