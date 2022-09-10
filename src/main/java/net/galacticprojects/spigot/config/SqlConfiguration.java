package net.galacticprojects.spigot.config;

import net.galacticprojects.spigot.iServer;
import net.galacticprojects.utils.JavaInstance;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SqlConfiguration {

    public File sql;
    public YamlConfiguration sqlConfiguration;

    public SqlConfiguration () throws IOException {

        JavaInstance.put(this);

        try {

            sql = new File(iServer.getPlugin(iServer.class).getDataFolder(), "mysql.yml");

            if (!(sql.exists())) {
                sql.createNewFile();
            }

            sqlConfiguration = YamlConfiguration.loadConfiguration(sql);

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

            sqlConfiguration.save(sql);
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
