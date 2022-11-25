package net.galacticprojects.common.modules.discord;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.galacticprojects.common.modules.LinkAPI;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class VerifyListener extends ListenerAdapter {

    public static HashMap<String, String> VERIFY_MAP = new HashMap<>();
    public static HashMap<String, UUID> PLAYER_MAP = new HashMap<>();

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Channel channel = event.getChannel();

        System.out.println("reacted");

        if(!VERIFY_MAP.containsKey(event.getMessageId())) {
            System.out.println("invalid Message");
            return;
        }
        String unicode = event.getReaction().getEmoji().asUnicode().toString();
        if(unicode.equals("U+274C")) {
            System.out.println("Unicode U+274C detected");
            VERIFY_MAP.remove(VERIFY_MAP.get(event.getMessageId()));
            return;
        }
        if(unicode.equals("U+2705")) {
            System.out.println("Unicode U+2705 detected");
            LinkAPI api = new LinkAPI();
            api.verifyDiscord(VERIFY_MAP.get(event.getMessageId()), PLAYER_MAP.get(VERIFY_MAP.get(event.getMessageId())));
            event.getChannel().deleteMessageById(event.getMessageId()).complete();
            PLAYER_MAP.remove(VERIFY_MAP.get(event.getMessageId()));
            VERIFY_MAP.remove(event.getMessageId());
            return;
        }
        System.out.println("nothing detected");
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
    }
}
