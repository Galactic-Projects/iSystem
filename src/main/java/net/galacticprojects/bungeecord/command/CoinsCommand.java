package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.command.provider.CommonPluginProvider;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Proxy;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.UUID;

@Command(name = "coins", description = "See your coins")
public class CoinsCommand {

    @Action("show")
    public void coins(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player", optional = true) String name) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();

        if (name != null) {
            UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

            common.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(uniqueIdTarget).thenAccept(targetData -> {
                    String coins = formatValue(targetData.getCoins());
                    actor.sendTranslatedMessage(CommandMessages.COINS_SHOW_OTHERS, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("input", coins));
                });
            });
            return;
        }

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                String coins = formatValue(playerData.getCoins());
                actor.sendTranslatedMessage(CommandMessages.COINS_SHOW, Key.of("input", coins));
            });
        });
    }

    @Action("pay")
    public void pay(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name="minimum", intValue = 1)
    }) int amount) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);
                Player targetData = sql.getPlayer(uniqueIdTarget).join();
                int playerCoins = playerData.getCoins();
                int targetCoins = targetData.getCoins();

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NEGATIVE);
                        return;
                    }

                    if (amount > playerCoins) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_ENOUGH);
                        return;
                    }

                    if(target == player || uniqueIdTarget == uniqueId){
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_SELF);
                        return;
                    }


                    playerCoins = (playerCoins - amount);
                    targetCoins = (targetCoins + amount);
                    playerData.setCoins(playerCoins);
                    targetData.setCoins(targetCoins);

                    sql.updatePlayer(playerData);
                    sql.updatePlayer(targetData);

                    String amountFormat = formatValue(amount);
                    actor.sendTranslatedMessage(CommandMessages.COINS_OTHERS_SENT_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("amount", amountFormat));
                    if (target != null) {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.COINS_OTHERS_SENT_TARGET, targetData.getLanguage(), Key.of("player", actor.getName()), Key.of("amount", amountFormat))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NUMBER);
                }
            });
        });

    }

    @Action("set")
    @Permission("system.command.coins")
    public void set(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name="minimum", intValue = 1),
    }) int amount) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);
                Player targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldA = targetData.getCoins();
                String oldAmount = formatValue(oldA);

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NEGATIVE);
                        return;
                    }

                    if (amount == targetData.getCoins()) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_ALREADY);
                        return;
                    }

                    String amountFormat = formatValue(amount);

                    targetData.setCoins(amount);
                    sql.updatePlayer(targetData);
                    actor.sendTranslatedMessage(CommandMessages.COINS_OTHERS_SET_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.COINS_OTHERS_SET_TARGET, targetData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NUMBER);
                }
            });
        });
    }

    @Action("add")
    @Permission("system.command.coins")
    public void add(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name="minimum", intValue = 1),
    }) int amount) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);
                Player targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldA = targetData.getCoins();
                String oldAmount = formatValue(oldA);

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NEGATIVE);
                        return;
                    }

                    int targetCoins = targetData.getCoins();
                    targetCoins = (targetCoins + amount);
                    String amountFormat = formatValue(targetCoins);

                    targetData.setCoins(targetCoins);
                    sql.updatePlayer(targetData);
                    actor.sendTranslatedMessage(CommandMessages.COINS_OTHERS_ADD_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.COINS_OTHERS_ADD_TARGET, targetData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NUMBER);
                }
            });
        });
    }

    @Action("remove")
    @Permission("system.command.coins")
    public void remove(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name="minimum", intValue = 1),
    }) int amount) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);
                Player targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldA = targetData.getCoins();
                String oldAmount = formatValue(oldA);

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NEGATIVE);
                        return;
                    }

                    if(oldA < amount) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_SMALL);
                        return;
                    }

                    int targetCoins = targetData.getCoins();
                    targetCoins = (targetCoins - amount);
                    String amountFormat = formatValue(targetCoins);

                    if(targetCoins < 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_SMALL);
                        return;
                    }

                    targetData.setCoins(targetCoins);
                    sql.updatePlayer(targetData);
                    actor.sendTranslatedMessage(CommandMessages.COINS_OTHERS_REMOVE_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.COINS_OTHERS_REMOVE_TARGET, targetData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NUMBER);
                }
            });
        });
    }

    private String formatValue(int value) {
        String format = Integer.toString(value);
        DecimalFormat decimal = new DecimalFormat();
        format = decimal.format((double) Integer.parseInt(format));
        return format;
    }
}
