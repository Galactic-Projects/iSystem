package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.bungeecord.party.PartyManager;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Command(name = "party", description = "command.party.desc")
public class PartyCommand {

    HashMap<UUID, String> tpa = new HashMap<>();

    @Action("create")
    public void create(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "Party Name", optional = true) String name) {

        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        Party party;
        if(name == null || name.equals("") || name.equals(" ")) {
            party = new Party(player, new ArrayList<>());
        } else {
            party = new Party(player, new ArrayList<>(), name);
        }
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate("", playerData.getLanguage(), Key.of("party", party.getName()))));
            });
        });

    }

    @Action("invite")
    public void invite(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "player") String name) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(MojangProfileService.getUniqueId(name));
        if(player == null) {
            return;
        }
        PartyManager manager = new PartyManager(actor.as(ProxiedPlayer.class).getHandle());
        String partyName = manager.getParty().getName();
        tpa.put(player.getUniqueId(), partyName);
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                actor.as(ProxiedPlayer.class).getHandle().sendMessage(ComponentParser.parse(""));
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate("", playerData.getLanguage(), Key.of("party", partyName))));
            });
        });
    }

    @Action("accept")
    public void accept(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("decline")
    public void decline(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("promote")
    public void promote(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "player") String name) {

    }

    @Action("public")
    public void publish(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "public") boolean visibility) {

    }

    @Action("delete")
    public void delete(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }
}
