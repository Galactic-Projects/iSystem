package net.galacticprojects.isystem.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

    private static File file = new File("/home/systemlog", "log.txt");

    public static void addMessage(String message, LogType type) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append("[" + type.name + "] " + message);
            bw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void setup() {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(LogType type, String name, String msg) {
        DateFormat format = new SimpleDateFormat("dd/MM HH:mm:ss");
        String date = format.format(new Date());
        String finalMessage = date + " - " + name + ": " + msg + "\n";
        addMessage(finalMessage, LogType.valueOf(type.toString().toUpperCase()));
    }

}
