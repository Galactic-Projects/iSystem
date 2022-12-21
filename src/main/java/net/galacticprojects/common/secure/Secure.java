package net.galacticprojects.common.secure;

public class Secure {

    private final GalacticSecure secure;
    private final String message;

    public Secure(String message) throws Exception {
        this.secure = new GalacticSecure();
        this.message = message;
    }

    public String encrypt() {
        return secure.powerfulHash(message);
    }

    public String decrypt() {
        return secure.powerfulDecrypt(message);
    }

    public GalacticSecure getAPI() {
        return secure;
    }
}
