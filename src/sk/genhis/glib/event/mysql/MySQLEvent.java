package sk.genhis.glib.event.mysql;

import org.bukkit.event.server.PluginEvent;
import org.bukkit.plugin.Plugin;

import sk.genhis.glib.mysql.MySQL;

public abstract class MySQLEvent extends PluginEvent {
	private final MySQL mysql;
	
	public MySQLEvent(Plugin plugin, MySQL mysql) {
		super(plugin);
		this.mysql = mysql;
	}
	
	public MySQL getMySQL() {
		return this.mysql;
	}
}