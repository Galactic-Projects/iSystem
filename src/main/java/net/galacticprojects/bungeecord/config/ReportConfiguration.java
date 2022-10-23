package net.galacticprojects.bungeecord.config;

import com.syntaxphoenix.syntaxapi.json.JsonArray;
import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.json.value.JsonNumber;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.bungeecord.config.info.BanInfo;
import net.galacticprojects.bungeecord.config.info.ReportInfo;
import net.galacticprojects.common.config.BaseConfiguration;
import net.galacticprojects.common.config.impl.ISection;
import net.galacticprojects.common.util.DynamicArray;

import java.io.File;

public class ReportConfiguration extends BaseConfiguration {

    DynamicArray<ReportInfo> infos = new DynamicArray<>();

    private String reasonColorStart;
    private String reasonColorEnd;
    public ReportConfiguration(ISimpleLogger logger, File dataFolder) {
        super(logger, dataFolder, "reportConfig.json");
    }
    @Override
    protected void onLoad() throws Throwable {
        loadInfos();
        ISection<JsonValue<?>, ValueType> section = config.createSection("message");
        reasonColorStart = section.getValueOrDefault("reason.start", "#780B00");
        reasonColorEnd = section.getValueOrDefault("reason.end", "#AA2D20");
    }

    private void loadInfos() throws Throwable {
        infos.clear();
        JsonArray array = (JsonArray) config.get("reports", ValueType.ARRAY);
        if (array == null) {
            config.set("reports", new JsonArray());
            return;
        }
        for (JsonValue<?> value : array) {
            if (value == null || !value.hasType(ValueType.OBJECT)) {
                continue;
            }
            JsonObject object = (JsonObject) value;
            JsonValue<?> rawReason = object.get("reason");
            String reason = (rawReason == null ? "N/A" : rawReason.getValue().toString());
            infos.add(new ReportInfo(reason));
        }
    }

    public ReportInfo getInfo(int id) {
        if (id < 1 || id > infos.length()) {
            return null;
        }
        return infos.get(id - 1);
    }
}
