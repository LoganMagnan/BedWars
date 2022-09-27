package rip.tilly.bedwars.game.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rip.tilly.bedwars.utils.CustomLocation;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Arena {

    private final String name;

    private List<CopiedArena> copiedArenas;
    private List<CopiedArena> availableArenas;

    private String icon;
    private int iconData;

    private CustomLocation a;
    private CustomLocation b;

    private CustomLocation min;
    private CustomLocation max;

    private CustomLocation teamAmin;
    private CustomLocation teamAmax;

    private CustomLocation teamBmin;
    private CustomLocation teamBmax;

    private int deadZone;
    private int buildMax;

    private CustomLocation teamAshop;
    private CustomLocation teamBshop;

    private CustomLocation teamAupgrades;
    private CustomLocation teamBupgrades;

    private List<CustomLocation> teamGenerators = new ArrayList<>();
    private List<CustomLocation> diamondGenerators = new ArrayList<>();
    private List<CustomLocation> emeraldGenerators = new ArrayList<>();

    private boolean enabled;

    public CopiedArena getAvailableArena() {
        CopiedArena copiedArena = this.availableArenas.get(0);
        this.availableArenas.remove(0);

        return copiedArena;
    }

    public void addCopiedArena(CopiedArena copiedArena) {
        this.copiedArenas.add(copiedArena);
    }

    public void addAvailableArena(CopiedArena copiedArena) {
        this.availableArenas.add(copiedArena);
    }
}
