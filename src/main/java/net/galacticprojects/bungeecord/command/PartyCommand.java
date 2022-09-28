package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.common.CommonPlugin;

@Command(name = "party", description = "command.party.desc")
public class PartyCommand {

    @Action("create")
    public void create(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "Party Name", optional = true) String name) {

    }

    @Action("invite")
    public void invite(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "player") String name) {

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
