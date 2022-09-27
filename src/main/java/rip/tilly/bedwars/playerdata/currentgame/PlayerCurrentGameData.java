package rip.tilly.bedwars.playerdata.currentgame;

import lombok.Data;
import rip.tilly.bedwars.BedWars;

@Data
public class PlayerCurrentGameData {

    private BedWars main = BedWars.getInstance();

    private int gameKills;
    private int gameBedsDestroyed;

    private int pickaxeLevel = 0;
    private int axeLevel = 0;
    private boolean shears = false;
}
