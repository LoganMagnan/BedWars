package rip.tilly.bedwars.menus.arena.buttons;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.game.arena.CopiedArena;
import rip.tilly.bedwars.runnables.ArenaCopyRemovalRunnable;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;

import java.util.Arrays;

@AllArgsConstructor
public class ArenaCopyButton extends Button {

	private final BedWars plugin = BedWars.getInstance();

	private final int number;
	private final Arena arena;
	private final CopiedArena arenaCopy;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.PAPER)
				.name("&d&lCopy #" + number)
				.lore(CC.translate(
						Arrays.asList(
								" ",
								"&dCopied Arena Information:",
								" &7⚫ &9Parent Arena: &e" + arena.getName(),
								" &7⚫ &91st Spawn: &e" + Math.round(arenaCopy.getA().getX()) + "&7, &e" + Math.round(arenaCopy.getA().getY()) + "&7, &e" + Math.round(arenaCopy.getA().getZ()),
								" &7⚫ &92nd Spawn: &e" + Math.round(arenaCopy.getB().getX()) + "&7, &e" + Math.round(arenaCopy.getB().getY()) + "&7, &e" + Math.round(arenaCopy.getB().getZ()),
								" ",
								"&a&lLeft Click &ato teleport to this copied arena",
								"&c&lRight Click &cto delete this copied arena"
						))
				)
				.build();
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
		switch (clickType) {
			case LEFT:
				player.teleport(arenaCopy.getA().toBukkitLocation());
				break;
			case RIGHT:
				new ArenaCopyRemovalRunnable(number, arena, arenaCopy).runTask(this.plugin);
				break;
		}

		player.closeInventory();
	}
}
