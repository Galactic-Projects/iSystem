package net.galacticprojects.spigot.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.labymod.serverapi.bukkit.LabyModPlugin;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SubtitleAPI {

    public static void setSubtitle(Player receiver, UUID subtitlePlayer, String value) {
        JsonArray array = new JsonArray();
        JsonObject subtitle = new JsonObject();
        subtitle.addProperty("uuid", subtitlePlayer.toString());
        subtitle.addProperty("size", 0.8d);
        if (value != null)
            subtitle.addProperty("value", value);
        array.add(subtitle);
        LabyModPlugin.getInstance().sendServerMessage(receiver, "account_subtitle", array);
    }

}
