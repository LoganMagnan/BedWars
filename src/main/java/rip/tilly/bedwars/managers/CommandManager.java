package rip.tilly.bedwars.managers;

import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.arena.ArenaCommand;
import rip.tilly.bedwars.commands.level.LevelCommand;
import rip.tilly.bedwars.commands.party.PartyCommand;
import rip.tilly.bedwars.commands.setspawn.SetSpawnCommand;
import rip.tilly.bedwars.commands.spectate.SpectateCommand;
import rip.tilly.bedwars.commands.toggle.ToggleCommand;
import rip.tilly.bedwars.commands.xp.XpCommand;

public class CommandManager {

    private BedWars main = BedWars.getInstance();

    public CommandManager() {
        this.registerCommands();
    }

    private void registerCommands() {
        this.main.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.main.getCommand("level").setExecutor(new LevelCommand());
        this.main.getCommand("xp").setExecutor(new XpCommand());
        this.main.getCommand("party").setExecutor(new PartyCommand());
        this.main.getCommand("p").setExecutor(new PartyCommand());
        this.main.getCommand("arena").setExecutor(new ArenaCommand());
        this.main.getCommand("toggle").setExecutor(new ToggleCommand());
        this.main.getCommand("spectate").setExecutor(new SpectateCommand());
        this.main.getCommand("spec").setExecutor(new SpectateCommand());
    }
}
