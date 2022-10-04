package net.galacticprojects.bungeecord.party;

import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.util.HashMaps;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PartyManager {

    private ProxiedPlayer proxiedPlayer;

    public PartyManager(Party party) {
        HashMaps.partyHashMap.put(party.getLeader(), party);
        for(UUID uniqueId : party.getMember()) {
            HashMaps.partyHashMap.put(uniqueId, party);
        }
    }

    public PartyManager(ProxiedPlayer proxiedPlayer) {
        this.proxiedPlayer = proxiedPlayer;
    }

    public Party getParty() {
        return HashMaps.partyHashMap.get(proxiedPlayer.getUniqueId());
    }
}
