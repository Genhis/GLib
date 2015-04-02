package sk.genhis.glib.event.scheduler;

import org.bukkit.event.Event;

import sk.genhis.glib.GLib;
import sk.genhis.glib.scheduler.GScheduler;
import sk.genhis.glib.scheduler.TaskExecutor;

public abstract class SchedulerEvent extends Event {
	private final TaskExecutor task;
	
	public SchedulerEvent(TaskExecutor task) {
		this.task = task;
	}
	
	public boolean isRepeating() {
		return this.task.isRepeating();
	}
	
	public GScheduler getScheduler() {
		return GLib.getScheduler();
	}
	
	public TaskExecutor getTask() {
		return this.task;
	}
}