package net.galacticprojects.common.util;

import java.util.Random;

public class CodeGenerator {
    private final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public String generate(int amount) {
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for(int i = 1; i < amount; i++) {
            int randomIndex = random.nextInt(CHAR_LIST.length());
            password.append(CHAR_LIST.charAt(randomIndex));
        }
        return password.toString();
    }
}
