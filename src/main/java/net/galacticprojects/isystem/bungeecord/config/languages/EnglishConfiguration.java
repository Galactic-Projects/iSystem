package net.galacticprojects.isystem.bungeecord.config.languages;

import net.galacticprojects.isystem.bungeecord.iProxy;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.color.Color;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class EnglishConfiguration {

    public File english;
    public Configuration englishConfiguration;

    private String day1;
    private String day2;
    private String day3;
    private String day4;
    private String day5;
    private String day6;
    private String day7;

    private String month1;
    private String month2;
    private String month3;
    private String month4;
    private String month5;
    private String month6;
    private String month7;
    private String month8;
    private String month9;
    private String month10;
    private String month11;
    private String month12;

    private String langGer;
    private String langEng;
    private String langFre;
    private String langSpa;
    private String motdsMaintenanceLineOne;
    private String motdsMaintenanceLineTwo;
    private String motdsNormalLineOne;
    private String motdsNormalLineTwo;
    private String tablistHeader;
    private String tablistFooter;
    private String maintenanceVersion;

    public EnglishConfiguration () throws IOException {
        JavaInstance.put(this);

        try {
            english = new File(iProxy.getLanguageDirectory(), "english.yml");

            if(!(english.exists())) {
                english.createNewFile();
            }

            englishConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(english);

            if(!(englishConfiguration.contains("Messages.TimeFormat.Days.1"))) {
                englishConfiguration.set("Messages.TimeFormat.Days.1", "Monday");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Days.2"))) {
                englishConfiguration.set("Messages.TimeFormat.Days.2", "Tuesday");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Days.3"))) {
                englishConfiguration.set("Messages.TimeFormat.Days.3", "Wednesday");
            }


            if(!(englishConfiguration.contains("Messages.TimeFormat.Days.4"))) {
                englishConfiguration.set("Messages.TimeFormat.Days.4", "Thursday");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Days.5"))) {
                englishConfiguration.set("Messages.TimeFormat.Days.5", "Friday");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Days.6"))) {
                englishConfiguration.set("Messages.TimeFormat.Days.6", "Saturday");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Days.7"))) {
                englishConfiguration.set("Messages.TimeFormat.Days.7", "Sunday");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.1"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.1", "January");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.2"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.2", "February");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.3"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.3", "March");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.4"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.4", "April");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.5"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.5", "May");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.6"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.6", "June");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.7"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.7", "July");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.8"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.8", "August");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.9"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.9", "September");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.10"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.10", "October");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.11"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.11", "November");
            }

            if(!(englishConfiguration.contains("Messages.TimeFormat.Months.12"))) {
                englishConfiguration.set("Messages.TimeFormat.Months.12", "December");
            }

            if(!(englishConfiguration.contains("Messages.Languages.English"))) {
                englishConfiguration.set("Messages.Languages.English", "English");
            }

            if(!(englishConfiguration.contains("Messages.Languages.German"))) {
                englishConfiguration.set("Messages.Languages.German", "German");
            }

            if(!(englishConfiguration.contains("Messages.Languages.French"))) {
                englishConfiguration.set("Messages.Languages.French", "French");
            }

            if(!(englishConfiguration.contains("Messages.Languages.Spanish"))) {
                englishConfiguration.set("Messages.Languages.Spanish", "Spanish");
            }

            if(!(englishConfiguration.contains("Messages.System.MOTDs.Maintenance.Line.1"))) {
                englishConfiguration.set("Messages.System.MOTDs.Maintenance.Line.1", " ");
            }

            if(!(englishConfiguration.contains("Messages.System.MOTDs.Maintenance.Line.2"))) {
                englishConfiguration.set("Messages.System.MOTDs.Maintenance.Line.2", " ");
            }

            if(!(englishConfiguration.contains("Messages.System.MOTDs.Normal.Line.1"))) {
                englishConfiguration.set("Messages.System.MOTDs.Normal.Line.1", " ");
            }

            if(!(englishConfiguration.contains("Messages.System.MOTDs.Normal.Line.2"))) {
                englishConfiguration.set("Messages.System.MOTDs.Normal.Line.2", " ");
            }

            if(!(englishConfiguration.contains("Messages.System.Tablist.Header"))) {
                englishConfiguration.set("Messages.System.Tablist.Header", "&7 \n &8「 &5&lGALACTIC&d&lPROJECTS &7■ &fYour &bnetwork &fwith &dgalactical &5power &8」\n &8► Connected to %server% &7■ Currently online &a%online%&d/&c%maxplayers% &8◄\n &7");
            }

            if(!(englishConfiguration.contains("Messages.System.Tablist.Footer"))) {
                englishConfiguration.set("Messages.System.Tablist.Footer", "&7 \n &8► Website &7× &dwww.galacticalprojects.net &f■ TeamSpeak &7× &dGalacticalProjects.net &8◄ \n &8► Forum &7× &dforum.galacticalprojects.net &f■ Shop &7× &dshop.galacticalprojects.net &8◄ \n &7");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.Version"))) {
                englishConfiguration.set("Messages.System.Maintenance.Version", "&8► &4&lMAINTENANCE");
            }


            ConfigurationProvider.getProvider(YamlConfiguration.class).save(englishConfiguration, english);

            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() throws IOException {
        try {
            englishConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(english);
            day1 = englishConfiguration.getString("Messages.TimeFormat.Days.1");
            day2 = englishConfiguration.getString("Messages.TimeFormat.Days.2");
            day3 = englishConfiguration.getString("Messages.TimeFormat.Days.3");
            day4 = englishConfiguration.getString("Messages.TimeFormat.Days.4");
            day5 = englishConfiguration.getString("Messages.TimeFormat.Days.5");
            day6 = englishConfiguration.getString("Messages.TimeFormat.Days.6");
            day7 = englishConfiguration.getString("Messages.TimeFormat.Days.7");
            month1 = englishConfiguration.getString("Messages.DayFormat.Months.1");
            month2 = englishConfiguration.getString("Messages.DayFormat.Months.2");
            month3 = englishConfiguration.getString("Messages.DayFormat.Months.3");
            month4 = englishConfiguration.getString("Messages.DayFormat.Months.4");
            month5 = englishConfiguration.getString("Messages.DayFormat.Months.5");
            month6 = englishConfiguration.getString("Messages.DayFormat.Months.6");
            month7 = englishConfiguration.getString("Messages.DayFormat.Months.7");
            month8 = englishConfiguration.getString("Messages.DayFormat.Months.8");
            month9 = englishConfiguration.getString("Messages.DayFormat.Months.9");
            month10 = englishConfiguration.getString("Messages.DayFormat.Months.10");
            month11 = englishConfiguration.getString("Messages.DayFormat.Months.11");
            month12 = englishConfiguration.getString("Messages.DayFormat.Months.12");
            langEng = englishConfiguration.getString("Messages.Languages.English");
            langGer = englishConfiguration.getString("Messages.Languages.German");
            langFre = englishConfiguration.getString("Messages.Languages.French");
            langSpa = englishConfiguration.getString("Messages.Languages.Spanish");

            motdsMaintenanceLineOne = Color.apply(englishConfiguration.getString("Messages.System.MOTDs.Maintenance.Line.1"));
            motdsMaintenanceLineTwo = Color.apply(englishConfiguration.getString("Messages.System.MOTDs.Maintenance.Line.2"));
            motdsNormalLineOne = Color.apply(englishConfiguration.getString("Messages.System.MOTDs.Normal.Line.1"));
            motdsNormalLineTwo = Color.apply(englishConfiguration.getString("Messages.System.MOTDs.Normal.Line.2"));
            tablistHeader = Color.apply(englishConfiguration.getString("Messages.System.Tablist.Header"));
            tablistFooter = Color.apply(englishConfiguration.getString("Messages.System.Tablist.Footer"));
            maintenanceVersion = Color.apply(englishConfiguration.getString("Messages.System.Maintenance.Version"));

        } catch (IOException e){
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

    public String getDay1() {
        return day1;
    }

    public String getDay2() {
        return day2;
    }

    public String getDay3() {
        return day3;
    }

    public String getDay4() {
        return day4;
    }

    public String getDay5() {
        return day5;
    }

    public String getDay6() {
        return day6;
    }

    public String getDay7() {
        return day7;
    }

    public String getMonth1() {
        return month1;
    }

    public String getMonth2() {
        return month2;
    }

    public String getMonth3() {
        return month3;
    }

    public String getMonth4() {
        return month4;
    }

    public String getMonth6() {
        return month6;
    }

    public String getMonth7() {
        return month7;
    }

    public String getMonth8() {
        return month8;
    }

    public String getMonth9() {
        return month9;
    }

    public String getMonth10() {
        return month10;
    }

    public String getMonth11() {
        return month11;
    }

    public String getMonth12() {
        return month12;
    }

    public String getLangEng() {
        return langEng;
    }

    public String getLangGer() {
        return langGer;
    }

    public String getLangFre() {
        return langFre;
    }

    public String getLangSpa() {
        return langSpa;
    }

    public String getMotdsMaintenanceLineOne() {
        return motdsMaintenanceLineOne;
    }

    public String getMotdsMaintenanceLineTwo() {
        return motdsMaintenanceLineTwo;
    }

    public String getMotdsNormalLineOne() {
        return motdsNormalLineOne;
    }

    public String getMotdsNormalLineTwo() {
        return motdsNormalLineTwo;
    }

    public String getTablistHeader() {
        return tablistHeader;
    }

    public String getTablistFooter() {
        return tablistFooter;
    }

    public String getMaintenanceVersion() {
        return maintenanceVersion;
    }
}
