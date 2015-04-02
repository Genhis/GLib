package sk.genhis.glib.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

public class GCommand extends Command implements PluginIdentifiableCommand {
	private final Plugin plugin;
	private CommandExecutor executor;
	
	public GCommand(Plugin plugin, String name, String permission) {
		super(name);
		this.plugin = plugin;
		this.executor = plugin;
		this.setPermission(permission);
		this.setPermissionMessage(ChatColor.RED + "Nemas opravnenie pre pouzitie prikazu");
	}
	
	public boolean execute(CommandSender s, String label, String[] args) {
		if(!this.plugin.isEnabled() || !this.testPermission(s))
			return false;
		
		return this.executor.onCommand(s, this, label, args);
	}
	
	public void setExecutor(CommandExecutor executor) {
		this.executor = executor;
	}
	
	public Plugin getPlugin() {
		return this.plugin;
	}
}