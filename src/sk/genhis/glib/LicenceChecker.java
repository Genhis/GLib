package sk.genhis.glib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import sk.genhis.glib.plugin.GPlugin;
import sk.genhis.glib.task.DisablePluginTask;

public final class LicenceChecker {
	private final GPlugin plugin;
	
	public LicenceChecker(GPlugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean checkLicence() {
		return LicenceChecker.checkLicence(GLib.getPlugin().getDescription().getWebsite() + "auth/" +
				this.plugin.getDescription().getName().toLowerCase() + ".txt");
	}
	
	public void unlicenced() {
		this.plugin.getOwnLogger().log("You are using unlicenced version of plugin " + this.plugin.getDescription().getName() + ", disabling it!",
				Level.WARNING);
		
		GLib.getScheduler().scheduleSyncDelayedTask(this.plugin, new DisablePluginTask(this.plugin), 20L);
	}
	
	public static boolean checkLicence(String address) {
		if(!GLib.checkEnabled())
			return false;
		
		try {
			URL url = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String text = "";
			String textLine;
			
			while((textLine = in.readLine()) != null) {
				text += textLine + ";";
			}
			in.close();
			if(GLib.in_array(text.split(";"), Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort()))
				return true;
		}
		catch (Exception ex) {
			GLib.getLog().log("Exception occured while checking server data!", Level.WARNING);
			if(GLib.debug())
				ex.printStackTrace();
		}
		return false;
	}
}
