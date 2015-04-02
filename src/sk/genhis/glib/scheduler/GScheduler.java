package sk.genhis.glib.scheduler;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.GLib;
import sk.genhis.glib.event.scheduler.CreateTaskEvent;

public final class GScheduler {
	private final HashMap<Integer, TaskExecutor> tasks = new HashMap<Integer, TaskExecutor>();
	
	public int scheduleSyncDelayedTask(JavaPlugin plugin, GTask task) {
		return this.scheduleSyncDelayedTask(plugin, task, 100L);
	}
	
	public int scheduleSyncDelayedTask(JavaPlugin plugin, GTask task, long delay) {
		final TaskExecutor t = new TaskExecutor(plugin, task, false);
		final CreateTaskEvent e = new CreateTaskEvent(t);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return -1;

		this.tasks.put(t.getId(), t);
		t.start(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, t, delay));
		return t.getId();
	}
	
	public int scheduleSyncRepeatingTask(JavaPlugin plugin, GTask task, long delay, long period) {
		final TaskExecutor t = new TaskExecutor(plugin, task, true);
		final CreateTaskEvent e = new CreateTaskEvent(t);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())
			return -1;
		
		this.tasks.put(t.getId(), t);
		t.start(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, t, delay, period));
		return t.getId();
	}
	
	public void cancelTask(int id) {
		if(!this.tasks.containsKey(id)) {
			GLib.getLog().log("!this.tasks.containsKey(id)");
			return;
		}
		
		TaskExecutor te = this.tasks.remove(id);
		te.cancel();
	}
	
	public void cancelTasks(JavaPlugin plugin) {
		for(TaskExecutor task : this.tasks.values())
			if(task.getOwningPlugin() == plugin)
				this.cancelTask(task.getId());
	}
	
	public void cancelAllTasks() {
		for(Integer id : this.tasks.keySet())
			this.cancelTask(id);
	}
	
	protected HashMap<Integer, TaskExecutor> getTasks() {
		return this.tasks;
	}
}