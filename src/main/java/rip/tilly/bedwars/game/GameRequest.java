package rip.tilly.bedwars.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.tilly.bedwars.game.arena.Arena;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GameRequest {

    private final UUID requester;
    private final UUID requested;

    private final Arena arena;
    private final boolean party;
}
