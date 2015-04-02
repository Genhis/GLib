package sk.genhis.glib.mysql;

public final class MySQLFieldTypeInfo {
	private final MySQLFieldType type;
	public final int length;
	
	public MySQLFieldTypeInfo(MySQLFieldType type) {
		this(type, 0);
	}
	
	public MySQLFieldTypeInfo(MySQLFieldType type, int length) {
		this.type = type;
		this.length = length;
	}
	
	public MySQLFieldType getType() throws NullPointerException {
		if(this.type == null)
			throw new NullPointerException("Field \"type\" is null!");
		
		return this.type;
	}
}