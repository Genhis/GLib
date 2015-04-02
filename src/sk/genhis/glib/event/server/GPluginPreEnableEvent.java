package sk.genhis.glib.event.server;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.PluginEvent;

import sk.genhis.glib.plugin.GPlugin;

public final class GPluginPreEnableEvent extends PluginEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	public GPluginPreEnableEvent(GPlugin plugin) {
		super(plugin);
	}
	
	public HandlerList getHandlers() {
		return GPluginDisableEvent.getHandlerList();
	}
	
	public static HandlerList getHandlerList() {
		return GPluginPreEnableEvent.handlers;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public void setCancelled(boolean value) {
		this.cancelled = value;
	}
}