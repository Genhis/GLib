package sk.genhis.glib.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.GLib;

public final class RegisteredEditor {
	private final JavaPlugin owner;
	private final Editor editor;
	private final Map<Player, Editor> editors = new HashMap<Player, Editor>();
	
	public RegisteredEditor(JavaPlugin plugin, Editor editor) {
		this.owner = plugin;
		this.editor = editor;
	}
	
	public boolean registerPlayer(Player player) {
		if(!GLib.checkEnabled())
			return false;
		
		if(this.editors.containsKey(player))
			return false;
		this.editors.put(player, this.editor.copy(player));
		return true;
	}
	
	public boolean unregisterPlayer(Player player) {
		if(!GLib.checkEnabled())
			return false;
		
		if(!this.editors.containsKey(player))
			return false;
		this.editors.remove(player);
		return true;
	}
	
	public JavaPlugin getOwner() {
		if(!GLib.checkEnabled())
			return null;
		
		return this.owner;
	}
	
	public String getName() {
		if(!GLib.checkEnabled())
			return null;
		
		return this.editor.getName();
	}
	
	public Editor getEditor(Player player) {
		if(!GLib.checkEnabled())
			return null;
		
		return this.editors.get(player);
	}
	
	public List<Player> getPlayers() {
		if(!GLib.checkEnabled())
			return null;
		
		final List<Player> players = new ArrayList<Player>();
		for(Entry<Player, Editor> entry : this.editors.entrySet())
			players.add(entry.getKey());
		return players;
	}
}