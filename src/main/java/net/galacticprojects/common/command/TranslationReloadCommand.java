package net.galacticprojects.common.command;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.command.annotation.Permission;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.message.MessageTranslationManager;
import net.galacticprojects.common.util.Ref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Command(name = "translation", description = "command.description.translation")
public class TranslationReloadCommand {

    private final MessageTranslationManager messageTranslationManager;

    public TranslationReloadCommand(){
        this.messageTranslationManager = CommonPlugin.instance().getTranslationManager();
    }

    @Action("")
    @Action("reload")
    @Permission("command.translation.reload")
    public void reload(Ref<SQLDatabase> ref, Actor<?> actor) {
        actor.sendTranslatedMessage("command.translation.reload.start");
        List<String> previous = Arrays.asList(messageTranslationManager.getLanguages());
        messageTranslationManager.reload();
        String[] languages = messageTranslationManager.getLanguages();
        ArrayList<String> newLanguages = new ArrayList<>();
        ArrayList<String> loadedLanguages = new ArrayList<>();
        ArrayList<String> unloadedLanguages = new ArrayList<>(previous);
        for(String language : languages){
            unloadedLanguages.remove(language);
            if(previous.contains(language)) {
                loadedLanguages.add(language);
                continue;
            }
            newLanguages.add(language);
        }
        actor.sendTranslatedMessage("command.translation.reload.end",
                Key.of("new", String.join(", ", newLanguages)),
                Key.of("loaded", String.join(", ", loadedLanguages)),
                Key.of("unloaded", String.join(", ", unloadedLanguages)));
    }

}
