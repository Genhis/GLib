package sk.genhis.glib.event.scheduler;

import org.bukkit.event.HandlerList;

import sk.genhis.glib.scheduler.TaskExecutor;

public final class TaskExecutedEvent extends SchedulerEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public TaskExecutedEvent(TaskExecutor task) {
		super(task);
	}
	
	public HandlerList getHandlers() {
		return TaskExecutedEvent.handlers;
	}
	
	public static HandlerList getHandlerList() {
		return TaskExecutedEvent.handlers;
	}
}