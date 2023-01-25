package net.galacticprojects.spigot.configuration;

import net.galacticprojects.spigot.SpigotPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class CommandProperties {

    private final Properties properties = new Properties();
    private final String fileName = "commands.properties";
    private final File file = new File(SpigotPlugin.getPlugin().getDataFolder(), fileName);
    private ArrayList<String> allowedCommands =  new ArrayList<>();


    public CommandProperties() throws Exception {
        if(!file.exists()) file.createNewFile();
        properties.load(new FileInputStream(file));
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            getAllowedCommands().add(properties.getProperty(key));
        }
    }

    public void save() {
        for(int i = 0; i < getAllowedCommands().size(); i++) {
            properties.setProperty("command" + (i++), getAllowedCommands().get(i));
        }
        try {
            properties.store(new FileOutputStream(file), null);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getAllowedCommands() {
        return allowedCommands;
    }
}
