package rip.tilly.bedwars.runnables;

import com.boydti.fawe.util.EditSessionBuilder;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.scheduler.BukkitRunnable;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.game.arena.CopiedArena;

public class ArenaCopyRemovalRunnable extends BukkitRunnable {

	private final int number;
	private final Arena arena;
	private final CopiedArena arenaCopy;

	private final BedWars plugin = BedWars.getInstance();

	public ArenaCopyRemovalRunnable(int number, Arena arena, CopiedArena arenaCopy) {
		this.number = number;
		this.arena = arena;
		this.arenaCopy = arenaCopy;
	}

	@Override
	public void run() {
		TaskManager.IMP.async(() -> {
			EditSession editSession = new EditSessionBuilder(arenaCopy.getA().getWorld()).fastmode(true).allowedRegionsEverywhere().autoQueue(false).limitUnlimited().build();
			CuboidRegion copyRegion = new CuboidRegion(
					new Vector(arenaCopy.getMax().getX(), arenaCopy.getMax().getY(), arenaCopy.getMax().getZ()),
					new Vector(arenaCopy.getMin().getX(), arenaCopy.getMin().getY(), arenaCopy.getMin().getZ())
			);

			try {
				editSession.setBlocks(copyRegion, new BaseBlock(BlockID.AIR));
			} catch (MaxChangedBlocksException e) {
				e.getStackTrace();
			}

			editSession.flushQueue();
		});

		this.plugin.getArenasConfig().getConfig().getConfigurationSection("arenas." + arena.getName() + ".copiedArenas").set(String.valueOf(number), null);
		this.plugin.getArenasConfig().save();

		this.plugin.getArenaManager().getArena(arena.getName()).getCopiedArenas().remove(arenaCopy);
		this.plugin.getArenaManager().getArena(arena.getName()).getAvailableArenas().remove(number);
	}
}
