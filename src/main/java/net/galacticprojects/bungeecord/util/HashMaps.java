package net.galacticprojects.bungeecord.util;

import net.galacticprojects.bungeecord.party.Party;

import java.util.HashMap;
import java.util.UUID;

public class HashMaps {

    private HashMaps() {
        throw new UnsupportedOperationException();
    }

    public static HashMap<UUID, Party> partyHashMap = new HashMap<>();

}
