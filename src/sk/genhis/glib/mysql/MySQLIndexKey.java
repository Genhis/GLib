package sk.genhis.glib.mysql;

public final class MySQLIndexKey {
	private final MySQLIndexKeyType key;
	private final MySQLField field;
	
	public MySQLIndexKey(MySQLIndexKeyType key, MySQLField field) {
		this.key = key;
		this.field = field;
	}
	
	public MySQLIndexKeyType getKey() throws NullPointerException {
		if(this.key == null)
			throw new NullPointerException("Field \"key\" is null!");
		
		return this.key;
	}
	
	public MySQLField getField() throws NullPointerException {
		if(this.field == null)
			throw new NullPointerException("Field \"field\" is null!");
		
		return this.field;
	}
}