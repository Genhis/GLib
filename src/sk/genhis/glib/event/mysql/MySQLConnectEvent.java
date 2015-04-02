package sk.genhis.glib.event.mysql;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.mysql.MySQL;

public final class MySQLConnectEvent extends MySQLEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancel = false;
	
	public MySQLConnectEvent(JavaPlugin plugin, MySQL mysql) {
		super(plugin, mysql);
	}

	public boolean isCancelled() {
		return this.cancel;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	public HandlerList getHandlers() {
		return MySQLConnectEvent.handlers;
	}
	
	public static HandlerList getHandlerList() {
		return MySQLConnectEvent.handlers;
	}
}