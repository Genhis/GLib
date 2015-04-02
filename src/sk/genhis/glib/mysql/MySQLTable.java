package sk.genhis.glib.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class MySQLTable {
	private final String name;
	private final ArrayList<MySQLField> fields;
	private final ArrayList<MySQLIndexKey> keys;
	
	private MySQL mysql = null;
	
	public MySQLTable(MySQL mysql, String name, ArrayList<MySQLField> fields, ArrayList<MySQLIndexKey> keys) {
		this.mysql = mysql;
		this.name = name;
		this.fields = fields;
		this.keys = keys;
	}
	
	public void table_create() throws SQLException, NullPointerException {
		this.testNullValues();
		if(this.fields == null)
			throw new NullPointerException("Field \"fields\" is null!");
		if(this.keys == null)
			throw new NullPointerException("Field \"keys\" is null!");
		
		String mysqlQuery = "CREATE TABLE IF NOT EXISTS " + this.name + "(";
		
		for(MySQLField field : this.fields) {
			if(field == null)
				throw new NullPointerException("Local field \"field\" is null!");
			
			mysqlQuery += field.getName() + " ";
			
			mysqlQuery += field.getType().getType().getName();
			if(field.getType().length != 0)
				mysqlQuery += "(" + field.getType().length + ")";
			mysqlQuery += " ";
			
			if(!field.isNull)
				mysqlQuery += "NOT ";
			mysqlQuery += "NULL ";
			
			if(field.isAutoIncrement)
				mysqlQuery += "AUTO_INCREMENT ";
			
			if(field.getDefaultValue() != null)
				mysqlQuery += "DEFAULT '" + field.getDefaultValue() + "' ";
			
			mysqlQuery += ",";
		}
		
		for(MySQLIndexKey key : this.keys)
			mysqlQuery += key.getKey().getName() + "(" + key.getField().getName() + "),";
		
		mysqlQuery = mysqlQuery.substring(0, mysqlQuery.length() - 1);
		mysqlQuery += ")";
		
		boolean isConnectionClosed = this.mysql.isClosed();
		if(isConnectionClosed)
			this.mysql.connect();
		
		this.mysql.uquery(mysqlQuery);
		
		if(isConnectionClosed)
			this.mysql.disconnect();
	}
	
	public void table_drop() throws SQLException, NullPointerException {
		this.testNullValues();
		
		String mysqlQuery = "DROP TABLE IF EXISTS " + this.name;
		
		boolean isConnectionClosed = this.mysql.isClosed();
		if(isConnectionClosed)
			this.mysql.connect();
		
		this.mysql.uquery(mysqlQuery);
		
		if(isConnectionClosed)
			this.mysql.disconnect();
	}
	
	public void table_truncate() throws SQLException, NullPointerException {
		this.testNullValues();
		
		String mysqlQuery = "TRUNCATE TABLE IF EXISTS " + this.name;
		
		boolean isConnectionClosed = this.mysql.isClosed();
		if(isConnectionClosed)
			this.mysql.connect();
		
		this.mysql.uquery(mysqlQuery);
		
		if(isConnectionClosed)
			this.mysql.disconnect();
	}
	
	public void table_recreate() throws SQLException, NullPointerException {
		this.table_drop();
		this.table_create();
	}
	
	public ResultSet select() throws SQLException, NullPointerException {
		return this.select(null);
	}
	
	public ResultSet select(HashMap<MySQLField, Object> where) throws SQLException, NullPointerException {
		this.testNullValues();
		
		String mysqlQuery = "SELECT * FROM " + this.name + " ";
		
		if(where != null)
			mysqlQuery = this.whereProcessing(mysqlQuery, where);
		
		boolean isConnectionClosed = this.mysql.isClosed();
		if(isConnectionClosed)
			this.mysql.connect();
		
		ResultSet result = this.mysql.query(mysqlQuery);
		
		if(isConnectionClosed)
			this.mysql.disconnect();
		
		return result;
	}
	
	public void insert(HashMap<MySQLField, Object> fieldMap) throws SQLException, NullPointerException {
		this.testNullValues();
		
		String mysqlQuery = "INSERT INTO " + this.name;
		
		String fields = "";
		String values = "";
		for(Map.Entry<MySQLField, Object> entry : fieldMap.entrySet()) {
			fields += entry.getKey().getName() + ",";
			values += entry.getValue().toString() + ",";
		}
		
		mysqlQuery += "(" + fields + ") VALUES(" + values + ")";
		
		boolean isConnectionClosed = this.mysql.isClosed();
		if(isConnectionClosed)
			this.mysql.connect();
		
		this.mysql.uquery(mysqlQuery);
		
		if(isConnectionClosed)
			this.mysql.disconnect();
	}
	
	public void delete(HashMap<MySQLField, Object> where) throws SQLException, NullPointerException {
		this.testNullValues();
		if(where == null)
			throw new NullPointerException("Local field \"where\" is null!");
		
		String mysqlQuery = "DELETE FROM " + this.name;
		
		if(where != null)
			mysqlQuery = this.whereProcessing(mysqlQuery, where);
	}
	
	private void testNullValues() throws NullPointerException {
		if(this.mysql == null)
			throw new NullPointerException("Field \"mysql\" is null!");
		if(this.name == null)
			throw new NullPointerException("Field \"name\" is null!");
	}
	
	private String whereProcessing(String mysqlQuery, HashMap<MySQLField, Object> where) throws NullPointerException {
		if(where == null)
			throw new NullPointerException("Local field \"where\" is null!");
		
		mysqlQuery += "WHERE ";
		for(Map.Entry<MySQLField, Object> entry : where.entrySet()) {
			if(entry.getKey() == null)
				throw new NullPointerException("Local field \"entry.getKey()\" is null!");
			if(entry.getValue() == null)
				throw new NullPointerException("Local field \"entry.getValue()\" is null!");
			
			mysqlQuery += entry.getKey().getName() + entry.getValue().toString() + "' AND ";
		}
		mysqlQuery = mysqlQuery.substring(0, mysqlQuery.length() - 4);
		
		return mysqlQuery;
	}
	
	public void setMySQL(MySQL mysql) {
		this.mysql = mysql;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<MySQLField> getFields() {
		return this.fields;
	}
	
	public ArrayList<MySQLIndexKey> getKeys() {
		return this.keys;
	}
}