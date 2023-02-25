package net.galacticprojects.common.util;

import java.util.HashMap;
import java.util.UUID;

public class Verify {

    private final HashMap<UUID, String> playersCode;

    public Verify() {
        playersCode = new HashMap<>();
    }

    /**
     * @param uuid UUID from the player
     * @param code The code as String
     */
    public void add(UUID uuid, String code) {
        playersCode.put(uuid, code);
    }

    /**
     * @param uuid UUID from the player
     */
    public void remove(UUID uuid) {
        playersCode.remove(uuid);
    }

    /**
     * @param uuid UUID from the player
     */
    public boolean contains(UUID uuid) {
        return playersCode.containsKey(uuid);
    }

    /**
     * @param code The code as String
     */
    public boolean contains(String code) {
        return playersCode.containsValue(code);
    }

    public String get(UUID uuid){
        return playersCode.get(uuid);
    }

}
