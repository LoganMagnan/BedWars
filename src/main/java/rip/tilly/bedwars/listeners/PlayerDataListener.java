package rip.tilly.bedwars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.playerdata.PlayerData;

public class PlayerDataListener implements Listener {

	private final BedWars plugin = BedWars.getInstance();

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		Player player = Bukkit.getPlayer(event.getUniqueId());
		if (player != null) {
			if (player.isOnline()) {
				event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
				event.setKickMessage("§cYou tried to login too quickly after disconnecting.\n§cTry again in a few seconds.");

				this.plugin.getServer().getScheduler().runTask(this.plugin, () -> player.kickPlayer("§cDuplicate Login"));
				return;
			}

			PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
			this.plugin.getPlayerDataManager().savePlayerData(playerData);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerLogin(PlayerLoginEvent event) {
		PlayerData playerData = this.plugin.getPlayerDataManager().getOrCreate(event.getPlayer().getUniqueId());
		if (playerData == null) {
			event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			event.setKickMessage("§cAn error has occurred while loading your profile. Please reconnect.");
			return;
		}

		if (!playerData.isLoaded()) {
			this.plugin.getPlayerDataManager().savePlayerData(playerData);
			event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			event.setKickMessage("§cAn error has occurred while loading your profile. Please reconnect.");
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		Player player = event.getPlayer();
		PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

		switch (playerData.getPlayerState()) {
			case PLAYING:
				this.plugin.getGameManager().removePlayerFromGame(player, playerData, false);
				break;
			case QUEUE:
				this.plugin.getQueueManager().removePlayerFromQueue(player);
				break;
			case SPECTATING:
				this.plugin.getGameManager().removeSpectator(player);
				break;
		}

		this.handleLeave(player);
		this.handleDataSave(playerData);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);

		Player player = event.getPlayer();
		PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

		switch (playerData.getPlayerState()) {
			case PLAYING:
				this.plugin.getGameManager().removePlayerFromGame(player, playerData, false);
				break;
			case QUEUE:
				this.plugin.getQueueManager().removePlayerFromQueue(player);
				break;
			case SPECTATING:
				this.plugin.getGameManager().removeSpectator(player);
				break;
		}

		this.handleLeave(player);
		this.handleDataSave(playerData);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		this.plugin.getPlayerDataManager().resetPlayer(event.getPlayer(), true);
	}

	private void handleLeave(Player player) {
		this.plugin.getPartyManager().leaveParty(player);
		this.plugin.getGameManager().removeGameRequests(player.getUniqueId());
		this.plugin.getPartyManager().removePartyInvites(player.getUniqueId());
	}

	private void handleDataSave(PlayerData playerData) {
		if (playerData != null) {
			this.plugin.getPlayerDataManager().deletePlayer(playerData.getUniqueId());
		}
	}
}
