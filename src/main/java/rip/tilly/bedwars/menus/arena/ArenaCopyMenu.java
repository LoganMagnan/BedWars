package rip.tilly.bedwars.menus.arena;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.game.arena.CopiedArena;
import rip.tilly.bedwars.menus.arena.buttons.ArenaCopyButton;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.Menu;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ArenaCopyMenu extends Menu {

	private final Arena arena;

	@Override
	public String getTitle(Player player) {
		return CC.translate("&eArena Copies");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		int i = 0;
		for (CopiedArena arenaCopy : BedWars.getInstance().getArenaManager().getArena(arena.getName()).getCopiedArenas()) {
			buttons.put(buttons.size(), new ArenaCopyButton(i, arena, arenaCopy));
			i++;
		}

		return buttons;
	}
}
