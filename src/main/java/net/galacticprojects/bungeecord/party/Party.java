package net.galacticprojects.bungeecord.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class Party {

    private UUID leader;
    private UUID moderator;
    private ArrayList<UUID> member;
    private String name;

    public Party(ProxiedPlayer leader, ArrayList<UUID> member) {
        this.leader = leader.getUniqueId();
        this.member = member;
        this.moderator = null;
        name = leader.getDisplayName() + "'s Party";
    }

    public Party(ProxiedPlayer leader, ArrayList<UUID> member, String name) {
        this.leader = leader.getUniqueId();
        this.member = member;
        this.moderator = null;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getLeader() {
        return leader;
    }

    public ArrayList<UUID> getMember() {
        return member;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public void addMember(UUID member) {
        this.member.add(member);
    }

    public void removeMember(UUID member) {
        this.member.remove(member);
    }

    public void setModerator(UUID moderator) {
        this.moderator = moderator;
    }

    public UUID getModerator() {
        return moderator;
    }

    public void setName(String name) {
        this.name = name;
    }

}
