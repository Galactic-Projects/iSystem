package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.command.annotation.Param;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.channel.LobbyMessage;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.swing.text.html.Option;

@Command(name = "language", description = " ")
public class LanguageCommand {

    @Action("")
    public void language(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "language") String language){

        Actor<CommandSender> senderActor = actor.as(CommandSender.class);

        SQLDatabase database = common.getDatabaseRef().get();
        if(language.toLowerCase().equals("deutsch")) {
            database.getPlayer(senderActor.getId()).thenAccept(player -> {
                actor.setLanguage("de-de");
                ProxiedPlayer proxiedPlayer = actor.as(ProxiedPlayer.class).getHandle();
                LobbyMessage lobbyMessage = new LobbyMessage(proxiedPlayer);
                lobbyMessage.sendMessageData(452, null);
                senderActor.sendMessage(common.getMessageManager().translate("command.language.changed", "de-de", Key.of("language", "Deutsch")));
            });
            return;
        }
        if(language.toLowerCase().equals("english")) {
            database.getPlayer(senderActor.getId()).thenAccept(player -> {
                actor.setLanguage("en-uk");
                ProxiedPlayer proxiedPlayer = actor.as(ProxiedPlayer.class).getHandle();
                LobbyMessage lobbyMessage = new LobbyMessage(proxiedPlayer);
                lobbyMessage.sendMessageData(452, null);
                senderActor.sendMessage(common.getMessageManager().translate("command.language.changed", "en-uk", Key.of("language", "English")));
            });
            return;
        }
        senderActor.sendMessage(common.getMessageManager().translate("command.language.not-found", actor.getLanguage(), Key.of("language", language)));
    }

}
