package sk.genhis.glib.configuration;

import org.bukkit.configuration.file.FileConfiguration;

public interface IYMLFile extends IFile {
	public FileConfiguration getFileContent();
}