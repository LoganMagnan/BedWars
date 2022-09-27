package rip.tilly.bedwars.events;

import lombok.Getter;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameTeam;

@Getter
public class GameEndEvent extends GameEvent {

    private final GameTeam winningTeam;
    private final GameTeam losingTeam;

    public GameEndEvent(Game game, GameTeam winningTeam, GameTeam losingTeam) {
        super(game);

        this.winningTeam = winningTeam;
        this.losingTeam = losingTeam;
    }
}
