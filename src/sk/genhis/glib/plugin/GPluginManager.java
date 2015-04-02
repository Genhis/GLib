package sk.genhis.glib.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import sk.genhis.glib.GLib;
import sk.genhis.glib.command.GCommand;

public final class GPluginManager {
	private SimpleCommandMap scm;
	private Map<String, Command> knownCmds;
	private Field loadersF;
	
	@SuppressWarnings("unchecked")
	public GPluginManager() {
		final SimplePluginManager spm = (SimplePluginManager)Bukkit.getPluginManager();
		
		try {
			Field scmF = spm.getClass().getDeclaredField("commandMap");
			scmF.setAccessible(true);
			this.scm = (SimpleCommandMap)scmF.get(spm);
			
			Field knownCmdsF = this.scm.getClass().getDeclaredField("knownCommands");
			knownCmdsF.setAccessible(true);
			this.knownCmds = (Map<String, Command>)knownCmdsF.get(this.scm);
		}
		catch (NoSuchFieldException ex) {
			ex.printStackTrace();
		}
		catch (SecurityException ex) {
			ex.printStackTrace();
		}
		catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean closeClassLoader(Plugin plugin) {
		try {
			((URLClassLoader)plugin.getClass().getClassLoader()).close();
			return true;
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	private void registerCommands(Plugin pl) {
		JavaPlugin plugin = (JavaPlugin)pl;
		Iterator<Entry<String, Map<String, Object>>> it = plugin.getDescription().getCommands().entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, Map<String, Object>> entry = it.next();
			PluginCommand cmd = plugin.getCommand(entry.getKey());
			if(cmd != null)
				this.knownCmds.put(cmd.getName().toLowerCase(), cmd);
		}
	}
	
	public GCommand registerCommand(JavaPlugin pl, String cmd, String perm) {
		GCommand gcmd = new GCommand(pl, cmd, perm);
		this.scm.register("_", gcmd);
		return gcmd;
	}
	
	public Plugin loadPlugin(String name) {
		try {
			Plugin plugin = Bukkit.getPluginManager().loadPlugin(new File("plugins/" + name + ".jar"));
			GLib.getLog().log((plugin.getDescription() != null ? 1 : 0) + ":" + (plugin.getDescription().getCommands() != null ? 1 : 0));
			this.registerCommands(plugin);
			//Bukkit.getPluginManager().enablePlugin(plugin);
			return plugin;
		}
		catch (UnknownDependencyException ex) {
			ex.printStackTrace();
		}
		catch (InvalidPluginException ex) {
			ex.printStackTrace();
		}
		catch (InvalidDescriptionException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public void unloadPlugin(Plugin plugin) {
		final SimplePluginManager spm = (SimplePluginManager)Bukkit.getPluginManager();
		
		Map names = new HashMap();
		List plugins = new ArrayList();
		try {
			Field namesF = spm.getClass().getDeclaredField("lookupNames");
			namesF.setAccessible(true);
			names = (Map)namesF.get(spm);
			
			Field pluginsF = spm.getClass().getDeclaredField("plugins");
			pluginsF.setAccessible(true);
			plugins = (List)pluginsF.get(spm);
		}
		catch (NoSuchFieldException ex) {
			ex.printStackTrace();
		}
		catch (SecurityException ex) {
			ex.printStackTrace();
		}
		catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
		
		synchronized(this.scm) {
			Iterator<Map.Entry<String, Command>> it = this.knownCmds.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, Command> entry = it.next();
				if(entry.getValue() instanceof PluginCommand) {
					PluginCommand cmd = (PluginCommand)entry.getValue();
					if(cmd.getPlugin().getName().equalsIgnoreCase(plugin.getName())) {
						cmd.unregister(this.scm);
						it.remove();
					}
				}
			}
		}
		
		spm.disablePlugin(plugin);
		synchronized(spm) {
			names.remove(plugin.getName());
			plugins.remove(plugin);
		}
		
		JavaPluginLoader jpl = (JavaPluginLoader)plugin.getPluginLoader();
		if(this.loadersF == null) {
			try {
				this.loadersF = jpl.getClass().getDeclaredField("loaders");
				this.loadersF.setAccessible(true);
			}
			catch (NoSuchFieldException ex) {
				ex.printStackTrace();
			}
			catch (SecurityException ex) {
				ex.printStackTrace();
			}
		}
		
		try {
			Map loaderMap = (Map)this.loadersF.get(jpl);
			loaderMap.remove(plugin.getDescription().getName());
		}
		catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
		
		this.closeClassLoader(plugin);
		System.gc();
	}
}