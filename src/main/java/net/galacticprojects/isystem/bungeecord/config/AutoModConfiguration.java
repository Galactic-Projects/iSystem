package net.galacticprojects.isystem.bungeecord.config;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.galacticprojects.isystem.bungeecord.iProxy;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AutoModConfiguration {

    public File amconfig;
    public Configuration autoModConfiguration;

    private ArrayList<String> blacklistedWords1;
    private ArrayList<String> blacklistedWords2;
    private ArrayList<String> blacklistedWords3;
    private ArrayList<String> blacklistedWords4;
    private ArrayList<String> blacklistedWords5;
    private HashMap<ArrayList<Integer>, ArrayList<String>> blacklist = new HashMap<>();
    private HashMap<Integer, String> punishments = new HashMap<>();

    public AutoModConfiguration() throws IOException {
        JavaInstance.put(this);

        try {
            amconfig = new File(iProxy.getPluginPath(), "blacklisted.yml");

            if (!(amconfig.exists())) {
                amconfig.createNewFile();
            }

            autoModConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(amconfig);

            if (!(autoModConfiguration.contains("System.Blocked"))) {
                HashMap<ArrayList<Integer>, ArrayList<String>> hash = new HashMap<>();
                ArrayList<Integer> bad = new ArrayList<>();
                bad.add(1);
                ArrayList<Integer> bad2 = new ArrayList<>();
                bad2.add(2);
                ArrayList<Integer> bad3 = new ArrayList<>();
                bad3.add(3);
                ArrayList<Integer> bad4 = new ArrayList<>();
                bad4.add(4);
                ArrayList<Integer> bad5 = new ArrayList<>();
                bad5.add(5);
                ArrayList<String> word = new ArrayList<>();
                word.add("jerk");
                ArrayList<String> word2 = new ArrayList<>();
                word2.add("cunt");
                ArrayList<String> word3 = new ArrayList<>();
                word3.add("motherfucker");
                ArrayList<String> word4 = new ArrayList<>();
                word4.add("son of a bitch");
                ArrayList<String> word5 = new ArrayList<>();
                word5.add("sieg heil");
                hash.put(bad, word);
                hash.put(bad2, word2);
                hash.put(bad3, word3);
                hash.put(bad4, word4);
                hash.put(bad5, word5);
                for(ArrayList<Integer> key : hash.keySet()) {
                    autoModConfiguration.set("System.Blocked." + key, hash.get(key));
                }

            }

            if(!(autoModConfiguration.contains("System.Punishment"))) {
                ArrayList<Integer> stages = new ArrayList<>();
                for(int i = 0; i < 5; i++) {
                    stages.add(i);
                }
                autoModConfiguration.set("System.Punishment."+stages, "//Hier kommt ne Bestrafung rein");
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(autoModConfiguration, amconfig);
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() throws IOException {
        try {
            autoModConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(amconfig);

            ArrayList<Integer> ids = new ArrayList<>();
            ArrayList<String> words = new ArrayList<>();
            for(String key : autoModConfiguration.getSection("System.Blocked").getKeys()) {
                ids.add(Integer.parseInt(key));
                words.add(autoModConfiguration.getString("System.Blocked.["+key+"]"));
            }
            blacklist.put(ids, words);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() throws IOException {
        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getBlacklistedWords1() {
        return blacklistedWords1;
    }

    public ArrayList<String> getBlacklistedWords2() {
        return blacklistedWords2;
    }

    public ArrayList<String> getBlacklistedWords3() {
        return blacklistedWords3;
    }

    public ArrayList<String> getBlacklistedWords4() {
        return blacklistedWords4;
    }

    public ArrayList<String> getBlacklistedWords5() {
        return blacklistedWords5;
    }
}
