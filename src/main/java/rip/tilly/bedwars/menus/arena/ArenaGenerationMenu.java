package rip.tilly.bedwars.menus.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.menus.arena.buttons.ArenaGenerateButton;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.Menu;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ArenaGenerationMenu extends Menu {

	private final Arena arena;

	@Getter private final int[] clonableAmounts = {1, 2, 3, 4, 5, 10, 15};

	@Override
	public String getTitle(Player player) {
		return CC.translate("&eArena Copies Generation");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (int curr : clonableAmounts) {
			buttons.put(1 + buttons.size(), new ArenaGenerateButton(arena, curr));
		}

		return buttons;
	}
}
