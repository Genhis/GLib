package sk.genhis.glib.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import sk.genhis.glib.command.CommandException;
import sk.genhis.glib.plugin.GPlugin;

public final class GCommandExecutor implements CommandExecutor {
	private final Object obj;
	private final Method method;
	
	private GCommandExecutor(GPlugin plugin, Object obj, Method method, String cmd, String permission) {
		this.obj = obj;
		this.method = method;
		
		String p[] = permission.split("\\.");
		Permission perm = Bukkit.getPluginManager().getPermission(permission);
		if(perm == null)
			Bukkit.getPluginManager().addPermission(perm = new Permission(permission));
		perm.addParent(p[p.length - 1] + ".*", true);
	}
	
	public boolean onCommand(CommandSender s, org.bukkit.command.Command cmd, String label, String[] args) {
		try {
			this.method.invoke(this.obj, s, args);
		}
		catch(IllegalAccessException ex) {
			ex.printStackTrace();
		}
		catch(IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		catch(InvocationTargetException ex) {
			if(ex.getCause() != null && ex.getCause() instanceof CommandException)
				s.sendMessage(ChatColor.RED + ex.getCause().getMessage());
			else
				ex.printStackTrace();
		}
		return true;
	}
	
	public static void registerCommands(GPlugin plugin, Object obj) {
		for(Method m : obj.getClass().getDeclaredMethods())
			if(m.isAnnotationPresent(Command.class)) {
				Command ant = (Command)m.getAnnotation(Command.class);
				new GCommandExecutor(plugin, obj, m, ant.name(), ant.permission());
			}
	}
}