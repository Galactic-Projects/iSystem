package net.galacticprojects.bungeecord;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import me.lauriichan.wildcard.systemcore.SystemCore;
import me.lauriichan.wildcard.systemcore.data.io.Reloadable;
import me.lauriichan.wildcard.systemcore.data.io.message.Message;
import me.lauriichan.wildcard.systemcore.util.JavaInstance;
import me.lauriichan.wildcard.systemcore.util.source.Resources;
import net.galacticprojects.bungeecord.command.MaintenanceCommand;
import net.galacticprojects.bungeecord.command.OnlineTimeCommand;
import net.galacticprojects.bungeecord.command.SystemCommand;
import net.galacticprojects.bungeecord.config.*;
import net.galacticprojects.bungeecord.config.commands.BanConfiguration;
import net.galacticprojects.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.bungeecord.config.languages.GermanConfiguration;
import net.galacticprojects.bungeecord.listener.ChatListener;
import net.galacticprojects.bungeecord.listener.ConnectListener;


import net.galacticprojects.bungeecord.listener.PingListener;
import net.galacticprojects.discord.Bot;
import net.galacticprojects.log.Logger;
import net.galacticprojects.common.databaseLegacy.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class iProxy extends Plugin {

    private static Plugin plugin;

    private SystemCore core;

    public PluginManager manager;
    private static String pluginDirectory;
    private static String pluginLangDirectory;

    @Override
    public void onLoad() {
        Message.setLogger(getLogger());
        Messages.PREFIX.getClass();
        core = new SystemCore(this);
        core.load();
        createConfigs();
        Reloadable.start();
    }

    @Override
    public void onEnable() {
        plugin = this;
        manager = ProxyServer.getInstance().getPluginManager();
        core.enable();
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
        JavaInstance.get(Bot.class).shutdown();
        core.disable();
        Reloadable.shutdown();
    }

    private void createConfigs() {
        Resources resources = core.getResources();
        new BanConfiguration(resources);
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
            new DiscordConfiguration();
            new PermissionConfiguration();
            new EnglishConfiguration();
            new GermanConfiguration();
            new AutoModConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectDatabase() {
        new MySQL(() -> {
            SqlConfiguration sql = JavaInstance.get(SqlConfiguration.class);
            HikariConfig poolConfig = new HikariConfig();
            if(sql != null) {
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

    public void initializeApiVersion() {
        Logger.setup();
        new Bot();
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
        manager.registerListener(this, new ChatListener());
    }

    public void initializeCommands() {
        manager.registerCommand(this, new SystemCommand());
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
