package rip.tilly.bedwars.commands.arena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.menus.arena.ArenaManageMenu;
import rip.tilly.bedwars.utils.CC;

public class ArenaCommand implements CommandExecutor {

    private BedWars main = BedWars.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.admin")) {
            player.sendMessage(CC.translate("&cNo permission"));

            return true;
        }

        if (args.length == 0) {
            player.sendMessage(CC.translate(CC.chatBar));
            player.sendMessage(CC.translate("&dArena Commands"));
            player.sendMessage(CC.translate(CC.chatBar));
            player.sendMessage(CC.translate("&7⚫ &9/arena &7- &eArena help information"));
            player.sendMessage(CC.translate("&7⚫ &9/arena create <arena> &7- &eCreate an arena"));
            player.sendMessage(CC.translate("&7⚫ &9/arena remove <arena> &7- &eRemove an arena"));
            player.sendMessage(CC.translate("&7⚫ &9/arena enable <arena> &7- &eEnable an arena"));
            player.sendMessage(CC.translate("&7⚫ &9/arena disable <arena> &7- &eDisable an arena"));
            player.sendMessage(CC.translate("&7⚫ &9/arena info <arena> &7- &eLook at an arena's information"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setIcon <arena> &7- &eSet an arena's icon"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setA <arena> &7- &eSet A"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setB <arena> &7- &eSet B"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setMin <arena> &7- &eSet min"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setMax <arena> &7- &eSet max"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamAMin <arena> &7- &eSet team A min"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamAMax <arena> &7- &eSet team A max"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamBMin <arena> &7- &eSet team B min"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamBMax <arena> &7- &eSet team B max"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setDeadZone <arena> <amount> &7- &eSet the dead zone"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setBuildMax <arena> <amount> &7- &eSet the build max"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamGenerator <arena> &7- &eSet the team's generator"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setDiamondGenerator <arena> &7- &eSet the diamond generator"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setEmeraldGenerator <arena> &7- &eSet the emerald generator"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamAShop <arena> &7- &eSet team A shop"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamBShop <arena> &7- &eSet team B shop"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamAUpgrades <arena> &7- &eSet team A upgrades"));
            player.sendMessage(CC.translate("&7⚫ &9/arena setTeamBUpgrades <arena> &7- &eSet team B upgrades"));
            player.sendMessage(CC.translate("&7⚫ &9/arena list &7- &eLook at all of the arenas"));
            player.sendMessage(CC.translate("&7⚫ &9/arena save &7- &eSave all of the arenas"));
            player.sendMessage(CC.translate("&7⚫ &9/arena manage &7- &eOpen the arena manage menu"));
            player.sendMessage(CC.translate("&7⚫ &9/arena generate <arena> <amount> &7- &eGenerate the specified amount for the specified arena"));
            player.sendMessage(CC.translate(CC.chatBar));
        } else {
            switch (args[0].toLowerCase()) {
                case "create":
                    new CreateCommand().executeAs(sender, cmd, label, args);

                    break;
                case "remove":
                    new RemoveCommand().executeAs(sender, cmd, label, args);

                    break;
                case "enable":
                case "disable":
                    new EnableAndDisableCommand().executeAs(sender, cmd, label, args);

                    break;
                case "info":
                    new InfoCommand().executeAs(sender, cmd, label, args);

                    break;
                case "seticon":
                    new SetIconCommand().executeAs(sender, cmd, label, args);

                    break;
                case "seta":
                    new SetACommand().executeAs(sender, cmd, label, args);

                    break;
                case "setb":
                    new SetBCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setmin":
                    new SetMinCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setmax":
                    new SetMaxCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteamamin":
                    new SetTeamAMinCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteamamax":
                    new SetTeamAMaxCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteambmin":
                    new SetTeamBMinCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteambmax":
                    new SetTeamBMaxCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setdeadzone":
                    new SetDeadZoneCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setbuildmax":
                    new SetBuildMaxCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteamgenerator":
                    new SetTeamGeneratorCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setdiamondgenerator":
                    new SetDiamondGeneratorCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setemeraldgenerator":
                    new SetEmeraldGeneratorCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteamashop":
                    new SetTeamAShopCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteambshop":
                    new SetTeamBShopCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteamaupgrades":
                    new SetTeamAUpgradesCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setteambupgrades":
                    new SetTeamBUpgradesCommand().executeAs(sender, cmd, label, args);

                    break;
                case "list":
                    player.sendMessage(CC.translate("&9&lArenas List &7(&eTotal: " + this.main.getArenaManager().getArenas().size() + "&7)"));

                    for (Arena arena : this.main.getArenaManager().getArenas().values()) {
                        player.sendMessage(CC.translate("&7⚫ &9" + arena.getName() + " &7(" + (arena.isEnabled() ? "&aEnabled" : "&cDisabled") + "&7)"));
                    }

                    break;
                case "save":
                    this.main.getArenaManager().reloadArenas();

                    player.sendMessage(CC.translate("&aSuccessfully saved all of the arenas"));

                    break;
                case "manage":
                    if (this.main.getArenaManager().getArenas().size() == 0) {
                        player.sendMessage(CC.translate("&cError: There are no arenas"));

                        return true;
                    }

                    new ArenaManageMenu().openMenu(player);

                    break;
                case "generate":
                    new GenerateCommand().executeAs(sender, cmd, label, args);

                    break;
            }
        }

        return true;
    }
}
