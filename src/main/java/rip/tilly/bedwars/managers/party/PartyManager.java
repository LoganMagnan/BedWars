package rip.tilly.bedwars.managers.party;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.Clickable;
import rip.tilly.bedwars.utils.TtlHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PartyManager {

    private final BedWars plugin = BedWars.getInstance();

    @Getter private Map<UUID, Party> parties = new ConcurrentHashMap<>();
    private Map<UUID, UUID> partyLeaders = new ConcurrentHashMap<>();
    private Map<UUID, List<UUID>> partyInvites = new TtlHashMap<>(TimeUnit.SECONDS, 15);

    public boolean isLeader(UUID uuid) {
        return this.parties.containsKey(uuid);
    }

    public Party getParty(UUID player) {
        if (this.parties.containsKey(player)) {
            return this.parties.get(player);
        }

        if (this.partyLeaders.containsKey(player)) {
            UUID leader = this.partyLeaders.get(player);
            return this.parties.get(leader);
        }

        return null;
    }

    public void removePartyInvites(UUID uuid) {
        this.partyInvites.remove(uuid);
    }

    public boolean hasPartyInvite(UUID player, UUID other) {
        return this.partyInvites.get(player) != null && this.partyInvites.get(player).contains(other);
    }

    public void createPartyInvite(UUID requester, UUID requested) {
        this.partyInvites.computeIfAbsent(requested, k -> new ArrayList<>()).add(requester);
        Clickable partyInv = new Clickable(
                CC.translate("&d" + Bukkit.getPlayer(requester).getName() + " &ehas invited you to play in their party!"),
                CC.translate("&aClick to join the party!"),
                "/party accept " + Bukkit.getPlayer(requester).getName()
        );
        partyInv.sendToPlayer(Bukkit.getPlayer(requested));
    }

    public boolean isInParty(UUID player, Party party) {
        Party targetParty = this.getParty(player);
        return targetParty != null && targetParty.getLeader() == party.getLeader();
    }

    public Party getPartyByLeader(UUID uuid) {
        if (this.partyLeaders.containsKey(uuid)) {
            UUID leader = this.partyLeaders.get(uuid);
            return this.parties.get(leader);
        }

        return null;
    }

    public void createParty(Player player) {
        if (this.getParty(player.getUniqueId()) != null) {
            player.sendMessage(CC.translate("&cYou are already in a party!"));
            return;
        }

        Party party = new Party(player.getUniqueId());
        this.parties.put(player.getUniqueId(), party);
        this.plugin.getPlayerDataManager().resetPlayer(player, false);

        player.sendMessage(CC.translate("&aSuccessfully created a party!"));
    }

    private void disbandParty(Party party) {
        this.parties.remove(party.getLeader());

        party.broadcast(CC.translate("&cThe party has been disbanded!"));

        party.members().forEach(member -> {
            PlayerData memberData = this.plugin.getPlayerDataManager().getPlayerData(member.getUniqueId());
            if (this.partyLeaders.get(memberData.getUniqueId()) != null) {
                this.partyLeaders.remove(memberData.getUniqueId());
            }

            if (memberData.getPlayerState() == PlayerState.SPAWN) {
                this.plugin.getPlayerDataManager().resetPlayer(member, false);
            }
        });
    }

    public void joinParty(UUID leader, Player player) {
        Party party = this.getParty(leader);
        if (party == null) {
            player.sendMessage(CC.translate("&cThis party doesn't exist!"));
            return;
        }
        if (!this.hasPartyInvite(player.getUniqueId(), leader)) {
            player.sendMessage(CC.translate("&cYou have no pending invites!"));
            return;
        }
        this.partyLeaders.put(player.getUniqueId(), leader);
        party.addMember(player.getUniqueId());
        this.plugin.getPlayerDataManager().resetPlayer(player, false);
        party.broadcast(CC.translate("&a" + player.getName() + " &ehas joined the party!"));
    }

    public void leaveParty(Player player) {
        Party party = this.getParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(CC.translate("&cYou are not in a party!"));
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (this.parties.containsKey(player.getUniqueId())) {
            this.disbandParty(party);
        } else {
            party.broadcast(CC.translate("&a" + player.getName() + " &ehas left the party!"));
            party.removeMember(player.getUniqueId());
            this.partyLeaders.remove(player.getUniqueId());
        }

        switch (playerData.getPlayerState()) {
            case PLAYING:
                this.plugin.getGameManager().removePlayerFromGame(player, playerData, false);
                break;
            case SPECTATING:
                // this.plugin.getGameManager().removeSpectator(player);
                break;
        }

        this.plugin.getPlayerDataManager().resetPlayer(player, false);
    }
}
