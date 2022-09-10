package net.galacticprojects.spigot.config;

import net.galacticprojects.spigot.iServer;
import net.galacticprojects.utils.JavaInstance;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SqlConfiguration {

    public File sql;
    public YamlConfiguration sqlConfiguration;

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public SqlConfiguration () throws IOException {

        JavaInstance.put(this);

        try {

            sql = new File(iServer.getPluginDirectory(), "mysql.yml");

            if (!(sql.exists())) {
                sql.createNewFile();
            }

            sqlConfiguration = YamlConfiguration.loadConfiguration(sql);
            sqlConfiguration.addDefault("Sql.Host", "localhost");
            sqlConfiguration.addDefault("Sql.Port", 3306);
            sqlConfiguration.addDefault("Sql.Database", "lobbysystem");
            sqlConfiguration.addDefault("Sql.User", "root");
            sqlConfiguration.addDefault("Sql.Password", " ");
            sqlConfiguration.options().copyDefaults(true);
            sqlConfiguration.save(sql);

            load();
        } catch (IOException e)  {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        sqlConfiguration = YamlConfiguration.loadConfiguration(sql);

        host = sqlConfiguration.getString("Sql.Host");
        port = sqlConfiguration.getInt("Sql.Port");
        database = sqlConfiguration.getString("Sql.Database");
        user = sqlConfiguration.getString("Sql.User");
        password = sqlConfiguration.getString("Sql.Password");
    }

    public void reload() {
        load();
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
