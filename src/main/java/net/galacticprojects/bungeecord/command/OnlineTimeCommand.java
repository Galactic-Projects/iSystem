package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;


@Command(name = "onlinetime", description = "See your onlinetime!")
public class OnlineTimeCommand {

    @Action("")
    public void onlineTime(BungeeActor<?> actor, ProxyPlugin plugin, CommonPlugin common, @Argument(name = "player", optional = true) String target) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uuid = player.getUniqueId();

        if(target != null){
            UUID targetUnique = MojangProfileService.getUniqueId(target);

            common.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(targetUnique).thenAccept(playerData -> {
                    long time = playerData.getOnlineTime();
                    long minute = (time % 3600) / 60;
                    long hours = (time % 86400) / 3600;
                    long days = (time % 2629746) / 86400;

                    actor.sendTranslatedMessage(CommandMessages.COMMAND_ONLINETIME_SUCCESS_OTHERS, Key.of("player", MojangProfileService.getName(targetUnique)), Key.of("daystime", days), Key.of("hourtime", hours), Key.of("minutetime", minute));
                });
            });
            return;
        }

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uuid).thenAccept(playerData -> {
                long time = playerData.getOnlineTime();
                long minute = (time % 3600) / 60;
                long hours = (time % 86400) / 3600;
                long days = (time % 2629746) / 86400;

                actor.sendTranslatedMessage(CommandMessages.COMMAND_ONLINETIME_SUCCESS, Key.of("daystime", days), Key.of("hourtime", hours), Key.of("minutetime", minute));
            });
        });
    }
}
