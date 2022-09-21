package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.annotation.*;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.CommandSender;

import java.util.UUID;

@Command(name = "ban", description = "command.description.ban")
public class BanCommand {

    @Action("")
    @Permission("system.ban")
    public void ban(Actor<?> actor, @Argument(name = "player") String name, @Argument(index = 1, params = {
            @Param(type = 3, name = "min", intValue = 1),
            @Param(type = 3,name = "max", intValue = 99)
    }) int banId) {

        Actor<CommandSender> senderActor = actor.as(CommandSender.class);
        if(!senderActor.isValid()) {

            return;
        }
        CommandSender sender = senderActor.getHandle();
        UUID uniqueId = MojangProfileService.getUniqueId(name.startsWith("!") ? name.substring(1) : name);
        if(uniqueId == null) {

            return;
        }

    }

}
