package net.galacticprojects.bungeecord.config.commands;

import com.syntaxphoenix.syntaxapi.json.JsonArray;
import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.json.value.JsonNumber;
import me.lauriichan.wildcard.systemcore.data.config.ISection;
import me.lauriichan.wildcard.systemcore.data.config.json.JsonConfig;
import me.lauriichan.wildcard.systemcore.data.io.ConfigReloadable;
import me.lauriichan.wildcard.systemcore.util.DynamicArray;
import me.lauriichan.wildcard.systemcore.util.JavaInstance;
import me.lauriichan.wildcard.systemcore.util.source.Resources;

public class BanConfiguration extends ConfigReloadable<JsonConfig> {

    private final DynamicArray<BanInfo> infos = new DynamicArray<>();

    private String reasonColorStart;
    private String reasonColorEnd;

    private String durationColorStart;
    private String durationColorEnd;

    public BanConfiguration(final Resources resources) {
        super(JsonConfig.class, resources.fileData("ban.json").getSource());
        JavaInstance.put(this);
    }

    @Override
    protected void onConfigLoad() throws Throwable {
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
            config.set("bans", new JsonArray());
            return;
        }
        int index = 0;
        for (JsonValue<?> value : array) {
            if (value == null || !value.hasType(ValueType.OBJECT)) {
                continue;
            }
            if (index >= 98) {
                break;
            }
            JsonObject object = (JsonObject) value;
            JsonValue<?> rawReason = object.get("reason");
            JsonValue<?> rawDays = object.get("days");
            JsonValue<?> rawHours = object.get("hours");
            int days = (rawDays == null ? 0 : ((JsonNumber<?>) rawDays).getValue().intValue());
            long hours = (rawHours == null ? 0 : ((JsonNumber<?>) rawHours).getValue().intValue()) + (days * 24);
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

    public static class BanInfo {

        private final String reason;
        private final long hours;

        public BanInfo(final String reason, final long hours) {
            this.reason = reason;
            this.hours = hours;
        }

        public long getHours() {
            return hours;
        }

        public String getReason() {
            return reason;
        }

    }
}
