package net.galacticprojects.bungeecord.listener;

import com.syntaxphoenix.syntaxapi.utils.java.Streams;
import com.syntaxphoenix.syntaxapi.utils.java.lang.StringBuilder;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.w3c.dom.Text;

import java.awt.*;
import java.sql.SQLData;
import java.util.Arrays;

public class PingListener implements Listener {

    private ProxyPlugin plugin;

    public PingListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPing(ProxyPingEvent event) {
        CommonPlugin commonPlugin = plugin.getCommonPlugin();
        PluginConfiguration configuration = plugin.getPluginConfiguration();
        SQLDatabase database = commonPlugin.getDatabaseRef().get();
        if(database == null) {
            commonPlugin.getLogger().error("Database is not available!");
            return;
        }
        String ip = event.getConnection().getAddress().getAddress().getHostAddress();
        if(ip == null) {
            return;
        }
        Player player = database.getPlayer(ip).join();
        if(player == null) {
            return;
        }
        ServerPing ping = event.getResponse();
        if(ping == null) {
            return;
        }
        ServerPing.Players players = ping.getPlayers();
        ServerPing.Protocol protocol = ping.getVersion();

        String language = "en-uk";
        if(player.getLanguage() != null) {
            language = player.getLanguage();
        }

        String motdM1 = commonPlugin.getMessageManager().translate("system.maintenance.1", language) + " \n" + commonPlugin.getMessageManager().translate("system.maintenance.2", language);
        TextComponent motdM = new TextComponent(ComponentParser.parse(motdM1));
        String motd1 = commonPlugin.getMessageManager().translate("system.motd.1", language) + " \n" + commonPlugin.getMessageManager().translate("system.motd.2", language);
        TextComponent motd = new TextComponent(ComponentParser.parse(motd1));

        if(commonPlugin.getMessageManager().translate("system.maintenance.version", language) == null) {
            return;
        }

        String version = commonPlugin.getMessageManager().translate("system.maintenance.version", language);

        if(configuration.isMaintenance()) {
            protocol.setProtocol(2);
            ping.setDescriptionComponent(motdM);
            protocol.setName(version);
            players.setMax(0);
            ping.setVersion(protocol);
            ping.setPlayers(players);
        } else {
            ping.setDescriptionComponent(motd);
            players.setMax(Integer.parseInt(configuration.getPlayerAmount()));
            ping.setPlayers(players);
        }
    }
}
