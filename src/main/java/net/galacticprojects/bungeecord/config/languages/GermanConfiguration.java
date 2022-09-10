package net.galacticprojects.bungeecord.config.languages;

import net.galacticprojects.bungeecord.iProxy;
import net.galacticprojects.utils.JavaInstance;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GermanConfiguration {

    public File german;
    public Configuration germanConfiguration;

    public GermanConfiguration () throws IOException {
        JavaInstance.put(this);

        try {
            german = new File(iProxy.getLanguageDirectory(), "german.yml");

            if(!(german.exists())) {
                german.createNewFile();
            }

            germanConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(german);

            if(!(germanConfiguration.contains(""))) {
                germanConfiguration.set("", "");
            }


            ConfigurationProvider.getProvider(YamlConfiguration.class).save(germanConfiguration, german);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
