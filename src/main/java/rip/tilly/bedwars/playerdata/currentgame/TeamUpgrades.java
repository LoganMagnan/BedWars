package rip.tilly.bedwars.playerdata.currentgame;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.generators.GeneratorTier;
import rip.tilly.bedwars.generators.GeneratorType;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.upgrades.Upgrade;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;

import java.util.*;

@Data
public class TeamUpgrades {

    private BedWars main = BedWars.getInstance();

    private ArmorType armorType = ArmorType.LEATHER;

    private Map<UUID, ArmorType> armor = new HashMap<UUID, ArmorType>();

    private Map<Upgrade, Integer> upgrades = new HashMap<Upgrade, Integer>();

    public int getLevelForUpgrade(Upgrade upgrade) {
        return this.upgrades.getOrDefault(upgrade, 0);
    }

    public int getCostToUpgrade(Upgrade upgrade) {
        return upgrade.getCostForLevel(this.upgrades.getOrDefault(upgrade, 0) + 1);
    }

    public void upgrade(Player player, Game game, GameTeam gameTeam, Upgrade upgrade) {
        this.upgrades.put(upgrade, this.upgrades.getOrDefault(upgrade, 0) + 1);

        int level = this.upgrades.get(upgrade);

        gameTeam.playingPlayers().forEach(teamPlayer -> {
            teamPlayer.sendMessage(CC.translate(gameTeam.getPlayerTeam().getChatColor() + player.getName() + " &eupgraded &9" + upgrade.getFormattedName() + " &eto &cTier " + upgrade.getNumberToRomanNumeral(level)));

            this.giveTeamArmor(player);

            int upgradeLevel = 0;

            switch (upgrade) {
                case SHARPENED_SWORDS:
                    for (ListIterator<ItemStack> listIterator = player.getInventory().iterator(); listIterator.hasNext();) {
                        ItemStack itemStack = listIterator.next();

                        if (itemStack != null && itemStack.getType().name().toLowerCase().contains("sword")) {
                            itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        }
                    }

                    break;
                case MANIAC_MINER:
                    upgradeLevel = this.getLevelForUpgrade(upgrade);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000, upgradeLevel));

                    break;
                case FASTER_FORGE:
                    upgradeLevel = this.getLevelForUpgrade(upgrade);

                    int upgradeLevelFinal = upgradeLevel;

                    game.getActivatedGenerators().forEach(generator -> {
                        switch (upgradeLevelFinal) {
                            case 1:
                                if (generator.getGeneratorType() != GeneratorType.EMERALD) {
                                    generator.setGeneratorTier(GeneratorTier.TWO);
                                }

                                break;
                            case 2:
                                if (generator.getGeneratorType() != GeneratorType.EMERALD) {
                                    generator.setGeneratorTier(GeneratorTier.THREE);
                                }

                                break;
                            case 3:
                                if (generator.getGeneratorType() != GeneratorType.EMERALD) {
                                    generator.setGeneratorTier(GeneratorTier.THREE);
                                } else {
                                    generator.setActivated(true);
                                    generator.setGeneratorTier(GeneratorTier.ONE);
                                }

                                break;
                            case 4:
                                if (generator.getGeneratorType() != GeneratorType.EMERALD) {
                                    generator.setGeneratorTier(GeneratorTier.FOUR);
                                } else {
                                    generator.setGeneratorTier(GeneratorTier.TWO);
                                }

                                break;
                        }
                    });

                    break;
            }
        });
    }

    public ArmorType getArmorType(Player player) {
        return this.armor.getOrDefault(player.getUniqueId(), ArmorType.LEATHER);
    }

    public void setArmorType(Player player, ArmorType armorType) {
        this.armor.put(player.getUniqueId(), armorType);

        this.giveTeamArmor(player);
    }

    public void giveTeamArmor(Player player) {
        Material leggingsMaterial;
        Material bootsMaterial;

        Map<Enchantment, Integer> protection = new HashMap<Enchantment, Integer>();

        int level = this.getLevelForUpgrade(Upgrade.PROTECTION);

        if (level != 0) {
            protection.put(Enchantment.PROTECTION_ENVIRONMENTAL, level);
        }

        Game game = this.main.getGameManager().getGame(player.getUniqueId());

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

        GameTeam gameTeam = game.getTeams().get(playerData.getTeamId());

        player.getInventory().setHelmet((new ItemBuilder(Material.LEATHER_HELMET)).color(gameTeam.getPlayerTeam().getColor()).addEnchantments(protection).addUnbreakable().build());
        player.getInventory().setChestplate((new ItemBuilder(Material.LEATHER_CHESTPLATE)).color(gameTeam.getPlayerTeam().getColor()).addEnchantments(protection).addUnbreakable().build());

        ArmorType armorType = this.getArmorType(player);

        if (armorType == ArmorType.LEATHER) {
            player.getInventory().setLeggings((new ItemBuilder(Material.LEATHER_LEGGINGS)).color(gameTeam.getPlayerTeam().getColor()).addEnchantments(protection).addUnbreakable().build());
            player.getInventory().setBoots((new ItemBuilder(Material.LEATHER_BOOTS)).color(gameTeam.getPlayerTeam().getColor()).addEnchantments(protection).addUnbreakable().build());

            return;
        }

        switch (armorType) {
            case CHAIN:
                leggingsMaterial = Material.CHAINMAIL_LEGGINGS;
                bootsMaterial = Material.CHAINMAIL_BOOTS;

                break;
            case IRON:
                leggingsMaterial = Material.IRON_LEGGINGS;
                bootsMaterial = Material.IRON_BOOTS;

                break;
            case DIAMOND:
                leggingsMaterial = Material.DIAMOND_LEGGINGS;
                bootsMaterial = Material.DIAMOND_BOOTS;

                break;
            default:
                return;
        }

        player.getInventory().setLeggings((new ItemBuilder(leggingsMaterial)).addEnchantments(protection).addUnbreakable().build());
        player.getInventory().setBoots((new ItemBuilder(bootsMaterial)).addEnchantments(protection).addUnbreakable().build());
    }
}
