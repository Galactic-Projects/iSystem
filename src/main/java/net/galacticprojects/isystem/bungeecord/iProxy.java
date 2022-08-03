package net.galacticprojects.isystem.bungeecord;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import net.galacticprojects.isystem.bungeecord.command.BanCommand;
import net.galacticprojects.isystem.bungeecord.command.LobbyCommand;
import net.galacticprojects.isystem.bungeecord.command.SystemCommand;
import net.galacticprojects.isystem.bungeecord.config.MainConfiguration;
import net.galacticprojects.isystem.bungeecord.config.PermissionConfiguration;
import net.galacticprojects.isystem.bungeecord.listener.ConnectListener;


import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.bungeecord.config.SqlConfiguration;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class iProxy extends Plugin {

    public Plugin plugin;

    public PluginManager manager;
    private static String pluginDirectory;

    @Override
    public void onEnable() {
        plugin = this;
        manager = ProxyServer.getInstance().getPluginManager();
        initializeConfiguration();
        connectDatabase();
        initializeApiVersion();
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
            new SqlConfiguration();
            new MainConfiguration();
            new PermissionConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectDatabase() {
        new MySQL(() -> {
            SqlConfiguration sql = JavaInstance.get(SqlConfiguration.class);
            HikariConfig poolConfig = new HikariConfig();
            poolConfig.setConnectionTimeout(7500);
            poolConfig.setMaximumPoolSize(8);
            poolConfig.setMinimumIdle(1);
            poolConfig.setJdbcUrl("jdbc:mysql://" + sql.getHost() + ":" + sql.getPort() + "/" + sql.getDatabase()
                    + "?serverTimezone=UTC&useLegacyDatetimeCode=false");
            System.out.println(poolConfig.getJdbcUrl());
            poolConfig.setUsername(sql.getUser());
            poolConfig.setPassword(sql.getPassword());
            return new HikariPool(poolConfig);
        });
    }

    public void initializeApiVersion() {

    }

    public void initializeListener(){
        manager.registerListener(this, new ConnectListener());
    }

    public void initializeCommands() {
        manager.registerCommand(this, new SystemCommand());
        manager.registerCommand(this, new LobbyCommand());
        manager.registerCommand(this, new BanCommand());
    }

    public Plugin getInstance() {
        return plugin;
    }

    public static String getPluginPath() {
        return pluginDirectory;
    }
}
