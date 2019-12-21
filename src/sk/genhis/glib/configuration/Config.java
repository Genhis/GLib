package sk.genhis.glib.configuration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.GLib;
import sk.genhis.glib.Logger;
import sk.genhis.glib.plugin.GPlugin;

public final class Config implements IYMLFile {
	private final JavaPlugin plugin;
	private final Logger logger;
	private final String file;
	private FileConfiguration config;
	
	public Config(JavaPlugin plugin) {
		this(plugin, "config");
	}
	
	public Config(JavaPlugin plugin, String file) {
		this.plugin = plugin;
		this.logger = plugin instanceof GPlugin ? ((GPlugin)plugin).getOwnLogger() : GLib.getLog();
		this.file = file + ".yml";
		this.saveConfig(); //vytvorenie configu v prípade, že neexistuje
		this.loadFile(); //načítanie configu
	}
	
	private void saveConfig() {
		if(!GLib.checkEnabled())
			return;
		
		final File file = new File(this.plugin.getDataFolder(), this.file);
		if(!file.exists()) {
			this.logger.log("Creating default " + this.file + " for plugin " + this.plugin.getDescription().getName());
			this.plugin.saveResource(this.file, false);
		}
	}
	
	public boolean saveCustomConfig() {
		if(!GLib.checkEnabled())
			return false;
		
		try {
			this.config.save(new File(this.plugin.getDataFolder(), this.file));
		}
		catch (IOException ex) {
			this.logger.log("Error occured while saving custom file!", Level.WARNING);
			if(GLib.debug())
				ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean saveCustomConfig(FileConfiguration config) {
		if(!GLib.checkEnabled())
			return false;
		
		this.config.setDefaults(config);
		return this.saveCustomConfig();
	}
	
	public void loadFile() {
		if(!GLib.checkEnabled())
			return;
		
		final File file = new File(this.plugin.getDataFolder(), this.file);
		this.config = YamlConfiguration.loadConfiguration(file);
		
		//final InputStream defaultConfig = this.plugin.getResource(this.file);
		if(file != null) {
			final FileConfiguration newConfig = YamlConfiguration.loadConfiguration(file);
			if(!this.config.getString("file_version", "0").equalsIgnoreCase(newConfig.getString("file_version"))) {
				if(!this.config.getBoolean("merge_config", true)) {
					this.logger.log("File version is different! Removing file " + this.file);
					final File newFile = new File(this.plugin.getDataFolder(), "broken_" + System.currentTimeMillis() + "_" + this.file);
					if(newFile.exists())
						file.delete();
					else
						file.renameTo(newFile);
					this.saveConfig();
				}
				else {
					this.logger.log("File version is different! Merging file " + this.file + " with new file.");
					
					for(String key : newConfig.getKeys(true))
						if(this.config.get(key) == null)
							this.config.set(key, newConfig.get(key));
					
					this.config.set("file_version", newConfig.get("file_version", "0"));
					
					this.saveCustomConfig();
				}
			}
		}
	}
	
	public FileConfiguration getConfig() {
		if(!GLib.checkEnabled())
			return null;
		return this.config;
	}
	
	public FileConfiguration getFileContent() {
		return this.getConfig();
	}
}
