package sk.genhis.glib.event.mysql;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.mysql.MySQL;

public final class MySQLQueryEvent extends MySQLEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	
	private final String query;
	private final boolean mysqlUpdate;
	private boolean cancel = false;
	
	public MySQLQueryEvent(JavaPlugin plugin, MySQL mysql, String query, boolean mysqlUpdate) {
		super(plugin, mysql);
		this.query = query;
		this.mysqlUpdate = mysqlUpdate;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public boolean mysqlUpdate() {
		return this.mysqlUpdate;
	}
	
	public boolean isCancelled() {
		return this.cancel;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	@Override
	public HandlerList getHandlers() {
		return MySQLQueryEvent.handlers;
	}
	
	public static HandlerList getHandlerList() {
		return MySQLQueryEvent.handlers;
	}
}