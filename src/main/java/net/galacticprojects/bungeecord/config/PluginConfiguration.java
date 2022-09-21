package net.galacticprojects.bungeecord.config;

import java.io.File;

import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.common.config.BaseConfiguration;

public final class PluginConfiguration extends BaseConfiguration {

	private boolean maintenance = false;
	private String maintenanceReason = null;
	
	public PluginConfiguration(ISimpleLogger logger, File dataFolder) {
		super(logger, dataFolder, "config.json");
	}
	
	@Override
	protected void onLoad() throws Throwable {
		maintenance = config.getValueOrDefault("maintenance.enabled", false);
		maintenanceReason = config.getValueOrDefault("maintenance.reason", "Example Reason");
	}

	@Override
	protected void onSave() throws Throwable {
		config.setValue("maintenance.enabled", maintenance);
		config.setValue("maintenance.reason", maintenanceReason);
	}
	
	public void setMaintenance(boolean maintenance) {
		this.maintenance = maintenance;
	}
	
	public void setMaintenanceReason(String maintenanceReason) {
		this.maintenanceReason = maintenanceReason;
	}
	
	public String getMaintenanceReason() {
		return maintenanceReason;
	}
	
	public boolean isMaintenance() {
		return maintenance;
	}
}
