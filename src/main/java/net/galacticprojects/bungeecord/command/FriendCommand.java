package net.galacticprojects.bungeecord.command;


import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.command.annotation.Param;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.common.CommonPlugin;

@Command(name = "friend",  description = "")
public class FriendCommand {

    @Action("toggle")
    public void toggle(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("togglemessage")
    public void toggleMessage(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("togglejump")
    public void toggleJump(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("list")
    public void list(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("clear")
    public void clear(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("requests")
    public void requests(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("acceptall")
    public void acceptall(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("denyall")
    public void denyall(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin) {

    }

    @Action("add")
    public void add(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name="player") String player) {

    }

    @Action("remove")
    public void remove(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name="player") String player) {

    }

    @Action("accept")
    public void accept(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name="player") String player) {

    }


    @Action("deny")
    public void deny(BungeeActor<?> bungeeActor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name="player") String player) {

    }

}
