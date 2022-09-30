package net.galacticprojects.bungeecord.party;

import net.galacticprojects.bungeecord.ProxyPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PartyManager {

    private HashMap<UUID, Party> partyHashMap = new HashMap<>();

    private ProxiedPlayer proxiedPlayer;

    public PartyManager(Party party) {
        partyHashMap.put(party.getLeader(), party);
        for(UUID uniqueId : party.getMember()) {
            partyHashMap.put(uniqueId, party);
        }
    }

    public PartyManager(ProxiedPlayer proxiedPlayer) {
        this.proxiedPlayer = proxiedPlayer;
    }

    public Party getParty() {
        return partyHashMap.get(proxiedPlayer.getUniqueId());
    }
}
