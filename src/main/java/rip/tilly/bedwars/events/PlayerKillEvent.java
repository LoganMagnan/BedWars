package rip.tilly.bedwars.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerKillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Player killer;

    public PlayerKillEvent(Player player, Player killer) {
        this.player = player;
        this.killer = killer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}