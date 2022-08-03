package net.galacticprojects.isystem.bungeecord.config;

import net.galacticprojects.isystem.bungeecord.iProxy;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SqlConfiguration {

    public File sql;
    public Configuration sqlConfiguration;

    public SqlConfiguration () throws IOException {

        JavaInstance.put(this);

        try {

            sql = new File(iProxy.getPluginPath(), "mysql.yml");

            if (!(sql.exists())) {
                sql.createNewFile();
            }

            sqlConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(sql);

            if (!(sqlConfiguration.contains("Sql.Host"))) {
                sqlConfiguration.set("Sql.Host", "localhost");
            }

            if (!(sqlConfiguration.contains("Sql.Port"))) {
                sqlConfiguration.set("Sql.Port", 3306);
            }

            if (!(sqlConfiguration.contains("Sql.Database"))) {
                sqlConfiguration.set("Sql.Database", "isystem");
            }

            if (!(sqlConfiguration.contains("Sql.User"))) {
                sqlConfiguration.set("Sql.User", "root");
            }

            if (!(sqlConfiguration.contains("Sql.Password"))) {
                sqlConfiguration.set("Sql.Password", " ");
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(sqlConfiguration, sql);
        } catch (IOException e)  {
            throw new RuntimeException(e);
        }
    }

    public String getHost() {
        return sqlConfiguration.getString("Sql.Host");
    }

    public Integer getPort() {
        return sqlConfiguration.getInt("Sql.Port");
    }

    public String getDatabase() {
        return sqlConfiguration.getString("Sql.Database");
    }

    public String getUser() {
        return sqlConfiguration.getString("Sql.User");
    }

    public String getPassword() {
        return sqlConfiguration.getString("Sql.Password");
    }
}
