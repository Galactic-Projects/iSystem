package net.galacticprojects.bungeecord.command;

import eu.cloudnetservice.driver.CloudNetDriver;
import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.localization.Key;
import me.lauriichan.laylib.localization.MessageProvider;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.config.BotConfiguration;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.LinkPlayer;
import net.galacticprojects.common.modules.LinkAPI;
import net.galacticprojects.common.modules.discord.EmbedCreator;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.TimeHelper;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.UUID;

@Command(name = "link", description = "Link your accounts")
public class LinkCommand {

    private final LinkAPI linkAPI = new LinkAPI();

    @Action("discord")
    public void discord(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "discord tag") String tag) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();


    }

    @Action("teamspeak")
    public void teamspeak(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        BotConfiguration botConfiguration = plugin.getBotConfiguration();
        String communityLink = botConfiguration.getChannelCLink();
        String networkLink = botConfiguration.getChannelNLink();
        OffsetDateTime currentTime = OffsetDateTime.now();

        SQLDatabase database = common.getDatabaseRef().get();
        if (database == null) {
            common.getLogger().error(new Throwable("Database ist unavailable"));
            return;
        }

        LinkPlayer linkPlayer = database.getLinkedPlayer(uniqueId).join();
        if ((linkPlayer == null) || (linkAPI.isError()) || (linkAPI.getIdentifier() == null)) {
            actor.sendTranslatedMessage(CommandMessages.VERIFICATION_SOMETHING_WENT_WRONG);
            linkAPI.setError(false);
            return;
        }

        if (linkPlayer.isTeamspeakLinked()) {
            actor.sendTranslatedMessage(CommandMessages.VERIFICATION_ALREADY_VERIFIED);
            return;
        }

        linkAPI.verifyTeamSpeak(player.getAddress().getAddress().getHostAddress(), CloudNetDriver.instance().permissionManagement().user(uniqueId));

        linkPlayer.setTeamspeakIp(player.getAddress().getAddress().getHostAddress());
        linkPlayer.setTeamspeakIdentifier(linkAPI.getIdentifier());
        linkPlayer.setTeamspeakTime(currentTime);
        linkPlayer.setTeamspeakLinked(true);

        database.updateLinkPlayer(linkPlayer);
        new EmbedCreator(communityLink, linkAPI.getIdentifier(), actor.getName(), player.getAddress().getAddress().getHostAddress(), TimeHelper.BAN_TIME_FORMATTER.format(currentTime));
        new EmbedCreator(networkLink, linkAPI.getIdentifier(), actor.getName(), player.getAddress().getAddress().getHostAddress(), TimeHelper.BAN_TIME_FORMATTER.format(currentTime));
        linkAPI.setIdentifier(null);
        actor.sendTranslatedMessage(CommandMessages.VERIFICATION_SOMETHING_SUCCESS, Key.of("service", "TeamSpeak-Server"));
    }
}
