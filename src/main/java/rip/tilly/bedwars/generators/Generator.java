package rip.tilly.bedwars.generators;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.utils.CC;

public class Generator {

    private final BedWars main = BedWars.getInstance();

    private final Location location;
    private final GeneratorType generatorType;
    private final boolean isIslandGenerator;
    private GeneratorTier generatorTier;
    private ArmorStand indictatorArmorStand = null;
    private ArmorStand generatorTypeArmorStand = null;
    private ArmorStand generatorTierArmorStand = null;
    private BukkitTask rotateIndicatorTask = null;
    private int secondsSinceActivation;
    private boolean activated;

    private final Game game;

    public Generator(Location location, GeneratorType generatorType, boolean isIslandGenerator, Game game) {
        this.location = location;
        this.generatorType = generatorType;
        this.isIslandGenerator = isIslandGenerator;
        this.secondsSinceActivation = this.getActivationTime();
        this.activated = false;
        this.game = game;
    }

    public void spawn() {
        Material material;

        if (this.generatorType == GeneratorType.DIAMOND && this.isIslandGenerator) {
            return;
        }

        if (!this.activated) {
            if (this.generatorTypeArmorStand != null) {
                this.generatorTypeArmorStand.setCustomNameVisible(false);
            }

            if (this.generatorTierArmorStand != null) {
                this.generatorTierArmorStand.setCustomNameVisible(false);
            }

            return;
        }

        this.secondsSinceActivation++;

        if (!this.isIslandGenerator) {
            this.generatorTypeArmorStand.setCustomNameVisible(true);
            this.generatorTypeArmorStand.setCustomName(CC.translate(this.getArmorStandName()));
            this.generatorTierArmorStand.setCustomNameVisible(true);
            this.generatorTierArmorStand.setCustomName(CC.translate("&fTier: &c" + this.generatorTier.getFormattedName()));
        }

        if (this.secondsSinceActivation < this.getActivationTime()) {
            return;
        }

        this.secondsSinceActivation = 0;

        switch (this.generatorType) {
            case IRON:
                material = Material.IRON_INGOT;

                break;
            case GOLD:
                material = Material.GOLD_INGOT;

                break;
            case DIAMOND:
                material = Material.DIAMOND;

                break;
            case EMERALD:
                material = Material.EMERALD;

                break;
            default:
                throw new IllegalStateException("Unexcepted value: " + this.generatorType);
        }

        ItemStack drop = new ItemStack(material, 1);
        this.game.getDroppedItems().add(drop);
        this.location.getWorld().dropItemNaturally(this.location, drop);
    }

    public void setActivated(boolean activated) {
        if (this.activated == activated) {
            return;
        }

        this.activated = activated;

        if (this.isIslandGenerator) {
            return;
        }

        if (!activated) {
            if (this.generatorTypeArmorStand != null) {
                this.generatorTypeArmorStand.remove();
            }

            if (this.generatorTierArmorStand != null) {
                this.generatorTierArmorStand.remove();
            }

            if (this.indictatorArmorStand != null) {
                this.indictatorArmorStand.remove();
            }

            if (this.rotateIndicatorTask != null) {
                this.rotateIndicatorTask.cancel();
            }

            return;
        }

        Material generatorMaterial;

        if (this.generatorType == GeneratorType.DIAMOND) {
            generatorMaterial = Material.DIAMOND_BLOCK;
        } else if (this.generatorType == GeneratorType.EMERALD) {
            generatorMaterial = Material.EMERALD_BLOCK;
        } else {
            generatorMaterial = Material.STONE;
        }

        this.indictatorArmorStand = this.location.getWorld().spawn(this.location.clone().add(0.0D, 0.5D, 0.0D), ArmorStand.class);
        this.indictatorArmorStand.setVisible(false);
        this.indictatorArmorStand.setOp(true);
        this.indictatorArmorStand.setGravity(false);
        this.indictatorArmorStand.getEquipment().setHelmet(new ItemStack(generatorMaterial));
        this.generatorTypeArmorStand = this.location.getWorld().spawn(this.location.clone().add(0.0D, 1.0D, 0.0D), ArmorStand.class);
        this.generatorTypeArmorStand.setVisible(false);
        this.generatorTypeArmorStand.setOp(true);
        this.generatorTypeArmorStand.setCustomNameVisible(true);
        this.generatorTypeArmorStand.setGravity(false);
        this.generatorTypeArmorStand.setCustomName(CC.translate(this.getArmorStandName()));
        this.generatorTierArmorStand = this.location.getWorld().spawn(this.location.clone().add(0.0D, 1.8D, 0.0D), ArmorStand.class);
        this.generatorTierArmorStand.setVisible(false);
        this.generatorTierArmorStand.setOp(true);
        this.generatorTierArmorStand.setCustomNameVisible(true);
        this.generatorTierArmorStand.setGravity(false);
        this.generatorTierArmorStand.setCustomName(CC.translate(this.getArmorStandName()));

        if (this.main != null) {
            this.rotateIndicatorTask = this.main.getServer().getScheduler().runTaskTimerAsynchronously(this.main, () -> {
                EulerAngle eulerAngle = this.indictatorArmorStand.getHeadPose();

                if (eulerAngle.getY() > 360D) {
                    eulerAngle = eulerAngle.setY(0D);
                }

                this.indictatorArmorStand.setHeadPose(eulerAngle.setY(eulerAngle.getY() + 5D));
            }, 0L, 4L);
        }
    }

    public int getActivationTime() {
        switch (this.generatorType) {
            case IRON:
                if (this.generatorTier == GeneratorTier.ONE) {
                    return 3;
                }

                if (this.generatorTier == GeneratorTier.TWO) {
                    return 2;
                }

                if (this.generatorTier == GeneratorTier.THREE) {
                    return 2;
                }

                return 1;
            case GOLD:
                if (this.generatorTier == GeneratorTier.ONE) {
                    return 12;
                }

                if (this.generatorTier == GeneratorTier.TWO) {
                    return 10;
                }

                if (this.generatorTier == GeneratorTier.THREE) {
                    return 8;
                }

                return 5;
            case DIAMOND:
                if (this.generatorTier == GeneratorTier.ONE) {
                    return 30;
                }

                if (this.generatorTier == GeneratorTier.TWO) {
                    return 20;
                }

                if (this.generatorTier == GeneratorTier.THREE) {
                    return 15;
                }

                return 10;
            case EMERALD:
                if (this.isIslandGenerator) {
                    if (this.generatorTier == GeneratorTier.ONE) {
                        return 30;
                    }

                    if (this.generatorTier == GeneratorTier.TWO) {
                        return 20;
                    }
                } else {
                    if (this.generatorTier == GeneratorTier.ONE) {
                        return 40;
                    }

                    if (this.generatorTier == GeneratorTier.TWO) {
                        return 20;
                    }

                    return 15;
                }

                break;
        }

        return 20;
    }

    public String getArmorStandName() {
        int timeLeft = this.getActivationTime() - this.secondsSinceActivation;

        if (timeLeft == 0) {
            timeLeft = this.getActivationTime();
        }

        String generatorTypeName = this.generatorType.name().toUpperCase().charAt(0) + this.generatorType.name().substring(1).toLowerCase();

        return this.generatorType.getColorCode() + generatorTypeName + " &7⚫ &f" + timeLeft + " second" + (timeLeft == 1 ? "" : "s") + "...";
    }

    public Location getLocation() {
        return this.location;
    }

    public GeneratorType getGeneratorType() {
        return this.generatorType;
    }

    public void setGeneratorTier(GeneratorTier generatorTier) {
        this.generatorTier = generatorTier;
    }
}
