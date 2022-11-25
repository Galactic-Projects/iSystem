package net.galacticprojects.common.modules.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.common.modules.DiscordBot;

import java.awt.*;
import java.util.UUID;

public class EmbedCreator {

    private EmbedBuilder embedBuilder = null;
    private String channelid = null;
    private String identifier = null;
    private String suspect = null;
    private String member = null;
    private String reason = null;
    private String ip = null;
    private long period = 0;
    private String timestamp = null;

    // ALL FORMAT
    public EmbedCreator(String channelid, String platform, String suspect, String member, String reason, String ip, long period, String timestamp) {
        if(ProxyPlugin.getInstance().getDiscordBot().getJDA() == null) {
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

    // VERIFY FORMAT
    public EmbedCreator(String channelid, String identifier, String member, String ip, String timestamp) {
        if(ProxyPlugin.getInstance().getDiscordBot().getJDA() == null) {
            return;
        }

        this.channelid = channelid;
        this.identifier = identifier;
        this.member = member;
        this.ip = ip;
        this.timestamp = timestamp;

        create();
    }

    // BAN FORMAT
    public EmbedCreator(String channelid, String suspect, String member, String reason, String ip, long period, String timestamp) {
        if(ProxyPlugin.getInstance().getDiscordBot().getJDA() == null) {
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
    public EmbedCreator(String channelid, String suspect, String member, String reason, String ip, String timestamp) {
        if(ProxyPlugin.getInstance().getDiscordBot().getJDA() == null) {
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
        if(identifier != null) {
            embedBuilder.addField(":bust_in_silhouette: Identifier: ", identifier.toString(), false);
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

    private void send() {
        TextChannel textChannel = ProxyPlugin.getInstance().getDiscordBot().getJDA().getTextChannelById(channelid);
        if(textChannel == null) {
            return;
        }

        textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
