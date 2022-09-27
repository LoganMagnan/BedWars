package rip.tilly.bedwars.menus.arena;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.menus.arena.buttons.ArenaButton;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.pagination.PageButton;
import rip.tilly.bedwars.utils.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class ArenaManageMenu extends PaginatedMenu {

	private final BedWars plugin = BedWars.getInstance();

	@Override
	public String getPrePaginatedTitle(Player player) {
		return CC.translate("&eArena Management");
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		this.plugin.getArenaManager().getArenas().forEach((s, arena) -> buttons.put(buttons.size(), new ArenaButton(arena)));

		return buttons;
	}

	@Override
	public Map<Integer, Button> getGlobalButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		buttons.put(0, new PageButton(-1, this));
		buttons.put(8, new PageButton(1, this));

		bottomTopButtons(false, buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(7).build());

		return buttons;
	}

	@Override
	public int getSize() {
		return 9 * 5;
	}

	@Override
	public int getMaxItemsPerPage(Player player) {
		return 9 * 3;
	}
}
