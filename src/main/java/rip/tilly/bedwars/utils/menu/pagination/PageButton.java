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
public class PageButton extends Button {

	private int mod;
	private PaginatedMenu menu;

	@Override
	public ItemStack getButtonItem(Player player) {
		List<String> lore = new ArrayList<>();

		lore.add(" ");
		lore.add("&9Right Click to");
		lore.add("&9switch to a page.");
		lore.add(" ");

		return new ItemBuilder(Material.CARPET)
				.name(this.hasNext(player) ? (this.mod > 0 ? "&aNext Page" : "&cPrevious Page") : (this.mod > 0 ? "&7Last Page" : "&7First Page"))
				.durability(this.hasNext(player) ? (this.mod > 0 ? 5 : 14) : 8)
				.lore(lore)
				.hideFlags()
				.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		if (clickType == ClickType.RIGHT) {
			new ViewAllPagesMenu(this.menu).openMenu(player);
			playNeutral(player);
		} else {
			if (hasNext(player)) {
				this.menu.modPage(player, this.mod);
				playNeutral(player);
			} else {
				playFail(player);
			}
		}
	}

	private boolean hasNext(Player player) {
		int pg = this.menu.getPage() + this.mod;
		return pg > 0 && this.menu.getPages(player) >= pg;
	}
}
