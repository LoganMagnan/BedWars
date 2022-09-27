package rip.tilly.bedwars.menus.arena.buttons;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.menus.arena.ArenaCopyMenu;
import rip.tilly.bedwars.menus.arena.ArenaGenerationMenu;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;

import java.util.Arrays;

@AllArgsConstructor
public class ArenaButton extends Button {

	private final Arena arena;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.valueOf(arena.getIcon()))
				.durability(arena.getIconData())
				.name("&d&l" + arena.getName())
				.lore(Arrays.asList(
						" ",
						"&dArena Information:",
						" &7⚫ &9State: " + (arena.isEnabled() ? "&aEnabled" : "&cDisabled"),
						" &7⚫ &9Copies: &e" + (arena.getCopiedArenas().size() == 0 ? "&cZERO!" : arena.getCopiedArenas().size()),
						" &7⚫ &9Available: &e" + (arena.getAvailableArenas().size() == 0 ? 0 : arena.getAvailableArenas().size()),
						" ",
						"&c/arena info " + arena.getName() + " for more information.",
						" ",
						(arena.getCopiedArenas().size() == 0 ? "&4&l&mMiddle Click &4&mto see copied arenas" : "&6&lMiddle Click &6to see copied arenas"),
						"&a&lLeft Click &ato teleport to arena",
						"&b&lRight Click &bto generate copied arenas")
				)
				.hideFlags()
				.build();
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
		playNeutral(player);
		switch (clickType) {
			case LEFT:
				player.teleport(arena.getA().toBukkitLocation());
				player.closeInventory();
				break;
			case RIGHT:
				Bukkit.getScheduler().runTaskLaterAsynchronously(BedWars.getInstance(), () -> new ArenaGenerationMenu(arena).openMenu(player), 1L);
				player.closeInventory();
				break;
			case MIDDLE:
				if (arena.getCopiedArenas().size() >= 1) {
					Bukkit.getScheduler().runTaskLaterAsynchronously(BedWars.getInstance(), () -> new ArenaCopyMenu(arena).openMenu(player), 1L);
				} else {
					player.sendMessage(CC.translate("&cThis arena has no copied arenas! &4Generate some!"));
				}
				break;
		}
	}
}
