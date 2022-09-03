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

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

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
            load();
        } catch (IOException e)  {
            throw new RuntimeException(e);
        }
    }

    public void load() throws IOException {
        try {
            sqlConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(sql);

            host = sqlConfiguration.getString("Sql.Host");
            port = sqlConfiguration.getInt("Sql.Port");
            database = sqlConfiguration.getString("Sql.Database");
            user = sqlConfiguration.getString("Sql.User");
            password = sqlConfiguration.getString("Sql.Password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() throws IOException {
        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
