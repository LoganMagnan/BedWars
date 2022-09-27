package rip.tilly.bedwars.commands.arena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.CustomLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InfoCommand extends BaseCommand {

    private BedWars main = BedWars.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cError: no arena found! /arena <args> <arena>"));
            return;
        }

        Arena arena = this.main.getArenaManager().getArena(args[1]);

        if (arena != null) {
            player.sendMessage(CC.translate(CC.chatBar));
            player.sendMessage(CC.translate("&dArena Information &7(&e" + arena.getName() + "&7)"));
            player.sendMessage(CC.translate(CC.chatBar));
            player.sendMessage(CC.translate("&7⚫ &9Name: &e" + arena.getName()));
            player.sendMessage(CC.translate("&7⚫ &9State: " + (arena.isEnabled() ? "&aEnabled" : "&cDisabled")));
            player.sendMessage(CC.translate("&7⚫ &91st Spawn: &e" + Math.round(arena.getA().getX()) + "&7, &e" + Math.round(arena.getA().getY()) + "&7, &e" + Math.round(arena.getA().getZ())));
            player.sendMessage(CC.translate("&7⚫ &92nd Spawn: &e" + Math.round(arena.getB().getX()) + "&7, &e" + Math.round(arena.getB().getY()) + "&7, &e" + Math.round(arena.getB().getZ())));
            player.sendMessage(CC.translate("&7⚫ &9Min Location: &e" + Math.round(arena.getMin().getX()) + "&7, &e" + Math.round(arena.getMin().getY()) + "&7, &e" + Math.round(arena.getMin().getZ())));
            player.sendMessage(CC.translate("&7⚫ &9Max Location: &e" + Math.round(arena.getMax().getX()) + "&7, &e" + Math.round(arena.getMax().getY()) + "&7, &e" + Math.round(arena.getMax().getZ())));
            player.sendMessage(CC.translate("&7⚫ &9TeamAmin Location: &e" + Math.round(arena.getTeamAmin().getX()) + "&7, &e" + Math.round(arena.getTeamAmin().getY()) + "&7, &e" + Math.round(arena.getTeamAmin().getZ())));
            player.sendMessage(CC.translate("&7⚫ &9TeamAmax Location: &e" + Math.round(arena.getTeamAmax().getX()) + "&7, &e" + Math.round(arena.getTeamAmax().getY()) + "&7, &e" + Math.round(arena.getTeamAmax().getZ())));
            player.sendMessage(CC.translate("&7⚫ &9TeamBmin Location: &e" + Math.round(arena.getTeamBmin().getX()) + "&7, &e" + Math.round(arena.getTeamBmin().getY()) + "&7, &e" + Math.round(arena.getTeamBmin().getZ())));
            player.sendMessage(CC.translate("&7⚫ &9TeamBmax Location: &e" + Math.round(arena.getTeamBmax().getX()) + "&7, &e" + Math.round(arena.getTeamBmax().getY()) + "&7, &e" + Math.round(arena.getTeamBmax().getZ())));
            player.sendMessage(CC.translate("&7⚫ &9DeadZone level: &e" + arena.getDeadZone()));
            player.sendMessage(CC.translate("&7⚫ &9BuildMax level: &e" + arena.getBuildMax()));

            player.sendMessage(CC.translate("&7⚫ &9Team Generators:"));
            AtomicInteger teamGen = new AtomicInteger(1);
            for (CustomLocation teamGenLocations : arena.getTeamGenerators()) {
                player.sendMessage(CC.translate("  &7⚫ &9#" + teamGen.getAndIncrement() + ": &e" + Math.round(teamGenLocations.getX()) + "&7, &e" + Math.round(teamGenLocations.getY()) + "&7, &e" + Math.round(teamGenLocations.getZ())));
            }

            player.sendMessage(CC.translate("&7⚫ &9Diamond Generators:"));
            AtomicInteger diaGen = new AtomicInteger(1);
            for (CustomLocation diaGenLocations : arena.getDiamondGenerators()) {
                player.sendMessage(CC.translate("  &7⚫ &9#" + diaGen.getAndIncrement() + ": &e" + Math.round(diaGenLocations.getX()) + "&7, &e" + Math.round(diaGenLocations.getY()) + "&7, &e" + Math.round(diaGenLocations.getZ())));
            }

            player.sendMessage(CC.translate("&7⚫ &9Emerald Generators:"));
            AtomicInteger emeGen = new AtomicInteger(1);
            for (CustomLocation emeGenLocations : arena.getEmeraldGenerators()) {
                player.sendMessage(CC.translate("  &7⚫ &9#" + emeGen.getAndIncrement() + ": &e" + Math.round(emeGenLocations.getX()) + "&7, &e" + Math.round(emeGenLocations.getY()) + "&7, &e" + Math.round(emeGenLocations.getZ())));
            }

            player.sendMessage(CC.translate("&7⚫ &9Available Arenas: &e" + (arena.getAvailableArenas().size())));
        } else {
            player.sendMessage(CC.translate("&cThis arena does not exist"));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
