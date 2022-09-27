package rip.tilly.bedwars.managers.queue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.tilly.bedwars.game.GameType;

@Getter
@RequiredArgsConstructor
public class QueueEntry {

    private final GameType gameType;
}
