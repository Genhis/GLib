package sk.genhis.glib;

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public final class Logger {
	private final UUID uid = UUID.randomUUID();
	private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Minecraft");
	private final String prefix;
	
	public Logger(String prefix) {
		this.prefix = prefix;
	}
	
	public Logger(JavaPlugin plugin) {
		this("[" + plugin.getDescription().getName() + "]");
	}
	
	public void log(Message message) {
		this.log(message.toString());
	}
	
	public void log(String message) {
		this.log(message, Level.INFO);
	}
	
	public void log(Message message, Level level) {
		this.log(message.toString(), level);
	}
	
	public void log(String message, Level level) {
		this.logger.log(level, this.prefix + " " + message);
	}
	
	public UUID getUID() {
		return this.uid;
	}
}
