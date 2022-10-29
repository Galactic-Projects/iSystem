package net.galacticprojects.bungeecord.util;

import com.google.common.collect.Table;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class Checkmode {

    private ArrayList<UUID> checkMode;

    public Checkmode() {
        this.checkMode = new ArrayList<>();
    }

    public void enable(UUID uniqueId) {
        if(!(checkMode.contains(uniqueId))) {
            checkMode.add(uniqueId);
        }
    }

    public void disable(UUID uniqueId) {
        checkMode.remove(uniqueId);
    }
}
