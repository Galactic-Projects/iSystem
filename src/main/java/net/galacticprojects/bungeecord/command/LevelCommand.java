package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLException;
import java.util.UUID;

@Command(name = "level", description = "See your level")
public class LevelCommand {

    @Action("show")
    public void show(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player", optional = true) String name){
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();

        if (name != null) {
            UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

            common.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(uniqueIdTarget).thenAccept(targetData -> {
                    actor.sendTranslatedMessage(CommandMessages.LEVEL_SHOW_OTHERS, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("input", targetData.getLevel()));
                });
            });
            return;
        }

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                actor.sendTranslatedMessage(CommandMessages.LEVEL_SHOW, Key.of("input", playerData.getLevel()));
            });
        });
    }

    @Action("set")
    @Permission("system.command.level")
    public void set(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name = "minimum", intValue = 1)
    }) int amount){
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);
                Player targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldAmount = Integer.parseInt(targetData.getLevel());

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_NEGATIVE);
                        return;
                    }

                    if (amount == Integer.parseInt(targetData.getLevel())) {
                        actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_ALREADY);
                        return;
                    }

                    if(amount > 100){
                        actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_BIG);
                        return;
                    }

                    targetData.setLevel(String.valueOf(amount));
                    sql.updatePlayer(targetData);
                    actor.sendTranslatedMessage(CommandMessages.LEVEL_OTHERS_SET_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", amount));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.LEVEL_OTHERS_SET_TARGET, targetData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", amount))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_NUMBER);
                }
            });
        });
    }

    @Action("add")
    @Permission("system.command.level")
    public void add(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name = "minimum", intValue = 1)
    }) int amount){
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);
                Player targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldAmount = Integer.parseInt(targetData.getLevel());

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_NEGATIVE);
                        return;
                    }

                    int newAmount = (oldAmount + amount);

                    if(newAmount > 100){
                        actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_BIG);
                        return;
                    }

                    targetData.setLevel(String.valueOf(newAmount));
                    sql.updatePlayer(targetData);
                    actor.sendTranslatedMessage(CommandMessages.LEVEL_OTHERS_ADD_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", newAmount));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.LEVEL_OTHERS_ADD_TARGET, targetData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", newAmount))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_NUMBER);
                }
            });
        });
    }

    @Action("remove")
    @Permission("system.command.level")
    public void remove(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name = "minimum", intValue = 1)
    }) int amount){
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);
                Player targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldAmount = Integer.parseInt(targetData.getLevel());

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_NEGATIVE);
                        return;
                    }

                    if(Integer.parseInt(targetData.getLevel()) < amount) {
                        actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_SMALL);
                        return;
                    }

                    int newAmount = (oldAmount - amount);

                    if(newAmount < 1){
                        actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_SMALL);
                        return;
                    }

                    targetData.setLevel(String.valueOf(newAmount));
                    sql.updatePlayer(targetData);
                    actor.sendTranslatedMessage(CommandMessages.LEVEL_OTHERS_REMOVE_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", newAmount));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.LEVEL_OTHERS_REMOVE_TARGET, targetData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", newAmount))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.LEVEL_ERRORS_NUMBER);
                }
            });
        });
    }
}
