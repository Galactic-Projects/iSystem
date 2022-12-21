package net.galacticprojects.common.secure;

public class Secure {

    private final GalacticSecure secure;

    public Secure() throws Exception {
        this.secure = new GalacticSecure();
    }

    public String hashString(String message) {
        return secure.powerfulHash(message);
    }

    public String unhashString (String message) {
        return secure.powerfulDecrypt(message);
    }

    public GalacticSecure getAPI() {
        return secure;
    }
}
