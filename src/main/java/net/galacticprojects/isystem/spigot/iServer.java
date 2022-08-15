package net.galacticprojects.isystem.spigot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.spigot.command.SubtitleCommand;
import net.galacticprojects.isystem.spigot.config.SqlConfiguration;
import net.galacticprojects.isystem.spigot.listener.LabyModListener;
import net.galacticprojects.isystem.utils.JavaInstance;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class iServer extends JavaPlugin {

    @Override
    public void onEnable() {
        initializeConfiguration();
        getServer().getPluginManager().registerEvents(new LabyModListener(), this);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDisable() {

    }
/*
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("labymod3:main")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            ByteBuf buf = Unpooled.wrappedBuffer(message);
            String key = LabyModProtocol.readString(buf, Short.MAX_VALUE);
            String json = LabyModProtocol.readString(buf, Short.MAX_VALUE);

            // LabyMod user joins the server
            if(key.equals("INFO")) {
                // Handle the json message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void initializeConfiguration() {
        try {
            new SqlConfiguration();
        } catch(IOException e) {
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

    private void injectCommands() {
        getCommand("subtitle").setExecutor(new SubtitleCommand());
    }

}
