package net.galacticprojects.bungeecord.config;

import java.io.File;

import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.common.config.BaseConfiguration;

public final class PluginConfiguration extends BaseConfiguration {

	private boolean maintenance = false;
	private String maintenanceReason = null;
	private String playerAmount = "-1";
	private String days = "0";
	
	public PluginConfiguration(ISimpleLogger logger, File dataFolder) {
		super(logger, dataFolder, "config.json");
	}
	
	@Override
	protected void onLoad() throws Throwable {
		maintenance = config.getValueOrDefault("maintenance.enabled", false);
		maintenanceReason = config.getValueOrDefault("maintenance.reason", "Example Reason");
		playerAmount = config.getValueOrDefault("players.amount",  Integer.toString(50));
		days = config.getValueOrDefault("maintenance.days", days);
	}

	@Override
	protected void onSave() throws Throwable {
		config.setValue("maintenance.enabled", maintenance);
		config.setValue("maintenance.reason", maintenanceReason);
		config.setValue("maintenance.days", days);
		config.setValue("players.amount", playerAmount);
	}
	
	public void setMaintenance(boolean maintenance) {
		this.maintenance = maintenance;
	}
	
	public void setMaintenanceReason(String maintenanceReason) {
		this.maintenanceReason = maintenanceReason;
	}

	public void setPlayerAmount(int playerAmount) {
		this.playerAmount = Integer.toString(playerAmount);
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getMaintenanceReason() {
		return maintenanceReason;
	}
	
	public boolean isMaintenance() {
		return maintenance;
	}

	public String getPlayerAmount() {
		return playerAmount;
	}

	public String getDays() {
		return days;
	}
}
