package net.galacticprojects.bungeecord.command;

import eu.cloudnetservice.driver.CloudNetDriver;
import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.localization.Key;
import me.lauriichan.laylib.localization.MessageProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.config.BotConfiguration;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.LinkPlayer;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.modules.LinkAPI;
import net.galacticprojects.common.modules.discord.EmbedCreator;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.TimeHelper;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static net.galacticprojects.common.modules.discord.VerifyListener.*;

import java.awt.*;
import java.sql.SQLData;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.UUID;

@Command(name = "link", description = "Link your accounts")
public class LinkCommand {

    private final LinkAPI linkAPI = new LinkAPI();

    @Action("discord")
    public void discord(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "discord tag") String tag) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();

        JDA jda = ProxyPlugin.getInstance().getDiscordBot().getJDA();
        HashMap<String, Long> members = new HashMap<>();
        Guild guild = jda.getGuilds().listIterator().next();
        SQLDatabase database = common.getDatabaseRef().get();

        if ((guild == null) || (database == null)) {
            common.getLogger().error(new Throwable("Database ist unavailable"));
            actor.sendTranslatedMessage(CommandMessages.VERIFICATION_SOMETHING_WENT_WRONG);
            return;
        }

        Player systemPlayer = database.getPlayer(uniqueId).join();
        LinkPlayer linkPlayer = database.getLinkedPlayer(uniqueId).join();

        if (linkPlayer.isDiscordLinked()) {
            actor.sendTranslatedMessage(CommandMessages.VERIFICATION_ALREADY_VERIFIED);
            return;
        }

        for (Member member : guild.loadMembers().get()) {
            members.put(member.getUser().getAsTag().toLowerCase(), member.getIdLong());
        }

        if (!members.containsKey(tag.toLowerCase())) {
            actor.sendTranslatedMessage(CommandMessages.VERIFICATION_SOMETHING_WENT_WRONG);
            return;
        }

        User user = jda.getUserByTag(tag.toLowerCase());


        PrivateChannel channel = user.openPrivateChannel().complete();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.green);
        builder.setTitle("Galactic Projects - Verification");
        builder.setThumbnail("https://www.galacticprojects.net/img/0108221659359489510D3E38-29B1-47512x.png");
        builder.addField("Minecraft Player", player.getDisplayName(), true);
        builder.addField("Discord User", tag, true);
        builder.addField("Verified", ":x:", true);
        builder.addField("Time", net.galacticprojects.bungeecord.util.TimeHelper.format(systemPlayer.getLanguage()).format(OffsetDateTime.now()), true);
        builder.setFooter("Copyright © | Since 2022 - GalacticProjects.net");
        Message message = channel.sendMessageEmbeds(builder.build()).complete();
        message.addReaction(Emoji.fromUnicode("U+2705")).complete();
        message.addReaction(Emoji.fromUnicode("U+274C")).complete();
        VERIFY_MAP.put(message.getId(), tag.toLowerCase());
        PLAYER_MAP.put(tag.toLowerCase(), player.getUniqueId());
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
