package rip.tilly.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.managers.party.Party;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerSettings;
import rip.tilly.bedwars.utils.CC;

public class PartyChatListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Party party = this.plugin.getPartyManager().getParty(player.getUniqueId());
        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        PlayerSettings settings = playerData.getPlayerSettings();

        if (party == null && !settings.isPartyChatEnabled()) {
            return;
        }

        String chatMessage = event.getMessage();
        if (party != null) {
            if (settings.isPartyChatEnabled()) {
                event.setCancelled(true);
                String message = CC.translate("&9Party > &d" + player.getDisplayName() + "&f: " + chatMessage);
                party.broadcast(message);
            }
        } else if (settings.isPartyChatEnabled()) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cSince you are not in a party we toggled your party chat to false!"));
            settings.setPartyChatEnabled(!settings.isPartyChatEnabled());
        }
    }
}
