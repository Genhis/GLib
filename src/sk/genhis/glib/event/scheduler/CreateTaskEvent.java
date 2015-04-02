package sk.genhis.glib.event.scheduler;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import sk.genhis.glib.scheduler.TaskExecutor;

public final class CreateTaskEvent extends SchedulerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	public CreateTaskEvent(TaskExecutor task) {
		super(task);
	}
	
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public HandlerList getHandlers() {
		return CreateTaskEvent.handlers;
	}
	
	public static HandlerList getHandlerList() {
		return CreateTaskEvent.handlers;
	}
}