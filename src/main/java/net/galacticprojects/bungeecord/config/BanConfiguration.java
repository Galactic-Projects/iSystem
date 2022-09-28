package net.galacticprojects.bungeecord.config;

import com.google.gson.JsonNull;
import com.syntaxphoenix.syntaxapi.json.JsonArray;
import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.json.value.JsonNumber;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.bungeecord.config.info.BanInfo;
import net.galacticprojects.common.config.BaseConfiguration;
import net.galacticprojects.common.config.impl.ISection;
import net.galacticprojects.common.util.DynamicArray;

import java.io.File;

public class BanConfiguration extends BaseConfiguration {

    DynamicArray<BanInfo> infos = new DynamicArray<>();

    private String reasonColorStart;
    private String reasonColorEnd;

    private String durationColorStart;
    private String durationColorEnd;

    public BanConfiguration(ISimpleLogger logger, File dataFolder) {
        super(logger, dataFolder, "banConfig.json");
    }
    @Override
    protected void onLoad() throws Throwable {
        loadInfos();
        ISection<JsonValue<?>, ValueType> section = config.createSection("message");
        reasonColorStart = section.getValueOrDefault("reason.start", "#780B00");
        reasonColorEnd = section.getValueOrDefault("reason.end", "#AA2D20");
        durationColorStart = section.getValueOrDefault("duration.start", "#BD7000");
        durationColorEnd = section.getValueOrDefault("duration.end", "#CA8828");
    }

    private void loadInfos() throws Throwable {
        infos.clear();
        JsonArray array = (JsonArray) config.get("bans", ValueType.ARRAY);
        if (array == null) {
            JsonArray s = new JsonArray();
            s.add(1);
            config.set("bans", s);
            return;
        }
        int index = 0;
        for (JsonValue<?> value : array) {
            if (value == null || !value.hasType(ValueType.OBJECT)) {
                continue;
            }
            if (index >= 100) {
                break;
            }
            JsonObject object = (JsonObject) value;
            JsonValue<?> rawReason = object.get("reason");
            JsonValue<?> rawDays = object.get("days");
            JsonValue<?> rawHours = object.get("hours");
            int day = (rawDays == null ? 0 : ((JsonNumber<?>) rawDays).getValue().intValue());
            long hours = (rawHours == null ? 0 : ((JsonNumber<?>) rawHours).getValue().longValue());
            hours = hours + (day * 24L);
            String reason = (rawReason == null ? "N/A" : rawReason.getValue().toString());
            infos.add(new BanInfo(reason, hours));
        }
    }

    public String getReasonColorStart() {
        return reasonColorStart;
    }

    public String getReasonColorEnd() {
        return reasonColorEnd;
    }

    public String getDurationColorStart() {
        return durationColorStart;
    }

    public String getDurationColorEnd() {
        return durationColorEnd;
    }

    public BanInfo getInfo(int id) {
        if (id < 1 || id > infos.length()) {
            return null;
        }
        return infos.get(id - 1);
    }
}
