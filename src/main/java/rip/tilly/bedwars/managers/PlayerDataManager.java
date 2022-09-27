package rip.tilly.bedwars.managers;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.managers.hotbar.impl.HotbarItem;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.utils.PlayerUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

	@Getter private final Map<UUID, PlayerData> players = new HashMap<>();

	private final BedWars plugin = BedWars.getInstance();

	public PlayerData getOrCreate(UUID uniqueId) {
		return this.players.computeIfAbsent(uniqueId, PlayerData::new);
	}

	public PlayerData getPlayerData(UUID uniqueId) {
		return this.players.getOrDefault(uniqueId, new PlayerData(uniqueId));
	}

	public Collection<PlayerData> getAllPlayers() {
		return this.players.values();
	}

	public void loadPlayerData(PlayerData playerData) {
		Document document = this.plugin.getMongoManager().getPlayers().find(Filters.eq("uniqueId", playerData.getUniqueId().toString())).first();

		if (document != null) {
			playerData.setKills(document.getInteger("kills"));
			playerData.setDeaths(document.getInteger("deaths"));
			playerData.setXp(document.getDouble("xp"));
			playerData.setLevel(document.getInteger("level"));
			playerData.setWins(document.getInteger("wins"));
			playerData.setLosses(document.getInteger("losses"));
			playerData.setGamesPlayed(document.getInteger("gamesPlayed"));
			playerData.setBedsDestroyed(document.getInteger("bedsDestroyed"));
		}

		playerData.setLoaded(true);
	}

	public void savePlayerData(PlayerData playerData) {
		Document document = new Document();

		document.put("uniqueId", playerData.getUniqueId().toString());

		document.put("kills", playerData.getKills());
		document.put("deaths", playerData.getDeaths());
		document.put("xp", playerData.getXp());
		document.put("level", playerData.getLevel());
		document.put("wins", playerData.getWins());
		document.put("losses", playerData.getLosses());
		document.put("gamesPlayed", playerData.getGamesPlayed());
		document.put("bedsDestroyed", playerData.getBedsDestroyed());

		this.plugin.getMongoManager().getPlayers().replaceOne(Filters.eq("uniqueId", playerData.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
	}

	public void deletePlayer(UUID uniqueId) {
		this.savePlayerData(getPlayerData(uniqueId));
		this.getPlayers().remove(uniqueId);
	}

	public MongoCursor<Document> getPlayersSorted(String stat, int limit) {
		final Document document = new Document();
		document.put(stat, -1);

		return this.plugin.getMongoManager().getPlayers().find().sort(document).limit(limit).iterator();
	}

	public void resetPlayer(Player player, boolean toSpawn) {
		PlayerData playerData = this.getPlayerData(player.getUniqueId());
		playerData.setPlayerState(PlayerState.SPAWN);

		PlayerUtil.clearPlayer(player);

		this.giveSpawnItems(player);

		if (toSpawn) {
			player.teleport(this.plugin.getSpawnManager().getSpawnLocation().toBukkitLocation());
		}
	}

	public void giveSpawnItems(Player player) {
		boolean inParty = this.plugin.getPartyManager().getParty(player.getUniqueId()) != null;

		if (inParty) {
			this.plugin.getHotbarManager().getPartyItems().stream().filter(HotbarItem::isEnabled).forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItemStack()));
		} else {
			this.plugin.getHotbarManager().getSpawnItems().stream().filter(HotbarItem::isEnabled).forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItemStack()));
		}

		player.updateInventory();
	}
}
