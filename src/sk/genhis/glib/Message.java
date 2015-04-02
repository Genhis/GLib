package sk.genhis.glib;

import org.bukkit.ChatColor;

public final class Message {
	public static final String NO_PERMISSION = ChatColor.RED + GLib.getMsg().getString("messages.no_permission");
	public static final String BAD_SENDER = ChatColor.RED + GLib.getMsg().getString("messages.bad_sender");
	public static final String TOO_FEW_ARGS = ChatColor.RED + GLib.getMsg().getString("messages.args.few");
	public static final String TOO_MANY_ARGS = ChatColor.RED + GLib.getMsg().getString("messages.args.many");
	public static final String EDITOR_USE = ChatColor.RED + GLib.getMsg().getString("messages.editor.use");
	public static final String EDITOR_NO_USE = ChatColor.RED + GLib.getMsg().getString("messages.editor.no_use");
	public static final String EXECUTOR_ITEMBINDER = ChatColor.RED + GLib.getMsg().getString("messages.executor.itembinder");
	public static final String ACTION_SUCCESSED = ChatColor.RED + GLib.getMsg().getString("messages.action.successed");
	public static final String ACTION_FAILED = ChatColor.RED + GLib.getMsg().getString("messages.action.failed");
	public static final String COMMAND_NOT_FOUND = ChatColor.RED + GLib.getMsg().getString("messages.command_not_found");
	
	private Message() {}
}