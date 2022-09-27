package rip.tilly.bedwars.utils.menu.pagination;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JumpToPageButton extends Button {

	private int page;
	private PaginatedMenu menu;
	private boolean current;

	@Override
	public ItemStack getButtonItem(Player player) {
		List<String> lore = new ArrayList<>();

		lore.add(" ");
		if (this.current) {
			lore.add("&9Current page");
		} else {
			lore.add("&9Other page");
		}
		lore.add(" ");

		return new ItemBuilder(Material.INK_SACK)
				.name("&d&lPage: &e" + this.page)
				.durability(this.current ? 10 : 8)
				.lore(lore)
				.hideFlags()
				.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		this.menu.modPage(player, this.page - this.menu.getPage());
		playNeutral(player);
	}
}
