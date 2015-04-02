package sk.genhis.glib.configuration;

import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public final class Configuration extends FileConfiguration {
	private final UUID uid = UUID.randomUUID();
	
	public Configuration(FileConfiguration config) {
		this.setDefaults(config);
	}
	
	public UUID getUID() {
		return this.uid;
	}

	@Deprecated
	protected String buildHeader() {
		return null;
	}

	@Deprecated
	public void loadFromString(String arg0) throws InvalidConfigurationException {}

	@Deprecated
	public String saveToString() {
		return null;
	}
}