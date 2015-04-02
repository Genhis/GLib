package sk.genhis.glib.event.mysql;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.mysql.MySQL;

public final class MySQLCloseEvent extends MySQLEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public MySQLCloseEvent(JavaPlugin plugin, MySQL mysql) {
		super(plugin, mysql);
	}
	
	public HandlerList getHandlers() {
		return MySQLCloseEvent.handlers;
	}
	
	public static HandlerList getHandlerList() {
		return MySQLCloseEvent.handlers;
	}
}