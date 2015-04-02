package sk.genhis.glib.editor;

import org.bukkit.entity.Player;

public interface Editor {
	public boolean open(Object arg0);
	public boolean close();
	public boolean isUsed();
	public Player getPlayer();
	public Object getObject();
	public String getName();
	public Editor copy(Player player);
}