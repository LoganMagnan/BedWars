package rip.tilly.bedwars.managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.CustomLocation;

@Getter
@Setter
public class SpawnManager {

    private final BedWars plugin = BedWars.getInstance();
    private final FileConfiguration config = this.plugin.getMainConfig().getConfig();

    private CustomLocation spawnLocation;
    private CustomLocation spawnMin;
    private CustomLocation spawnMax;

    public SpawnManager() {
        this.loadConfig();
    }

    private void loadConfig() {
        if (this.config.contains("SPAWN.LOCATION")) {
            try {
                this.spawnLocation = CustomLocation.stringToLocation(this.config.getString("SPAWN.LOCATION"));
                this.spawnMin = CustomLocation.stringToLocation(this.config.getString("SPAWN.MIN"));
                this.spawnMax = CustomLocation.stringToLocation(this.config.getString("SPAWN.MAX"));
            } catch (NullPointerException exception) {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&cSpawn min & max locations not found!"));
            }
        }
    }

    public void saveConfig() {
        this.config.set("SPAWN.LOCATION", CustomLocation.locationToString(this.spawnLocation));
        this.config.set("SPAWN.MIN", CustomLocation.locationToString(this.spawnMin));
        this.config.set("SPAWN.MAX", CustomLocation.locationToString(this.spawnMax));

        this.plugin.getMainConfig().save();
    }
}
