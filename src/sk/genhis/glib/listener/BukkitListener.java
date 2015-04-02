package sk.genhis.glib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import sk.genhis.glib.GLib;

public final class BukkitListener implements Listener {
	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent e) {
		GLib.unregisterPlayer(e.getPlayer());
	}
}