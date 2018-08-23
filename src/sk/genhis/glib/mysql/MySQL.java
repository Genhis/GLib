package sk.genhis.glib.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import sk.genhis.glib.GLib;
import sk.genhis.glib.event.mysql.MySQLCloseEvent;
import sk.genhis.glib.event.mysql.MySQLConnectEvent;
import sk.genhis.glib.event.mysql.MySQLQueryEvent;

public final class MySQL {
	private final UUID uid = UUID.randomUUID();
    private final String host, user, pass;
	private final JavaPlugin plugin;
    private Connection conn;
    
    public MySQL(JavaPlugin plugin, FileConfiguration config) throws SQLException {
    	this(plugin, config.getString("mysql.host"), config.getString("mysql.user"), config.getString("mysql.pass"), config.getString("mysql.db"));
    }
    
    public MySQL(JavaPlugin plugin, String host, String user, String pass, String db) throws SQLException {
    	this.plugin = plugin;
    	this.host = "jdbc:mysql://" + host + "/" + db;
    	this.user = user;
    	this.pass = pass;
    	
    	this.conn = DriverManager.getConnection(this.host, user, pass);
    	this.conn.close();
	}
    
    public void connect() throws SQLException {
		if(!GLib.checkEnabled() || !this.conn.isClosed())
			return;
		
		final MySQLConnectEvent e = new MySQLConnectEvent(this.plugin, this);
		Bukkit.getPluginManager().callEvent(e);
		if(!e.isCancelled())
			this.conn = DriverManager.getConnection(this.host, this.user, this.pass);
    }
    
    public void disconnect() throws SQLException {
		if(!GLib.checkEnabled())
			return;
		Bukkit.getPluginManager().callEvent(new MySQLCloseEvent(this.plugin, this));
		if(!this.conn.isClosed())
			this.conn.close();
    }
    
    public ResultSet query(String query, Object... values) throws SQLException {
		if(!GLib.checkEnabled())
			return null;
		if(this.conn.isClosed())
			this.connect();

		ResultSet result = null;
		final MySQLQueryEvent e = new MySQLQueryEvent(this.plugin, this, query, false);
		Bukkit.getPluginManager().callEvent(e);
		if(!e.isCancelled()) {
			PreparedStatement ps = this.conn.prepareStatement(query);
			prepare(ps, values);
			result = ps.executeQuery();
		}
		return result;
    }
    
    public void uquery(String query, Object... values) throws SQLException {
		if(!GLib.checkEnabled())
			return;
		if(this.conn.isClosed())
			this.connect();
		
		final MySQLQueryEvent e = new MySQLQueryEvent(this.plugin, this, query, true);
		Bukkit.getPluginManager().callEvent(e);
		if(!e.isCancelled()) {
			PreparedStatement ps = this.conn.prepareStatement(query);
			prepare(ps, values);
			ps.executeUpdate();
		}
			
    }
    
    public void multiUquery(String query) throws SQLException {
		for(String q : query.split(";"))
			this.uquery(q);
    }
	
	public boolean isClosed() {
		try {
			return this.conn.isClosed();
		}
		catch (SQLException e) {
			if(GLib.debug())
				e.printStackTrace();
		}
		return true;
	}
	
	public void prepare(PreparedStatement ps, Object... values) {
		try {
			for(int i = 0; i < values.length; i++) {
				if(values[i] instanceof String) {
					ps.setString(i+1, (String)values[i]);
				} else if (values[i] instanceof Integer) {
					ps.setInt(i+1, (int)values[i]);
				} else if (values[i] instanceof Double) {
					ps.setDouble(i+1, (double)values[i]);
				} else if (values[i] instanceof Long) {
					ps.setLong(i+1, (long)values[i]);
				}
			}
		} catch (SQLException e) {
			this.plugin.getLogger().log(Level.WARNING, "Error parsing value to prepared statement", e);
		}
	}
	
	public UUID getUID() {
		return this.uid;
	}
}