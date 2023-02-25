package net.galacticprojects.bungeecord.message;

import me.lauriichan.laylib.localization.source.IMessageDefinition;

public enum BanMessage implements IMessageDefinition {

    COMMAND_BAN_ID_1("Unallowed Clientmodifications"), COMMAND_BAN_ID_2("t"), COMMAND_BAN_ID_3("t"), COMMAND_BAN_ID_4("t"), COMMAND_BAN_ID_5("t"), COMMAND_BAN_ID_6("t"), COMMAND_BAN_ID_7("t"), COMMAND_BAN_ID_8("t"), COMMAND_BAN_ID_9("t"),
    AUTO_BAN_FLIGHT("Unallowed Clientmodifications | Flight"), AUTO_BAN_ELYTRAFLY("Unallowed Clientmodifications | ElytraFly"), AUTO_BAN_WATER_WALK("Unallowed Clientmodifications | WaterWalk"), AUTO_BAN_FAST_PLACE("Unallowed Clientmodifications | FastPlace"), AUTO_BAN_CHAT_SPAM("Unallowed Clientmodifications | ChatSpam"),
    AUTO_BAN_COMMAND_SPAM("Unallowed Clientmodifications | CommandSpam"), AUTO_BAN_SPRINT("Unallowed Clientmodifications | Sprint"), AUTO_BAN_SNEAK("Unallowed Clientmodifications | Sneak"), AUTO_BAN_SPEED("Unallowed Clientmodifications | Speed"), AUTO_BAN_VCLIP("Unallowed Clientmodifications | vClip"),
    AUTO_BAN_SPIDER("Unallowed Clientmodifications | Spider"), AUTO_BAN_NO_FALL("Unallowed Clientmodifications | NoFall"), AUTO_BAN_FAST_BOW("Unallowed Clientmodifications | FastBow"), AUTO_BAN_FAST_EAT("Unallowed Clientmodifications | FastEat"), AUTO_BAN_FAST_HEAL("Unallowed Clientmodifications | FastHeal"),
    AUTO_BAN_KILLAURA("Unallowed Clientmodifications | KillAura"), AUTO_BAN_FAST_PROJECTILE("Unallowed Clientmodifications | FastProjectile"), AUTO_BAN_ITEM_SPAM("Unallowed Clientmodifications | ItemSpam"), AUTO_BAN_FAST_INVENTORY("Unallowed Clientmodifications | FastInventory"),
    AUTO_BAN_VELOCITY("Unallowed Clientmodifications | Velocity"), AUTO_BAN_CRITICALS("Unallowed Clientmodifications | Criticals"), AUTO_BAN_CHAT_UNICODE("Unallowed Clientmodifications | ChatUnicode"), AUTO_BAN_ILLEGALINTERACT("Unallowed Clientmodifications | IllegalInteract"),
    AUTO_BAN_FASTLADDER("Unallowed Clientmodifications | FastLadder"), AUTO_BAN_AIMBOT("Unallowed Clientmodifications | Aimbot"), AUTO_BAN_STRAFE("Unallowed Clientmodifications | Strafe"), AUTO_BAN_NOSLOW("Unallowed Clientmodifications | NoSlow"), AUTO_BAN_BOATFLY("Unallowed Clientmodifications | BoatFly");

    private final String fallback;

private BanMessage() {this(null);}
    private BanMessage(String fallback) {this.fallback = fallback;}

    @Override
    public String id() {
        return name().toLowerCase().replace('_', '.').replace('$', '_');
    }

    @Override
    public String fallback() {
        return fallback;
    }
}
