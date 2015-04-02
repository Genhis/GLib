package sk.genhis.glib.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.GLib;

public final class ResourceReader implements ITXTFile {
	private final JavaPlugin plugin;
	private final String file;
	private String content;
	
	public ResourceReader(JavaPlugin plugin, String file) {
		this.plugin = plugin;
		this.file = file;
		this.loadFile();
	}
	
	public void loadFile() {
		if(!GLib.checkEnabled())
			return;

		final InputStream resource = this.plugin.getResource(this.file);
		if(resource == null)
			throw new NullPointerException("File \"" + this.file + "\" not exist!");

		StringBuffer out = new StringBuffer();
		/*final char buffer[] = new char[1024];
		try {
			final InputStreamReader isr = new InputStreamReader(resource);
			while(isr.read(buffer, 0, buffer.length) > 0)
				out.append(buffer);
			isr.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}*/
		String s;
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(resource));
			while((s = br.readLine()) != null)
				out.append(s);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		this.content = out.toString();
	}
	
	public String getFileContent() {
		return this.content;
	}
}