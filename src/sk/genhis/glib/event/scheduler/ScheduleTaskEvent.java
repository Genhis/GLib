package sk.genhis.glib.event.scheduler;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import sk.genhis.glib.scheduler.TaskExecutor;

public final class ScheduleTaskEvent extends SchedulerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private final boolean runnable;
	private boolean cancelled = false;
	
	public ScheduleTaskEvent(TaskExecutor task, boolean runnable) {
		super(task);
		this.runnable = runnable;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public boolean isRunnable() {
		return this.runnable;
	}
	
	public HandlerList getHandlers() {
		return ScheduleTaskEvent.handlers;
	}
	
	public static HandlerList getHandlerList() {
		return ScheduleTaskEvent.handlers;
	}
}