package rip.tilly.bedwars.utils.aether.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import rip.tilly.bedwars.utils.aether.scoreboard.Board;

public class BoardCreateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Board board;
    @Getter private final Player player;

    public BoardCreateEvent(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
