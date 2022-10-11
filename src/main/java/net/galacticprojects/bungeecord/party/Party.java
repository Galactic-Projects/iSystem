package net.galacticprojects.bungeecord.party;

import net.galacticprojects.bungeecord.util.HashMaps;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void deleteParty() {
        for (UUID uniqueId : member) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
            if(player != null) {
                player.sendMessage("");
            }
        }
        HashMaps.partyHashMap.remove(leader);
        member.clear();
        moderator = null;
        leader = null;
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
        if(HashMaps.partyHashMap.get(leader) != null) {
            HashMaps.partyHashMap.remove(leader, this);
        }
        HashMaps.partyHashMap.put(leader, this);
    }

    public void addMember(UUID member) {
        this.member.add(member);
        HashMaps.partyHashMap.remove(leader, this);
        HashMaps.partyHashMap.put(leader, this);
    }

    public void removeMember(UUID member) {
        this.member.remove(member);
        HashMaps.partyHashMap.remove(leader, this);
        HashMaps.partyHashMap.put(leader, this);
    }

    public void setModerator(UUID moderator) {
        this.moderator = moderator;
        HashMaps.partyHashMap.remove(leader, this);
        HashMaps.partyHashMap.put(leader, this);
    }

    public UUID getModerator() {
        return moderator;
    }

    public void setName(String name) {
        this.name = name;
        HashMaps.partyHashMap.remove(leader, this);
        HashMaps.partyHashMap.put(leader, this);
    }

}
