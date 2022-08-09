package net.galacticprojects.isystem.bungeecord;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import net.galacticprojects.isystem.bungeecord.command.BanCommand;
import net.galacticprojects.isystem.bungeecord.command.MaintenanceCommand;
import net.galacticprojects.isystem.bungeecord.command.OnlineTimeCommand;
import net.galacticprojects.isystem.bungeecord.command.SystemCommand;
import net.galacticprojects.isystem.bungeecord.config.MainConfiguration;
import net.galacticprojects.isystem.bungeecord.config.PermissionConfiguration;
import net.galacticprojects.isystem.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.isystem.bungeecord.config.languages.GermanConfiguration;
import net.galacticprojects.isystem.bungeecord.listener.ConnectListener;


import net.galacticprojects.isystem.bungeecord.listener.PingListener;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.bungeecord.config.SqlConfiguration;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class iProxy extends Plugin {

    private static Plugin plugin;

    public PluginManager manager;
    private static String pluginDirectory;
    private static String pluginLangDirectory;

    @Override
    public void onEnable() {
        plugin = this;
        manager = ProxyServer.getInstance().getPluginManager();
        initializeConfiguration();
        connectDatabase();
        initializeApiVersion();
        initializeTimeUnits();
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

            if(!(languageDirectory.exists())){
                languageDirectory.mkdir();
            }

            pluginLangDirectory = languageDirectory.getPath();

            new SqlConfiguration();
            new MainConfiguration();
            new PermissionConfiguration();
            new EnglishConfiguration();
            new GermanConfiguration();
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

    public void initializeTimeUnits() {
        ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                MySQL mySQL = JavaInstance.get(MySQL.class);

                for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    if(mySQL != null) {
                        mySQL.updateOnlineTime(all.getUniqueId(), 60);
                    }
                }


            }
        }, 1, 1, TimeUnit.MINUTES);


    }

    public void initializeListener(){
        manager.registerListener(this, new ConnectListener());
        manager.registerListener(this, new PingListener());
    }

    public void initializeCommands() {
        manager.registerCommand(this, new SystemCommand());
        manager.registerCommand(this, new BanCommand());
        manager.registerCommand(this, new OnlineTimeCommand());
        manager.registerCommand(this, new MaintenanceCommand());
    }

    public static Plugin getInstance() {
        return plugin;
    }

    public static String getPluginPath() {
        return pluginDirectory;
    }

    public static String getLanguageDirectory() {
        return pluginLangDirectory;
    }
}
