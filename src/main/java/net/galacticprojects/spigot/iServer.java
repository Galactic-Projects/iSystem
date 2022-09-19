package net.galacticprojects.spigot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import net.galacticprojects.database.MySQL;
import net.galacticprojects.spigot.command.SubtitleCommand;
import net.galacticprojects.spigot.config.SqlConfiguration;
import net.galacticprojects.spigot.listener.ConnectionListener;
import net.galacticprojects.spigot.listener.LabyModListener;
import net.galacticprojects.spigot.listener.MoveListener;
import net.galacticprojects.utils.JavaInstance;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class iServer extends JavaPlugin {

    private static Plugin plugin;

    public PluginManager manager;
    private static String pluginDirectory;
    private static String pluginLangDirectory;

    @Override
    public void onEnable() {
        initializeConfiguration();
        connectDatabase();
        initializeListener();
        initializeCommands();

    }

    @Override
    public void onDisable() {
        JavaInstance.get(MySQL.class).shutdown();
    }

    public void initializeConfiguration() {
        try {
            if (!(getDataFolder().exists())) {
                getDataFolder().mkdir();
            }
            pluginDirectory = getDataFolder().getPath();

            File languageDirectory = new File(getDataFolder().getPath() + "/languages");

            if (!(languageDirectory.exists())) {
                languageDirectory.mkdir();
            }

            pluginLangDirectory = languageDirectory.getPath();

            new SqlConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectDatabase() {
        new MySQL(() -> {
            SqlConfiguration sql = JavaInstance.get(SqlConfiguration.class);
            HikariConfig poolConfig = new HikariConfig();
            if (sql != null) {
                poolConfig.setConnectionTimeout(7500);
                poolConfig.setMaximumPoolSize(8);
                poolConfig.setMinimumIdle(1);
                if (sql.getHost() != null && sql.getPort() != null && sql.getDatabase() != null && sql.getUser() != null && sql.getPassword() != null) {
                    poolConfig.setJdbcUrl("jdbc:mysql://" + sql.getHost() + ":" + sql.getPort() + "/" + sql.getDatabase()
                            + "?serverTimezone=UTC&useLegacyDatetimeCode=false");
                    System.out.println(poolConfig.getJdbcUrl());
                    poolConfig.setUsername(sql.getUser());
                    poolConfig.setPassword(sql.getPassword());
                }
            }
            return new HikariPool(poolConfig);
        });
    }

    public void initializeListener() {
        manager = Bukkit.getPluginManager();
        manager.registerEvents(new ConnectionListener(), this);
        manager.registerEvents(new LabyModListener(), this);
        manager.registerEvents(new MoveListener(), this);
    }

    public void initializeCommands() {
        getCommand("subtitle").setExecutor(new SubtitleCommand());
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static String getPluginDirectory() {
        return pluginDirectory;
    }

    public static String getPluginLangDirectory() {
        return pluginLangDirectory;
    }
}
