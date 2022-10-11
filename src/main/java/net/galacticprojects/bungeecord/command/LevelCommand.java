package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Command;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.common.CommonPlugin;

@Command(name = "level", description = "See your level")
public class LevelCommand {

    @Action("")
    public void coins(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin){

    }
}
