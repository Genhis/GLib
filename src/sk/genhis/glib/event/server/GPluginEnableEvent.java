package sk.genhis.glib.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.PluginEvent;

import sk.genhis.glib.plugin.GPlugin;

public final class GPluginEnableEvent extends PluginEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public GPluginEnableEvent(GPlugin plugin) {
		super(plugin);
	}
	
	public HandlerList getHandlers() {
		return GPluginDisableEvent.getHandlerList();
	}
	
	public static HandlerList getHandlerList() {
		return GPluginEnableEvent.handlers;
	}
}