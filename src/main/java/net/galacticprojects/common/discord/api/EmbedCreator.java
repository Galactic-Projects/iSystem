package net.galacticprojects.common.discord.api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.common.util.DiscordBot;

import java.awt.*;
import java.util.Arrays;

public class EmbedCreator {

    private EmbedBuilder embedBuilder = null;
    private long channelid = 0;
    private String platform = null;
    private String suspect = null;
    private String member = null;
    private String reason = null;
    private String ip = null;
    private long period = 0;
    private String timestamp = null;

    // ALL FORMAT
    public EmbedCreator(long channelid, String platform, String suspect, String member, String reason, String ip, long period, String timestamp) {
        if(DiscordBot.getJDA() == null) {
            return;
        }

        this.channelid = channelid;
        this.platform = platform;
        this.suspect = suspect;
        this.member = member;
        this.reason = reason;
        this.ip = ip;
        this.period = period;
        this.timestamp = timestamp;

        create();
    }

    // VERIFY FORMAT
    public EmbedCreator(long channelid, String platform, String member, String ip, String timestamp) {
        if(DiscordBot.getJDA() == null) {
            return;
        }

        this.channelid = channelid;
        this.platform = platform;
        this.member = member;
        this.ip = ip;
        this.timestamp = timestamp;

        create();
    }

    // BAN FORMAT
    public EmbedCreator(long channelid, String suspect, String member, String reason, String ip, long period, String timestamp) {
        if(DiscordBot.getJDA() == null) {
            return;
        }

        this.channelid = channelid;
        this.suspect = suspect;
        this.member = member;
        this.reason = reason;
        this.ip = ip;
        this.period = period;
        this.timestamp = timestamp;

        create();
    }

    // REPORT FORMAT
    public EmbedCreator(long channelid, String suspect, String member, String reason, String ip, String timestamp) {
        if(DiscordBot.getJDA() == null) {
            return;
        }

        this.channelid = channelid;
        this.suspect = suspect;
        this.member = member;
        this.reason = reason;
        this.ip = ip;
        this.timestamp = timestamp;

        create();
    }

    private void create() {
        embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.darkGray);
        embedBuilder.setTitle("Galactic Projects - Log");
        embedBuilder.setThumbnail("https://www.galacticprojects.net/img/0108221659359489510D3E38-29B1-47512x.png");
        if(suspect != null) {
            embedBuilder.addField(":bust_in_silhouette: Suspect: ", suspect, false);
        }
        if(member != null) {
            embedBuilder.addField(":speaking_head: Member: ", member, false);
        }
        if(platform != null) {
            embedBuilder.addField(":desktop: Platform: ", platform, false);
        }
        if(reason != null) {
            embedBuilder.addField(":writing_hand: Reason: ", reason, false);
        }
        if(ip != null) {
            embedBuilder.addField(":satellite: IP: ", ip, false);
        }
        if(period != 0) {
            embedBuilder.addField(":alarm_clock: Period: ", period + " Hour(s)", false);
        }
        if(timestamp != null) {
            embedBuilder.addField(":timer: Timestamp: ", timestamp, false);
        }
        embedBuilder.setFooter("Copyright © | Since 2022 - GalacticProjects.net");

        send();
    }

    public void send() {
        TextChannel textChannel = DiscordBot.getJDA().getTextChannelById(channelid);
        if(textChannel == null) {
            return;
        }

        textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
