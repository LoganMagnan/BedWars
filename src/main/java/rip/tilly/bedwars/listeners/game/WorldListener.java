package rip.tilly.bedwars.listeners.game;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.customitems.popuptower.types.TowerEast;
import rip.tilly.bedwars.customitems.popuptower.types.TowerNorth;
import rip.tilly.bedwars.customitems.popuptower.types.TowerSouth;
import rip.tilly.bedwars.customitems.popuptower.types.TowerWest;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameState;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;

public class WorldListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();

    private boolean isInGame(PlayerData playerData) {
        return playerData.getPlayerState() == PlayerState.PLAYING;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            this.plugin.getLogger().warning(player.getName() + "'s player data is null");
            event.setCancelled(true);
            return;
        }

        if (!this.isInGame(playerData)) {
            event.setCancelled(true);
            return;
        }

        Game game = this.plugin.getGameManager().getGame(player.getUniqueId());
        if (game == null) {
            event.setCancelled(true);
            return;
        }

        if (game.getGameState() != GameState.FIGHTING) {
            event.setCancelled(true);
            return;
        }

        Block block = event.getBlock();
        if (!game.isBreakable(block)) {
            event.setCancelled(true);
            return;
        }

        if (block.getType() == Material.BED_BLOCK) {
            GameTeam playerTeam = game.getTeams().get(playerData.getTeamId());
            if ((playerTeam.getId() == 1 ? game.getCopiedArena().getA().toBukkitLocation() : game.getCopiedArena().getB().toBukkitLocation()).distance(block.getLocation()) < 20) {
                event.setCancelled(true);
                return;
            }

            event.setCancelled(true);
            block.setType(Material.AIR);

            GameTeam opposingTeam = playerData.getTeamId() == 0 ? game.getTeams().get(1) : game.getTeams().get(0);
            opposingTeam.destroyBed();
            game.broadcastWithSound(playerTeam.getPlayerTeam().getChatColor() + player.getName() + " &ehas destroyed " + opposingTeam.getPlayerTeam().getChatColor() + opposingTeam.getPlayerTeam().getName() + "'s &ebed!", Sound.ENDERDRAGON_GROWL);
            game.broadcastSound(Sound.ENDERDRAGON_GROWL);

            playerData.getCurrentGameData().setGameBedsDestroyed(playerData.getCurrentGameData().getGameBedsDestroyed() + 1);
            playerData.setBedsDestroyed(playerData.getBedsDestroyed() + 1);

            Location location = block.getLocation();
            World world = location.getWorld();
            world.playEffect(location, Effect.CRIT, 1, 400);
            world.playEffect(location, Effect.FLAME, 1, 400);
            world.playEffect(location, Effect.MOBSPAWNER_FLAMES, 1, 400);
            world.playEffect(location, Effect.PARTICLE_SMOKE, 1, 400);
            world.playEffect(location, Effect.VILLAGER_THUNDERCLOUD, 1, 400);
            world.playEffect(location, Effect.LARGE_SMOKE, 1, 400);
            world.playEffect(location, Effect.SMOKE, 1, 400);
            world.playEffect(location, Effect.PARTICLE_SMOKE, 1, 400);
            return;
        }

        game.removePlacedBlock(block);

        ItemStack[] itemStack = block.getDrops().toArray(new ItemStack[0]);
        for (ItemStack drops : itemStack) {
            block.getWorld().dropItemNaturally(block.getLocation(), drops);
            game.getDroppedItems().add(drops);
        }

        for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1)) {
            if (entity instanceof Item) {
                game.addEntityToRemove(entity);
            }
        }

        block.setType(Material.AIR);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            this.plugin.getLogger().warning(player.getName() + "'s player data is null");
            event.setCancelled(true);
            return;
        }

        if (!this.isInGame(playerData)) {
            event.setCancelled(true);
            return;
        }

        Game game = this.plugin.getGameManager().getGame(player.getUniqueId());
        if (game == null) {
            event.setCancelled(true);
            return;
        }

        if (game.getGameState() != GameState.FIGHTING) {
            event.setCancelled(true);
            return;
        }

        Block block = event.getBlock();
        if (block.getLocation().getBlockY() >= game.getArena().getBuildMax()) {
            event.setCancelled(true);
            return;
        }

        if (!game.isPlaceable(block.getLocation(), game)) {
            event.setCancelled(true);
            return;
        }

        if (game.isInside(block.getLocation(), game)) {
            event.setCancelled(true);
            return;
        }

        Block chest = event.getBlockPlaced();
        if (chest.getType() == Material.CHEST) {
            event.setCancelled(true);

            Location location = event.getBlockPlaced().getLocation();

            double rotation = ((player.getLocation().getYaw() - 90) % 360);
            if (rotation < 0) {
                rotation += 360;
            }

            if (45 <= rotation && rotation < 135) {
                new TowerSouth(location, block, playerData.getPlayerTeam(), player);
            } else if (225 <= rotation && rotation < 315) {
                new TowerNorth(location, block, playerData.getPlayerTeam(), player);
            } else if (135 <= rotation && rotation < 225) {
                new TowerWest(location, block, playerData.getPlayerTeam(), player);
            } else if (0 <= rotation && rotation < 45) {
                new TowerEast(location, block, playerData.getPlayerTeam(), player);
            } else if (315 <= rotation && rotation < 360) {
                new TowerEast(location, block, playerData.getPlayerTeam(), player);
            }

            return;
        }

        game.addPlacedBlock(block);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (!this.isInGame(playerData)) {
            event.setCancelled(true);
            return;
        }

        Game game = this.plugin.getGameManager().getGame(playerData);
        if (game == null) {
            event.setCancelled(true);
            return;
        }

        if (game.getGameState() != GameState.FIGHTING) {
            event.setCancelled(true);
            return;
        }

        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();
        String name = itemStack.getType().name();
        if (name.endsWith("_SWORD") || name.endsWith("_AXE") || name.endsWith("_PICKAXE") || name.contains("SHEARS")) {
            event.setCancelled(true);
            return;
        }

        game.getDroppedItems().add(itemStack);
        game.addEntityToRemove(item);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (!this.isInGame(playerData)) {
            event.setCancelled(true);
            return;
        }

        Game game = this.plugin.getGameManager().getGame(playerData);
        if (game == null) {
            event.setCancelled(true);
            return;
        }

        if (game.getGameState() != GameState.FIGHTING) {
            event.setCancelled(true);
            return;
        }

//        if (!game.getDroppedItems().contains(event.getItem().getItemStack())) {
//            event.setCancelled(true);
//            return;
//        }

        game.getDroppedItems().remove(event.getItem().getItemStack());
        game.removeEntityToRemove(event.getItem());
    }
}
