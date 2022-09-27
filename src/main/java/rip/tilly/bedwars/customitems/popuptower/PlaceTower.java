package rip.tilly.bedwars.customitems.popuptower;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.playerdata.PlayerTeam;

public class PlaceTower {

    private final BedWars plugin = BedWars.getInstance();

    public PlaceTower(Block block, String xyz, PlayerTeam playerTeam, Player player, boolean ladder, int ladderData) {
        int x = Integer.parseInt(xyz.split(", ")[0]);
        int y = Integer.parseInt(xyz.split(", ")[1]);
        int z = Integer.parseInt(xyz.split(", ")[2]);
        Block relative = block.getRelative(x, y, z);
        Game game = this.plugin.getGameManager().getGame(player.getUniqueId());
        if (!game.isInside(relative.getLocation(), game)) {
            if (relative.getType() == Material.AIR) {
                if (!ladder) {
                    relative.setType(Material.WOOL);
                    relative.setData((byte) playerTeam.getColorData());
                } else {
                    relative.setType(Material.LADDER);
                    relative.setData((byte) ladderData);
                }
                this.plugin.getGameManager().getGame(player.getUniqueId()).addPlacedBlock(relative);
            }
        }
    }
}
