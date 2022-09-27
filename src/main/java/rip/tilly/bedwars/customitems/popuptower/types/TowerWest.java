package rip.tilly.bedwars.customitems.popuptower.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.customitems.popuptower.PlaceTower;
import rip.tilly.bedwars.playerdata.PlayerTeam;

import java.util.ArrayList;
import java.util.List;

public class TowerWest {

    private BukkitTask bukkitTask;

    public TowerWest(Location location, Block block, PlayerTeam playerTeam, Player player) {
        ItemStack hand = player.getItemInHand();
        if (hand.getAmount() == 1) {
            hand.setType(Material.AIR);
            player.setItemInHand(hand);
        } else {
            hand.setAmount(hand.getAmount() - 1);
        }

        List<String> locList = new ArrayList<>();
        locList.add("-2, 0, 1");
        locList.add("-1, 0, 2");
        locList.add("0, 0, 2");
        locList.add("1, 0, 1");
        locList.add("1, 0, 0");
        locList.add("1, 0, -1");
        locList.add("0, 0, -2");
        locList.add("-1, 0, -2");
        locList.add("-2, 0, -1");
        locList.add("0, 0, 0, ladder4");
        locList.add("-2, 1, 1");
        locList.add("-1, 1, 2");
        locList.add("0, 1, 2");
        locList.add("1, 1, 1");
        locList.add("1, 1, 0");
        locList.add("1, 1, -1");
        locList.add("0, 1, -2");
        locList.add("-1, 1, -2");
        locList.add("-2, 1, -1");
        locList.add("0, 1, 0, ladder4");
        locList.add("-2, 2, 1");
        locList.add("-1, 2, 2");
        locList.add("0, 2, 2");
        locList.add("1, 2, 1");
        locList.add("1, 2, 0");
        locList.add("1, 2, -1");
        locList.add("0, 2, -2");
        locList.add("-1, 2, -2");
        locList.add("-2, 2, -1");
        locList.add("0, 2, 0, ladder4");
        locList.add("-2, 3, 0");
        locList.add("-2, 3, 1");
        locList.add("-1, 3, 2");
        locList.add("0, 3, 2");
        locList.add("1, 3, 1");
        locList.add("1, 3, 0");
        locList.add("1, 3, -1");
        locList.add("0, 3, -2");
        locList.add("-1, 3, -2");
        locList.add("-2, 3, -1");
        locList.add("0, 3, 0, ladder4");
        locList.add("-2, 4, 0");
        locList.add("-2, 4, 1");
        locList.add("-1, 4, 2");
        locList.add("0, 4, 2");
        locList.add("1, 4, 1");
        locList.add("1, 4, 0");
        locList.add("1, 4, -1");
        locList.add("0, 4, -2");
        locList.add("-1, 4, -2");
        locList.add("-2, 4, -1");
        locList.add("0, 4, 0, ladder4");
        locList.add("1, 5, 2");
        locList.add("0, 5, 2");
        locList.add("-1, 5, 2");
        locList.add("-2, 5, 2");
        locList.add("1, 5, 1");
        locList.add("0, 5, 1");
        locList.add("-1, 5, 1");
        locList.add("-2, 5, 1");
        locList.add("1, 5, 0");
        locList.add("-1, 5, 0");
        locList.add("-2, 5, 0");
        locList.add("1, 5, -1");
        locList.add("0, 5, -1");
        locList.add("-1, 5, -1");
        locList.add("-2, 5, -1");
        locList.add("0, 5, 0, ladder4");
        locList.add("1, 5, -2");
        locList.add("0, 5, -2");
        locList.add("-1, 5, -2");
        locList.add("-2, 5, -2");
        locList.add("-2, 5, 3");
        locList.add("-2, 6, 3");
        locList.add("-2, 7, 3");
        locList.add("-1, 6, 3");
        locList.add("0, 6, 3");
        locList.add("1, 5, 3");
        locList.add("1, 6, 3");
        locList.add("1, 7, 3");
        locList.add("2, 5, 2");
        locList.add("2, 6, 2");
        locList.add("2, 7, 2");
        locList.add("2, 6, 1");
        locList.add("2, 5, 0");
        locList.add("2, 6, 0");
        locList.add("2, 7, 0");
        locList.add("2, 6, -1");
        locList.add("2, 5, -2");
        locList.add("2, 6, -2");
        locList.add("2, 7, -2");
        locList.add("-2, 5, -3");
        locList.add("-2, 6, -3");
        locList.add("-2, 7, -3");
        locList.add("-1, 6, -3");
        locList.add("0, 6, -3");
        locList.add("1, 5, -3");
        locList.add("1, 6, -3");
        locList.add("1, 7, -3");
        locList.add("-3, 5, 2");
        locList.add("-3, 6, 2");
        locList.add("-3, 7, 2");
        locList.add("-3, 6, 1");
        locList.add("-3, 5, 0");
        locList.add("-3, 6, 0");
        locList.add("-3, 7, 0");
        locList.add("-3, 6, -1");
        locList.add("-3, 5, -2");
        locList.add("-3, 6, -2");
        locList.add("-3, 7, -2");

        int[] i = { 0 };
        this.bukkitTask = Bukkit.getScheduler().runTaskTimer(BedWars.getInstance(), () -> {
            location.getWorld().playSound(location, Sound.CHICKEN_EGG_POP, 1f, 0.5f);
            if (locList.size() + 1 == i[0] + 1) {
                this.bukkitTask.cancel();
                return;
            }
            String string = locList.get(i[0]);
            if (string.contains("ladder")) {
                int ladderData = Integer.parseInt(string.split("ladder")[1]);
                new PlaceTower(block, string, playerTeam, player, true, ladderData);
            } else {
                new PlaceTower(block, string, playerTeam, player, false, 0);
            }
            if (locList.size() + 1 == i[0] + 2) {
                this.bukkitTask.cancel();
                return;
            }
            String string2 = locList.get(i[0] + 1);
            if (string2.contains("ladder")) {
                int ladderData = Integer.parseInt(string2.split("ladder")[1]);
                new PlaceTower(block, string2, playerTeam, player, true, ladderData);
            } else {
                new PlaceTower(block, string2, playerTeam, player, false, 0);
            }
            i[0] = i[0] + 2;
        }, 0, 1);
    }
}
