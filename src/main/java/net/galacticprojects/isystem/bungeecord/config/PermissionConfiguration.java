package net.galacticprojects.isystem.bungeecord.config;

import net.galacticprojects.isystem.bungeecord.iProxy;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PermissionConfiguration {

    public File permissions;
    public Configuration permsConfiguration;

    public PermissionConfiguration() throws IOException {
        JavaInstance.put(this);

        try {
            permissions = new File(iProxy.getPluginPath(), "permissions.yml");

            if (!(permissions.exists())) {
                permissions.createNewFile();
            }

            permsConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(permissions);

            if(!(permsConfiguration.contains(""))) {
                permsConfiguration.set("", "");
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(permsConfiguration, permissions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
