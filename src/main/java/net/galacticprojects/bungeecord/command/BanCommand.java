package net.galacticprojects.bungeecord.command;

import me.lauriichan.wildcard.systemcore.command.annotation.Action;
import me.lauriichan.wildcard.systemcore.command.annotation.Argument;
import me.lauriichan.wildcard.systemcore.command.annotation.Param;
import me.lauriichan.wildcard.systemcore.inject.Inject;
import me.lauriichan.wildcard.systemcore.util.JavaInstance;
import me.lauriichan.wildcard.systemcore.util.Tuple;
import net.galacticprojects.bungeecord.Messages;
import net.galacticprojects.bungeecord.config.commands.BanConfiguration;
import net.galacticprojects.bungeecord.config.commands.BanConfiguration.BanInfo;
import net.galacticprojects.common.databaseLegacy.MySQL;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.TimeHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.UUID;

public class BanCommand {

    @Inject
    private MySQL database;

    private BanConfiguration config = JavaInstance.get(BanConfiguration.class);

    @SuppressWarnings("unchecked")
    @Action(path = "")
    public void onCommand(@Argument(sender = true) CommandSender sender, @Argument(index = 0) String name, @Argument(index = 1, params = {
         @Param(name = "min", value = "1"),
         @Param(name = "max", value = "99")
    }) int banId) {
        if(!sender.hasPermission("system.ban")) {

            return;
        }
        UUID uniqueId = MojangProfileService.getUniqueId(name.startsWith("!") ? name.substring(1) : name);
        if(uniqueId == null) {

            return;
        }
        BanInfo info = config.getInfo(banId);
        if(info == null) {

            return;
        }
        OffsetDateTime creation = OffsetDateTime.now();
        OffsetDateTime time = info.getHours() == 0 ? null : creation.plusHours(info.getHours());
        Ban ban = database.createBan(uniqueId, (sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getUniqueId() : null),
                info.getReason(), time, creation).join();
        if(ban == null) {

            return;
        }
        ban.getPlayerAsPlayer().ifPresent(player -> {
            Messages.GENERAL_BAN_FORMAT.kick(player, Messages.buildBanPlaceholders(ban, true));
        });
        if(ban.isPermanent()) {
            Messages.COMMAND_BAN_BANNED_PERMANENT.send(sender, Tuple.of("player", name));
            return;
        }
        Messages.COMMAND_BAN_BANNED_TIME.send(sender, Tuple.of("player", name),
                Tuple.of("time", TimeHelper.BAN_TIME_FORMATTER.format(time)));
    }

    @SuppressWarnings("unchecked")
    @Action(path = "delete")
    public void onDelete(@Argument(sender = true) CommandSender sender, String name) {
        if(!sender.hasPermission("system.ban")) {

            return;
        }
        UUID uniqueId = MojangProfileService.getUniqueId(name);
        if (uniqueId == null) {
            return;
        }
        if (database.deleteBan(uniqueId).join()) {
            Messages.COMMAND_BAN_UNBAN_SUCCESSFUL.send(sender, Tuple.of("player", name));
            return;
        }
        Messages.COMMAND_BAN_UNBAN_UNSUCCESSFUL.send(sender, Tuple.of("player", name));
    }
}