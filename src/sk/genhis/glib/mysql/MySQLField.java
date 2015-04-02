package sk.genhis.glib.mysql;

public final class MySQLField {
	private final String name;
	private final MySQLFieldTypeInfo type;
	private final String defaultValue;
	
	public final boolean isNull;
	public final boolean isAutoIncrement;
	
	public MySQLField(String name, MySQLFieldTypeInfo type) {
		this(name, type, false, false, null);
	}
	
	public MySQLField(String name, MySQLFieldTypeInfo type, boolean isNull) {
		this(name, type, isNull, false, null);
	}
	
	public MySQLField(String name, MySQLFieldTypeInfo type, boolean isNull, boolean isAutoIncrement) {
		this(name, type, isNull, isAutoIncrement, null);
	}
	
	public MySQLField(String name, MySQLFieldTypeInfo type, boolean isNull, String defaultValue) {
		this(name, type, isNull, false, defaultValue);
	}
	
	public MySQLField(String name, MySQLFieldTypeInfo type, boolean isNull, boolean isAutoIncrement, String defaultValue) {
		this.name = name;
		this.type = type;
		this.isNull = isNull;
		this.isAutoIncrement = isAutoIncrement;
		this.defaultValue = defaultValue;
	}
	
	public String getName() throws NullPointerException {
		if(this.name == null)
			throw new NullPointerException("Field \"name\" is null!");
		
		return this.name;
	}
	
	public MySQLFieldTypeInfo getType() throws NullPointerException {
		if(this.type == null)
			throw new NullPointerException("Field \"type\" is null!");
		
		return this.type;
	}
	
	public String getDefaultValue() {
		return this.defaultValue;
	}
}