package sk.genhis.glib.task;

import sk.genhis.glib.GLib;
import sk.genhis.glib.plugin.GPlugin;
import sk.genhis.glib.scheduler.GTask;

@Deprecated
@SuppressWarnings("unused")
public final class DisablePluginTask implements GTask {
	private final GPlugin plugin;
	
	public DisablePluginTask(GPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		//GLib.getPluginManager().unloadPlugin(this.plugin);
	}
	
	public String getName() {
		return this.getClass().getName();
	}
}