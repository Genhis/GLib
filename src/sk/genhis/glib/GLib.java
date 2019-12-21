package sk.genhis.glib;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.configuration.Config;
import sk.genhis.glib.configuration.Configuration;
import sk.genhis.glib.editor.Editor;
import sk.genhis.glib.editor.RegisteredEditor;
import sk.genhis.glib.listener.BukkitListener;
import sk.genhis.glib.mysql.MySQL;
import sk.genhis.glib.plugin.GPlugin;
import sk.genhis.glib.scheduler.GScheduler;

public final class GLib extends JavaPlugin {
	private static GLib plugin = null;
	private static Logger logger = null;
	private static Configuration config = null;
	private static Configuration lang = null;
	private static MySQL mysql = null;
	//private static boolean enabled = false;
	
	private static final Map<String, RegisteredEditor> editors = new HashMap<String, RegisteredEditor>();
	private static final Map<String, GPlugin> plugins = new HashMap<String, GPlugin>();
	private static final GScheduler scheduler = new GScheduler();
	//private static final GPluginManager pluginManager = new GPluginManager();
	
	private static final Pattern IPv4 = Pattern.compile("(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])");
	private static final Pattern IPv6 = Pattern.compile("([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}");
	
	public void onEnable() {
		//GLib.enabled = true;
		
		GLib.plugin = this;
		GLib.logger = new Logger(this);
		GLib.config = new Configuration(new Config(this).getConfig());
		GLib.lang = new Configuration(new Config(this, "lang").getConfig());
		
		if(GLib.getConf().getBoolean("mysql.enable")) {
			GLib.logger.log("Checking MySQL connection");
			try {
				GLib.mysql = new MySQL(this, GLib.getConf());
			}
			catch (SQLException ex) {
				GLib.getLog().log("MySQL Error! Disabling plugin", Level.SEVERE);
				Bukkit.getPluginManager().disablePlugin(this);
				if(GLib.debug())
					ex.printStackTrace();
			}
		}
		
		GLib.logger.log("Registering commands");
		//this.getCommand("glib").setExecutor(new GLibCMD());
		
		GLib.logger.log("Registering listeners");
		Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);
	}
	
	public void onDisable() {
		GLib.unregisterEditors();
		//GLib.unloadPlugins();
		//GLib.enabled = false;
		GLib.config = null;
		GLib.lang = null;
		GLib.mysql = null;
	}
	/*
	public void onReload() {
		GLib.unregisterEditors();
		GLib.reloadPlugins();
		
		this.onEnable();
	}
	
	public static void loadPlugin(GPlugin plugin) {
		GLib.plugins.put(plugin.getName(), plugin);
	}
	
	public static void unloadPlugin(GPlugin plugin) {
		GLib.plugins.remove(plugin);
	}
	
	private static void unloadPlugins() {
		for(GPlugin p : GLib.plugins.values()) {
			GLib.getLog().log(p.getName());
			GLib.getPluginManager().unloadPlugin(p);
			GLib.unloadPlugin(p);
		}
	}
	
	public static void reloadPlugin(String plugin) {
		GLib.plugins.get(plugin).onReload();
	}
	
	private static void reloadPlugins() {
		for(GPlugin p : GLib.plugins.values())
			p.onReload();
	}
	*/
	public static RegisteredEditor registerEditor(Editor editor, JavaPlugin plugin) {
		if(!GLib.checkEnabled())
			return null;
		
		if(GLib.editors.containsKey(editor.getName().toLowerCase()))
			return null;
		GLib.logger.log("Registering editor \"" + editor.getName() + "\" for plugin " + plugin.getName());
		final RegisteredEditor e = new RegisteredEditor(plugin, editor);
		GLib.editors.put(editor.getName().toLowerCase(), e);
		return e;
	}
	
	public static boolean unregisterEditor(String name) {
		if(!GLib.checkEnabled())
			return false;
		
		return GLib.unregisterEditor(name.toLowerCase(), true);
	}
	
	private static boolean unregisterEditor(String name, boolean log) {
		if(!GLib.editors.containsKey(name.toLowerCase()))
			return false;
		if(log)
			GLib.logger.log("Unregistering editor \"" + name + "\"");
		GLib.editors.remove(name.toLowerCase());
		return true;
	}
	
	public static void unregisterEditors(JavaPlugin plugin) {
		if(!GLib.checkEnabled())
			return;
		
		GLib.logger.log("Unregistering editors owning by plugin " + plugin.getDescription().getName());
		final HashMap<String, RegisteredEditor> editors = new HashMap<String, RegisteredEditor>(GLib.editors);
		for(RegisteredEditor e : editors.values())
			if(e.getOwner() == plugin)
				GLib.unregisterEditor(e.getName(), false);
	}
	
	public static void unregisterEditors() {
		if(!GLib.checkEnabled())
			return;
		
		GLib.logger.log("Unregistering all editors");
		GLib.editors.clear();
	}
	
	public static boolean registerPlayer(String editor, Player player) {
		if(!GLib.checkEnabled())
			return false;
		
		if(!GLib.editors.containsKey(editor))
			return false;
		GLib.editors.get(editor).registerPlayer(player);
		return true;
	}
	
	public static void unregisterPlayer(Player player) {
		if(!GLib.checkEnabled())
			return;
		
		boolean log = false;
		for(RegisteredEditor e : GLib.getEditors()) {
			if(e.getPlayers().contains(player)) {
				log = true;
				e.unregisterPlayer(player);
			}
		}
		if(log)
			GLib.logger.log("The player " + player.getName() +
					" left the game, unregistering all editors");
	}
	
	public static boolean checkEnabled() {
		//if(!GLib.enabled)
			//TODO: GLib.logger.log("This function is disabled, because GLib is disabled.", Level.WARNING);
		//return GLib.enabled;
		return true;
	}
	
	public static GLib getPlugin() {
		return GLib.plugin;
	}
	
	public static GPlugin getGPlugin(String name) {
		return GLib.plugins.get(name);
	}
	
	public static Logger getLog() {
		return GLib.logger;
	}
	
	public static Configuration getConf() {
		return GLib.config;
	}
	
	public static boolean debug() {
		return GLib.getConf().getBoolean("debug", true);
	}
	
	public static Configuration getMsg() {
		return GLib.lang;
	}
	
	public static MySQL getMysql() {
		return GLib.mysql;
	}
	
	public static GScheduler getScheduler() {
		return GLib.scheduler;
	}
	/*
	public static GPluginManager getPluginManager() {
		return GLib.pluginManager;
	}
	*/
	public static RegisteredEditor getEditor(String name) {
		return GLib.editors.get(name);
	}
	
	public static Collection<RegisteredEditor> getEditors() {
		return GLib.editors.values();
	}
	
	public static boolean in_array(Object[] array, Object seach) {
		if(!GLib.checkEnabled())
			return false;
		
		for(int i=0; i < array.length; i++)
			if(array[i].hashCode() == seach.hashCode())
				return true;
		return false;
	}
	
	public static String implode(String[] array, String c) {
		String s = "";
		for(int i=0; i < array.length; i++) {
			if(i > 0)
				s += c;
			s += array[i];
		}
		return s;
	}
	
	public static String implode(List<String> array, String c) {
		String s = "";
		for(int i=0; i < array.size(); i++) {
			if(i > 0)
				s += c;
			s += array.get(i);
		}
		return s;
	}
	
	public static Object getMetadata(List<MetadataValue> metadata, JavaPlugin plugin) {
		if(!GLib.checkEnabled())
			return null;
		
		for(MetadataValue value : metadata) {
			if(value.getOwningPlugin().hashCode() == plugin.hashCode())
				return value.value();
		}
		return null;
	}
	
	public static String[] removeArgs(String[] oldArgs, List<Integer> remove) {
		String[] newArgs = new String[oldArgs.length - remove.size()];
		for(int i=0, j=0; i < oldArgs.length; i++) {
			if(remove.contains(i))
				continue;
			newArgs[j] = oldArgs[i];
			j++;
		}
		return newArgs;
	}
	
	public static double round(double a, int b) {
		if(!GLib.checkEnabled())
			return 0;
		
		return Math.round(a * Math.pow(a, b)) / Math.pow(a, b);
	}
	
	public static List<Integer> createIntList(int to) {
		List<Integer> arr = new ArrayList<Integer>();
		for(int i=0; i < to; i++)
			arr.add(i);
		return arr;
	}
	
	public static boolean isInt(String s) {
		return s == s.replaceAll("[^0-9]", "");
	}
	
	public static boolean isValidIPAddress(String ip) {
		return GLib.IPv4.matcher(ip).matches() || GLib.IPv6.matcher(ip).matches();
	}
}