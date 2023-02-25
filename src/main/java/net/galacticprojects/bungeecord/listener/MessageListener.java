package net.galacticprojects.bungeecord.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.syntaxphoenix.syntaxapi.utils.java.UniCode;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.message.BanMessage;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLData;
import java.time.OffsetDateTime;

public class MessageListener implements Listener {

    ProxyPlugin plugin;

    public MessageListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String subChannel = in.readUTF();
        if(subChannel.equals("AntiCheat")) {
            String arg = in.readUTF();
            String type = in.readUTF();
            if(arg.equals("kick")) {
                player.disconnect(new TextComponent("§5§lGalacticSecurity §8" + UniCode.ARROWS_RIGHT + " §7" + capitalizeString(type.toLowerCase()) + " detected!!"));
            }
            if(arg.equals("ban")) {
                SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
                Player iPlayer = database.getPlayer(player.getUniqueId()).join();
                switch (type.toLowerCase()) {
                    case "permanent" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.COMMAND_BAN_ID_1.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusYears(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.COMMAND_BAN_ID_1.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusYears(12))))));
                        break;
                    }
                    case "flight" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_FLIGHT.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(30), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_FLIGHT.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(30))))));
                        break;
                    }
                    case "elytrafly" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_ELYTRAFLY.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(30), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_ELYTRAFLY.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(30))))));
                        break;
                    }
                    case "waterwalk" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_WATER_WALK.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_WATER_WALK.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(12))))));
                        break;
                    }
                    case "fastplace" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_FAST_PLACE.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_FAST_PLACE.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(12))))));
                        break;
                    }
                    case "chatspam" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_CHAT_SPAM.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(2), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_CHAT_SPAM.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(2))))));
                        break;
                    }
                    case "commandspam" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_COMMAND_SPAM.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(2), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_COMMAND_SPAM.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(2))))));
                        break;
                    }
                    case "sprint" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_SPRINT.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_SPRINT.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(12))))));
                        break;
                    }
                    case "sneak" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_SNEAK.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_SNEAK.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(12))))));
                        break;
                    }
                    case "speed" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_SPEED.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_SPEED.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(12))))));
                        break;
                    }
                    case "vclip" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_VCLIP.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(30), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_VCLIP.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(30))))));
                        break;
                    }
                    case "spider" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_SPIDER.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(30), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_SPIDER.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(30))))));
                        break;
                    }
                    case "nofall" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_NO_FALL.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(30), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_NO_FALL.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(30))))));
                        break;
                    }
                    case "fastbow" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_FAST_BOW.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_FAST_BOW.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(12))))));
                        break;
                    }
                    case "fasteat" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_FAST_EAT.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(4), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_FAST_EAT.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(4))))));
                        break;
                    }
                    case "fastheal" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_FAST_HEAL.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(30), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_FAST_HEAL.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(30))))));
                        break;
                    }
                    case "killaura" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_KILLAURA.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(90), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_KILLAURA.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(90))))));
                        break;
                    }
                    case "fastprojectile" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_FAST_PROJECTILE.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(20), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_FAST_PROJECTILE.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(20))))));
                        break;
                    }
                    case "itemspam" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_ITEM_SPAM.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_ITEM_SPAM.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(12))))));
                        break;
                    }
                    case "fastinventory" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_FAST_INVENTORY.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(12), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_FAST_INVENTORY.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(12))))));
                        break;
                    }
                    case "velocity" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_VELOCITY.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(60), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_VELOCITY.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(60))))));
                        break;
                    }
                    case "chatunicode" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_CHAT_UNICODE.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(2), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_CHAT_UNICODE.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(2))))));
                        break;
                    }
                    case "illegalinteract" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_ILLEGALINTERACT.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(10), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_ILLEGALINTERACT.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(10))))));
                        break;
                    }
                    case "fastladder" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_FASTLADDER.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(20), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_FASTLADDER.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(20))))));
                        break;
                    }
                    case "aimbot" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_AIMBOT.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(60), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_AIMBOT.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(60))))));
                        break;
                    }
                    case "strafe" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_STRAFE.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(30), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_STRAFE.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(30))))));
                        break;
                    }
                    case "noslow" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_NOSLOW.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(30), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_NOSLOW.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(30))))));
                        break;
                    }
                    case "boatfly" -> {
                        database.banPlayer(player.getUniqueId(), null, 101, plugin.getCommonPlugin().getMessageManager().translate(BanMessage.AUTO_BAN_BOATFLY.id(), iPlayer.getLanguage()), OffsetDateTime.now().plusDays(20), OffsetDateTime.now());
                        player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, iPlayer.getLanguage(), Key.of("reason", BanMessage.AUTO_BAN_BOATFLY.id()), Key.of("time", TimeHelper.format(iPlayer.getLanguage()).format(OffsetDateTime.now().plusDays(20))))));
                        break;
                    }
                    default -> {
                        player.disconnect(new TextComponent("§c§l" + type));
                    }
                }
            }
        }
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

}
