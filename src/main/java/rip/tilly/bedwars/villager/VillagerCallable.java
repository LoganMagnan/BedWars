package rip.tilly.bedwars.villager;

import org.bukkit.Location;

public interface VillagerCallable {
    CustomVillager spawn(Location location, String name);

    void register();
}
