package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.command.annotation.Permission;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.util.Type;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.model.History;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.TimeHelper;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.UUID;

@Command(name = "history", description = "See the history by players.")

public class HistoryCommand {

    @Action("get")
    @Permission("system.command.history")
    public void get(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                if (sql.getHistory(uniqueIdTarget).join() == null) {
                    actor.sendTranslatedMessage(CommandMessages.COMMAND_HISTORY_NOHISTORY);
                    return;
                }

                String playerName = MojangProfileService.getName(uniqueIdTarget);
                actor.sendTranslatedMessage(CommandMessages.COMMAND_HISTORY_FORMATPREFIX, Key.of("player", playerName));

                for (History history : sql.getHistory(uniqueIdTarget).join()) {
                    int id = history.getId();
                    UUID teamMember = history.getOwner();
                    String reason = history.getReason();
                    Type type = history.getType();
                    String time = TimeHelper.BAN_TIME_FORMATTER.format(history.getTime());
                    String creationTime = TimeHelper.BAN_TIME_FORMATTER.format(history.getCreationTime());
                    actor.sendTranslatedMessage(CommandMessages.COMMAND_HISTORY_FORMAT, Key.of("id", id), Key.of("staff", MojangProfileService.getName(teamMember)), Key.of("type", type), Key.of("reason", reason), Key.of("time", time), Key.of("creationTime", creationTime));
                }
            });
        });
    }

    @Action("reset")
    @Permission("system.command.history")
    public void reset(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                if (sql.getHistory(uniqueIdTarget).join() == null) {
                    actor.sendTranslatedMessage(CommandMessages.COMMAND_HISTORY_NOHISTORY);
                    return;
                }

                sql.deleteHistory(uniqueIdTarget);
                String playerName = MojangProfileService.getName(uniqueIdTarget);
                actor.sendTranslatedMessage(CommandMessages.COMMAND_HISTORY_DELETED, Key.of("player", playerName));
            });
        });
    }
}


