package net.galacticprojects.isystem.bungeecord.config.languages;

import net.galacticprojects.isystem.bungeecord.config.MainConfiguration;
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
    public MainConfiguration mainConfiguration;

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
    private String errorsPermission;
    private String errorsPlayer;
    private String errorsNotExists;
    private String motdsMaintenanceLineOne;
    private String motdsMaintenanceLineTwo;
    private String motdsNormalLineOne;
    private String motdsNormalLineTwo;
    private String tablistHeader;
    private String tablistFooter;
    private String maintenaceUsage;
    private String maintenanceSuccessfulTurnedOn;
    private String maintenanceSuccessfulTurnedOff;
    private String maintenanceErrorsAlreadyOn;
    private String maintenanceErrorsAlreadyOff;
    private String maintenanceVersion;
    private String maintenanceReason;
    private String maintenanceEndDate;
    private String onlineTimeUsage;
    private String onlineTimeSuccessSelf;
    private String onlineTimeSuccessOthers;
    private String onlineTimeSuccessEnabled;
    private String onlineTimeSuccessDisabled;
    private String onlineTimeErrorsNotEnabled;
    private String onlineTimeErrorsAlreadyEnabled;
    private String onlineTimeErrorsAlreadyDisabled;
    private String kickMaintenanceCurrently;
    private String kickMaintenanceNow;

    public EnglishConfiguration () throws IOException {
        JavaInstance.put(this);
        mainConfiguration = JavaInstance.get(MainConfiguration.class);

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

            if(!(englishConfiguration.contains("Messages.System.Errors.Permission"))) {
                englishConfiguration.set("Messages.System.Errors.Permission", "&cYou do not have permission to execute this command!");
            }

            if(!(englishConfiguration.contains("Messages.System.Errors.Player"))) {
                englishConfiguration.set("Messages.System.Errors.Player", "&cYou must be a player to perform this command!");
            }

            if(!(englishConfiguration.contains("Messages.System.Errors.NotExists"))) {
                englishConfiguration.set("Messages.System.Errors.NotExists", "&cThat player wasn't on the network yet.");
            }

            if(!(englishConfiguration.contains("Messages.System.MOTDs.Maintenance.Line.1"))) {
                englishConfiguration.set("Messages.System.MOTDs.Maintenance.Line.1", "&8「 &5&lG&d&lP &7■ &fYour &bnetwork &fwith &dgalactical &5power &8」");
            }

            if(!(englishConfiguration.contains("Messages.System.MOTDs.Maintenance.Line.2"))) {
                englishConfiguration.set("Messages.System.MOTDs.Maintenance.Line.2", "&8► &7We are &c&lcurrently &7under &4&lmaintenance&7!");
            }

            if(!(englishConfiguration.contains("Messages.System.MOTDs.Normal.Line.1"))) {
                englishConfiguration.set("Messages.System.MOTDs.Normal.Line.1", "&8「 &5&lG&d&lP &7■ &fYour &bnetwork &fwith &dgalactical &5power &8」");
            }

            if(!(englishConfiguration.contains("Messages.System.MOTDs.Normal.Line.2"))) {
                englishConfiguration.set("Messages.System.MOTDs.Normal.Line.2", "&8► &4&lOPEN-BETA &fNOW! ");
            }

            if(!(englishConfiguration.contains("Messages.System.Tablist.Header"))) {
                englishConfiguration.set("Messages.System.Tablist.Header", "&7 \n &8「 &5&lGALACTIC&d&lPROJECTS &7■ &fYour &bnetwork &fwith &dgalactical &5power &8」\n &8► &fConnected to &b%server% &7■ &fCurrently online &b%online%&f/&3%maxplayers% &8◄\n &7");
            }

            if(!(englishConfiguration.contains("Messages.System.Tablist.Footer"))) {
                englishConfiguration.set("Messages.System.Tablist.Footer", "&7 \n &8► &fWebsite &7× &dwww.galacticalprojects.net &8■ &fTeamSpeak &7× &dGalacticalProjects.net &8◄ \n &8► &fForum &7× &dforum.galacticalprojects.net &8■ &fShop &7× &dshop.galacticalprojects.net &8◄ \n &7");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.Usage"))) {
                englishConfiguration.set("Messages.System.Maintenance.Usage", "%maintenancePrefix% &eUsage &8► &f/maintenance <on, off> <Time (If on, in Hours)> <Reason (If on)>");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.Successful.TurnedOn"))) {
                englishConfiguration.set("Messages.System.Maintenance.Successful.TurnedOn", "%maintenancePrefix% &7You successfully turned maintenance with the &creason &4%reason% &7and the end &bdate &3%enddate% &7on.");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.Successful.TurnedOff"))) {
                englishConfiguration.set("Messages.System.Maintenance.Successful.TurnedOff", "%maintenancePrefix% &7You successfully turned maintenance off.");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.Error.AlreadyOn"))) {
                englishConfiguration.set("Messages.System.Maintenance.Error.AlreadyOn", "%maintenancePrefix% &7The maintenance is already turned on.");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.Error.AlreadyOff"))) {
                englishConfiguration.set("Messages.System.Maintenance.Error.AlreadyOff", "%maintenancePrefix% &7The maintenance is already turned off.");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.Version"))) {
                englishConfiguration.set("Messages.System.Maintenance.Version", "&8► &4&lMAINTENANCE");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.Reason"))) {
                englishConfiguration.set("Messages.System.Maintenance.Reason", "Construction");
            }

            if(!(englishConfiguration.contains("Messages.System.Maintenance.EndDate"))) {
                englishConfiguration.set("Messages.System.Maintenance.EndDate", "Fridat the 16. August 16:20:00 (UTC)");
            }

            if(!(englishConfiguration.contains("Messages.System.OnlineTime.Usage"))) {
                englishConfiguration.set("Messages.System.OnlineTime.Usage", "%onlinePrefix% &eUsage &8► &f/onlinetime <Off, On, Player>");
            }

            if(!(englishConfiguration.contains("Messages.System.OnlineTime.Success.Self"))) {
                englishConfiguration.set("Messages.System.OnlineTime.Success.Self", "%onlinePrefix% &7Your &bonlinetime &7is &3%year% Year(s)&7,&3 %month% Month(s)&7,&3 %day% Day(s)&7,&3 %hour% Hour(s) &7and &3%minute% Minute(s)&7.");
            }

            if(!(englishConfiguration.contains("Messages.System.OnlineTime.Success.Others"))) {
                englishConfiguration.set("Messages.System.OnlineTime.Success.Others", "%onlinePrefix% &7The &bonlinetime &7by %player% &7is &3%year% Year(s)&7,&3 %month% Month(s)&7,&3 %day% Day(s)&7,&3 %hour% Hour(s) &7and &3%minute% Minute(s)&7.");
            }

            if(!(englishConfiguration.contains("Messages.System.OnlineTime.Success.Enabled"))) {
                englishConfiguration.set("Messages.System.OnlineTime.Success.Enabled", "%onlinePrefix% &7You &asuccessfully &benabled &7showing your &3onlinetime &7to others.");
            }

            if(!(englishConfiguration.contains("Messages.System.OnlineTime.Success.Disabled"))) {
                englishConfiguration.set("Messages.System.OnlineTime.Success.Disabled", "%onlinePrefix% &7You &asuccessfully &bdisabled &7showing your &3onlinetime &7to others.");
            }

            if(!(englishConfiguration.contains("Messages.System.OnlineTime.Errors.AlreadyOn"))) {
                englishConfiguration.set("Messages.System.OnlineTime.Errors.AlreadyOn", "%onlinePrefix% &cYou already enabled showing your onlinetime to others.");
            }

            if(!(englishConfiguration.contains("Messages.System.OnlineTime.Errors.AlreadyOff"))) {
                englishConfiguration.set("Messages.System.OnlineTime.Errors.AlreadyOff", "%onlinePrefix% &cYou already disabled showing  your onlinetime to others.");
            }

            if(!(englishConfiguration.contains("Messages.System.OnlineTime.Errors.NotEnabled"))) {
                englishConfiguration.set("Messages.System.OnlineTime.Errors.NotEnabled", "%onlinePrefix% &cThat player disabled that others can see their online time.");
            }
            
            if(!(englishConfiguration.contains("Messages.System.Kick.Maintenance.Currently"))) {
                englishConfiguration.set("Messages.System.Kick.Maintenance.Currently", "%menuPrefix%\n&7The &bnetwork &7is &3currently &7in &cmaintenance mode&7.\n\n&7► &cReason &8■ &4%reason%\n&7► &cEnd Date &8■ &4%enddate%\n\n&7Want to contact us?\n&7► &dWebsite &8■ &5www.GalacticProjects.net");
            }

            if(!(englishConfiguration.contains("Messages.System.Kick.Maintenance.Now"))) {
                englishConfiguration.set("Messages.System.Kick.Maintenance.Now", "%menuPrefix%\n&7The &bnetwork &7is &3now &7in &cmaintenance mode&7.\n\n&7► &cReason &8■ &4%reason%\n&7► &cEnd Date &8■ &4%enddate%\n\n&7Want to contact us?\n&7► &dWebsite &8■ &5www.GalacticProjects.net");
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

            errorsPermission = Color.apply(englishConfiguration.getString("Messages.System.Errors.Permission"));
            errorsPlayer = Color.apply(englishConfiguration.getString("Messages.System.Errors.Player"));
            errorsNotExists = Color.apply(englishConfiguration.getString("Messages.System.Errors.NotExists"));

            motdsMaintenanceLineOne = Color.apply(englishConfiguration.getString("Messages.System.MOTDs.Maintenance.Line.1"));
            motdsMaintenanceLineTwo = Color.apply(englishConfiguration.getString("Messages.System.MOTDs.Maintenance.Line.2"));
            motdsNormalLineOne = Color.apply(englishConfiguration.getString("Messages.System.MOTDs.Normal.Line.1"));
            motdsNormalLineTwo = Color.apply(englishConfiguration.getString("Messages.System.MOTDs.Normal.Line.2"));

            tablistHeader = Color.apply(englishConfiguration.getString("Messages.System.Tablist.Header"));
            tablistFooter = Color.apply(englishConfiguration.getString("Messages.System.Tablist.Footer"));

            maintenaceUsage = Color.apply(englishConfiguration.getString("Messages.System.Maintenance.Usage").replaceAll("%maintenancePrefix%", mainConfiguration.getMaintenancePrefix()));
            maintenanceSuccessfulTurnedOn = Color.apply(englishConfiguration.getString("Messages.System.Maintenance.Successful.TurnedOn").replaceAll("%maintenancePrefix%", mainConfiguration.getMaintenancePrefix()));
            maintenanceSuccessfulTurnedOff = Color.apply(englishConfiguration.getString("Messages.System.Maintenance.Successful.TurnedOff").replaceAll("%maintenancePrefix%", mainConfiguration.getMaintenancePrefix()));
            maintenanceErrorsAlreadyOn = Color.apply(englishConfiguration.getString("Messages.System.Maintenance.Error.AlreadyOn").replaceAll("%maintenancePrefix%", mainConfiguration.getMaintenancePrefix()));
            maintenanceErrorsAlreadyOff = Color.apply(englishConfiguration.getString("Messages.System.Maintenance.Error.AlreadyOff").replaceAll("%maintenancePrefix%", mainConfiguration.getMaintenancePrefix()));
            maintenanceVersion = Color.apply(englishConfiguration.getString("Messages.System.Maintenance.Version"));
            maintenanceReason = englishConfiguration.getString("Messages.System.Maintenance.Reason");
            maintenanceEndDate = englishConfiguration.getString("Messages.System.Maintenance.EndDate");

            onlineTimeUsage = Color.apply(englishConfiguration.getString("Messages.System.OnlineTime.Usage").replaceAll("%onlinePrefix%", mainConfiguration.getOnlineTimePrefix()));
            onlineTimeSuccessSelf = Color.apply(englishConfiguration.getString("Messages.System.OnlineTime.Success.Self").replaceAll("%onlinePrefix%", mainConfiguration.getOnlineTimePrefix()));
            onlineTimeSuccessOthers = Color.apply(englishConfiguration.getString("Messages.System.OnlineTime.Success.Others").replaceAll("%onlinePrefix%", mainConfiguration.getOnlineTimePrefix()));
            onlineTimeSuccessEnabled = Color.apply(englishConfiguration.getString("Messages.System.OnlineTime.Success.Enabled").replaceAll("%onlinePrefix%", mainConfiguration.getOnlineTimePrefix()));
            onlineTimeSuccessDisabled = Color.apply(englishConfiguration.getString("Messages.System.OnlineTime.Success.Disabled").replaceAll("%onlinePrefix%", mainConfiguration.getOnlineTimePrefix()));
            onlineTimeErrorsAlreadyEnabled = Color.apply(englishConfiguration.getString("Messages.System.OnlineTime.Errors.AlreadyOn").replaceAll("%onlinePrefix%", mainConfiguration.getOnlineTimePrefix()));
            onlineTimeErrorsAlreadyDisabled = Color.apply(englishConfiguration.getString("Messages.System.OnlineTime.Errors.AlreadyOff").replaceAll("%onlinePrefix%", mainConfiguration.getOnlineTimePrefix()));
            onlineTimeErrorsNotEnabled = Color.apply(englishConfiguration.getString("Messages.System.OnlineTime.Errors.NotEnabled").replaceAll("%onlinePrefix%", mainConfiguration.getOnlineTimePrefix()));

            kickMaintenanceCurrently = Color.apply(englishConfiguration.getString("Messages.System.Kick.Maintenance.Currently").replaceAll("%menuPrefix%", mainConfiguration.getMenuPrefix()));
            kickMaintenanceNow = Color.apply(englishConfiguration.getString("Messages.System.Kick.Maintenance.Now").replaceAll("%menuPrefix%", mainConfiguration.getMenuPrefix()));

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

    public String getMonth5() {
        return month5;
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

    public String getErrorsPermission() {
        return errorsPermission;
    }

    public String getErrorsPlayer() {
        return errorsPlayer;
    }

    public String getErrorsNotExists() {
        return errorsNotExists;
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

    public String getMaintenaceUsage() {
        return maintenaceUsage;
    }

    public String getMaintenanceSuccessfulTurnedOn() {
        return maintenanceSuccessfulTurnedOn;
    }

    public String getMaintenanceSuccessfulTurnedOff() {
        return maintenanceSuccessfulTurnedOff;
    }

    public String getMaintenanceErrorsAlreadyOn() {
        return maintenanceErrorsAlreadyOn;
    }

    public String getMaintenanceErrorsAlreadyOff() {
        return maintenanceErrorsAlreadyOff;
    }

    public String getMaintenanceVersion() {
        return maintenanceVersion;
    }

    public String getMaintenanceReason() {
        return maintenanceReason;
    }

    public String getMaintenanceEndDate() { return maintenanceEndDate; }

    public String getOnlineTimeUsage() {
        return onlineTimeUsage;
    }

    public String getOnlineTimeSuccessSelf() {
        return onlineTimeSuccessSelf;
    }

    public String getOnlineTimeSuccessOthers() {
        return onlineTimeSuccessOthers;
    }

    public String getOnlineTimeSuccessEnabled() {
        return onlineTimeSuccessEnabled;
    }

    public String getOnlineTimeSuccessDisabled() {
        return onlineTimeSuccessDisabled;
    }

    public String getOnlineTimeErrorsAlreadyEnabled() {
        return onlineTimeErrorsAlreadyEnabled;
    }

    public String getOnlineTimeErrorsAlreadyDisabled() {
        return onlineTimeErrorsAlreadyDisabled;
    }

    public String getOnlineTimeErrorsNotEnabled() {
        return onlineTimeErrorsNotEnabled;
    }

    public String getKickMaintenanceCurrently() {
        return kickMaintenanceCurrently;
    }

    public String getKickMaintenanceNow() {
        return kickMaintenanceNow;
    }
}
