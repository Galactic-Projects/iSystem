package net.galacticprojects.bungeecord.channel;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class LobbyMessage {

    ProxiedPlayer player;

    ProxyPlugin plugin;

    public LobbyMessage(@Nullable ProxiedPlayer player, ProxyPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    /**
     * 0: Language Changed<br>
     * 1: N/A<br>
     * 2: N/A<br>
     * 3: N/A<br>
     * 4: N/A<br>
     * 5: N/A<br>
     * 6: N/A<br>
     * 7: N/A<br>
     * 8: N/A<br>
     * 9: N/A<br>
     * 10: N/A<br>
     * 11: N/A<br>
     * 12: N/A<br>
     * 13: N/A<br>
     * 14: N/A<br>
     * 15: N/A
     *
     * @return type of this param
     */
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

        if(player == null) {
            for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
                player = proxiedPlayer;
                break;
            }
        }

        player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());
    }

    /**
     * 0: Coins<br>
     * 1: Level<br>
     * 2: N/A<br>
     * 3: N/A<br>
     * 4: N/A<br>
     * 5: N/A<br>
     * 6: N/A<br>
     * 7: N/A<br>
     * 8: N/A<br>
     * 9: N/A<br>
     * 10: N/A<br>
     * 11: N/A<br>
     * 12: N/A<br>
     * 13: N/A<br>
     * 14: N/A<br>
     * 15: N/A
     *
     * @return type of this param
     */
    public void sendMessageData(int type, int data) {

        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();

        if(networkPlayers == null || networkPlayers.isEmpty()) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Lobby");
        out.writeInt(type);
        if(data != 0) {
            out.writeInt(data);
        }

        if(player == null) {
            for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
                player = proxiedPlayer;
                break;
            }
        }

        player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());
    }

    /**
     * 22: Friends<br>
     * 1: N/A<br>
     * 2: N/A<br>
     * 3: N/A<br>
     * 4: N/A<br>
     * 5: N/A<br>
     * 6: N/A<br>
     * 7: N/A<br>
     * 8: N/A<br>
     * 9: N/A<br>
     * 10: N/A<br>
     * 11: N/A<br>
     * 12: N/A<br>
     * 13: N/A<br>
     * 14: N/A<br>
     * 15: N/A
     *
     * @return type of this param
     */
    public void sendPlayerMessage(int type, boolean data, UUID uniqueId) {

        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();

        if(networkPlayers == null || networkPlayers.isEmpty()) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Lobby");
        out.writeInt(type);
        out.writeBoolean(data);
        out.writeUTF(uniqueId.toString());
        if(player == null) {
            for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
                player = proxiedPlayer;
                break;
            }
        }

        player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());
    }

}
