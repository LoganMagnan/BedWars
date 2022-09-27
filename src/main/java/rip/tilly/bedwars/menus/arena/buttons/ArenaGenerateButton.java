package rip.tilly.bedwars.menus.arena.buttons;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;

import java.util.Arrays;

@AllArgsConstructor
public class ArenaGenerateButton extends Button {

	private final Arena arena;
	private final int currentCopyAmount;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.PAPER)
				.name("&eCreate " + currentCopyAmount + " Arena Copies")
				.lore(CC.translate(
						Arrays.asList(
								" ",
								"&7Clicking here will generate &d&l" + currentCopyAmount,
								"&7arenas for the map &d&l" + arena.getName() + "&7!",
								" ",
								"&a&lLeft Click &ato generate arenas")
				))
				.amount(currentCopyAmount)
				.build();
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
		player.performCommand("arena generate " + arena.getName() + " " + currentCopyAmount);

		player.sendMessage(CC.chatBar);
		player.sendMessage(CC.translate("&d&lGENERATING ARENAS&d..."));
		player.sendMessage(CC.translate(" "));
		player.sendMessage(CC.translate(" &7⚫ &9Arena: &e" + arena.getName()));
		player.sendMessage(CC.translate(" &7⚫ &9Copies: &e" + currentCopyAmount));
		player.sendMessage(CC.translate(" "));
		player.sendMessage(CC.translate("&7&oYou can check the progress in console."));
		player.sendMessage(CC.chatBar);

		player.closeInventory();
	}
}
