package sk.genhis.glib.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.GLib;
import sk.genhis.glib.event.scheduler.ScheduleTaskEvent;
import sk.genhis.glib.event.scheduler.TaskExecutedEvent;

public final class TaskExecutor implements Runnable {
	private static final ArrayList<Integer> taskIds = new ArrayList<Integer>();
	private static final HashMap<Integer, Integer> bukkitTaskId = new HashMap<Integer, Integer>();
	
	private final int id = TaskExecutor.getFreeId();
	private final JavaPlugin owner;
	private final GTask task;
	private final boolean runnable;
	
	private boolean started = false;
	private boolean scheduled = false;
	
	static {
		for(int i=1; i <= 9999; i++)
			TaskExecutor.taskIds.add(i);
	}
	
	public TaskExecutor(JavaPlugin owner, GTask task, boolean runnable) {
		this.owner = owner;
		this.task = task;
		this.runnable = runnable;
		
		TaskExecutor.taskIds.remove((Integer)this.id);
	}
	
	public void start(int bukkitId) {
		TaskExecutor.bukkitTaskId.put(this.id, bukkitId);
		this.started = true;
	}
	
	public void run() {
		if(!this.started || this.scheduled)
			return;
		
		final ScheduleTaskEvent ste = new ScheduleTaskEvent(this, this.runnable);
		Bukkit.getPluginManager().callEvent(ste);
		if(ste.isCancelled()) {
			this.cancel();
			return;
		}
		
		final long startTime = System.currentTimeMillis();
		this.task.run();
		if(GLib.debug())
			GLib.getLog().log("Task " + this.task.getName() + " with id " + this.id + " owning by plugin " +
					this.owner.getDescription().getName() + " executed! (" + (System.currentTimeMillis() - startTime) + " ms)");
		if(!this.runnable)
			this.done();
		
		Bukkit.getPluginManager().callEvent(new TaskExecutedEvent(this));
	}
	
	public void cancel() {
		Bukkit.getScheduler().cancelTask(TaskExecutor.getBukkitTaskId(this.id));
		this.done();
	}
	
	private void done() {
		this.started = false;
		this.scheduled = true;
		
		TaskExecutor.bukkitTaskId.remove(this.id);
		TaskExecutor.taskIds.add(this.id);
	}
	
	public boolean isRepeating() {
		return this.runnable;
	}
	
	public int getId() {
		return this.id;
	}
	
	public JavaPlugin getOwningPlugin() {
		return this.owner;
	}
	
	public GTask getTask() {
		return this.task;
	}
	
	private static int getFreeId() {
		int taskId = 0;
		while(taskId == 0) {
			final Integer id = TaskExecutor.taskIds.get(new Random().nextInt(
					TaskExecutor.taskIds.size() - 1));
			if(!GLib.getScheduler().getTasks().containsKey(id))
				taskId = id;
		}
		return taskId;
	}
	
	protected static int getBukkitTaskId(int taskId) {
		return TaskExecutor.bukkitTaskId.get(taskId);
	}
}