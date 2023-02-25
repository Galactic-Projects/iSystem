package net.galacticprojects.bungeecord.util;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import eu.cloudnetservice.modules.bridge.player.PlayerProvider;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static net.minecraft.locale.Language.inject;

public class TablistManager {

    @Inject
    public TablistManager(ProxyPlugin plugin, ProxiedPlayer player) {
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();

        if (database == null) {
            plugin.getCommonPlugin().getLogger().error("Database is not available!");
            return;
        }
        Player playerData = database.getPlayer(player.getUniqueId()).join();

        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(ProxyServer.getInstance().getPlayers().size() == 0) {
                    return;
                }

                String language = "en-uk";
                if (playerData.getLanguage() != null) {
                    language = playerData.getLanguage();
                }

                Key server = Key.of("server", ProxyPlugin.getInstance().getProxy().getPlayer(player.getUniqueId()).getServer().getInfo().getName());
                Key players = Key.of("online", plugin.getProxy().getOnlineCount());
                Key maxPlayers = Key.of("max", plugin.getPluginConfiguration().getPlayerAmount());

                String header = plugin.getCommonPlugin().getMessageManager().translate("system.tablist.header", language, server, players, maxPlayers);
                String footer = plugin.getCommonPlugin().getMessageManager().translate("system.tablist.footer", language);
                player.setTabHeader(ComponentParser.parse(header), ComponentParser.parse(footer));
            }
        }, 1L, 1L, TimeUnit.SECONDS);
    }

    public void set() {

    }

    public void reset(ProxiedPlayer player) {
        player.resetTabHeader();
    }
}
