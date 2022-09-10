package net.galacticprojects.log;

public enum LogType {

    ERROR("Error"), PLAYER("Player"), ACTION("Action"), WARNING("Warning"), TEST("Test");

    public String name;

    LogType(String name) {this.name = name;}

    String getName(LogType type) {return type.name;}
}
