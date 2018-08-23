package sk.genhis.glib.plugin;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.GLib;
import sk.genhis.glib.Logger;
import sk.genhis.glib.configuration.Configuration;
import sk.genhis.glib.event.server.GPluginDisableEvent;
import sk.genhis.glib.event.server.GPluginEnableEvent;
import sk.genhis.glib.event.server.GPluginPreEnableEvent;
import sk.genhis.glib.mysql.MySQL;

public abstract class GPlugin extends JavaPlugin {
	@Override
	public final void onEnable() {
		final GPluginPreEnableEvent e = new GPluginPreEnableEvent(this);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled()) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		final long startTime = System.currentTimeMillis();
		this.checkRegister();
		this.glibOperations();
		if(this.enable() && this.isEnabled()) {
			//GLib.loadPlugin(this);
			this.getOwnLogger().log("Enabled! (" + (System.currentTimeMillis() - startTime) + "ms)");
		}
		
		Bukkit.getPluginManager().callEvent(new GPluginEnableEvent(this));
	}
	
	@Override
	public final void onDisable() {
		final long startTime = System.currentTimeMillis();
		//GLib.unloadPlugin(this);
		this.disable();
		this.getOwnLogger().log("Disabled! (" + (System.currentTimeMillis() - startTime) + "ms)");
		
		Bukkit.getPluginManager().callEvent(new GPluginDisableEvent(this));
	}
	
	public void onReload() {
		Bukkit.getPluginManager().disablePlugin(this);
		Bukkit.getPluginManager().enablePlugin(this);
	}
	
	protected boolean enable() {
		return true;
	}
	
	protected void disable() {}
	
	public final void disable(String msg) {
		if(msg != "" && msg != null)
			this.getOwnLogger().log(msg, Level.SEVERE);
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	private void checkRegister() {
		final String pl = this.getDescription().getName();
		
		if(this.getOwnLogger() != null && this.getOwnLogger().getUID() == GLib.getLog().getUID())
			GLib.getLog().log("[" + pl + "] " + "Own logger not found! Hooked into GLib logger.");
		
		if(this.enableMysql())
			if(this.getOwnMysql() != null && this.getOwnMysql().getUID() == GLib.getMysql().getUID())
				GLib.getLog().log("[" + pl + "] " +
					"Own MySQL configuration not found! Hooked into GLib configuration.");
	}
	
	private void glibOperations() {
		if(GLib.getConf().getBoolean("permissions.reload_plugins", true)) {
			if(Bukkit.getPluginManager().getPermission("glib.glib.reload." + this.getName()) == null) {
				Permission p = new Permission("glib.glib.reload." + this.getName());
				Bukkit.getPluginManager().addPermission(p);
				p.addParent("glib.glib.reload", true);
			}
		}
	}
	
	protected abstract boolean enableMysql();
	
	public Logger getOwnLogger() {
		return GLib.getLog();
	}
	
	public Configuration getOwnConfig() {
		return null;
	}
	
	public Configuration getOwnMsg() {
		return null;
	}
	
	public MySQL getOwnMysql() {
		if(!this.enableMysql())
			return null;
		return GLib.getMysql();
	}
	
	public boolean debug() {
		return GLib.debug();
	}
	
	public void processException(Exception ex) {
		this.getOwnLogger().log("Exception: " + ex.getMessage());
		if(GLib.debug())
			ex.printStackTrace();
	}
}