package sk.genhis.glib.configuration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sk.genhis.glib.GLib;
import sk.genhis.glib.plugin.GPlugin;

public final class Database implements IYMLFile {
	private final GPlugin plugin;
	private final String file;
	private FileConfiguration db;
	
	public Database(GPlugin plugin, String file) {
		this.plugin = plugin;
		this.file = file + ".yml";
		this.loadFile();
	}
	
	public boolean save() {
		return this.save(this.db);
	}
	
	public boolean save(FileConfiguration db) {
		try {
			this.db.setDefaults(db);
			db.save(new File(this.plugin.getDataFolder(), this.file));
		}
		catch (IOException ex) {
			this.plugin.getOwnLogger().log("Error occured while saving custom file!", Level.WARNING);
			if(GLib.debug())
				ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void loadFile() {
		final File file = new File(this.plugin.getDataFolder(), this.file);
		try {
			if(!file.exists())
				file.createNewFile();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
		this.db = YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration getData() {
		return this.db;
	}
	
	public FileConfiguration getFileContent() {
		return this.getData();
	}
}