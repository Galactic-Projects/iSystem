package net.galacticprojects.bungeecord.channel;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class LobbyMessage {

    ProxiedPlayer player;

    public LobbyMessage(ProxiedPlayer player) {
        this.player = player;
    }

    public void sendMessageData(int type, @Nullable String data) {

        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();

        if(networkPlayers == null || networkPlayers.isEmpty()) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Lobby");
        out.writeInt(type);
        if(data != null) {
            out.writeUTF(data);
        }

        player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());
    }

}
