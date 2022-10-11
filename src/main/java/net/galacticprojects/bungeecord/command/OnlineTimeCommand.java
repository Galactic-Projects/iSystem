package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.command.message.CommandMessages;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.md_5.bungee.api.connection.ProxiedPlayer;


@Command(name = "onlinetime", description = " ")
public class OnlineTimeCommand {

    @Action("")
    public void onlineTime(BungeeActor<?> bungeeActor, ProxyPlugin plugin, CommonPlugin common) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(bungeeActor.getId());

        if (player == null) {
            bungeeActor.sendTranslatedMessage(CommandMessages.COMMAND_HELP_TREE$EXECUTABLE, common);
        }
        common.getDatabaseRef().asOptional().ifPresent(sqlDatabase -> {
            sqlDatabase.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                long time = playerData.getOnlineTime();
                long minute = (time % 3600) / 60;
                long hours = (time % 86400) / 3600;
                long days = (time % 2629746) / 86400;

                String language = "en-uk";
                if(playerData.getLanguage() != null) {
                    language = playerData.getLanguage();
                }

                
                bungeeActor.sendTranslatedMessage(net.galacticprojects.bungeecord.message.CommandMessages.COMMAND_ONLINETIME_SUCCESS, common, 
                        Key.of("daystime", days), Key.of("hourtime", hours), Key.of("minutetime", minute));
            });
        });
    }

    @Action("player")
    public void onlineTimePlayer(BungeeActor<?> actor, CommonPlugin common) {
        SQLDatabase database = common.getDatabaseRef().get();

        if (database == null) {
            common.getLogger().error(new Throwable("Database is unavailable"));
            return;
        }
        /* Maths HANS GET SE FLAMETHROWER
             seconds / 31556952; - YEAR
             seconds % 31556952 / 2629746; - MONTH
             seconds % 2629746 / 86400; - DAY
             seconds % 86400 / 3600; - HOUR
             seconds % 3600 / 60; - MINUTE
         */
        database.getPlayer(actor.getId()).thenAccept(player -> {
            long time = player.getOnlineTime();
            long minute = (time % 3600) / 60;
            long hours = (time % 86400) / 3600;
            long days = (time % 2629746) / 86400;

        });
    }
}
